package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Animize {
    public JMenuItem animizeItem;
    public Window window;

    public Animize(Window window) {
        this.window = window;
        animizeItem = new JMenuItem("Animize");
        animizeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
    }
}
