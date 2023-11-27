package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
/**
* @Description: 对应菜单栏的File选项以及其子选项，负责文件的打开，保存和另存为功能
* @author: 卢思文
* @date: 11/27/2023 4:42 PM
* @version: 1.0
**/
public class MyFile {
    public JMenu fileMenu;
    private Window window;
    private Open open;
    private Save save;

    public MyFile(Window window) {
        this.window = window;
        this.open = new Open(window);
        this.save = new Save(window);
        fileMenu = new JMenu("File");
        fileMenu.add(open.openItem);
        fileMenu.add(save.saveItem);
        fileMenu.add(save.saveAsItem);
    }

    public Window getWindow() {
        return window;
    }

    public Open getOpen() {
        return open;
    }

    public Save getSave() {
        return save;
    }
}
