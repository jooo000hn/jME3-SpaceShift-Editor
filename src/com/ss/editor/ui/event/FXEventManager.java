package com.ss.editor.ui.event;

import com.ss.editor.annotation.FXThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.manager.ExecutorManager;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.jetbrains.annotations.NotNull;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.dictionary.DictionaryFactory;
import rlib.util.dictionary.ObjectDictionary;

/**
 * The class to manage javaFX events.
 *
 * @author JavaSaBr
 */
public class FXEventManager {

    @NotNull
    private static final FXEventManager INSTANCE = new FXEventManager();

    @NotNull
    public static FXEventManager getInstance() {
        return INSTANCE;
    }

    /**
     * The table of event handlers.
     */
    @NotNull
    private final ObjectDictionary<EventType<? extends Event>, Array<EventHandler<? super Event>>> eventHandlers;

    public FXEventManager() {
        this.eventHandlers = DictionaryFactory.newObjectDictionary();
    }

    /**
     * Add a new event handler.
     *
     * @param eventType    the event type.
     * @param eventHandler the event handler.
     */
    @FXThread
    public void addEventHandler(@NotNull final EventType<? extends Event> eventType,
                                @NotNull final EventHandler<? super Event> eventHandler) {
        final ObjectDictionary<EventType<? extends Event>, Array<EventHandler<? super Event>>> eventHandlers = getEventHandlers();
        final Array<EventHandler<? super Event>> handlers = eventHandlers.get(eventType, () -> ArrayFactory.newArray(EventHandler.class));
        handlers.add(eventHandler);
    }

    /**
     * Remove an old event handler.
     *
     * @param eventType    the event type.
     * @param eventHandler the event handler.
     */
    @FXThread
    public void removeEventHandler(@NotNull final EventType<? extends Event> eventType,
                                   @NotNull final EventHandler<? super Event> eventHandler) {

        final ObjectDictionary<EventType<? extends Event>, Array<EventHandler<? super Event>>> eventHandlers = getEventHandlers();
        final Array<EventHandler<? super Event>> handlers = eventHandlers.get(eventType);
        if (handlers == null) return;

        handlers.slowRemove(eventHandler);
    }

    /**
     * @return the table of event handlers.
     */
    @NotNull
    @FXThread
    private ObjectDictionary<EventType<? extends Event>, Array<EventHandler<? super Event>>> getEventHandlers() {
        return eventHandlers;
    }

    /**
     * Notify about a new event.
     *
     * @param event the new event.
     */
    @FromAnyThread
    public void notify(@NotNull final Event event) {
        if (Platform.isFxApplicationThread()) {
            notifyImpl(event);
        } else {
            final ExecutorManager executorManager = ExecutorManager.getInstance();
            executorManager.addFXTask(() -> notifyImpl(event));
        }
    }

    /**
     * The process of handling a new event.
     */
    @FXThread
    private void notifyImpl(@NotNull final Event event) {

        final ObjectDictionary<EventType<? extends Event>, Array<EventHandler<? super Event>>> eventHandlers = getEventHandlers();

        for (EventType<? extends Event> eventType = event.getEventType();
             eventType != null; eventType = (EventType<? extends Event>) eventType.getSuperType()) {

            final Array<EventHandler<? super Event>> handlers = eventHandlers.get(eventType);
            if (handlers == null || handlers.isEmpty()) continue;

            handlers.forEach(event, EventHandler::handle);
        }

        if (event instanceof ConsumableEvent && !event.isConsumed()) {
            final ExecutorManager executorManager = ExecutorManager.getInstance();
            executorManager.addFXTask(() -> notifyImpl(event));
        }
    }
}
