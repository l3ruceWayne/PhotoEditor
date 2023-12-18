package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;

import javax.swing.*;

import static com.buaa.PhotoEditor.window.Constant.*;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @author 张旖霜、罗雨曦
 * @version 2.0
 * @Description 实现剪切图片的多线程执行
 * @date 12/9/2023 9:46 AM
 */
public class Cut {
    public JMenuItem cutItem;
    public CutThread[] cutThread;
    private Window window;

    /**
     * @param window 当前窗口
     * @Description 构造方法——生成子菜单项并设置快捷键
     * @author 罗雨曦
     * @date 2023/11/27
     */
    public Cut(Window window) {
        this.window = window;
        this.cutItem = new JMenuItem("Cut");
        this.cutThread = new CutThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            cutThread[i] = new CutThread(this.window, i, cutItem);
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
