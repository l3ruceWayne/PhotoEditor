package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.PasteThread;

import static com.buaa.PhotoEditor.window.Constant.NUM_FOR_NEW;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;


/**
 * @author 卢思文
 * @version 1.0
 * @Description 实现粘贴功能的Paste类
 * @date 2023/11/26
 */
public class Paste {
    public PasteThread[] pasteThread;

    public Paste(Window window) {
        this.pasteThread = new PasteThread[NUM_FOR_NEW];
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
