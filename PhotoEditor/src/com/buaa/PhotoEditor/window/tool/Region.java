package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
/*
* @Description:drag后重新定位的bug已修改
* 点击选择区域功能后，光标暂且设置成默认
* （如果上一个状态是画笔，如果不设置成默认，就一直是画笔了）
* @author: 张旖霜、卢思文
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

    private boolean flag;

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

                    window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                    window.tool.drag.dragItem.setSelected(false);

                    if (flag == false) {
                        regionListener();
                        flag = true;
                    }

                }
            }
        });
    }

    /*
     * @param:
     * @return
     * @Description:监听鼠标状态（从tool类换到这里主要是为了修改drag后无法使用画笔的bug）
     * （在tool类是在window.panel上进行监听，换到Region类，在window.showImgRegionLabel进行监听）
     * @author: 张旖霜
     * @date: 11/27/2023 7:53 PM
     * @version: 1.0
     */
    public void regionListener() {
        ImageIcon imgIcon = new ImageIcon(MatUtil.bufferedImg(window.img));
        window.showImgRegionLabel.setIcon(imgIcon);
        MouseInputAdapter mia = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (selectRegionItem.isSelected()) {
                    addRegion(e.getPoint());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectRegionItem.isSelected()) {
                    setRegionSize(e.getX(), e.getY());
                }
            }
        };

        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }

    public void removeRegionSelected() {
        window.panel.setLayout(null);
        window.showImgRegionLabel.remove(selectedRegionLabel);
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

        // 进入pasting模式
        window.pasting = true;
    }



    public void addRegion(Point p) {
        selectedRegionLabel.setLocation(p);
        selectedRegionLabel.setSize(1, 1);
        selectedRegionLabel.setBorder(BorderFactory
                .createLineBorder(Color.cyan));

        window.showImgRegionLabel.setLayout(null);
        window.showImgRegionLabel.add(selectedRegionLabel);
//        // 可能过后改bug会用
//        // 在panel的z轴视角上设置各组件的优先级/遮盖关系：index小的，优先级高
        window.showImgRegionLabel.setComponentZOrder(selectedRegionLabel, 0);
//        for (int i = 0; i < window.add.widget.widgetLabelList.size(); i++) {
//            window.panel.setComponentZOrder(window.add.widget.widgetLabelList.get(i), i + 1);
//        }
//        window.panel.setComponentZOrder(window.showImgRegionLabel,
//                window.add.widget.widgetLabelList.size() + 1);
//
        window.showImgRegionLabel.revalidate();
        window.showImgRegionLabel.repaint();

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
