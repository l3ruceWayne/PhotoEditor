package com.buaa.PhotoEditor.window.tool;
/*
 * @Description:拖拽图片，实现方式是根据鼠标拖动的位置重新设置图片的位置
 * （Bug：drag后，region,pen,eraser定位有问题，而且拖动时有点卡；要限制拖动范围）
 * @author: 张旖霜
 * @date: 11/27/2023 12:57 PM
 * @version: 1.0
 */
import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Drag {
    public Window window;
    public JCheckBoxMenuItem dragItem;

    public int newX, newY;
    public boolean flag;

    public Drag(Window window) {
        this.window = window;
        dragItem = new JCheckBoxMenuItem("Drag");

        newX = 0;
        newY = 0;

        if (window.img != null) dragListener();

        dragItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.tool.region.removeRegionSelected();
                    window.tool.pen.penItem.setSelected(false);
                    window.tool.eraser.eraserItem.setSelected(false);
                }
            }
        });
    }
    public void dragListener() {
        JLabel imgLabel = window.showImgRegionLabel;
        ImageIcon imgIcon = new ImageIcon(MatUtil.bufferedImg(window.img));
        imgLabel.setIcon(imgIcon);
        MouseInputAdapter mia = new MouseInputAdapter() {
            int ex;
            int ey;

            public void mousePressed(MouseEvent e) {
                ex = e.getX();
                ey = e.getY();
            }

            public void mouseDragged(MouseEvent e) {
                if (dragItem.isSelected()) {
                    int lx = imgLabel.getX();
                    int ly = imgLabel.getY();
                    int x = e.getX() + lx;
                    int y = e.getY() + ly;
                    newX = lx;
                    newY = ly;
                    imgLabel.setLocation(x - ex, y - ey);
                    window.panel.setComponentZOrder(imgLabel, 100);

                    window.panel.revalidate();
                    window.panel.repaint();
                }
            }
        };

        imgLabel.addMouseListener(mia);
        imgLabel.addMouseMotionListener(mia);


        window.panel.add(imgLabel);
        window.panel.revalidate();
        window.panel.repaint();

    }
}
