package com.ss.editor.ui.control.model.property.builder.impl;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.model.property.control.particle.influencer.BooleanParticleInfluencerPropertyControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.EnumParticleInfluencerEmitterPropertyControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.FloatParticleInfluencerPropertyControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.IntArrayParticleInfluencerPropertyControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.PhysicsNodeListControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.Vector3fParticleInfluencerPropertyControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.interpolation.control.AlphaInfluencerControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.interpolation.control.ColorInfluencerControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.interpolation.control.DestinationInfluencerControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.interpolation.control.RotationInfluencerControl;
import com.ss.editor.ui.control.model.property.control.particle.influencer.interpolation.control.SizeInfluencerControl;
import com.ss.editor.ui.control.property.builder.PropertyBuilder;
import com.ss.editor.ui.control.property.builder.impl.AbstractPropertyBuilder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javafx.scene.layout.VBox;
import rlib.ui.util.FXUtils;
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.AlphaInfluencer;
import tonegod.emitter.influencers.impl.ColorInfluencer;
import tonegod.emitter.influencers.impl.DestinationInfluencer;
import tonegod.emitter.influencers.impl.GravityInfluencer;
import tonegod.emitter.influencers.impl.GravityInfluencer.GravityAlignment;
import tonegod.emitter.influencers.impl.ImpulseInfluencer;
import tonegod.emitter.influencers.impl.PhysicsInfluencer;
import tonegod.emitter.influencers.impl.PhysicsInfluencer.CollisionReaction;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer.RadialPullAlignment;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer.RadialPullCenter;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer.RadialUpAlignment;
import tonegod.emitter.influencers.impl.RotationInfluencer;
import tonegod.emitter.influencers.impl.SizeInfluencer;
import tonegod.emitter.influencers.impl.SpriteInfluencer;

