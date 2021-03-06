package com.ss.editor.executor.impl;

import com.ss.editor.annotation.EditorThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.util.EditorUtil;

import org.jetbrains.annotations.NotNull;

import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.ArrayUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;
import rlib.util.array.ConcurrentArray;

/**
 * The executor to execute tasks in the editor thread.
 *
 * @author JavaSaBr
 */
public class EditorThreadExecutor {

    @NotNull
    private static final Logger LOGGER = LoggerManager.getLogger(EditorThreadExecutor.class);

    @NotNull
    private static final EditorThreadExecutor INSTANCE = new EditorThreadExecutor();

    public static EditorThreadExecutor getInstance() {
        return INSTANCE;
    }

    /**
     * The list of waited tasks.
     */
    @NotNull
    private final ConcurrentArray<Runnable> waitTasks;

    /**
     * The list with tasks to execute.
     */
    @NotNull
    private final Array<Runnable> execute;

    private EditorThreadExecutor() {
        this.waitTasks = ArrayFactory.newConcurrentAtomicARSWLockArray(Runnable.class);
        this.execute = ArrayFactory.newArray(Runnable.class);
    }

    /**
     * Add a task to execute.
     *
     * @param task the task.
     */
    @FromAnyThread
    public void addToExecute(@NotNull final Runnable task) {
        ArrayUtils.runInWriteLock(waitTasks, task, (tasks, toAdd) -> tasks.add(task));
    }

    /**
     * Execute waited tasks.
     */
    @EditorThread
    public void execute() {
        if (waitTasks.isEmpty()) return;

        ArrayUtils.runInWriteLock(waitTasks, execute, ArrayUtils::move);

        try {
            execute.forEach(EditorThreadExecutor::execute);
        } finally {
            execute.clear();
        }
    }

    @EditorThread
    private static void execute(@NotNull final Runnable runnable) {
        try {
            runnable.run();
        } catch (final Exception e) {
            EditorUtil.handleException(LOGGER, getInstance(), e);
        }
    }
}
