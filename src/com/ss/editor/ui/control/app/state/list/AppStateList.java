package com.ss.editor.ui.control.app.state.list;

import com.ss.editor.JFXApplication;
import com.ss.editor.model.undo.editor.SceneChangeConsumer;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.app.state.dialog.CreateSceneAppStateDialog;
import com.ss.editor.ui.control.app.state.operation.RemoveAppStateOperation;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;
import com.ss.editor.ui.scene.EditorFXScene;
import com.ss.extension.scene.SceneNode;
import com.ss.extension.scene.app.state.EditableSceneAppState;
import com.ss.extension.scene.app.state.SceneAppState;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import rlib.ui.util.FXUtils;
import rlib.util.array.Array;

import java.util.function.Consumer;

/**
 * The component to show and to edit app states.
 *
 * @author JavaSaBr
 */
public class AppStateList extends VBox {

    private static final JFXApplication JFX_APPLICATION = JFXApplication.getInstance();

    /**
     * The selection handler.
     */
    @NotNull
    private final Consumer<EditableSceneAppState> selectHandler;

    /**
     * The changes consumer.
     */
    @NotNull
    private final SceneChangeConsumer changeConsumer;

    /**
     * The list view with created scene app states.
     */
    private ListView<EditableSceneAppState> listView;

    public AppStateList(@NotNull final Consumer<EditableSceneAppState> selectHandler,
                        @NotNull final SceneChangeConsumer changeConsumer) {
        setId(CSSIds.SCENE_APP_STATE_CONTAINER);
        this.changeConsumer = changeConsumer;
        this.selectHandler = selectHandler;
        createComponents();
    }

    /**
     * Create components of this component.
     */
    private void createComponents() {

        listView = new ListView<>();
        listView.setCellFactory(param -> new AppStateListCell(this));
        listView.setEditable(false);
        listView.setFocusTraversable(true);
        listView.prefHeightProperty().bind(heightProperty());
        listView.prefWidthProperty().bind(widthProperty());

        final MultipleSelectionModel<EditableSceneAppState> selectionModel = listView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) ->
                selectHandler.accept(newValue));

        final Button addButton = new Button();
        addButton.setGraphic(new ImageView(Icons.ADD_12));
        addButton.setOnAction(event -> addAppState());

        final Button removeButton = new Button();
        removeButton.setGraphic(new ImageView(Icons.REMOVE_12));
        removeButton.setOnAction(event -> removeAppState());
        removeButton.disableProperty().bind(selectionModel.selectedItemProperty().isNull());

        final HBox buttonContainer = new HBox(addButton, removeButton);
        buttonContainer.setAlignment(Pos.CENTER);

        FXUtils.addToPane(listView, this);
        FXUtils.addToPane(buttonContainer, this);
        FXUtils.addClassTo(listView, CSSClasses.TRANSPARENT_LIST_VIEW);
    }

    /**
     * Fill a list of app states.
     */
    public void fill(@NotNull final SceneNode sceneNode) {

        final MultipleSelectionModel<EditableSceneAppState> selectionModel = listView.getSelectionModel();
        final EditableSceneAppState selected = selectionModel.getSelectedItem();

        final ObservableList<EditableSceneAppState> items = listView.getItems();
        items.clear();

        final Array<SceneAppState> appStates = sceneNode.getAppStates();
        appStates.stream().filter(appState -> appState instanceof EditableSceneAppState)
                .map(editableState -> (EditableSceneAppState) editableState)
                .forEach(items::add);

        if (selected != null && appStates.contains(selected)) {
            selectionModel.select(selected);
        }
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        final MultipleSelectionModel<EditableSceneAppState> selectionModel = listView.getSelectionModel();
        selectionModel.select(null);
    }

    /**
     * Handle adding a new app state.
     */
    private void addAppState() {
        final EditorFXScene scene = JFX_APPLICATION.getScene();
        final CreateSceneAppStateDialog dialog = new CreateSceneAppStateDialog(changeConsumer);
        dialog.show(scene.getWindow());
    }

    /**
     * Handle removing an old app state.
     */
    private void removeAppState() {

        final MultipleSelectionModel<EditableSceneAppState> selectionModel = listView.getSelectionModel();
        final EditableSceneAppState appState = selectionModel.getSelectedItem();
        final SceneNode sceneNode = changeConsumer.getCurrentModel();

        changeConsumer.execute(new RemoveAppStateOperation(appState, sceneNode));
    }

    /**
     * @return the changes consumer.
     */
    @NotNull
    public SceneChangeConsumer getChangeConsumer() {
        return changeConsumer;
    }
}
