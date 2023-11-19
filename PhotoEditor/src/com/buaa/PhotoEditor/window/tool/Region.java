package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Region {
    public int selectedRegionX, selectedRegionY;
    public Mat copyRegionMat;
    public JLabel selectedRegionLabel;
    public Window window;
    public JCheckBoxMenuItem selectRegionItem;

    public Region(Window window) {
        this.window = window;
        selectedRegionLabel = new JLabel();
        selectRegionItem = new JCheckBoxMenuItem("Region");
        selectRegionItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.pen.penItem.setSelected(false);
                    window.eraser.eraserItem.setSelected(false);
                }
            }
        });
    }

    public void removeRegionSelected() {
        window.panel.setLayout(null);
        window.panel.remove(selectedRegionLabel);
        window.panel.revalidate();
        window.panel.repaint();
        selectRegionItem.setSelected(false);
    }
    public void copySelectedRegion(ActionEvent evt) {

        selectRegionItem.setSelected(false);
        // 如果还没有选择区域，弹出对话框
        if(selectedRegionLabel.getBorder() == null) {
            // pending 对话框的位置？
            JOptionPane.showMessageDialog(null,
                    "Please select region first");
            return;
        }
        copyRegionMat = window.img.submat(MatUtil.getRect(selectedRegionLabel));
        // pending 下面代码的作用不明
//        JLabel lbRegion = new JLabel();
//        MatUtil.show(copyRegionMat, lbRegion);
//        selectedRegionLabel.setLayout(null);
//        selectedRegionLabel.add(lbRegion);
//        selectedRegionLabel.revalidate();
//        selectedRegionLabel.repaint();

        // 进入pasting模式
        window.pasting = true;
    }

}
