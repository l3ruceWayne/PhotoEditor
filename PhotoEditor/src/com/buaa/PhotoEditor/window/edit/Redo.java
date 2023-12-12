package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.RedoThread;

import javax.swing.*;


import static com.buaa.PhotoEditor.window.Constant.*;

/**
 * @author 罗雨曦、张旖霜
 * @version 2.0
 * @Description 重做上一步操作； 目前对于是重做操作还是恢复图片下一状态还没有设置明确，后将改进;已改进
 * 实现了redo的多线程
 * @date 2023/12/5
 */

import static com.buaa.PhotoEditor.util.MatUtil.resize;
import static com.buaa.PhotoEditor.window.Constant.*;


public class Redo {
    public JMenuItem redoItem;
    public RedoThread[] redoThread;
    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦、张旖霜
     * @date: 2023/11/27 14:06
     * @version: 1.0
     **/
    public Redo(Window window) {
        redoItem = new JMenuItem("Redo");
        redoThread = new RedoThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            redoThread[i] = new RedoThread(window, i, redoItem);
            redoThread[i].start();
            try {
                // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
                redoThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}
