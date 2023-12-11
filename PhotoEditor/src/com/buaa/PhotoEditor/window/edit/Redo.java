package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.RedoThread;

import javax.swing.*;

import static com.buaa.PhotoEditor.util.MatUtil.resize;
import static com.buaa.PhotoEditor.window.Constant.*;

/**
 * @Description: 反撤销操作
 * @author: 张旖霜、罗雨曦
 * @date: 12/5/2023 9:37 PM
 * @version: 1.0
 */
public class Redo {
    public JMenuItem redoItem;
    private Window window;
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
        this.window = window;
        redoItem = new JMenuItem("Redo");
        redoThread = new RedoThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            redoThread[i] = new RedoThread(window, i, redoItem);
            redoThread[i].start();
            try {
                // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
                redoThread[i].join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        };
    }
}
