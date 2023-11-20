package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class MyFile {
    public JMenu fileMenu;
    private Window window;
    private Open open;
    private Save save;

    public MyFile(Window window) {
        this.window = window;
        this.open = new Open(window);
        this.save = new Save(window);
        fileMenu = new JMenu("File");
    }

    public Window getWindow() {
        return window;
    }

    public Open getOpen() {
        return open;
    }

    public Save getSave() {
        return save;
    }
}
