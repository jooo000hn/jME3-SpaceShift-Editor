package com.ss.editor.ui.control.model.tree.action.physics.shape;

import static java.util.Objects.requireNonNull;
import static rlib.util.ClassUtils.unsafeCast;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.scene.Spatial;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The action to create a new shape.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateShapeAction<T extends PhysicsCollisionObject> extends
        AbstractNodeAction<ModelChangeConsumer> {

    AbstractCreateShapeAction(@NotNull final AbstractNodeTree<?> nodeTree, @NotNull final ModelNode<?> node) {
        super(nodeTree, node);
    }

    @Nullable
    @Override
    protected Image getIcon() {
        return Icons.GEOMETRY_16;
    }

    @Override
    protected void process() {

        final AbstractNodeTree<?> nodeTree = getNodeTree();

        final ModelNode<?> modelNode = getNode();
        final ModelNode<?> parentNode = requireNonNull(modelNode.getParent());

        final Spatial parentElement = (Spatial) requireNonNull(parentNode.getElement());
        final T element = unsafeCast(modelNode.getElement());

        createShape(element, parentElement, nodeTree);
    }

    protected abstract void createShape(@NotNull final T object, @NotNull final Spatial parentElement,
                                        @NotNull final AbstractNodeTree<?> nodeTree);
}
