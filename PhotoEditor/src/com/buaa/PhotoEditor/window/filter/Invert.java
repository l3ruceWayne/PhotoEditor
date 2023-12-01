package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Invert {
    public JMenuItem invertItem;
    public Window window;

    public Invert(Window window) {
        this.window = window;
        invertItem = new JMenuItem("Invert");
        invertItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        invertItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertActionPerformed(evt);
            }
        });
    }
    public void invertActionPerformed(ActionEvent evt) {

        Mat newImg = MatUtil.copy(window.img);

        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.inversor(newImg, MatUtil.getRect(window.tool.region.selectedRegionLabel));
            window.tool.region.removeRegionSelected();
        } else
            MatUtil.inversor(newImg);

        MatUtil.show(newImg, window.showImgRegionLabel);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.img);
        window.img = newImg;

    }
}
