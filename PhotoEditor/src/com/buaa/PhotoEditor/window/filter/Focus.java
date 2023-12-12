package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

/**
 * @Description: 设置 Focus 效果
 * @author: 卢思文
 * @date: 11/26/2023 8:58 PM
 * @version: 1.0
 */
public class Focus {
    public JMenuItem focusItem;
    public Window window;

    public Focus(Window window) {
        this.window = window;
        focusItem = new JMenuItem("Focus");
        focusItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    }

}
