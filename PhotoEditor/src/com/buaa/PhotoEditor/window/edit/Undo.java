package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;
import com.buaa.PhotoEditor.window.thread.UndoThread;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import org.opencv.core.Size;


import javax.swing.*;
import java.awt.event.ActionEvent;
import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;
import static com.buaa.PhotoEditor.util.MatUtil.resize;
import static com.buaa.PhotoEditor.window.Constant.*;


/**
 * @Description: 撤销上一步操作； 目前对于是撤销操作还是恢复图片上一状态还没有设置明确，后将改进
 * @author 罗雨曦
 * @date 2023/11/27 14:06
 * @version: 1.0
 **/
/*
 * @Description:实现了undo的多线程
 * @author: 张旖霜
 * @date: 12/5/2023 9:36 PM
 * @version: 1.0
 */
public class Undo {
    public JMenuItem undoItem;
    private Window window;
    public UndoThread[] undoThread;


    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:06
     * @version: 1.0
     **/
    public Undo(Window window) {
        this.window = window;
        undoItem = new JMenuItem("Undo");
        undoThread = new UndoThread[NUM_FOR_NEW];

        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            undoThread[i] = new UndoThread(window, i, undoItem);
            undoThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                undoThread[i].join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        };
    }
}
