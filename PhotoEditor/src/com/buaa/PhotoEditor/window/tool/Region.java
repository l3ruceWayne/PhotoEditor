package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
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
                    window.tool.pen.penItem.setSelected(false);
                    window.tool.eraser.eraserItem.setSelected(false);
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

    public void addRegion(Point p) {

        selectedRegionLabel.setLocation(p);
        selectedRegionLabel.setSize(1, 1);
        selectedRegionLabel.setBorder(BorderFactory
                .createLineBorder(Color.cyan));

        window.panel.setLayout(null);
        window.panel.add(selectedRegionLabel);

        // 在panel的z轴视角上设置各组件的优先级/遮盖关系：index小的，优先级高
        window.panel.setComponentZOrder(selectedRegionLabel, 0);
        for (int i = 0; i < window.add.widget.widgetLabelList.size(); i++) {
            window.panel.setComponentZOrder(window.add.widget.widgetLabelList.get(i), i + 1);
        }
        window.panel.setComponentZOrder(window.showImgRegionLabel,
                window.add.widget.widgetLabelList.size() + 1);

        window.panel.revalidate();
        window.panel.repaint();

        selectedRegionX = selectedRegionLabel.getX();
        selectedRegionY = selectedRegionLabel.getY();
    }

    public void setRegionSize(int x, int y) {

        int width = Math.abs(selectedRegionX - x);
        int height = Math.abs(selectedRegionY - y);
        x = Math.min(x, selectedRegionX);
        y = Math.min(y, selectedRegionY);
        selectedRegionLabel.setBounds(x, y, width, height);

    }

    public void disableListeners() {
        selectRegionItem.setSelected(false);
        window.pasting = false;
    }

}
