package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.UndoThread;

import javax.swing.*;

import static com.buaa.PhotoEditor.window.Constant.*;

/**
 * @author 罗雨曦、张旖霜
 * @Description: 撤销上一步操作； 目前对于是撤销操作还是恢复图片上一状态还没有设置明确，后将改进
 * 实现多线程
 * @date 2023/12/5
 * @version: 2.0
 **/
public class Undo {
    public JMenuItem undoItem;
    public UndoThread[] undoThread;

    /**
     * @param window 当前窗口
     * @Description 构造方法——生成子菜单项并设置快捷键
     * @author 罗雨曦
     * @date 2023/11/27
     * @version: 1.0
     **/
    public Undo(Window window) {
        undoItem = new JMenuItem("Undo");
        undoThread = new UndoThread[NUM_FOR_NEW];

        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            undoThread[i] = new UndoThread(window, i, undoItem);
            undoThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                undoThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
