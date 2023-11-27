package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
/**
* @Description: 主菜单栏上的一级菜单，整合文件相关操作，下设open save两个功能子菜单
* @author 罗雨曦
* @date 2023/11/27 11:26
* @version: 1.0
**/
public class MyFile {
    public JMenu fileMenu;
    private Window window;
    private Open open;
    private Save save;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单栏
     * @author: 罗雨曦
     * @date: 2023/11/27 14:08
     * @version: 1.0
     **/
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
