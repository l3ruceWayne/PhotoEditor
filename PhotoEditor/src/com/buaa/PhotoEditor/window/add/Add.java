package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

/**
 * @author 卢思文
 * @version 1.0
 * @Description 添加文字、小组件
 * @date 2023/12/11
 */
public class Add {
    public JMenu addMenu;
    public Window window;
    private final Widget widget;

    public Add(Window window) {
        this.window = window;
        this.widget = new Widget(window);
        Text text = new Text(window);
        addMenu = new JMenu("Add");
        addMenu.add(widget.widgetItem);
        addMenu.add(text.addTextItem);
    }

    public Window getWindow() {
        return window;
    }

    public Widget getWidget() {
        return widget;
    }
}
