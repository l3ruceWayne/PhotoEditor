package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.buaa.PhotoEditor.window.Constant.*;

/**
 * @Description 实现了cut的多线程
 * @author 张旖霜
 * @date 12/9/2023 10:21 AM
 * @version 1.0
 */
public class CutThread extends Thread {
    public Window window;
    public int i;
    public JMenuItem cutItem;

    public CutThread(Window window, int i, JMenuItem cutItem) {
        this.window = window;
        this.i = i;
        this.cutItem = cutItem;
    }

    @Override
    public void run() {
        // 进行监听
        cutItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                java.awt.event.InputEvent.ALT_MASK
                        | java.awt.event.InputEvent.CTRL_MASK));
        cutItem.addActionListener(new ActionListener() {
            /**
             * @param evt 触发操作
             * @Description 利用MatUtil实现图片的剪切与展示
             * @author 罗雨曦
             * @date 2023/11/27 14:11
             * @version 1.0
             */
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (window.zoomImg == null) {
                    if (i == window.counter) {
                        JOptionPane.showMessageDialog(null,
                                "Please open an image first");
                    }
                    return;
                }
                if (i == window.counter) {
                    window.tool.getRegion().selectRegionItem.setSelected(false);
                }
                // 如果还没有选择区域，弹出提示框
                if (window.tool.getRegion().selectedRegionLabel[i].getBorder() == null) {
                    if (i == window.counter) {
                        JOptionPane.showMessageDialog(null,
                                "Please select region first");
                    }
                } else {
                    if (i == ORIGINAL_SIZE_COUNTER) {
                        window.next.clear();
                        window.nextOriginalImg.clear();
                        window.nextPropertyValue.clear();
                        // 当前property的值入栈
                        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
                        // 将当前的window.img压入window.last中，保存上一张图片
                        window.last.push(copyImgArray(window.zoomImg));
                        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
                    }
                    //从window.img图像中裁剪出window.region.selectedRegionLabel[window.counter]标识的区域，并将裁剪后的图像赋值给新的Mat对象newImg
                    window.zoomImg[i] = MatUtil.cut(window.zoomImg[i], MatUtil.getRect(window.tool.getRegion().selectedRegionLabel[i]));
                    window.originalZoomImg[i] = MatUtil.cut(window.originalZoomImg[i], MatUtil.getRect(window.tool.getRegion().selectedRegionLabel[i]));
                    // 当前大小改成cut后的图片大小
                    window.size[i][0] = window.zoomImg[i].width();
                    window.size[i][1] = window.zoomImg[i].height();
                    if (i == window.counter) {
                        window.panel.setLayout(null);
                        // 显示图片
                        MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
                        window.showImgRegionLabel.setSize(window.zoomImg[window.counter].width(),
                                window.zoomImg[window.counter].height());
                        window.panel.setLayout(window.gridBagLayout);
                    }
                    window.tool.getRegion().selectedRegionLabel[i].setBorder(null);
                    window.showImgRegionLabel.remove(window.tool.getRegion().selectedRegionLabel[i]);
                    window.panel.revalidate();
                    window.panel.repaint();
                }
            }
        });
    }
}
