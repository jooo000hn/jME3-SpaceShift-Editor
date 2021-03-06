package com.ss.editor.ui.control.model.tree.action.control;

import static java.util.Objects.requireNonNull;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.AddControlOperation;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;
import org.jetbrains.annotations.NotNull;

/**
 * The action to create the {@link Control}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateControlAction extends AbstractNodeAction<ModelChangeConsumer> {

    public AbstractCreateControlAction(@NotNull final AbstractNodeTree<?> nodeTree, @NotNull final ModelNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    protected void process() {

        final ModelNode<?> modelNode = getNode();
        final Spatial parent = (Spatial) modelNode.getElement();

        final AbstractNodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final Control control = createControl(parent);

        final ModelChangeConsumer consumer = requireNonNull(nodeTree.getChangeConsumer());
        consumer.execute(new AddControlOperation(control, parent));
    }

    @NotNull
    protected abstract Control createControl(@NotNull final Spatial parent);
}