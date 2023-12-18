package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

/**
 * @author 罗雨曦
 * @version 1.0
 * @Description 主菜单栏上的一级菜单，整合编辑相关操作，下设copy cut undo redo四个功能子菜单
 * @date 2023/11/27 14:01
 */
public class Edit {
    public JMenu editMenu;
    private Window window;
    private Copy copy;

    /**
     * @param window 当前窗口
     * @Description 构造方法——生成子菜单栏
     * @author 罗雨曦
     * @date 2023/11/27
     */
    public Edit(Window window) {
        this.window = window;
        this.copy = new Copy(window);
        Cut cut = new Cut(window);
        Undo undo = new Undo(window);
        Redo redo = new Redo(window);
        Paste paste = new Paste(window);
        editMenu = new JMenu("Edit");
        editMenu.add(copy.copyItem);
        editMenu.add(cut.cutItem);
        editMenu.add(undo.undoItem);
        editMenu.add(redo.redoItem);
    }

    public Window getWindow() {
        return window;
    }

    public Copy getCopy() {
        return copy;
    }
}
