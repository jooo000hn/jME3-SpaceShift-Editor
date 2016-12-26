package com.ss.editor.ui.control.model.tree.action.emitter.influerencer;

import com.ss.editor.Messages;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.node.ModelNode;

import org.jetbrains.annotations.NotNull;

import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.AlphaInfluencer;

/**
 * The action for creating the {@link AlphaInfluencer} for the {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class CreateAlphaParticleInfluencerAction extends AbstractCreateParticleInfluencerAction {

    public CreateAlphaParticleInfluencerAction(@NotNull final ModelNodeTree nodeTree, @NotNull final ModelNode<?> node) {
        super(nodeTree, node);
    }

    @NotNull
    @Override
    protected String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_EMITTER_CREATE_INFLUENCER_ALPHA;
    }

    @NotNull
    @Override
    protected ParticleInfluencer createInfluencer() {
        return new AlphaInfluencer();
    }
}
