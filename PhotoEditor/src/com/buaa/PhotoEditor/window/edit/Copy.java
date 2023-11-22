package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Copy {
    public JMenuItem copyItem;
    private Window window;

    public Copy(Window window) {
        this.window = window;
        copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copySelectedRegion(evt);
            }
        });
    }

    private void copySelectedRegion(ActionEvent e) {
        window.region.selectRegionItem.setSelected(false);
        // 如果还没有选择区域，弹出提示框
        if (window.region.selectedRegionLabel.getBorder() == null) {
            JOptionPane.showMessageDialog(null, "Please select region first");
            return;
        }
        window.region.copyRegionMat = window.img.submat(MatUtil.getRect(window.region.selectedRegionLabel));
        // pasting状态置1
        window.pasting = true;
    }
}
