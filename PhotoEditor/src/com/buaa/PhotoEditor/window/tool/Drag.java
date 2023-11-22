package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Drag {
    public Window window;
    public Tool tool;
    public JMenu dragItem;

    public Drag(Window window) {
        this.window = window;
        tool = new Tool(window);
        dragItem = new JMenu("Drag");
    }
}
