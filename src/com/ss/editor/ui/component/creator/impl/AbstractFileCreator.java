package com.ss.editor.ui.component.creator.impl;

import static java.util.Objects.requireNonNull;
import com.ss.editor.Editor;
import com.ss.editor.JFXApplication;
import com.ss.editor.Messages;
import com.ss.editor.config.EditorConfig;
import com.ss.editor.manager.ExecutorManager;
import com.ss.editor.ui.component.asset.tree.ResourceTree;
import com.ss.editor.ui.component.asset.tree.resource.ResourceElement;
import com.ss.editor.ui.component.creator.FileCreator;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;
import com.ss.editor.ui.dialog.AbstractSimpleEditorDialog;
import com.ss.editor.ui.event.FXEventManager;
import com.ss.editor.ui.event.impl.RequestSelectFileEvent;
import com.ss.editor.ui.scene.EditorFXScene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.ui.util.FXUtils;
import rlib.util.StringUtils;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The base implementation of a file creator.
 *
 * @author JavaSaBr
 */
public abstract class AbstractFileCreator extends AbstractSimpleEditorDialog implements FileCreator {

    protected static final Logger LOGGER = LoggerManager.getLogger(FileCreator.class);

    protected static final Insets SETTINGS_CONTAINER_OFFSET = new Insets(10, CANCEL_BUTTON_OFFSET.getRight(), 10, 0);
    protected static final Insets RESOURCE_TREE_OFFSET = new Insets(3, 0, 0, 0);

    protected static final Point DIALOG_SIZE = new Point(900, 401);

    protected static final ExecutorManager EXECUTOR_MANAGER = ExecutorManager.getInstance();
    protected static final FXEventManager FX_EVENT_MANAGER = FXEventManager.getInstance();
    protected static final JFXApplication JFX_APPLICATION = JFXApplication.getInstance();
    protected static final Editor EDITOR = Editor.getInstance();

    /**
     * The resources tree.
     */
    @Nullable
    private ResourceTree resourceTree;

    /**
     * The filed with new file name.
     */
    @Nullable
    private TextField fileNameField;

    /**
     * The init file.
     */
    @Nullable
    private Path initFile;

    @Override
    public void start(@NotNull final Path file) {
        this.initFile = file;

        final EditorConfig editorConfig = EditorConfig.getInstance();
        final Path currentAsset = requireNonNull(editorConfig.getCurrentAsset());

        final EditorFXScene scene = JFX_APPLICATION.getScene();
        show(scene.getWindow());

        final ResourceTree resourceTree = getResourceTree();
        resourceTree.setOnLoadHandler(finished -> expand(file, resourceTree, finished));
        resourceTree.fill(currentAsset);

        EXECUTOR_MANAGER.addFXTask(getFileNameField()::requestFocus);

        validateFileName();
    }

    private void expand(@NotNull final Path file, @NotNull final ResourceTree resourceTree,
                        @NotNull final Boolean finished) {
        if (finished) resourceTree.expandTo(file, true);
    }

    /**
     * @return the resources tree.
     */
    @NotNull
    private ResourceTree getResourceTree() {
        return requireNonNull(resourceTree);
    }

    /**
     * @param initFile the init file.
     */
    private void setInitFile(@NotNull final Path initFile) {
        this.initFile = initFile;
    }

    /**
     * @return the init file.
     */
    @NotNull
    private Path getInitFile() {
        return requireNonNull(initFile);
    }

    @NotNull
    @Override
    protected String getButtonOkLabel() {
        return Messages.FILE_CREATOR_BUTTON_OK;
    }

    /**
     * @return the selected file in the resources tree.
     */
    @NotNull
    private Path getSelectedFile() {

        final ResourceTree resourceTree = getResourceTree();
        final MultipleSelectionModel<TreeItem<ResourceElement>> selectionModel = resourceTree.getSelectionModel();
        final TreeItem<ResourceElement> selectedItem = selectionModel.getSelectedItem();
        if (selectedItem == null) return getInitFile();

        final ResourceElement element = selectedItem.getValue();
        return element.getFile();
    }

