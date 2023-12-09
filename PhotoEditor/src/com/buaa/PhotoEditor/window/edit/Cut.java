package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;
import org.opencv.core.Size;

import javax.swing.*;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;
import static com.buaa.PhotoEditor.window.Constant.AUTO_SIZE_COUNTER;

/**
 * @Description: 剪切图片
 * @author 罗雨曦
 * @date 2023/11/27 14:09
 * @version: 1.0
 **/
/*
 * @Description:实现剪切图片的多线程执行
 * @author: 张旖霜
 * @date: 12/9/2023 9:46 AM
 * @version: 1.0
 */
public class Cut {
    public JMenuItem cutItem;
    private Window window;
    // cut的线程
    public CutThread[] cutThread;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:10
     * @version: 1.0
     **/
    public Cut(Window window) {
        this.window = window;
        cutItem = new JMenuItem("Cut");
        cutThread = new CutThread[NUM_FOR_NEW];

        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            cutThread[i] = new CutThread(window, i, cutItem); // 创建线程
            cutThread[i].start(); // 执行线程
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                cutThread[i].join(); // 等待上一个线程执行完毕才开始下一个线程
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }
}
