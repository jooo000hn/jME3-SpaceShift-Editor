package com.ss.editor.ui.event.impl;

import static java.util.Objects.requireNonNull;
import com.ss.editor.ui.event.SceneEvent;
import javafx.event.Event;
import javafx.event.EventType;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The event about created a new file.
 *
 * @author JavaSaBr
 */
public class CreatedFileEvent extends SceneEvent {

    @NotNull
    public static final EventType<SceneEvent> EVENT_TYPE;

    static {
        synchronized (Event.class) {
            EVENT_TYPE = new EventType<>(SceneEvent.EVENT_TYPE, CreatedFileEvent.class.getSimpleName());
        }
    }

    private static final String FILE = "file";
    private static final String NEED_SELECT = "need_select";
    private static final String IS_DIRECTORY = "isDirectory";

    public CreatedFileEvent() {
        super(EVENT_TYPE);
    }

    /**
     * @param needSelect true if need to select a file.
     */
    public void setNeedSelect(final boolean needSelect) {
        set(NEED_SELECT, needSelect);
    }

    /**
     * @return true if need to select a file.
     */
    public boolean isNeedSelect() {
        return get(NEED_SELECT) == Boolean.TRUE;
    }

    /**
     * @return true if the file is directory.
     */
    public boolean isDirectory() {
        return get(IS_DIRECTORY) == Boolean.TRUE;
    }

    /**
     * @return the new file.
     */
    @NotNull
    public Path getFile() {
        return requireNonNull(get(FILE), "Can't find a file");
    }

    /**
     * @param file the new file.
     */
    public void setFile(@NotNull final Path file) {
        set(FILE, file);
    }

    /**
     * @param directory the directory.
     */
    public void setDirectory(final boolean directory) {
        set(IS_DIRECTORY, directory);
    }
}
