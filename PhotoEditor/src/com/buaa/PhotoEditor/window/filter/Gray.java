package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @Description 设置 Gray 滤镜
 * @author 卢思文
 * @date 11/26/2023 8:58 PM
 * @version 1.0
 */
public class Gray {
    public JMenuItem grayItem;
    public Window window;

    public Gray(Window window) {
        this.window = window;
        grayItem = new JMenuItem("Gray");

        grayItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                InputEvent.CTRL_DOWN_MASK));

    }

}
