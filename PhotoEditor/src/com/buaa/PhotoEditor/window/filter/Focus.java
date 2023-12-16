package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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
        focusItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
            InputEvent.CTRL_DOWN_MASK));
    }

}
