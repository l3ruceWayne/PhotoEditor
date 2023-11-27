package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
/*
* @Description:还未解决因为drag而产生的bug，重定位问题
* @author: 张旖霜
* @date: 11/27/2023 3:31 PM
* @version: 1.0
*/
public class Region {
    public int selectedRegionX, selectedRegionY;
    public Mat copyRegionMat;
    public JLabel selectedRegionLabel;
    public Window window;
    public JCheckBoxMenuItem selectRegionItem;

    public Point pointRegion;

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
                    window.tool.drag.dragItem.setSelected(false);
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

    public void addRegion(Point p) {
//        int newX = window.tool.drag.newX;
//        int newY = window.tool.drag.newY;
//        if (p.x < window.img.width()+newX || p.x > newX || p.y < window.img.height()+newY || p.y > newY) return;
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
        pointRegion = new Point(x+width, y-height);
    }

    public void disableListeners() {
        selectRegionItem.setSelected(false);
        window.pasting = false;
    }

}
