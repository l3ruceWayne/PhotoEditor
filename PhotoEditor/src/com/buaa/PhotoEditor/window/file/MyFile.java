package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

/**
 * @author 罗雨曦、卢思文
 * @version 1.0
 * @Description 主菜单栏上的一级菜单，整合文件相关操作，下设open save两个功能子菜单
 * @date 2023/11/27 11:26
 */
public class MyFile {
    public JMenu fileMenu;
    private Window window;
    private Open open;
    private Save save;

    public MyFile(Window window) {
        this.window = window;
        this.open = new Open(window);
        this.save = new Save(window);
        this.fileMenu = new JMenu("File");
        this.fileMenu.add(open.openItem);
        this.fileMenu.add(save.saveItem);
        this.fileMenu.add(save.saveAsItem);
    }

    public Window getWindow() {
        return window;
    }

    public Open getOpen() {
        return open;
    }
}
