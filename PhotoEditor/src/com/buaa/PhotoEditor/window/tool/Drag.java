package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;

/**
 * @Description 拖拽图片，实现方式是根据鼠标拖动的位置重新设置图片的位置
 * @author 张旖霜
 * @date 11/27/2023 12:57 PM
 * @version: 2.0
 */
public class Drag {
    private Window window;
    public JCheckBoxMenuItem dragItem;
    private boolean flag;

    public Drag(Window window) {
        this.window = window;
        dragItem = new JCheckBoxMenuItem("Drag");
        flag = false;

        Dimension preferredSize = new Dimension(80, 35);
        dragItem.setPreferredSize(preferredSize);

        dragItem.addActionListener(e -> {
            if (window.originalImg == null) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
                dragItem.setSelected(false);
            }
        });

        dragItem.addItemListener(e -> initDragItem(e));
    }

    /**
     * @param e 事件
     * @Description 初始化 dragItem，若已选择，开始监听滑鼠
     * @author 张旖霜
     * @date 11/27/2023 12:57 PM
     * @version: 2.0
     */
    private void initDragItem(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            window.tool.getRegion().removeRegionSelected();
            window.tool.getPen().penItem.setSelected(false);
            window.tool.getEraser().eraserItem.setSelected(false);
            window.showImgRegionLabel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            // flag 的作用是让监听器只启动一次，启动后将一直监听
            if (!flag) {
                dragListener();
                flag = true;
            }
        }
    }

    private void dragListener() {
        window.panel.setLayout(null);
        MouseInputAdapter mia = new MouseInputAdapter() {
            int ex;
            int ey;

            /**
             * @param e 鼠标事件
             * @Description 获取鼠标位置
             * @author 张旖霜
             * @date 11/27/2023 12:57 PM
             * @version: 2.0
             */
            public void mousePressed(MouseEvent e) {
                ex = e.getX();
                ey = e.getY();
            }

            /**
             * @param e 鼠标事件
             * @Description 根据鼠标位置移动图片，实现拖动图片操作
             * @author 张旖霜
             * @date 11/27/2023 12:57 PM
             * @version: 2.0
             */
            public void mouseDragged(MouseEvent e) {
                if (dragItem.isSelected()) {
                    int lx = window.showImgRegionLabel.getX();
                    int ly = window.showImgRegionLabel.getY();
                    int x = e.getX() + lx;
                    int y = e.getY() + ly;
                    window.showImgRegionLabel.setLocation(x - ex, y - ey);
                    window.panel.revalidate();
                    window.panel.repaint();
                }
            }
        };
        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }
}