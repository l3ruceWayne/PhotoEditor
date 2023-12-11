package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;

import javax.swing.*;

import static com.buaa.PhotoEditor.window.Constant.*;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @author 罗雨曦、张旖霜
 * @Description 剪切图片
 * 实现剪切图片的多线程执行
 * @date 2023/11/27
 * @version: 2.0
 **/
public class Cut {
    public JMenuItem cutItem;
    // cut的线程
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
            // 创建线程
            cutThread[i] = new CutThread(window, i, cutItem);
            cutThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                // 等待上一个线程执行完毕才开始下一个线程
                cutThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
