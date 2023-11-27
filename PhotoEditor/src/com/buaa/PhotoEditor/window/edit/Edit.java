package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.nio.file.Path;

public class Edit {
    public JMenu editMenu;
    private Window window;
    private Copy copy;
    private Paste paste;
    private Cut cut;
    private Undo undo;
    private Redo redo;

    public Edit(Window window) {
        this.window = window;
        this.copy = new Copy(window);
        this.cut = new Cut(window);
        this.undo = new Undo(window);
        this.redo = new Redo(window);
        this.paste = new Paste(window);
        editMenu = new JMenu("Edit");
        editMenu.add(copy.copyItem);
        editMenu.add(cut.cutItem);
        editMenu.add(undo.undoItem);
        editMenu.add(redo.redoItem);
    }

    public Window getWindow() {
        return window;
    }

    public Copy getCopy() {
        return copy;
    }

    public Cut getCut() {
        return cut;
    }

    public Undo getUndo() {
        return undo;
    }

    public Redo getRedo() {
        return redo;
    }
    public Paste getPaste(){return paste;}
}
