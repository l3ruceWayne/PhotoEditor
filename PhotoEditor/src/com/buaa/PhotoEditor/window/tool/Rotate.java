package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Rotate {
    public Window window;
    public JMenu rotateItem;

    public Rotate(Window window) {
        this.window = window;
        rotateItem = new JMenu("Rotate");
    }
}
