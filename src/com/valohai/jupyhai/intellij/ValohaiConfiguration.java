package com.valohai.jupyhai.intellij;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * This ProjectConfigurable class appears on Settings dialog,
 * to let user to configure this plugin's behavior.
 */
public class ValohaiConfiguration implements SearchableConfigurable {

    ValohaiConfigurationGUI gui;

    private final ValohaiConfig mConfig;

    @SuppressWarnings("FieldCanBeLocal")
    private final Project mProject;

    public ValohaiConfiguration(@NotNull Project project) {
        mProject = project;
        mConfig = ValohaiConfig.getInstance(project);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        gui = new ValohaiConfigurationGUI();
        gui.createUI(mProject);
        return gui.getRootPanel();
    }

    @Override
    public void disposeUIResources() {
        gui = null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Single File Execution Plugin";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.SingleFileExecutionConfigurable";
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.SingleFileExecutionConfigurable";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Override
    public boolean isModified() {
        if(gui == null) {
            return false;
        }
        return gui.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        gui.apply();
    }

    @Override
    public void reset() {
        gui.reset();
    }
}