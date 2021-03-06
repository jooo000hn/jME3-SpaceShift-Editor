package com.ss.editor.ui.control.model.property.control.particle.influencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.component.editor.impl.model.ModelFileEditor;
import com.ss.editor.ui.control.model.property.operation.ParticleInfluencerPropertyOperation;
import com.ss.editor.ui.control.property.AbstractPropertyControl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

import rlib.function.SixObjectConsumer;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The base implementation of the property control for the {@link ModelFileEditor}.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencerPropertyControl<D extends ParticleInfluencer, T> extends AbstractPropertyControl<ModelChangeConsumer, D, T> {

    @NotNull
    public static <D extends ParticleInfluencer, T> SixObjectConsumer<ModelChangeConsumer, D, String, T, T, BiConsumer<D, T>> newChangeHandler(@NotNull final Object parent) {
        return (changeConsumer, object, propName, newValue, oldValue, handler) -> {

            final ParticleInfluencerPropertyOperation<D, T> operation =
                    new ParticleInfluencerPropertyOperation<>(object, parent, propName, newValue, oldValue);
            operation.setApplyHandler(handler);

            changeConsumer.execute(operation);
        };
    }

    public ParticleInfluencerPropertyControl(@Nullable final T propertyValue, @NotNull final String propertyName,
                                             @NotNull final ModelChangeConsumer changeConsumer, @NotNull final Object parent) {
        super(propertyValue, propertyName, changeConsumer, newChangeHandler(parent));
    }
}
