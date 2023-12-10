package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CopyThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.buaa.PhotoEditor.window.Constant.NUM_FOR_NEW;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @author 罗雨曦
 * @Description: 选择某个选区后复制该选区，通过在画布上点击鼠标实现该选区的剪贴
 * @date 2023/11/27 14:12
 * @version: 1.0
 **/
public class Copy {
    public JMenuItem copyItem;
    public Window window;
    public CopyThread[] copyThread;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:13
     * @version: 1.0
     **/
    public Copy(Window window) {
        copyThread = new CopyThread[NUM_FOR_NEW];
        this.window = window;
        copyItem = new JMenuItem("Copy");
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            copyThread[i] = new CopyThread(window, copyItem, i);
            copyThread[i].start();
            try {
                copyThread[i].join(); // 等待上一个线程执行完毕才开始下一个线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
