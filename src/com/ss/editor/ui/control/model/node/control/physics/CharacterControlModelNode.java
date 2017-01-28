package com.ss.editor.ui.control.model.node.control.physics;

import com.jme3.bullet.control.CharacterControl;
import com.ss.editor.Messages;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.node.control.ControlModelNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of node to show {@link CharacterControl}.
 *
 * @author JavaSaBr
 */
public class CharacterControlModelNode extends ControlModelNode<CharacterControl> {

    public CharacterControlModelNode(@NotNull final CharacterControl element, final long objectId) {
        super(element, objectId);
    }

    @Nullable
    @Override
    public Image getIcon() {
        return Icons.CHARACTER_16;
    }

    @NotNull
    @Override
    public String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_CHARACTER_CONTROL;
    }
}
