package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.UndoThread;


import javax.swing.*;

import static com.buaa.PhotoEditor.util.MatUtil.resize;
import static com.buaa.PhotoEditor.window.Constant.*;

/**
 * @Description: 撤销操作
 * @author: 张旖霜、罗雨曦
 * @date: 12/5/2023 9:36 PM
 * @version: 1.0
 */
public class Undo {
    public JMenuItem undoItem;
    private Window window;
    public UndoThread[] undoThread;
    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦、张旖霜
     * @date: 2023/11/27 14:06
     * @version: 2.0
     **/
    public Undo(Window window) {
        this.window = window;
        undoItem = new JMenuItem("Undo");
        undoThread = new UndoThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            undoThread[i] = new UndoThread(window, i, undoItem);
            undoThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                undoThread[i].join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        };
    }
}