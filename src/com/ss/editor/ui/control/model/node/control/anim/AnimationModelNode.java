package com.ss.editor.ui.control.model.node.control.anim;

import static com.ss.editor.ui.control.tree.node.ModelNodeFactory.createFor;
import static java.util.Objects.requireNonNull;
import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Track;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.tree.ModelNodeTree;
import com.ss.editor.ui.control.model.tree.action.RenameNodeAction;
import com.ss.editor.ui.control.model.tree.action.animation.ManualExtractSubAnimationAction;
import com.ss.editor.ui.control.model.tree.action.animation.PlayAnimationAction;
import com.ss.editor.ui.control.model.tree.action.animation.RemoveAnimationAction;
import com.ss.editor.ui.control.model.tree.action.animation.StopAnimationAction;
import com.ss.editor.ui.control.model.tree.action.operation.animation.RenameAnimationNodeOperation;
import com.ss.editor.ui.control.tree.AbstractNodeTree;
import com.ss.editor.ui.control.tree.node.ModelNode;
import com.ss.editor.util.AnimationUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.util.ArrayUtils;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

/**
 * The implementation of node to show {@link Animation}.
 *
 * @author JavaSaBr
 */
public class AnimationModelNode extends ModelNode<Animation> {

    /**
     * The node of an animation control.
     */
    @Nullable
    private AnimationControlModelNode controlModelNode;

    /**
     * The animation control.
     */
    @Nullable
    private AnimControl control;

    /**
     * The index of playing animation.
     */
    private int channel;

    public AnimationModelNode(@NotNull final Animation element, final long objectId) {
        super(element, objectId);
        this.channel = -1;
    }

    @Override
    public void fillContextMenu(@NotNull final AbstractNodeTree<?> nodeTree,
                                @NotNull final ObservableList<MenuItem> items) {

        final Animation animation = getElement();
        final int frameCount = AnimationUtils.getFrameCount(animation);

        if (getChannel() < 0) {
            items.add(new PlayAnimationAction(nodeTree, this));
            items.add(new RemoveAnimationAction(nodeTree, this));
            items.add(new RenameNodeAction(nodeTree, this));
        } else {
            items.add(new StopAnimationAction(nodeTree, this));
        }

        if (getChannel() < 0 && frameCount > 0) {
            items.add(new ManualExtractSubAnimationAction(nodeTree, this));
        }

        super.fillContextMenu(nodeTree, items);
    }

    @Override
    public boolean hasChildren(@NotNull final AbstractNodeTree<?> nodeTree) {

        final Animation element = getElement();
        final Track[] tracks = element.getTracks();

        return tracks != null && tracks.length > 0 && nodeTree instanceof ModelNodeTree;
    }

    @NotNull
    @Override
    public Array<ModelNode<?>> getChildren(@NotNull final AbstractNodeTree<?> nodeTree) {

        final Animation element = getElement();
        final Track[] tracks = element.getTracks();

        final Array<ModelNode<?>> result = ArrayFactory.newArray(ModelNode.class, tracks.length);
        ArrayUtils.forEach(tracks, track -> result.add(createFor(track)));

        return result;
    }

    @Override
    public boolean canEditName() {
        return true;
    }

    @Override
    public void changeName(@NotNull final AbstractNodeTree<?> nodeTree, @NotNull final String newName) {
        if (StringUtils.equals(getName(), newName)) return;

        super.changeName(nodeTree, newName);

        final AnimationControlModelNode controlModelNode = requireNonNull(getControlModelNode());
        final AnimControl control = controlModelNode.getElement();
        final RenameAnimationNodeOperation operation = new RenameAnimationNodeOperation(getName(), newName, control);

        final ChangeConsumer changeConsumer = requireNonNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(operation);
    }

    /**
     * @return the node of an animation control.
     */
    @Nullable
    public AnimationControlModelNode getControlModelNode() {
        return controlModelNode;
    }

    /**
     * @param controlModelNode the node of an animation control.
     */
    public void setControlModelNode(@Nullable final AnimationControlModelNode controlModelNode) {
        this.controlModelNode = controlModelNode;
    }

    /**
     * @return the animation control.
     */
    @Nullable
    public AnimControl getControl() {
        return control;
    }

    /**
     * @param control the animation control.
     */
    public void setControl(@Nullable final AnimControl control) {
        this.control = control;
    }

    @NotNull
    @Override
    public String getName() {
        return getElement().getName();
    }

    /**
     * @return the index of playing animation.
     */
    public int getChannel() {
        return channel;
    }

    /**
     * @param channel the index of playing animation.
     */
    public void setChannel(final int channel) {
        this.channel = channel;
    }

    @Nullable
    @Override
    public Image getIcon() {
        return getChannel() < 0 ? Icons.PLAY_16 : Icons.STOP_16;
    }

    @Override
    public void notifyChildPreAdd(@NotNull final ModelNode<?> modelNode) {
        final AnimationTrackModelNode<?> animationTrackModelNode = (AnimationTrackModelNode<?>) modelNode;
        animationTrackModelNode.setControl(getControl());
        super.notifyChildPreAdd(modelNode);
    }
}
