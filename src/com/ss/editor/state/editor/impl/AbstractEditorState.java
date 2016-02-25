package com.ss.editor.state.editor.impl;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.ss.editor.Editor;
import com.ss.editor.state.editor.EditorState;

/**
 * Базовая реализация.
 *
 * @author Ronn
 */
public abstract class AbstractEditorState extends AbstractAppState implements EditorState {

    protected static final Editor EDITOR = Editor.getInstance();

    /**
     * Опциональная камера для сцены.
     */
    private final ChaseCamera chaseCamera;

    /**
     * Рутовый узел.
     */
    private final Node stateNode;

    public AbstractEditorState() {
        this.stateNode = new Node(getClass().getSimpleName());
        this.chaseCamera = needChaseCamera()? createChaseCamera() : null;
    }

    /**
     * @return рутовый узел.
     */
    protected Node getStateNode() {
        return stateNode;
    }

    /**
     * @return опциональная камера для сцены.
     */
    protected ChaseCamera getChaseCamera() {
        return chaseCamera;
    }

    @Override
    public void initialize(final AppStateManager stateManager, final Application application) {
        super.initialize(stateManager, application);

        final Node rootNode = EDITOR.getRootNode();
        rootNode.attachChild(getStateNode());

        final ChaseCamera chaseCamera = getChaseCamera();

        if(chaseCamera != null) {
            chaseCamera.setEnabled(true);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();

        final Node rootNode = EDITOR.getRootNode();
        rootNode.detachChild(getStateNode());

        final ChaseCamera chaseCamera = getChaseCamera();

        if(chaseCamera != null) {
            chaseCamera.setEnabled(false);
        }
    }

    /**
     * Нужна ли камера для этой части.
     */
    protected boolean needChaseCamera() {
        return false;
    }

    protected ChaseCamera createChaseCamera() {

        final Camera camera = EDITOR.getCamera();

        final ChaseCamera chaser = new ChaseCamera(camera, stateNode, EDITOR.getInputManager());
        chaser.setDragToRotate(true);
        chaser.setMinVerticalRotation(-FastMath.HALF_PI);
        chaser.setMaxDistance(1000);
        chaser.setSmoothMotion(true);
        chaser.setRotationSensitivity(10);
        chaser.setZoomSensitivity(5);

        return chaser;
    }
}
