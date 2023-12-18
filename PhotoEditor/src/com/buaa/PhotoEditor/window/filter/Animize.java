package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;


/**
 * @Description 设置 Animize 滤镜
 * @author 卢思文
 * @date 11/26/2023 8:58 PM
 * @version 1.0
 */
public class Animize {
    public JMenuItem animizeItem;
    public Window window;

    public Animize(Window window) {
        this.window = window;
        animizeItem = new JMenuItem("Animize");
        animizeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK));
    }
}
