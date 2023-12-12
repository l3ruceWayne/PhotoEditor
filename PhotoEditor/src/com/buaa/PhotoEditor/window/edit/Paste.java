package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.PasteThread;

import static com.buaa.PhotoEditor.window.Constant.NUM_FOR_NEW;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;


/**
 * @Description 实现粘贴功能的Paste类
 * @author 卢思文
 * @date 2023/11/26
 * @version 1.0
 */
public class Paste {
    public PasteThread[] pasteThread;
    private Window window;
    public Paste(Window window) {
        pasteThread = new PasteThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            pasteThread[i] = new PasteThread(window, i);
            pasteThread[i].start();
            try {
                // 等待线程完成
                pasteThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
