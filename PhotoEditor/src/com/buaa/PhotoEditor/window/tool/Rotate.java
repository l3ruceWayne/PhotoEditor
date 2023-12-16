package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;
import static com.buaa.PhotoEditor.window.Constant.*;
import com.buaa.PhotoEditor.window.thread.RotateThread;
import javax.swing.*;


/**
 * @Description 图片旋转功能（顺时针旋转90°）
 * @author 卢思文，张旖霜
 * @date 11/27/2023 12:59 PM
 * @version: 2.0
 */
public class Rotate {
    public Window window;
    public JMenu rotateItem;
    public RotateThread[] rotateThread;

    public Rotate(Window window) {
        this.window = window;
        rotateItem = new JMenu("Rotate");
        rotateThread = new RotateThread[NUM_FOR_NEW];
        // 执行多线程
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            rotateThread[i] = new RotateThread(window, rotateItem, i);
            rotateThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                rotateThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
