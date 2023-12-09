package com.buaa.PhotoEditor.window.filter;

import static com.buaa.PhotoEditor.util.MatUtil.*;

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

        Mat newImg = copy(window.zoomImg[window.counter]);

        if (window.tool.region.selectRegionItem.isSelected()) {

            MatUtil.grayScale(newImg,
                    getRect(window.tool.region
                            .selectedRegionLabel[window.counter]));
            window.tool.region.removeRegionSelected();

        } else {
            MatUtil.grayScale(newImg);
        }

        MatUtil.show(newImg, window.showImgRegionLabel);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.zoomImg);
        window.zoomImg[window.counter] = newImg;
    }
}