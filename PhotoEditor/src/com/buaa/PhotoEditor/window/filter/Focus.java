package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Focus {
    public JMenuItem focusItem;
    public Window window;

    public Focus(Window window) {
        this.window = window;
        focusItem = new JMenuItem("Focus");
        focusItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    }

}
