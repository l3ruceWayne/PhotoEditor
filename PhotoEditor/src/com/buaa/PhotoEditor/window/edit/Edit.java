package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
/**
 * @Description: 主菜单栏上的一级菜单，整合编辑相关操作，下设copy cut undo redo四个功能子菜单
 * @author 罗雨曦
 * @date 2023/11/27 11:09
 **/
public class Edit {
    public JMenu editMenu;
    private Window window;
    private Copy copy;
    private Cut cut;
    private Undo undo;
    private Redo redo;

    public Edit(Window window) {
        /**
         * @param window 当前窗口
         * @return null
         * @Description:构造方法——生成子菜单栏
         * @author: 罗雨曦
         * @date: 2023/11/27 11:19
         **/

        this.window = window;
        this.copy = new Copy(window);
        this.cut = new Cut(window);
        this.undo = new Undo(window);
        this.redo = new Redo(window);

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

}
