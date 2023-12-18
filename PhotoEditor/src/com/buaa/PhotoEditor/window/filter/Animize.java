package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * @author 卢思文
 * @version 1.0
 * @Description 设置 Animize 滤镜
 * @date 11/26/2023 8:58 PM
 */
public class Animize {
    public JMenuItem animizeItem;
    public Window window;

    public Animize(Window window) {
        this.window = window;
        this.animizeItem = new JMenuItem("Animize");
        this.animizeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.CTRL_DOWN_MASK));
    }
}
