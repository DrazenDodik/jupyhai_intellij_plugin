package com.valohai.jupyhai.intellij;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.*;

public class JupyhaiAction extends AnAction {

    private StringBuffer errorMessage = new StringBuffer();
    private ValohaiConfig valohaiConfig = null;
    private HttpResponse httpResponse = null;
    private String commitID = null;
    private URL executionURL = null;

    @Override
    public void actionPerformed(AnActionEvent event) {

        Project currentProject = event.getProject();
        valohaiConfig = currentProject.getService(ValohaiConfig.class);

        // Access current projects Jupyter settings in IntelliJ
        // ToDo: Access Notebook server settings and automatically set the url
        //JupyterSettings jupyter = ServiceManager.getService(JupyterSettings.class);

        String notebook = "N/A";

        final VirtualFile virtualFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        final String basePath = currentProject.getBasePath();

        if (basePath == null) return;

        try {
            notebook = VfsUtil.loadText(virtualFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(notebook == null) {
            this.errorMessage.append("Failed loading the notebook");
        }
        else {
            VirtualFile rootPath = ProjectFileIndex.SERVICE.getInstance(currentProject).getContentRootForFile(virtualFile);
            String selectedFilePath = VfsUtilCore.getRelativePath(virtualFile, rootPath);

            boolean prepareExecutionSucceeded = prepareExecution(selectedFilePath, notebook);

            if (prepareExecutionSucceeded && this.errorMessage.length() == 0) {
                boolean createExecutionSucceeded = createExecution(notebook);

                if (createExecutionSucceeded && this.errorMessage.length() == 0) {
                    BrowserUtil.browse(executionURL);
                }
            }
        }

        if(this.errorMessage.length() > 0) {
            Messages.showMessageDialog(currentProject, this.errorMessage.toString(), "Error creating execution", Messages.getErrorIcon());
            this.errorMessage = new StringBuffer();
        }

    }

    @Override
    public void update(AnActionEvent e) {
        DataContext dataContext = e.getDataContext();
        Presentation presentation = e.getPresentation();
        final VirtualFile virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);

        if (virtualFile == null) {
            return;
        }

        FileType type = virtualFile.getFileType();

        if(type.getName() != "Jupyter") {
            hide(presentation);
            return;
        }
        show(presentation);
    }

    private static void show(Presentation presentation) {
        presentation.setEnabled(true);
        presentation.setVisible(true);
    }

    private static void hide(Presentation presentation) {
        presentation.setEnabled(false);
        presentation.setVisible(false);
    }

    private boolean createExecution(String notebook) {

        String createExecutionPayload = "{\n" +
                "\"commit\": \"" + this.commitID + "\",\n" +
                "\"environmentId\": null,\n" +
                "\"image\": \"" + this.valohaiConfig.dockerimage + "\",\n" +
                "\"projectId\": \"" + this.valohaiConfig.projectid + "\",\n" +
                "\"title\": \"Notebook execution\",\n" +
                "\"content\": {\n" +
                "  \"type\": \"notebook\"," +
                "  \"content\": " +
                notebook +
                "}}";
        String executeAddress = this.valohaiConfig.hostaddress + "jupyhai/execute?" + this.valohaiConfig.token;

        boolean executionCreated = sendRequest(executeAddress, createExecutionPayload);
        String executionResponse = null;
        JSONObject execution = null;

        if(executionCreated) {
            try {
                executionResponse = new BasicResponseHandler().handleResponse(this.httpResponse);
                this.httpResponse = null;
                execution = (JSONObject) JSONValue.parse(executionResponse);
            } catch (Exception e) {
                this.errorMessage.append("Failed creating execution");
                return false;
            }
        }


        if (execution.get("success").toString() == "true") {
            JSONObject executionDetails = (JSONObject) execution.get("execution");
            try {
                this.executionURL = new URL(((JSONObject) executionDetails.get("urls")).get("display").toString());
            } catch (MalformedURLException e) {
                this.errorMessage.append("Failed navigating to the execution");
                return false;
            }
        } else {
            this.errorMessage.append("Failed creating the execution");
            return false;
        }

        return true;

    }
        private boolean prepareExecution(String selectedFilePath, String notebook) {

            String ignoreFiles = this.valohaiConfig.ignorefiles.replaceAll("\\n", "\",\"");
            String prepareAddress = this.valohaiConfig.hostaddress
                                        + "jupyhai/prepare?"
                                        + this.valohaiConfig.token
                                        + "&ignore=[\"" + ignoreFiles + "\"]"
                                        + "&notebook_path=" + selectedFilePath;

            String prepareMessage = "{\n" +
                    "\"path\": \"" + selectedFilePath + "\",\n" +
                    "\"ignore\": [\"" + ignoreFiles + "\"]," +
                    "\"content\": {\n" +
                    "  \"type\": \"notebook\"," +
                    "  \"content\": " +
                    notebook +
                    "}}";

            boolean prepareSucceeded = sendRequest(prepareAddress, prepareMessage);

            if (prepareSucceeded == false || this.httpResponse == null) {
                return false;
            }

            String prepareResponse = null;

            try {
                prepareResponse = new BasicResponseHandler().handleResponse(this.httpResponse);
            } catch (IOException e) {
                e.printStackTrace();
                this.errorMessage.append("Error creating request. Make sure you've logged in to the Valohai CLI tools. Run \"vh login\" in your terminal and then restart your Jupyter server.");
                return false;
            }

            this.httpResponse = null;

            JSONObject commit = null;
            try {
                commit = (JSONObject) JSONValue.parse(prepareResponse);
                this.commitID = commit.get("commit").toString();

            } catch (Exception e) {
                errorMessage.append("Failed preparing the notebook package");
                return false;
            }

            return true;
        }

    private boolean sendRequest(String address, String message) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            URI uri = new URI(URIUtil.encodeQuery(address));
            HttpPost request = new HttpPost(uri);
            StringEntity params = new StringEntity(message);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

            this.httpResponse = response;
            return true;

        } catch (HttpResponseException | UnsupportedEncodingException ex) {
            this.errorMessage.append("Error connecting to your Jupyter server - the token might be expired. Try to re-launch Jupyter Notebook and set a new token in IntelliJ settings.");
            return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            this.errorMessage.append("Unable to prepare the notebook for execution.");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            this.errorMessage.append("Unable to connect to server. Make sure your Jupyter server is running and the connection information configured in IntelliJ settings.");
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            this.errorMessage.append("Invalid request. Check your settings in IntelliJ for any formatting errors.");
            return false;
        }
    }

}
