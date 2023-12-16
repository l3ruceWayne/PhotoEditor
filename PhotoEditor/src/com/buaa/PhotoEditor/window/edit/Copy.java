package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CopyThread;

import javax.swing.*;

import static com.buaa.PhotoEditor.window.Constant.NUM_FOR_NEW;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description: 选择某个选区后复制该选区，通过在画布上点击鼠标实现该选区的剪贴
 * @author 罗雨曦、张旖霜
 * @date 2023/11/27 14:12
 * @version: 2.0
 **/
public class Copy {
    public JMenuItem copyItem;
    public Window window;
    public CopyThread[] copyThread;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 张旖霜、罗雨曦
     * @date: 2023/11/27 14:13
     * @version: 2.0
     **/
    public Copy(Window window) {
        this.copyThread = new CopyThread[NUM_FOR_NEW];
        this.window = window;
        this.copyItem = new JMenuItem("Copy");
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            copyThread[i] = new CopyThread(window, copyItem, i);
            copyThread[i].start();
            try {
                // 等待上一个进程执行结束再进行下一个线程的启动
                copyThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
