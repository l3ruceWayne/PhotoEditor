package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;

import javax.swing.*;

import static com.buaa.PhotoEditor.window.Constant.*;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description:实现剪切图片的多线程执行
 * @author: 张旖霜、罗雨曦
 * @date: 12/9/2023 9:46 AM
 * @version: 2.0
 */
public class Cut {
    public JMenuItem cutItem;
    public CutThread[] cutThread;
    /**
     * @param window 当前窗口
     * @Description 构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date 2023/11/27
     * @version: 1.0
     **/
    public Cut(Window window) {
        cutItem = new JMenuItem("Cut");
        cutThread = new CutThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            cutThread[i] = new CutThread(window, i, cutItem);
            cutThread[i].start();
            try {
                // 等待上一个进程执行结束再进行下一个线程的启动
                cutThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
