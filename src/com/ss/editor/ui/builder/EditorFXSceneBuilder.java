package com.ss.editor.ui.builder;

import static javafx.geometry.Pos.TOP_CENTER;
import static javafx.scene.paint.Color.TRANSPARENT;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FXThread;
import com.ss.editor.ui.component.asset.AssetComponent;
import com.ss.editor.ui.component.bar.EditorBarComponent;
import com.ss.editor.ui.component.editor.area.EditorAreaComponent;
import com.ss.editor.ui.component.log.LogView;
import com.ss.editor.ui.component.split.pane.GlobalBottomToolSplitPane;
import com.ss.editor.ui.component.split.pane.GlobalLeftToolSplitPane;
import com.ss.editor.ui.component.tab.GlobalBottomToolComponent;
import com.ss.editor.ui.component.tab.GlobalLeftToolComponent;
import com.ss.editor.ui.css.CSSIds;
import com.ss.editor.ui.event.EventRedirector;
import com.ss.editor.ui.scene.EditorFXScene;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import rlib.ui.util.FXUtils;

/**
 * The scene builder for building a scene for the Editor.
 *
 * @author JavaSaBr.
 */
public class EditorFXSceneBuilder {

    /**
     * The path to the base CSS styles.
     */
    private static final String CSS_FILE_BASE = "/ui/css/base.bss";

    /**
     * The path to the external CSS styles.
     */
    private static final String CSS_FILE_EXTERNAL = "/ui/css/external.bss";

    /**
     * The path to the custom ids CSS styles.
     */
    private static final String CSS_FILE_CUSTOM_IDS = "/ui/css/custom_ids.bss";

    /**
     * The path to the custom classes CSS styles.
     */
    private static final String CSS_FILE_CUSTOM_CLASSES = "/ui/css/custom_classes.bss";

    @NotNull
    @FXThread
    public static EditorFXScene build(@NotNull final Stage stage) {

        final Group root = new Group();
        //root.getTransforms().add(new Scale(1.5, 1.5));

        final EditorFXScene scene = new EditorFXScene(root);
        scene.setFill(TRANSPARENT);
        scene.setRoot(root);

        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.add(CSS_FILE_BASE);
        stylesheets.add(CSS_FILE_EXTERNAL);
        stylesheets.add(CSS_FILE_CUSTOM_IDS);
        stylesheets.add(CSS_FILE_CUSTOM_CLASSES);

        final StackPane container = scene.getContainer();
        container.setAlignment(TOP_CENTER);

        build(scene, container, stage);

        //TODO implement scalling
        //bindFixedSize(container, scene.widthProperty().divide(1.5), scene.heightProperty().divide(1.5));

        stage.setScene(scene);

        return scene;
    }

    private static void build(@NotNull final EditorFXScene scene, @NotNull final StackPane container, @NotNull final Stage stage) {

        final Canvas canvas = scene.getCanvas();
        final EditorBarComponent barComponent = new EditorBarComponent();
        final EditorAreaComponent editorAreaComponent = new EditorAreaComponent();

        new EventRedirector(editorAreaComponent, canvas, stage);

        final GlobalLeftToolSplitPane leftSplitContainer = new GlobalLeftToolSplitPane(scene);
        leftSplitContainer.setId(CSSIds.MAIN_SPLIT_PANEL);
        leftSplitContainer.prefHeightProperty().bind(container.heightProperty());

        final GlobalBottomToolSplitPane bottomSplitContainer = new GlobalBottomToolSplitPane(scene);
        bottomSplitContainer.setId(CSSIds.MAIN_SPLIT_PANEL);

        final GlobalLeftToolComponent globalLeftToolComponent = new GlobalLeftToolComponent(leftSplitContainer);
        globalLeftToolComponent.addComponent(new AssetComponent(), Messages.EDITOR_TOOL_ASSET);

        final GlobalBottomToolComponent globalBottomToolComponent = new GlobalBottomToolComponent(bottomSplitContainer);
        globalBottomToolComponent.addComponent(LogView.getInstance(), Messages.LOG_VIEW_TITLE);

        leftSplitContainer.initFor(globalLeftToolComponent, bottomSplitContainer);
        bottomSplitContainer.initFor(globalBottomToolComponent, editorAreaComponent);

        final Pane editorBarOffset = new Pane();
        editorBarOffset.setId(CSSIds.EDITOR_BAR_COMPONENT_OFFSET);

        FXUtils.addToPane(new VBox(editorBarOffset, leftSplitContainer), container);
        FXUtils.addToPane(barComponent, container);

        barComponent.createDrawer(container, stage);
        barComponent.toFront();

        FXUtils.bindFixedWidth(editorBarOffset, container.widthProperty());
        FXUtils.bindFixedWidth(leftSplitContainer, container.widthProperty());
        FXUtils.bindFixedWidth(barComponent, container.widthProperty());
    }
}
