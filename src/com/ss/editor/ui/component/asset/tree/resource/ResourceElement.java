package com.ss.editor.ui.component.asset.tree.resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.util.array.Array;

import java.nio.file.Path;

/**
 * The base implementation of resource.
 *
 * @author JavaSaBr
 */
public abstract class ResourceElement implements Comparable<ResourceElement> {

    @NotNull
    protected static final Logger LOGGER = LoggerManager.getLogger(ResourceElement.class);

    /**
     * The reference to the file.
     */
    @NotNull
    protected final Path file;

    public ResourceElement(@NotNull final Path file) {
        this.file = file;
    }

    /**
     * @return the reference to the file.
     */
    @NotNull
    public Path getFile() {
        return file;
    }

    /**
     * @return list of children resource elements.
     */
    public Array<ResourceElement> getChildren(@NotNull final Array<String> extensionFilter, final boolean onlyFolders) {
        return null;
    }

    /**
     * @return true if this element has children.
     */
    public boolean hasChildren(@NotNull final Array<String> extensionFilter, final boolean onlyFolders) {
        return false;
    }

    @Override
    public int compareTo(@Nullable final ResourceElement other) {
        if (other == null) return -1;

        final Path file = getFile();
        final Path otherFile = other.getFile();

        return file.getNameCount() - otherFile.getNameCount();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ResourceElement that = (ResourceElement) o;
        return file.equals(that.file);
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public String toString() {
        return "ResourceElement{" + "file=" + file + '}';
    }
}
