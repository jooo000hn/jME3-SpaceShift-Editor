package com.ss.editor.ui.event.impl;

import com.ss.editor.ui.event.SceneEvent;
import javafx.event.EventType;

/**
 * The event about changed focus of a window.
 *
 * @author JavaSaBr
 */
public class WindowChangeFocusEvent extends SceneEvent {

    public static final EventType<SceneEvent> EVENT_TYPE;

    static {
        synchronized (EventType.class) {
            EVENT_TYPE = new EventType<>(SceneEvent.EVENT_TYPE, WindowChangeFocusEvent.class.getSimpleName());
        }
    }

    private static final String FOCUS = "focus";

    public WindowChangeFocusEvent() {
        super(EVENT_TYPE);
    }

    /**
     * @return true if a window has focus.
     */
    public boolean isFocused() {
        return get(FOCUS) == Boolean.TRUE;
    }

    /**
     * @param focused true if a window has focus.
     */
    public void setFocused(final boolean focused) {
        set(FOCUS, focused);
    }
}
