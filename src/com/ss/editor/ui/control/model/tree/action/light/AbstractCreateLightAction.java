package com.ss.editor.ui.control.model.tree.action.light;

import static java.util.Objects.requireNonNull;

import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.AddLightOperation;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;

import org.jetbrains.annotations.NotNull;

/**
 * The action to create a {@link Light}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateLightAction extends AbstractNodeAction<ModelChangeConsumer> {

    public AbstractCreateLightAction(@NotNull final AbstractNodeTree<?> nodeTree, @NotNull final ModelNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    protected void process() {

        final AbstractNodeTree<?> nodeTree = getNodeTree();

        final Light light = createLight();
        final ModelNode<?> modelNode = getNode();
        final Node element = (Node) modelNode.getElement();

        final ChangeConsumer changeConsumer = requireNonNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new AddLightOperation(light, element));
    }

    @NotNull
    protected abstract Light createLight();
}
