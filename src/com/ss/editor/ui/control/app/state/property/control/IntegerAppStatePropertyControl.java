package com.ss.editor.ui.control.app.state.property.control;

import static com.ss.editor.ui.control.app.state.property.control.AppStatePropertyControl.newChangeHandler;

import com.ss.editor.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.ui.control.property.AbstractPropertyControl;
import com.ss.editor.ui.control.property.impl.AbstractIntegerPropertyControl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The implementation of the {@link AbstractPropertyControl} to edit integer values.
 *
 * @author JavaSaBr
 */
public class IntegerAppStatePropertyControl<T> extends AbstractIntegerPropertyControl<SceneChangeConsumer, T> {

    public IntegerAppStatePropertyControl(@Nullable final Integer propertyValue, @NotNull final String propertyName,
                                          @NotNull final SceneChangeConsumer changeConsumer) {
        super(propertyValue, propertyName, changeConsumer, newChangeHandler());
    }
}