/**
 * The implementation of the {@link PropertyBuilder} to build property controls for {@link ParticleInfluencer}.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencerPropertyBuilder extends AbstractPropertyBuilder<ModelChangeConsumer> {

    private static final GravityAlignment[] GRAVITY_ALIGNMENTS = GravityAlignment.values();
    private static final CollisionReaction[] COLLISION_REACTIONS = CollisionReaction.values();
    private static final RadialPullCenter[] RADIAL_PULL_CENTERS = RadialPullCenter.values();
    private static final RadialPullAlignment[] RADIAL_PULL_ALIGNMENTS = RadialPullAlignment.values();
    private static final RadialUpAlignment[] RADIAL_UP_ALIGNMENTS = RadialUpAlignment.values();

    @NotNull
    private static final PropertyBuilder INSTANCE = new ParticleInfluencerPropertyBuilder();

    @NotNull
    public static PropertyBuilder getInstance() {
        return INSTANCE;
    }

    private ParticleInfluencerPropertyBuilder() {
        super(ModelChangeConsumer.class);
    }

    @Override
    protected void buildForImpl(@NotNull final Object object, @Nullable final Object parent, @NotNull final VBox container,
                                @NotNull final ModelChangeConsumer changeConsumer) {

        if (!(object instanceof ParticleInfluencer) || parent == null) return;

        if (object instanceof AlphaInfluencer) {
            createControls(container, changeConsumer, (AlphaInfluencer) object, parent);
        } else if (object instanceof ColorInfluencer) {
            createControls(container, changeConsumer, (ColorInfluencer) object, parent);
        } else if (object instanceof SizeInfluencer) {
            createControls(container, changeConsumer, (SizeInfluencer) object, parent);
        } else if (object instanceof DestinationInfluencer) {
            createControls(container, changeConsumer, (DestinationInfluencer) object, parent);
        } else if (object instanceof ImpulseInfluencer) {
            createControls(container, changeConsumer, (ImpulseInfluencer) object, parent);
        } else if (object instanceof GravityInfluencer) {
            createControls(container, changeConsumer, (GravityInfluencer) object, parent);
        } else if (object instanceof RadialVelocityInfluencer) {
            createControls(container, changeConsumer, (RadialVelocityInfluencer) object, parent);
        } else if (object instanceof RotationInfluencer) {
            createControls(container, changeConsumer, (RotationInfluencer) object, parent);
        } else if (object instanceof PhysicsInfluencer) {
            createControls(container, changeConsumer, (PhysicsInfluencer) object, parent);
        } else if (object instanceof SpriteInfluencer) {
            createControls(container, changeConsumer, (SpriteInfluencer) object, parent);
        }

        if (object instanceof InterpolatedParticleInfluencer) {
            createControls(container, changeConsumer, (InterpolatedParticleInfluencer) object, parent);
        }
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final InterpolatedParticleInfluencer influencer, @NotNull final Object parent) {

        final float fixedDuration = influencer.getFixedDuration();

        final FloatParticleInfluencerPropertyControl<InterpolatedParticleInfluencer> fixedDurationControl =
                new FloatParticleInfluencerPropertyControl<>(fixedDuration, Messages.PARTICLE_EMITTER_INFLUENCER_FIXED_DURATION, changeConsumer, parent);

        fixedDurationControl.setSyncHandler(InterpolatedParticleInfluencer::getFixedDuration);
        fixedDurationControl.setApplyHandler(InterpolatedParticleInfluencer::setFixedDuration);
        fixedDurationControl.setEditObject(influencer);

        FXUtils.addToPane(fixedDurationControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final AlphaInfluencer influencer, @NotNull final Object parent) {

        final AlphaInfluencerControl influencerControl = new AlphaInfluencerControl(changeConsumer, influencer, parent);
        influencerControl.reload();

        FXUtils.addToPane(influencerControl, container);

        addSplitLine(container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final ColorInfluencer influencer, @NotNull final Object parent) {

        final boolean randomStartColor = influencer.isRandomStartColor();

        final ColorInfluencerControl colorControl = new ColorInfluencerControl(changeConsumer, influencer, parent);
        colorControl.reload();

        final BooleanParticleInfluencerPropertyControl<ColorInfluencer> randomStartColorControl =
                new BooleanParticleInfluencerPropertyControl<>(randomStartColor, Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_START_COLOR, changeConsumer, parent);

        randomStartColorControl.setSyncHandler(ColorInfluencer::isRandomStartColor);
        randomStartColorControl.setApplyHandler(ColorInfluencer::setRandomStartColor);
        randomStartColorControl.setEditObject(influencer);

        FXUtils.addToPane(colorControl, container);

        addSplitLine(container);

        FXUtils.addToPane(randomStartColorControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final SizeInfluencer influencer, @NotNull final Object parent) {

        final boolean randomSize = influencer.isRandomSize();

        final float randomSizeTolerance = influencer.getRandomSizeTolerance();

        final SizeInfluencerControl sizeControl = new SizeInfluencerControl(changeConsumer, influencer, parent);
        sizeControl.reload();

        final BooleanParticleInfluencerPropertyControl<SizeInfluencer> randomStartSizeControl =
                new BooleanParticleInfluencerPropertyControl<>(randomSize,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_START_SIZE, changeConsumer, parent);

        randomStartSizeControl.setSyncHandler(SizeInfluencer::isRandomSize);
        randomStartSizeControl.setApplyHandler(SizeInfluencer::setRandomSize);
        randomStartSizeControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<SizeInfluencer> sizeVariationToleranceControl =
                new FloatParticleInfluencerPropertyControl<>(randomSizeTolerance,
                        Messages.PARTICLE_EMITTER_INFLUENCER_SIZE_VARIATION_FACTOR, changeConsumer, parent);

        sizeVariationToleranceControl.setSyncHandler(SizeInfluencer::getRandomSizeTolerance);
        sizeVariationToleranceControl.setApplyHandler(SizeInfluencer::setRandomSizeTolerance);
        sizeVariationToleranceControl.setEditObject(influencer);

        FXUtils.addToPane(sizeControl, container);

        addSplitLine(container);

        FXUtils.addToPane(randomStartSizeControl, container);
        FXUtils.addToPane(sizeVariationToleranceControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final SpriteInfluencer influencer, @NotNull final Object parent) {

        final int[] frameSequence = influencer.getFrameSequence();
        final float fixedDuration = influencer.getFixedDuration();
        final boolean animate = influencer.isAnimate();
        final boolean randomStartImage = influencer.isRandomStartImage();

        final IntArrayParticleInfluencerPropertyControl<SpriteInfluencer> frameSequenceControl =
                new IntArrayParticleInfluencerPropertyControl<>(frameSequence,
                        Messages.PARTICLE_EMITTER_INFLUENCER_FRAME_SEQUENCE, changeConsumer, parent);

        frameSequenceControl.setSyncHandler(SpriteInfluencer::getFrameSequence);
        frameSequenceControl.setApplyHandler(SpriteInfluencer::setFrameSequence);
        frameSequenceControl.setEditObject(influencer);

        final BooleanParticleInfluencerPropertyControl<SpriteInfluencer> randomStartImageControl =
                new BooleanParticleInfluencerPropertyControl<>(randomStartImage,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_START_IMAGE, changeConsumer, parent);

        randomStartImageControl.setSyncHandler(SpriteInfluencer::isRandomStartImage);
        randomStartImageControl.setApplyHandler(SpriteInfluencer::setRandomStartImage);
        randomStartImageControl.setEditObject(influencer);

        final BooleanParticleInfluencerPropertyControl<SpriteInfluencer> animateControl =
                new BooleanParticleInfluencerPropertyControl<>(animate,
                        Messages.PARTICLE_EMITTER_INFLUENCER_ANIMATE, changeConsumer, parent);

        animateControl.setSyncHandler(SpriteInfluencer::isAnimate);
        animateControl.setApplyHandler(SpriteInfluencer::setAnimate);
        animateControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<SpriteInfluencer> fixedDurationControl =
                new FloatParticleInfluencerPropertyControl<>(fixedDuration,
                        Messages.PARTICLE_EMITTER_INFLUENCER_FIXED_DURATION, changeConsumer, parent);

        fixedDurationControl.setSyncHandler(SpriteInfluencer::getFixedDuration);
        fixedDurationControl.setApplyHandler(SpriteInfluencer::setFixedDuration);
        fixedDurationControl.setEditObject(influencer);

        FXUtils.addToPane(frameSequenceControl, container);
        FXUtils.addToPane(randomStartImageControl, container);
        FXUtils.addToPane(animateControl, container);
        FXUtils.addToPane(fixedDurationControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final DestinationInfluencer influencer, @NotNull final Object parent) {

        final boolean randomStartDestination = influencer.isRandomStartDestination();

        final DestinationInfluencerControl influencerControl = new DestinationInfluencerControl(changeConsumer, influencer, parent);
        influencerControl.reload();

        final BooleanParticleInfluencerPropertyControl<DestinationInfluencer> randomStartDestinationControl =
                new BooleanParticleInfluencerPropertyControl<>(randomStartDestination,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_START_DESTINATION, changeConsumer, parent);

        randomStartDestinationControl.setSyncHandler(DestinationInfluencer::isRandomStartDestination);
        randomStartDestinationControl.setApplyHandler(DestinationInfluencer::setRandomStartDestination);
        randomStartDestinationControl.setEditObject(influencer);

        FXUtils.addToPane(influencerControl, container);

        addSplitLine(container);

        FXUtils.addToPane(randomStartDestinationControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final ImpulseInfluencer influencer, @NotNull final Object parent) {

        final float chance = influencer.getChance();
        final float strength = influencer.getStrength();
        final float magnitude = influencer.getMagnitude();

        final FloatParticleInfluencerPropertyControl<ImpulseInfluencer> chanceControl =
                new FloatParticleInfluencerPropertyControl<>(chance,
                        Messages.PARTICLE_EMITTER_INFLUENCER_CHANCE, changeConsumer, parent);

        chanceControl.setSyncHandler(ImpulseInfluencer::getChance);
        chanceControl.setApplyHandler(ImpulseInfluencer::setChance);
        chanceControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<ImpulseInfluencer> strengthControl =
                new FloatParticleInfluencerPropertyControl<>(strength,
                        Messages.PARTICLE_EMITTER_INFLUENCER_STRENGTH, changeConsumer, parent);

        strengthControl.setSyncHandler(ImpulseInfluencer::getStrength);
        strengthControl.setApplyHandler(ImpulseInfluencer::setStrength);
        strengthControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<ImpulseInfluencer> magnitudeControl =
                new FloatParticleInfluencerPropertyControl<>(magnitude,
                        Messages.PARTICLE_EMITTER_INFLUENCER_MAGNITUDE, changeConsumer, parent);

        magnitudeControl.setSyncHandler(ImpulseInfluencer::getMagnitude);
        magnitudeControl.setApplyHandler(ImpulseInfluencer::setMagnitude);
        magnitudeControl.setEditObject(influencer);

        FXUtils.addToPane(chanceControl, container);
        FXUtils.addToPane(strengthControl, container);
        FXUtils.addToPane(magnitudeControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final GravityInfluencer influencer, @NotNull final Object parent) {

        final Vector3f gravity = influencer.getGravity().clone();
        final GravityAlignment alignment = influencer.getAlignment();

        final float magnitude = influencer.getMagnitude();

        final Vector3fParticleInfluencerPropertyControl<GravityInfluencer> gravityControl =
                new Vector3fParticleInfluencerPropertyControl<>(gravity,
                        Messages.PARTICLE_EMITTER_INFLUENCER_GRAVITY, changeConsumer, parent);

        gravityControl.setSyncHandler(GravityInfluencer::getGravity);
        gravityControl.setApplyHandler(GravityInfluencer::setGravity);
        gravityControl.setEditObject(influencer);

        final EnumParticleInfluencerEmitterPropertyControl<GravityInfluencer, GravityAlignment> gravityAlignmentControl =
                new EnumParticleInfluencerEmitterPropertyControl<>(alignment,
                        Messages.PARTICLE_EMITTER_INFLUENCER_ALIGNMENT, changeConsumer, GRAVITY_ALIGNMENTS, parent);

        gravityAlignmentControl.setSyncHandler(GravityInfluencer::getAlignment);
        gravityAlignmentControl.setApplyHandler(GravityInfluencer::setAlignment);
        gravityAlignmentControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<GravityInfluencer> magnitudeControl =
                new FloatParticleInfluencerPropertyControl<>(magnitude,
                        Messages.PARTICLE_EMITTER_INFLUENCER_MAGNITUDE, changeConsumer, parent);

        magnitudeControl.setSyncHandler(GravityInfluencer::getMagnitude);
        magnitudeControl.setApplyHandler(GravityInfluencer::setMagnitude);
        magnitudeControl.setEditObject(influencer);

        FXUtils.addToPane(gravityAlignmentControl, container);
        FXUtils.addToPane(magnitudeControl, container);
        FXUtils.addToPane(gravityControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final PhysicsInfluencer influencer, @NotNull final Object parent) {

        final CollisionReaction collisionReaction = influencer.getCollisionReaction();
        final float restitution = influencer.getRestitution();

        final PhysicsNodeListControl nodeListControl = new PhysicsNodeListControl(changeConsumer, influencer, parent);
        nodeListControl.reload();

        final EnumParticleInfluencerEmitterPropertyControl<PhysicsInfluencer, CollisionReaction> reactionControl =
                new EnumParticleInfluencerEmitterPropertyControl<>(collisionReaction,
                        Messages.PARTICLE_EMITTER_INFLUENCER_REACTION, changeConsumer, COLLISION_REACTIONS, parent);

        reactionControl.setSyncHandler(PhysicsInfluencer::getCollisionReaction);
        reactionControl.setApplyHandler(PhysicsInfluencer::setCollisionReaction);
        reactionControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<PhysicsInfluencer> restitutionControl =
                new FloatParticleInfluencerPropertyControl<>(restitution,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RESTITUTION, changeConsumer, parent);

        restitutionControl.setSyncHandler(PhysicsInfluencer::getRestitution);
        restitutionControl.setApplyHandler(PhysicsInfluencer::setRestitution);
        restitutionControl.setEditObject(influencer);

        FXUtils.addToPane(nodeListControl, container);
        FXUtils.addToPane(reactionControl, container);
        FXUtils.addToPane(restitutionControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final RadialVelocityInfluencer influencer, @NotNull final Object parent) {

        final RadialPullCenter pullCenter = influencer.getRadialPullCenter();
        final RadialPullAlignment pullAlignment = influencer.getRadialPullAlignment();
        final RadialUpAlignment upAlignment = influencer.getRadialUpAlignment();

        final float tangentForce = influencer.getTangentForce();
        final float radialPull = influencer.getRadialPull();

        final boolean randomDirection = influencer.isRandomDirection();

        final BooleanParticleInfluencerPropertyControl<RadialVelocityInfluencer> randomDirectionControl =
                new BooleanParticleInfluencerPropertyControl<>(randomDirection,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_DIRECTION, changeConsumer, parent);

        randomDirectionControl.setSyncHandler(RadialVelocityInfluencer::isRandomDirection);
        randomDirectionControl.setApplyHandler(RadialVelocityInfluencer::setRandomDirection);
        randomDirectionControl.setEditObject(influencer);

        final EnumParticleInfluencerEmitterPropertyControl<RadialVelocityInfluencer, RadialPullCenter> pullCenterControl =
                new EnumParticleInfluencerEmitterPropertyControl<>(pullCenter,
                        Messages.PARTICLE_EMITTER_INFLUENCER_PULL_CENTER, changeConsumer, RADIAL_PULL_CENTERS, parent);

        pullCenterControl.setSyncHandler(RadialVelocityInfluencer::getRadialPullCenter);
        pullCenterControl.setApplyHandler(RadialVelocityInfluencer::setRadialPullCenter);
        pullCenterControl.setEditObject(influencer);

        final EnumParticleInfluencerEmitterPropertyControl<RadialVelocityInfluencer, RadialPullAlignment> pullAlignmentControl =
                new EnumParticleInfluencerEmitterPropertyControl<>(pullAlignment,
                        Messages.PARTICLE_EMITTER_INFLUENCER_PULL_ALIGNMENT, changeConsumer, RADIAL_PULL_ALIGNMENTS, parent);

        pullAlignmentControl.setSyncHandler(RadialVelocityInfluencer::getRadialPullAlignment);
        pullAlignmentControl.setApplyHandler(RadialVelocityInfluencer::setRadialPullAlignment);
        pullAlignmentControl.setEditObject(influencer);

        final EnumParticleInfluencerEmitterPropertyControl<RadialVelocityInfluencer, RadialUpAlignment> upAlignmentControl =
                new EnumParticleInfluencerEmitterPropertyControl<>(upAlignment,
                        Messages.PARTICLE_EMITTER_INFLUENCER_UP_ALIGNMENT, changeConsumer, RADIAL_UP_ALIGNMENTS, parent);

        upAlignmentControl.setSyncHandler(RadialVelocityInfluencer::getRadialUpAlignment);
        upAlignmentControl.setApplyHandler(RadialVelocityInfluencer::setRadialUpAlignment);
        upAlignmentControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<RadialVelocityInfluencer> radialPullControl =
                new FloatParticleInfluencerPropertyControl<>(radialPull,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RADIAL_PULL, changeConsumer, parent);

        radialPullControl.setSyncHandler(RadialVelocityInfluencer::getRadialPull);
        radialPullControl.setApplyHandler(RadialVelocityInfluencer::setRadialPull);
        radialPullControl.setEditObject(influencer);

        final FloatParticleInfluencerPropertyControl<RadialVelocityInfluencer> tangetForceControl =
                new FloatParticleInfluencerPropertyControl<>(tangentForce,
                        Messages.PARTICLE_EMITTER_INFLUENCER_TANGENT_FORCE, changeConsumer, parent);

        tangetForceControl.setSyncHandler(RadialVelocityInfluencer::getTangentForce);
        tangetForceControl.setApplyHandler(RadialVelocityInfluencer::setTangentForce);
        tangetForceControl.setEditObject(influencer);

        FXUtils.addToPane(randomDirectionControl, container);
        FXUtils.addToPane(pullCenterControl, container);
        FXUtils.addToPane(pullAlignmentControl, container);
        FXUtils.addToPane(upAlignmentControl, container);
        FXUtils.addToPane(radialPullControl, container);
        FXUtils.addToPane(tangetForceControl, container);
    }

    protected void createControls(final @NotNull VBox container, final @NotNull ModelChangeConsumer changeConsumer,
                                  @NotNull final RotationInfluencer influencer, @NotNull final Object parent) {

        final boolean randomDirection = influencer.isRandomDirection();
        final boolean randomSpeed = influencer.isRandomSpeed();
        final boolean randomStartRotationX = influencer.isRandomStartRotationX();
        final boolean randomStartRotationY = influencer.isRandomStartRotationY();
        final boolean randomStartRotationZ = influencer.isRandomStartRotationZ();

        final RotationInfluencerControl influencerControl = new RotationInfluencerControl(changeConsumer, influencer, parent);
        influencerControl.reload();

        final BooleanParticleInfluencerPropertyControl<RotationInfluencer> randomDirectionControl =
                new BooleanParticleInfluencerPropertyControl<>(randomDirection,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_DIRECTION, changeConsumer, parent);

        randomDirectionControl.setSyncHandler(RotationInfluencer::isRandomDirection);
        randomDirectionControl.setApplyHandler(RotationInfluencer::setRandomDirection);
        randomDirectionControl.setEditObject(influencer);

        final BooleanParticleInfluencerPropertyControl<RotationInfluencer> randomSpeedControl =
                new BooleanParticleInfluencerPropertyControl<>(randomSpeed,
                        Messages.PARTICLE_EMITTER_INFLUENCER_RANDOM_SPEED, changeConsumer, parent);

        randomSpeedControl.setSyncHandler(RotationInfluencer::isRandomSpeed);
        randomSpeedControl.setApplyHandler(RotationInfluencer::setRandomSpeed);
        randomSpeedControl.setEditObject(influencer);

        final BooleanParticleInfluencerPropertyControl<RotationInfluencer> randomStartRotationXControl =
                new BooleanParticleInfluencerPropertyControl<>(randomStartRotationX,
                        Messages.PARTICLE_EMITTER_INFLUENCER_START_RANDOM_ROTATION_X + " X", changeConsumer, parent);

        randomStartRotationXControl.setSyncHandler(RotationInfluencer::isRandomStartRotationX);
        randomStartRotationXControl.setApplyHandler(RotationInfluencer::setRandomStartRotationX);
        randomStartRotationXControl.setEditObject(influencer);

        final BooleanParticleInfluencerPropertyControl<RotationInfluencer> randomStartRotationYControl =
                new BooleanParticleInfluencerPropertyControl<>(randomStartRotationY,
                        Messages.PARTICLE_EMITTER_INFLUENCER_START_RANDOM_ROTATION_X + " Y", changeConsumer, parent);

        randomStartRotationYControl.setSyncHandler(RotationInfluencer::isRandomStartRotationY);
        randomStartRotationYControl.setApplyHandler(RotationInfluencer::setRandomStartRotationY);
        randomStartRotationYControl.setEditObject(influencer);

        final BooleanParticleInfluencerPropertyControl<RotationInfluencer> randomStartRotationZControl =
                new BooleanParticleInfluencerPropertyControl<>(randomStartRotationZ,
                        Messages.PARTICLE_EMITTER_INFLUENCER_START_RANDOM_ROTATION_X + " Z", changeConsumer, parent);

        randomStartRotationZControl.setSyncHandler(RotationInfluencer::isRandomStartRotationZ);
        randomStartRotationZControl.setApplyHandler(RotationInfluencer::setRandomStartRotationZ);
        randomStartRotationZControl.setEditObject(influencer);

        FXUtils.addToPane(influencerControl, container);

        addSplitLine(container);

        FXUtils.addToPane(randomDirectionControl, container);
        FXUtils.addToPane(randomSpeedControl, container);
        FXUtils.addToPane(randomStartRotationXControl, container);
        FXUtils.addToPane(randomStartRotationYControl, container);
        FXUtils.addToPane(randomStartRotationZControl, container);
    }
}
