package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Pen {
    public JCheckBoxMenuItem penItem;
    public Window window;
    public Pen(Window window) {
        this.window = window;
        penItem = new JCheckBoxMenuItem("Pen");
    }
}
