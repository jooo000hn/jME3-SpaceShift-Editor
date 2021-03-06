package com.ss.editor.ui.control.property.impl;

import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.ui.control.property.AbstractPropertyControl;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;

import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import rlib.function.SixObjectConsumer;
import rlib.ui.util.FXUtils;

/**
 * The default implementation of the property control.
 *
 * @author JavaSaBr
 */
public abstract class AbstractDefaultPropertyControl<C extends ChangeConsumer, D, T> extends AbstractPropertyControl<C, D, T> {

    /**
     * The label with value of the property.
     */
    protected Label propertyValueLabel;

    /**
     * The string function.
     */
    private Function<T, String> toStringFunction;

    public AbstractDefaultPropertyControl(@Nullable final T propertyValue, @NotNull final String propertyName,
                                          @NotNull final C changeConsumer,
                                          @NotNull final SixObjectConsumer<C, D, String, T, T, BiConsumer<D, T>> changeHandler) {
        super(propertyValue, propertyName, changeConsumer, changeHandler);
    }

    /**
     * @param toStringFunction the string function.
     */
    public void setToStringFunction(final Function<T, String> toStringFunction) {
        this.toStringFunction = toStringFunction;
    }

    /**
     * @return the string function.
     */
    private Function<T, String> getToStringFunction() {
        return toStringFunction;
    }

    /**
     * @return the label with value of the property.
     */
    private Label getPropertyValueLabel() {
        return propertyValueLabel;
    }

    @Override
    protected void createComponents(@NotNull final HBox container) {
        super.createComponents(container);

        propertyValueLabel = new Label();
        propertyValueLabel.setId(CSSIds.ABSTRACT_PARAM_CONTROL_LABEL_VALUE);

        FXUtils.addClassTo(propertyValueLabel, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addToPane(propertyValueLabel, container);
    }

    @Override
    public void reload() {
        super.reload();

        final Function<T, String> function = getToStringFunction();

        final Label propertyValueLabel = getPropertyValueLabel();
        propertyValueLabel.setText(function == null ? String.valueOf(getPropertyValue()) : function.apply(getPropertyValue()));
    }
}
