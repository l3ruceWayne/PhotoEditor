package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Eraser {
    public JCheckBoxMenuItem eraserItem;
    public Window window;
    public Tool tool;
    public Eraser(Window window) {
        this.window = window;
        tool = new Tool(window);
        eraserItem = new JCheckBoxMenuItem("Eraser");
        eraserItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.region.removeRegionSelected();
                    window.pen.penItem.setSelected(false);
                }
            }
        });
    }

    // pending 加一个erase的大小调节功能
    // 一键清除功能
    public void erase(int x, int y) {
        // pending
        if (x > window.img.width() || y > window.img.height()) return;

        if (window.paintingImg == null) {
            window.paintingImg = MatUtil.copy(window.img);
        }

        Mat eraseRegion = window.paintingImg.submat(new Rect(x, y, tool.eraserSize,
                tool.eraserSize));

        // pending
            /*if (zoomOut.isEnabled())
                img2 = nexLayerImg.submat(MatUtil.getRect(zoomRegion))
                        .submat(new Rect(x, y, width, height));
            else

             */
        Mat originalRegion = window.originalImg.submat(new Rect(x, y, tool.eraserSize, tool.eraserSize));
        // 拿原图覆盖现在正在画的图，就相当于橡皮擦操作
        MatUtil.overlay(eraseRegion, originalRegion);

        MatUtil.show(window.paintingImg, window.showImgRegionLabel);

    }
}
