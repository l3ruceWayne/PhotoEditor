package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

public class Pen {
    public JCheckBoxMenuItem penItem;
    public Window window;
    public Pen(Window window) {
        this.window = window;
        // menubar
        penItem = new JCheckBoxMenuItem("Pen");
        // shortcut
//        penItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
//                InputEvent.ALT_MASK
//                        | InputEvent.SHIFT_MASK
//                        | InputEvent.CTRL_MASK)
//        );
        // onclick event
        penItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.tool.region.removeRegionSelected();
                    window.tool.eraser.eraserItem.setSelected(false);
                    window.tool.drag.dragItem.setSelected(false);
                }
            }
        });
    }

    public void paint(int x, int y) {
        // 调整界面大小，让图片填充后可以删掉
        // pending
        int newX = window.tool.drag.newX;
        int newY = window.tool.drag.newY;
//        if (x > window.img.width()+newX || x < newX || y > window.img.height()+newY || y < newY) return;
        // pending
        if (window.paintingImg == null) {
            window.paintingImg = MatUtil.copy(window.img);
        }
        MatUtil.paint(new int[]{
                        window.penColor.getBackground().getBlue(),
                        window.penColor.getBackground().getGreen(),
                        window.penColor.getBackground().getRed()},
                window.tool.penSize,
                window.tool.penSize,
                x - newX,
                y - newY,
                window.paintingImg);

        // 实现笔画的方法是，鼠标的坐标+penSize构成Rect，将img的rect区域进行像素更改

        MatUtil.show(window.paintingImg, window.showImgRegionLabel);

    }


}
