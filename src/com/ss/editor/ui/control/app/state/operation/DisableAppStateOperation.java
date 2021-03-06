package com.ss.editor.ui.control.app.state.operation;

import com.ss.editor.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;
import com.ss.extension.scene.app.state.SceneAppState;

import org.jetbrains.annotations.NotNull;

/**
 * The implementation of the {@link AbstractEditorOperation} to disable a {@link SceneAppState}.
 *
 * @author JavaSaBr
 */
public class DisableAppStateOperation extends AbstractEditorOperation<SceneChangeConsumer> {

    /**
     * The scene app state.
     */
    @NotNull
    private final SceneAppState appState;

    public DisableAppStateOperation(@NotNull final SceneAppState appState) {
        this.appState = appState;
    }

    @Override
    protected void redoImpl(@NotNull final SceneChangeConsumer editor) {
        EXECUTOR_MANAGER.addEditorThreadTask(() -> {
            appState.setEnabled(false);
            EXECUTOR_MANAGER.addFXTask(() -> editor.notifyChangedAppState(appState));
        });
    }

    @Override
    protected void undoImpl(@NotNull final SceneChangeConsumer editor) {
        EXECUTOR_MANAGER.addEditorThreadTask(() -> {
            appState.setEnabled(true);
            EXECUTOR_MANAGER.addFXTask(() -> editor.notifyChangedAppState(appState));
        });
    }
}
