package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class Add {
    public JMenu addMenu;
    public Window window;
    public Widget widget;
    public Text text;

    public Add(Window window) {
        this.window = window;
        this.widget = new Widget(window);
        this.text = new Text(window);
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

    public Text getText() {
        return text;
    }
}
