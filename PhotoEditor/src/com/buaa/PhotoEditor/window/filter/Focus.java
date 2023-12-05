package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

public class Focus {
    public JMenuItem focusItem;
    public Window window;

    public Focus(Window window) {
        this.window = window;
        focusItem = new JMenuItem("Focus");
        focusItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        focusItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                focusActionPerformed(evt);
            }
        });
    }
    public void focusActionPerformed(ActionEvent evt) {
        // 如果未选择图片，弹窗提示并return
        if (window.originalImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }

        if (window.tool.region.selectRegionItem.isSelected()) {

            Mat newImg = MatUtil.copy(window.img);
            MatUtil.focus(newImg, MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
            MatUtil.show(newImg, window.showImgRegionLabel);

            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            window.last.push(window.img);
            window.img = newImg;

            window.tool.region.removeRegionSelected();

        } else
            JOptionPane.showMessageDialog(null, "Please select a region first");
    }
}
