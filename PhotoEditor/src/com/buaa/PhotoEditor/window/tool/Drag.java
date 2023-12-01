package com.buaa.PhotoEditor.window.tool;
/*
 * @Description:拖拽图片，实现方式是根据鼠标拖动的位置重新设置图片的位置
 * !!!因为实现了拖拽功能，所以很多操作都要改成在window.showImgRegionLabel上操作，而不能在window.panel上操作
 * （可能过后合并后，有位置偏移或者操作位置不对的问题，很大概率会是这个原因）
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

    public boolean flag;

    public Drag(Window window) {
        this.window = window;
        dragItem = new JCheckBoxMenuItem("Drag");


        flag = false;

        dragItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.tool.region.removeRegionSelected();
                    window.tool.pen.penItem.setSelected(false);
                    window.tool.eraser.eraserItem.setSelected(false);
                    window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    // flag 的作用是让监听器只启动一次，启动后将一直监听
                    if (flag == false) {
                        dragListener();
                        flag = true;
                    }
                }
            }
        });
    }
    public void dragListener() {
//        JLabel imgLabel = window.showImgRegionLabel;
        /*
         下面这两行代码是不是等价于MatUtil.show(window.img, window.showImgRegionLabel);
         是不是不写也行？
         */
        ImageIcon imgIcon = new ImageIcon(MatUtil.bufferedImg(window.img));
        window.showImgRegionLabel.setIcon(imgIcon);
        MouseInputAdapter mia = new MouseInputAdapter() {
            int ex;
            int ey;

            public void mousePressed(MouseEvent e) {
                ex = e.getX();
                ey = e.getY();
            }

            public void mouseDragged(MouseEvent e) {
                if (dragItem.isSelected()) {
                    int lx = window.showImgRegionLabel.getX();
                    int ly = window.showImgRegionLabel.getY();
                    int x = e.getX() + lx;
                    int y = e.getY() + ly;
//                    if (x-ex < 0 || y-ey<0 || x-ex>window.getSize().width || y-ex > window.getSize().height) return;
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
