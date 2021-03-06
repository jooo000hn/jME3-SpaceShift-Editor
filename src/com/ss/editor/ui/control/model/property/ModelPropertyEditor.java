package com.ss.editor.ui.control.model.property;

import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Spatial;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.AbstractPropertyEditor;
import com.ss.editor.util.NodeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.node.ParticleNode;

/**
 * The component to contains property controls in the editor.
 *
 * @author JavaSaBr
 */
public class ModelPropertyEditor extends AbstractPropertyEditor<ModelChangeConsumer> {

    public ModelPropertyEditor(@NotNull final ModelChangeConsumer changeConsumer) {
        super(changeConsumer);
    }

    protected boolean isNeedUpdate(@Nullable final Object object) {

        final Object currentObject = getCurrentObject();

        if (currentObject instanceof ParticleNode && object instanceof ParticleEmitterNode) {
            final Object parent = NodeUtils.findParent((Spatial) currentObject, spatial -> spatial instanceof ParticleEmitterNode);
            return parent == object;
        }

        return super.isNeedUpdate(object);
    }

    @Override
    protected boolean canEdit(@NotNull final Object object) {

        if (object instanceof Spatial) {
            final Object parent = NodeUtils.findParent((Spatial) object, spatial -> spatial instanceof AssetLinkNode);
            return parent == null || parent == object;
        }

        return super.canEdit(object);
    }
}
