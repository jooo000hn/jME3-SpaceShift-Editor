package com.ss.editor.model.undo.editor;

import com.ss.editor.annotation.FXThread;
import com.ss.extension.scene.SceneNode;
import com.ss.extension.scene.app.state.SceneAppState;
import com.ss.extension.scene.filter.SceneFilter;

import org.jetbrains.annotations.NotNull;

/**
 * The interface to notify about any changes of a scene.
 *
 * @author JavaSaBr
 */
public interface SceneChangeConsumer extends ModelChangeConsumer {

    /**
     * Notify about added an app state.
     */
    @FXThread
    void notifyAddedAppState(@NotNull SceneAppState appState);

    /**
     * Notify about removed an app state.
     */
    @FXThread
    void notifyRemovedAppState(@NotNull SceneAppState appState);

    /**
     * Notify about changed an app state.
     */
    @FXThread
    void notifyChangedAppState(@NotNull SceneAppState appState);

    /**
     * Notify about added a filter.
     */
    @FXThread
    void notifyAddedFilter(@NotNull SceneFilter<?> sceneFilter);

    /**
     * Notify about removed a filter.
     */
    @FXThread
    void notifyRemovedFilter(@NotNull SceneFilter<?> sceneFilter);

    /**
     * Notify about changed a filter.
     */
    @FXThread
    void notifyChangedFilter(@NotNull SceneFilter<?> sceneFilter);

    @NotNull
    @Override
    SceneNode getCurrentModel();
}
