package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.PaintThread;
import com.buaa.PhotoEditor.window.thread.PasteThread;
import org.opencv.core.Mat;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.util.MatUtil.copy;
import static com.buaa.PhotoEditor.window.Constant.NUM_FOR_NEW;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description: 实现粘贴功能的Paste类
 * @Author 卢思文
 * @Create 11/26/2023 9:22 PM
 * @Version 1.0
 */
public class Paste {

    public PasteThread[] pasteThread;
    private Window window;

    public Paste(Window window) {
        this.window = window;
        pasteThread = new PasteThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            pasteThread[i] = new PasteThread(window, i);
            pasteThread[i].start();
            // 等待线程完成
            try {
                pasteThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void disablePasteMode() {
        window.tool.region.removeRegionSelected();
    }
}
