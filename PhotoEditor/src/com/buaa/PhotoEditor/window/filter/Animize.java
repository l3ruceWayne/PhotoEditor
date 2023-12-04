package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animize {
    public JMenuItem animizeItem;
    public Window window;

    public Animize(Window window) {
        this.window = window;
        animizeItem = new JMenuItem("Animize");
        animizeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        animizeItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animizeActionPerformed(evt);
            }
        });
    }
    public void animizeActionPerformed(ActionEvent evt) {

        Mat newImg = MatUtil.copy(window.img);

        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.cartoon(newImg, MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
            window.tool.region.removeRegionSelected();
        } else
            MatUtil.cartoon(newImg);

        MatUtil.show(newImg, window.showImgRegionLabel);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.img);
        window.img = newImg;

    }
}
