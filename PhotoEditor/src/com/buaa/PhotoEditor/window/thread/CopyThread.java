package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @Description 实现复制的多线程，同时对多张图片执行复制操作
 * @author 卢思文
 * @date 12/10/2023 10:33 AM
 * @version 1.1
 */

public class CopyThread extends Thread {
    public Window window;
    public JMenuItem copyItem;
    public int i;

    public CopyThread(Window window, JMenuItem copyItem, int i) {
        this.window = window;
        this.copyItem = copyItem;
        this.i = i;
    }

    @Override
    public void run() {
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
            InputEvent.CTRL_MASK));
        copyItem.addActionListener(evt -> copySelectedRegion());
    }
    /**
     * @Description 获取选区并将pasting状态置1 增加未选择图片弹窗
     * @author 罗雨曦、卢思文
     * @date 12/5/2023 3:28 PM
     * @version: 2.0
     **/
    private void copySelectedRegion() {
        window.tool.getRegion().selectRegionItem.setSelected(false);
        //如果未选择图片，弹窗提示并return
        if (window.zoomImg == null) {
            if(i == window.counter){
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        // 如果还没有选择区域，弹出提示框
        if (window.tool.getRegion().selectedRegionLabel[i].getBorder() == null) {
            if(i == window.counter){
                JOptionPane.showMessageDialog(null, "Please select region first");
            }
            return;
        }
        window.copyRegionImg[i] = window.zoomImg[i].submat(MatUtil.getRect(window.tool.getRegion().selectedRegionLabel[i]));
        // pasting状态置1
        window.pasting = true;
    }
}