    /**
     * @return the file to creating.
     */
    @Nullable
    protected Path getFileToCreate() {

        final TextField fileNameField = getFileNameField();
        final String filename = fileNameField.getText();
        if (StringUtils.isEmpty(filename)) return null;

        final String fileExtension = getFileExtension();

        final Path selectedFile = getSelectedFile();
        final Path directory = Files.isDirectory(selectedFile) ? selectedFile : selectedFile.getParent();

        return StringUtils.isEmpty(fileExtension) ? directory.resolve(filename) :
                directory.resolve(filename + "." + fileExtension);
    }

    /**
     * @return the file extension.
     */
    @NotNull
    protected String getFileExtension() {
        return StringUtils.EMPTY;
    }

    /**
     * Notify about the file created.
     */
    protected void notifyFileCreated(@NotNull final Path createdFile, final boolean needSelect) {
        if (!needSelect) return;

        final RequestSelectFileEvent event = new RequestSelectFileEvent();
        event.setFile(createdFile);

        FX_EVENT_MANAGER.notify(event);
    }

    @Override
    protected void createContent(@NotNull final VBox root) {
        super.createContent(root);

        final HBox container = new HBox();
        container.setId(CSSIds.FILE_CREATOR_DIALOG_CONTAINER);

        final GridPane settingsContainer = new GridPane();
        settingsContainer.setId(CSSIds.FILE_CREATOR_DIALOG_GRID_SETTINGS_CONTAINER);
        settingsContainer.prefHeightProperty().bind(container.heightProperty());
        settingsContainer.prefWidthProperty().bind(root.widthProperty().multiply(0.5));

        resourceTree = new ResourceTree(null, true);
        resourceTree.prefWidthProperty().bind(root.widthProperty().multiply(0.5));

        final MultipleSelectionModel<TreeItem<ResourceElement>> selectionModel = resourceTree.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> validateFileName());

        createSettings(settingsContainer);

        FXUtils.addToPane(resourceTree, container);
        FXUtils.addToPane(settingsContainer, container);
        FXUtils.addToPane(container, root);

        HBox.setMargin(resourceTree, RESOURCE_TREE_OFFSET);
        HBox.setMargin(settingsContainer, SETTINGS_CONTAINER_OFFSET);
    }

    /**
     * @return the filed with new file name.
     */
    @NotNull
    private TextField getFileNameField() {
        return requireNonNull(fileNameField);
    }

    /**
     * Create settings of the creating file.
     */
    protected void createSettings(@NotNull final GridPane root) {

        final Label fileNameLabel = new Label(getFileNameLabelText() + ":");
        fileNameLabel.setId(CSSIds.EDITOR_DIALOG_DYNAMIC_LABEL);
        fileNameLabel.prefWidthProperty().bind(root.widthProperty().multiply(DEFAULT_LABEL_W_PERCENT));

        fileNameField = new TextField();
        fileNameField.setId(CSSIds.EDITOR_DIALOG_FIELD);
        fileNameField.prefWidthProperty().bind(root.widthProperty());
        fileNameField.textProperty().addListener((observable, oldValue, newValue) -> validateFileName());
        fileNameField.prefWidthProperty().bind(root.widthProperty().multiply(DEFAULT_FIELD_W_PERCENT));

        root.add(fileNameLabel, 0, 0);
        root.add(fileNameField, 1, 0);

        FXUtils.addClassTo(fileNameLabel, CSSClasses.SPECIAL_FONT_14);
        FXUtils.addClassTo(fileNameField, CSSClasses.SPECIAL_FONT_14);
    }

    /**
     * @return the label text "file name".
     */
    @NotNull
    protected String getFileNameLabelText() {
        return Messages.FILE_CREATOR_FILE_NAME_LABEL;
    }

    /**
     * Validate the inputted name.
     */
    protected void validateFileName() {

        final Path fileToCreate = getFileToCreate();
        final Button okButton = getOkButton();

        if (fileToCreate == null || Files.exists(fileToCreate)) {
            okButton.setDisable(true);
            return;
        }

        okButton.setDisable(false);
    }

    @Override
    protected Point getSize() {
        return DIALOG_SIZE;
    }
}
