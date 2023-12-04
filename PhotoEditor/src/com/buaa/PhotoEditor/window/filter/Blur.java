package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Blur {
    public JMenuItem blurItem;
    public Window window;

    public Blur(Window window) {
        this.window = window;
        blurItem = new JMenuItem("Blur");
        blurItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_MASK));
        blurItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                blurActionPerformed(evt);
            }
        });
    }
    public void blurActionPerformed(ActionEvent evt) {
        // 如果未选择图片，弹窗提示并return
        if (window.img == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }

        int blurLevel = Integer.parseInt(JOptionPane.showInputDialog(null, "Nível de desfoque", JOptionPane.WARNING_MESSAGE));

        Mat newImg = MatUtil.copy(window.img);

        if (window.tool.region.selectRegionItem.isSelected()) {

            MatUtil.blur(newImg, blurLevel, MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
            window.tool.region.removeRegionSelected();

        } else {
            MatUtil.blur(newImg, blurLevel);
        }

        MatUtil.show(newImg, window.showImgRegionLabel);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.img);
        window.img = newImg;

    }
}
