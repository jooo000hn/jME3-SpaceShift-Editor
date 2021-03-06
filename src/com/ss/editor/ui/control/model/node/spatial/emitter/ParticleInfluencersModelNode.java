package com.ss.editor.ui.control.model.node.spatial.emitter;

import static com.ss.editor.ui.control.tree.node.ModelNodeFactory.createFor;
import static rlib.util.ClassUtils.getConstructor;
import static rlib.util.ClassUtils.newInstance;
import com.ss.editor.Messages;
import com.ss.editor.model.node.ParticleInfluencers;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.action.emitter.influerencer.*;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.*;

import java.lang.reflect.Constructor;

/**
 * The implementation of the {@link ModelNode} for representing the {@link ParticleInfluencers} in the editor.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencersModelNode extends ModelNode<ParticleInfluencers> {

    @NotNull
    private static final ObjectDictionary<Class<? extends ParticleInfluencer>, Constructor<? extends MenuItem>> CONSTRUCTORS =
            DictionaryFactory.newObjectDictionary();

    static {
        CONSTRUCTORS.put(AlphaInfluencer.class, getConstructor(CreateAlphaParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(ColorInfluencer.class, getConstructor(CreateColorParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(DestinationInfluencer.class, getConstructor(CreateDestinationParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(GravityInfluencer.class, getConstructor(CreateGravityParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(ImpulseInfluencer.class, getConstructor(CreateImpulseParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(PhysicsInfluencer.class, getConstructor(CreatePhysicsParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(RadialVelocityInfluencer.class, getConstructor(CreateRadialVelocityParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(RotationInfluencer.class, getConstructor(CreateRotationParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(SizeInfluencer.class, getConstructor(CreateSizeParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
        CONSTRUCTORS.put(SpriteInfluencer.class, getConstructor(CreateSpriteParticleInfluencerAction.class, AbstractNodeTree.class, ModelNode.class));
    }

    public ParticleInfluencersModelNode(@NotNull final ParticleInfluencers element, final long objectId) {
        super(element, objectId);
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_EMITTER_INFLUENCERS;
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.NODE_16;
    }

    @Override
    public void fillContextMenu(@NotNull final AbstractNodeTree<?> nodeTree, @NotNull final ObservableList<MenuItem> items) {

        final ParticleInfluencers element = getElement();
        final ParticleEmitterNode emitterNode = element.getEmitterNode();

        final Menu createMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_CREATE, new ImageView(Icons.ADD_12));
        final ObservableList<MenuItem> createItems = createMenu.getItems();

        CONSTRUCTORS.forEach((type, constructor) -> {
            if (emitterNode.getInfluencer(type) != null) return;
            createItems.add(newInstance(constructor, nodeTree, this));
        });

        items.add(createMenu);

        super.fillContextMenu(nodeTree, items);
    }

    @NotNull
    @Override
    public Array<ModelNode<?>> getChildren(@NotNull final AbstractNodeTree<?> nodeTree) {
        final Array<ModelNode<?>> result = ArrayFactory.newArray(ModelNode.class);
        final ParticleInfluencers element = getElement();
        final Array<ParticleInfluencer> influencers = element.getInfluencers();
        influencers.forEach(result, (influencer, toStore) -> toStore.add(createFor(influencer)));
        return result;
    }

    @Override
    public boolean hasChildren(@NotNull final AbstractNodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }
}
