/*
 *     Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *     WSO2 Inc. licenses this file to you under the Apache License,
 *     Version 2.0 (the "License"); you may not use this file except
 *     in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing,
 *    software distributed under the License is distributed on an
 *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *    KIND, either express or implied.  See the License for the
 *    specific language governing permissions and limitations
 *    under the License.
 */
package org.wso2.integrationstudio.humantaskeditor.wizards;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.wso2.integrationstudio.humantaskeditor.HumantaskEditorConstants;

public class HumanTaskProjectWizardPage extends WizardPage {

    private Text containerText;

    private Text fileText;

    private Text taskText;

    private Text tnsText;

    private ISelection selection;

    /**
     * Constructor for Human Task Wizard Page.
     *
     * @param pageName
     */
    public HumanTaskProjectWizardPage(ISelection selection) {
        super(HumantaskEditorConstants.WIZARD_PAGE_NAME);
        setTitle(HumantaskEditorConstants.PROJECT_WIZARD_PAGE_TITLE);
        setDescription(HumantaskEditorConstants.PROJECT_WIZARD_PAGE_DESCRIPTION);
        this.selection = selection;
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        layout.verticalSpacing = 9;
        Label label = new Label(container, SWT.NULL);
        label.setText(HumantaskEditorConstants.WIZARD_PROJECT_NAME_LABEL);

        containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        containerText.setLayoutData(gd);
        containerText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        label = new Label(container, SWT.NULL);
        label.setText(HumantaskEditorConstants.WIZARD_FILE_NAME_LABEL);

        fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        fileText.setLayoutData(gd);
        fileText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        label = new Label(container, SWT.NULL);
        label.setText(HumantaskEditorConstants.WIZARD_TASK_NAME_LABEL);

        taskText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        taskText.setLayoutData(gd);
        taskText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });

        label = new Label(container, SWT.NULL);
        label.setText(HumantaskEditorConstants.WIZARD_TARGET_NAMESPACE_LABEL);

        tnsText = new Text(container, SWT.BORDER | SWT.SINGLE);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        tnsText.setLayoutData(gd);
        tnsText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                dialogChanged();
            }
        });
        initialize();
        dialogChanged();
        setControl(container);
        PlatformUI.getWorkbench().getHelpSystem()
                .setHelp(container, HumantaskEditorConstants.NEW_ARTIFACT_HELP_CONTEXT_ID);
    }

    /**
     * Tests if the current workbench selection is a suitable container to use.
     */
    private void initialize() {
        if (selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (structuredSelection.size() > 1) {
                return;
            }
            Object obj = structuredSelection.getFirstElement();
            if (obj instanceof IResource) {
                containerText.setText(HumantaskEditorConstants.WIZARD_PAGE_TEXT);
            }
        }
        fileText.setText(HumantaskEditorConstants.NEW_HT_FILE_NAME);
    }

    /**
     * Ensures that both text fields are set.
     */
    private void dialogChanged() {
        String fileName = getFileName();
        String taskName = getTaskName();
        String tnsName = getTNSName();

        if (getContainerName().length() == 0) {
            updateStatus(HumantaskEditorConstants.FILE_CONTAINER_MUST_BE_SPECIFIED_MESSAGE);
            return;
        }
        if (taskName.trim().isEmpty()) {
            updateStatus(HumantaskEditorConstants.TASK_NAME_CANNOT_BE_EMPTY_MESSAGE);
            return;
        }
        if (taskName.contains(" ")) {
            updateStatus(HumantaskEditorConstants.TASK_NAME_CANNOT_HAVE_SPACES_MESSAGE);
            return;
        }
        if (fileName.length() == 0) {
            updateStatus(HumantaskEditorConstants.FILE_NAME_MUST_BE_SPECIFIED_MESSAGE);
            return;
        }
        if (fileName.split("\\.").length != 0 && fileName.split("\\.")[0].length() == 0) {
            updateStatus(HumantaskEditorConstants.FILE_NAME_MUST_BE_VALID_MESSAGE);
            return;
        }
        if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
            updateStatus(HumantaskEditorConstants.FILE_NAME_MUST_BE_VALID_MESSAGE);
            return;
        }
        if (!fileName.matches(HumantaskEditorConstants.FILE_NAME_REGEX)) {
            updateStatus(HumantaskEditorConstants.ENTER_A_VALID_FILENAME);
            return;
        }
        if (tnsName.trim().isEmpty()) {
            updateStatus(HumantaskEditorConstants.TARGET_NAME_SPACE_CANNOT_BE_EMPTY_MESSAGE);
            return;
        }
        updateStatus(null);
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getContainerName() {
        return containerText.getText();
    }

    public String getFileName() {
        return fileText.getText();
    }

    public String getTaskName() {
        return taskText.getText();
    }

    public String getTNSName() {
        return tnsText.getText();
    }
}