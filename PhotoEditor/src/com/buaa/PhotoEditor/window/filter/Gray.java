package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Gray {
    public JMenuItem grayItem;
    public Window window;

    public Gray(Window window) {
        this.window = window;
        grayItem = new JMenuItem("Gray");

        grayItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                InputEvent.CTRL_MASK));

        grayItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grayActionPerformed(evt);
            }
        });
    }
    public void grayActionPerformed(ActionEvent evt) {

        Mat newImg = MatUtil.copy(window.img);

        if (window.tool.region.selectRegionItem.isSelected()) {

            MatUtil.grayScale(newImg, MatUtil.getRect(window.tool.region.selectedRegionLabel));
            window.tool.region.removeRegionSelected();

        } else {
            MatUtil.grayScale(newImg);
        }

        MatUtil.show(newImg, window.showImgRegionLabel);

        window.isProperty.push(0);
        window.last.push(window.img);
        window.img = newImg;
    }
}
