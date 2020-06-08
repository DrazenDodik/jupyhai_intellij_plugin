package com.valohai.jupyhai.intellij;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name="ValohaiConfig",
        storages = {
                @Storage("ValohaiConfig.xml")}
)
public class ValohaiConfig implements PersistentStateComponent<ValohaiConfig> {

    public String serverUrl;
    public String hostaddress;
    public String token;
    public String projectid;
    public String dockerimage;
    public String ignorefiles;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        String[] addressparts = serverUrl.split("\\?");
        this.hostaddress = addressparts[0];
        this.token = addressparts[1];
        this.serverUrl = serverUrl;
    }

    public String getHostaddress() {
        return hostaddress;
    }

    private void setHostaddress(String hostaddress) {
        this.hostaddress = hostaddress;
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getDockerimage() {
        if(dockerimage == "") {
            return "valohai/pypermill";
        }

        return dockerimage;
    }

    public void setDockerimage(String dockerimage) {
        this.dockerimage = dockerimage;
    }

    public String getIgnorefiles() {
        if(ignorefiles == "") {
            return "*.ipybn";
        }
        return ignorefiles;
    }

    public void setIgnorefiles(String ignorefiles) {
        this.ignorefiles = ignorefiles;
    }

    @Nullable
    @Override
    public ValohaiConfig getState() {
        return this;
    }

    @Override
    public void loadState(ValohaiConfig valohaiConfig) {
        XmlSerializerUtil.copyBean(valohaiConfig, this);
    }

    @Nullable
    public static ValohaiConfig getInstance(Project project) {
        return ServiceManager.getService(project, ValohaiConfig.class);
    }
}