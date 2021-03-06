package com.ss.editor.ui.control.property.impl;

import static java.util.Objects.requireNonNull;

import com.jme3.math.Vector3f;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.ui.control.property.AbstractPropertyControl;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;
import com.ss.editor.ui.util.UIUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import rlib.function.SixObjectConsumer;
import rlib.ui.control.input.FloatTextField;
import rlib.ui.util.FXUtils;

/**
 * The implementation of the {@link AbstractPropertyControl} to edit {@link Vector3f} values.
 *
 * @author JavaSaBr
 */
public abstract class AbstractVector3fPropertyControl<C extends ChangeConsumer, T>
        extends AbstractPropertyControl<C, T, Vector3f> {

    /**
     * The field X.
     */
    private FloatTextField xField;

    /**
     * The field Y.
     */
    private FloatTextField yField;

    /**
     * The field Z.
     */
    private FloatTextField zField;

    public AbstractVector3fPropertyControl(@Nullable final Vector3f propertyValue, @NotNull final String propertyName,
                                           @NotNull final C changeConsumer,
                                           @NotNull final SixObjectConsumer<C, T, String, Vector3f, Vector3f, BiConsumer<T, Vector3f>> changeHandler) {
        super(propertyValue, propertyName, changeConsumer, changeHandler);
    }

    @Override
    protected void createComponents(@NotNull final HBox container) {
        super.createComponents(container);

        final Label xLabel = new Label("x:");
        xLabel.setId(CSSIds.ABSTRACT_PARAM_CONTROL_NUMBER_LABEL);

        xField = new FloatTextField();
        xField.setId(CSSIds.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD);
        xField.setOnKeyReleased(this::updateVector);
        xField.addChangeListener((observable, oldValue, newValue) -> updateVector(null));
        xField.prefWidthProperty().bind(widthProperty().divide(3));
        xField.setScrollPower(getScrollPower());

        final Label yLabel = new Label("y:");
        yLabel.setId(CSSIds.ABSTRACT_PARAM_CONTROL_NUMBER_LABEL);

        yField = new FloatTextField();
        yField.setId(CSSIds.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD);
        yField.setOnKeyReleased(this::updateVector);
        yField.addChangeListener((observable, oldValue, newValue) -> updateVector(null));
        yField.prefWidthProperty().bind(widthProperty().divide(3));
        yField.setScrollPower(getScrollPower());

        final Label zLabel = new Label("z:");
        zLabel.setId(CSSIds.ABSTRACT_PARAM_CONTROL_NUMBER_LABEL);

        zField = new FloatTextField();
        zField.setId(CSSIds.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD);
        zField.setOnKeyReleased(this::updateVector);
        zField.addChangeListener((observable, oldValue, newValue) -> updateVector(null));
        zField.prefWidthProperty().bind(widthProperty().divide(3));
        zField.setScrollPower(getScrollPower());

        FXUtils.addToPane(xLabel, container);
        FXUtils.addToPane(xField, container);
        FXUtils.addToPane(yLabel, container);
        FXUtils.addToPane(yField, container);
        FXUtils.addToPane(zLabel, container);
        FXUtils.addToPane(zField, container);

        FXUtils.addClassTo(xLabel, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addClassTo(xField, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addClassTo(yLabel, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addClassTo(yField, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addClassTo(zLabel, CSSClasses.SPECIAL_FONT_13);
        FXUtils.addClassTo(zField, CSSClasses.SPECIAL_FONT_13);
    }

    /**
     * @return the scroll power.
     */
    protected float getScrollPower() {
        return 10F;
    }

    /**
     * @return the field X.
     */
    protected FloatTextField getXField() {
        return xField;
    }

    /**
     * @return the field Y.
     */
    protected FloatTextField getYFiled() {
        return yField;
    }

    /**
     * @return the field Z.
     */
    protected FloatTextField getZField() {
        return zField;
    }

    @Override
    protected void reload() {

        final Vector3f element = requireNonNull(getPropertyValue(), "The property value can't be null.");

        final FloatTextField xField = getXField();
        xField.setValue(element.getX());
        xField.positionCaret(xField.getText().length());

        final FloatTextField yFiled = getYFiled();
        yFiled.setValue(element.getY());
        yFiled.positionCaret(xField.getText().length());

        final FloatTextField zField = getZField();
        zField.setValue(element.getZ());
        zField.positionCaret(xField.getText().length());
    }

    /**
     * Update the vector.
     */
    protected void updateVector(@Nullable final KeyEvent event) {
        UIUtils.consumeIfIsNotHotKey(event);

        if (isIgnoreListener() || (event != null && event.getCode() != KeyCode.ENTER)) return;

        final FloatTextField xField = getXField();
        final float x = xField.getValue();

        final FloatTextField yFiled = getYFiled();
        final float y = yFiled.getValue();

        final FloatTextField zField = getZField();
        final float z = zField.getValue();

        final Vector3f oldValue = requireNonNull(getPropertyValue(), "The property value can't be null.");
        final Vector3f newValue = new Vector3f();
        newValue.set(x, y, z);

        changed(newValue, oldValue.clone());
    }
}
