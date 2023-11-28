package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

import java.nio.file.Path;

/**
* @Description: 主菜单栏上的一级菜单，整合编辑相关操作，下设copy cut undo redo四个功能子菜单
* @author 罗雨曦
* @date 2023/11/27 14:01
* @version: 1.0
**/


public class Edit {
    public JMenu editMenu;
    private Window window;
    private Copy copy;
    private Paste paste;
    private Cut cut;
    private Undo undo;
    private Redo redo;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单栏
     * @author: 罗雨曦
     * @date: 2023/11/27 14:02
     * @version: 1.0
     **/
    public Edit(Window window) {
        this.window = window;
        this.copy = new Copy(window);
        this.cut = new Cut(window);
        this.undo = new Undo(window);
        this.redo = new Redo(window);
        this.paste = new Paste(window);
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

    public Cut getCut() {
        return cut;
    }

    public Undo getUndo() {
        return undo;
    }

    public Redo getRedo() {
        return redo;
    }
    public Paste getPaste(){return paste;}
}
