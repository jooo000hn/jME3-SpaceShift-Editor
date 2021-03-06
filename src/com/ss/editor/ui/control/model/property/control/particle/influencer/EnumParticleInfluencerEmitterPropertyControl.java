package com.ss.editor.ui.control.model.property.control.particle.influencer;

import static com.ss.editor.ui.control.model.property.control.particle.influencer.ParticleInfluencerPropertyControl.newChangeHandler;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.model.property.control.ModelPropertyControl;
import com.ss.editor.ui.control.property.impl.AbstractEnumPropertyControl;

import org.jetbrains.annotations.NotNull;

import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link ModelPropertyControl} to edit the {@link Enum} values.
 *
 * @author JavaSaBr
 */
public class EnumParticleInfluencerEmitterPropertyControl<T extends ParticleInfluencer, E extends Enum<E>>
        extends AbstractEnumPropertyControl<ModelChangeConsumer, T, E> {

    public EnumParticleInfluencerEmitterPropertyControl(@NotNull final E element, @NotNull final String paramName,
                                                        @NotNull final ModelChangeConsumer modelChangeConsumer,
                                                        @NotNull final E[] availableValues, final @NotNull Object parent) {
        super(element, paramName, modelChangeConsumer, availableValues, newChangeHandler(parent));
    }
}
