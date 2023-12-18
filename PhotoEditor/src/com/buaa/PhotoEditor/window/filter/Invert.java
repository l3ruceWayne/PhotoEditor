package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

/**
 * @author 卢思文
 * @version 1.0
 * @Description 设置 Invert 滤镜
 * @date 11/26/2023 8:58 PM
 */
public class Invert {
    public JMenuItem invertItem;
    public Window window;

    public Invert(Window window) {
        this.window = window;
        invertItem = new JMenuItem("Invert");
        invertItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    }
}
