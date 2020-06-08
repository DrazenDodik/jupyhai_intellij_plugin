package com.valohai.jupyhai.intellij;

import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.valohai.jupyhai.intellij.ValohaiConfig;

import javax.swing.*;
import java.awt.*;

public class ValohaiConfigurationGUI {
    private JPanel rootPanel;
    private JTextField serverTextField;
    private JTextField projectField;
    private JTextField dockerField;
    private JTextArea ignoreField;

    private ValohaiConfig mConfig;

    ValohaiConfigurationGUI() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setRequestFocusEnabled(true);

        final JLabel serverLabel = new JLabel();
        serverLabel.setText("Local Notebook URL");
        rootPanel.add(serverLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(80, 16), null, 0, false));

        serverTextField = new JTextField();
        serverTextField.setAutoscrolls(true);
        serverTextField.setEditable(true);
        serverTextField.setEnabled(true);
        serverTextField.setHorizontalAlignment(10);
        serverLabel.setLabelFor(serverTextField);
        rootPanel.add(serverTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

        final JLabel serverHintLabel = new JLabel();
        serverHintLabel.setText("Include the url and token (e.g. http://127.0.0.1:8888/?token=<your-token>)");
        serverHintLabel.setVerticalAlignment(0);
        rootPanel.add(serverHintLabel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));


        final JLabel projectLabel = new JLabel();
        projectLabel.setText("Project ID");
        rootPanel.add(projectLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(80, 16), null, 0, false));

        projectField = new JTextField();
        projectField.setAutoscrolls(true);
        projectField.setEditable(true);
        projectField.setEnabled(true);
        projectField.setHorizontalAlignment(10);
        projectLabel.setLabelFor(projectField);
        rootPanel.add(projectField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

        final JLabel dockerLabel = new JLabel();
        dockerLabel.setText("Docker Image");
        rootPanel.add(dockerLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(80, 16), null, 0, false));

        dockerField = new JTextField("valohai/pypermill");
        dockerField.setAutoscrolls(true);
        dockerField.setEditable(true);
        dockerField.setEnabled(true);
        dockerField.setHorizontalAlignment(10);
        dockerLabel.setLabelFor(dockerField);
        rootPanel.add(dockerField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

        final JLabel ignoreLabel = new JLabel();
        ignoreLabel.setText("Ignore files");
        rootPanel.add(ignoreLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(80, 16), null, 0, false));

        ignoreField = new JTextArea("*.ipybn");
        ignoreField.setAutoscrolls(true);
        ignoreField.setEditable(true);
        ignoreField.setEnabled(true);
        ignoreField.setRows(5);
        ignoreLabel.setLabelFor(ignoreField);
        rootPanel.add(ignoreField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));

        final JLabel ignoreHintLabel = new JLabel();
        ignoreHintLabel.setText("Add one item per line");
        ignoreHintLabel.setVerticalAlignment(0);
        rootPanel.add(ignoreHintLabel, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));


        final Spacer spacer2 = new Spacer();
        rootPanel.add(spacer2, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

    }

    public void createUI(Project project) {
        mConfig = ValohaiConfig.getInstance(project);
        serverTextField.setText(mConfig.getServerUrl());
        projectField.setText(mConfig.getProjectid());
        dockerField.setText(mConfig.getDockerimage());
        ignoreField.setText(mConfig.getIgnorefiles());
    }

    public void apply() {
        mConfig.setServerUrl(serverTextField.getText());
        mConfig.setProjectid(projectField.getText());
        mConfig.setDockerimage(dockerField.getText());
        mConfig.setIgnorefiles(ignoreField.getText());
    }

    public void reset() {
        serverTextField.setText(mConfig.getServerUrl());
        dockerField.setText(mConfig.getDockerimage());
        projectField.setText(mConfig.getProjectid());
        ignoreField.setText(mConfig.getIgnorefiles());
    }

    public boolean isModified() {
        boolean modified = false;
        modified |= !serverTextField.getText().equals(mConfig.getServerUrl());
        modified |= !dockerField.getText().equals(mConfig.getDockerimage());
        modified |= !projectField.getText().equals(mConfig.getProjectid());
        modified |= !ignoreField.getText().equals(mConfig.getIgnorefiles());
        return modified;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}