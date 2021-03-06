package com.ss.editor.ui.component.editor.impl;

import static java.util.Objects.requireNonNull;
import com.ss.editor.Messages;
import com.ss.editor.ui.component.editor.EditorDescription;
import com.ss.editor.ui.component.editor.EditorRegistry;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.editor.ui.css.CSSIds;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import rlib.ui.util.FXUtils;
import rlib.util.FileUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The implementation of editor to edit text files.
 *
 * @author JavaSaBr
 */
public class TextFileEditor extends AbstractFileEditor<VBox> {

    @NotNull
    public static final EditorDescription DESCRIPTION = new EditorDescription();

    static {
        DESCRIPTION.setConstructor(TextFileEditor::new);
        DESCRIPTION.setEditorName(Messages.TEXT_FILE_EDITOR_NAME);
        DESCRIPTION.setEditorId(TextFileEditor.class.getSimpleName());
        DESCRIPTION.addExtension(EditorRegistry.ALL_FORMATS);
    }

    /**
     * The original content of the opened file.
     */
    @Nullable
    private String originalContent;

    /**
     * The text area.
     */
    @Nullable
    private TextArea textArea;

    @NotNull
    @Override
    protected VBox createRoot() {
        return new VBox();
    }

    @Override
    protected void createContent(@NotNull final VBox root) {

        textArea = new TextArea();
        textArea.setId(CSSIds.TEXT_EDITOR_TEXT_AREA);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> updateDirty(newValue));
        textArea.prefHeightProperty().bind(root.heightProperty());
        textArea.prefWidthProperty().bind(root.widthProperty());

        FXUtils.addToPane(textArea, root);
        FXUtils.addClassTo(textArea, CSSClasses.MAIN_FONT_13);
    }

    /**
     * Update dirty state.
     */
    private void updateDirty(final String newContent) {
        setDirty(!getOriginalContent().equals(newContent));
    }

    @Override
    protected boolean needToolbar() {
        return true;
    }

    @Override
    protected void createToolbar(@NotNull final HBox container) {
        super.createToolbar(container);
        FXUtils.addToPane(createSaveAction(), container);
    }

    /**
     * @return the text area.
     */
    @NotNull
    private TextArea getTextArea() {
        return requireNonNull(textArea);
    }

    @Override
    public void openFile(@NotNull final Path file) {
        super.openFile(file);

        setOriginalContent(FileUtils.read(file));

        final TextArea textArea = getTextArea();
        textArea.setText(getOriginalContent());
    }

    /**
     * @return the original content of the opened file.
     */
    @NotNull
    private String getOriginalContent() {
        return requireNonNull(originalContent);
    }

    /**
     * @param originalContent the original content of the opened file.
     */
    private void setOriginalContent(@NotNull final String originalContent) {
        this.originalContent = originalContent;
    }

    @Override
    public void doSave() {
        super.doSave();

        final TextArea textArea = getTextArea();
        final String newContent = textArea.getText();

        try (final PrintWriter out = new PrintWriter(Files.newOutputStream(getEditFile()))) {
            out.print(newContent);
        } catch (final IOException e) {
            LOGGER.warning(this, e);
        }

        setOriginalContent(newContent);
        updateDirty(newContent);
    }

    @NotNull
    @Override
    public EditorDescription getDescription() {
        return DESCRIPTION;
    }
}
