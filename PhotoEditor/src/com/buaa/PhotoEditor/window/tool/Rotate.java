package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Rotate {
    public Window window;
    public Tool tool;
    public JMenu rotateItem;

    public Rotate(Window window) {
        this.window = window;
        tool = new Tool(window);
        rotateItem = new JMenu("Rotate");
    }
}
