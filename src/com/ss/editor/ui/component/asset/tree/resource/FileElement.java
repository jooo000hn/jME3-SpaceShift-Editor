package com.ss.editor.ui.component.asset.tree.resource;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The presentation of a file.
 *
 * @author JavaSaBr
 */
public class FileElement extends ResourceElement {

    FileElement(@NotNull final Path file) {
        super(file);
    }
}
