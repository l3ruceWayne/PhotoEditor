package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Invert {
    public JMenuItem invertItem;
    public Window window;

    public Invert(Window window) {
        this.window = window;
        invertItem = new JMenuItem("Invert");
        invertItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));

    }

}
