package com.ss.editor.ui.component.bar.action;

import com.ss.editor.JFXApplication;
import com.ss.editor.Messages;
import com.ss.editor.ui.dialog.SettingsDialog;
import com.ss.editor.ui.scene.EditorFXScene;

import javafx.scene.control.Button;

/**
 * The action for opening the dialog with settings.
 *
 * @author JavaSaBr.
 */
public class OpenSettingsAction extends Button {

    private static final JFXApplication JFX_APPLICATION = JFXApplication.getInstance();

    public OpenSettingsAction() {
        super(Messages.EDITOR_BAR_SETTINGS);
        setOnAction(event -> process());
    }

    /**
     * The process of opening.
     */
    private void process() {

        final EditorFXScene scene = JFX_APPLICATION.getScene();

        final SettingsDialog dialog = new SettingsDialog();
        dialog.show(scene.getWindow());
    }
}
