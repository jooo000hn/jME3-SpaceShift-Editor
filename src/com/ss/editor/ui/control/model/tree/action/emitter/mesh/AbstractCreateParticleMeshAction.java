package com.ss.editor.ui.control.model.tree.action.emitter.mesh;

import static java.util.Objects.requireNonNull;

import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.model.tree.action.operation.ChangeParticleMeshOperation;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.scene.image.Image;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.particle.ParticleDataMeshInfo;

/**
 * The action to switch a particle mesh of the {@link ParticleGeometry} to another mesh.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateParticleMeshAction extends AbstractNodeAction<ModelChangeConsumer> {

    AbstractCreateParticleMeshAction(@NotNull final AbstractNodeTree<?> nodeTree, @NotNull final ModelNode<?> node) {
        super(nodeTree, node);
    }

    @Nullable
    @Override
    protected Image getIcon() {
        return Icons.MESH_16;
    }

    @Override
    protected void process() {

        final AbstractNodeTree<?> nodeTree = getNodeTree();

        final ModelNode<?> modelNode = getNode();
        final ParticleEmitterNode emitterNode = (ParticleEmitterNode) modelNode.getElement();
        final ParticleGeometry geometry = emitterNode.getParticleGeometry();
        final ParticleDataMeshInfo meshInfo = createMeshInfo();

        final ChangeConsumer changeConsumer = requireNonNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new ChangeParticleMeshOperation(meshInfo, geometry));
    }

    @NotNull
    protected abstract ParticleDataMeshInfo createMeshInfo();
}
