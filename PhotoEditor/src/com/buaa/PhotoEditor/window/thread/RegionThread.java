package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author 卢思文
 * @version 1.0
 * @Description 区域选择多线程，是复制、剪切、区域滤镜等的基础
 * @date 12/2/2023 8:56 PM
 */

public class RegionThread extends Thread {
    public Window window;
    public int i;

    public RegionThread(Window window, int i) {
        this.window = window;
        this.i = i;
    }

    @Override
    public void run() {
        MouseInputAdapter mia = new MouseInputAdapter() {
            /**
             * @Description 点击后确定区域的左上顶点或者右下顶点
             * @author 卢思文
             * @date 12/11/2023 9:30 PM
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (window.tool.getRegion().selectRegionItem.isSelected()) {
                    addRegion(e.getX(), e.getY());
                }
            }

            /**
             * @Description 拖拽以实现区域的放大缩小
             * @author 卢思文
             * @date 12/11/2023 9:31 PM
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                if (window.tool.getRegion().selectRegionItem.isSelected()) {
                    setRegionSize(e.getX(), e.getY());
                }
            }
        };
        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }

    /**
     * @Description 用于确定区域起点
     * @author 卢思文
     * @date 12/11/2023 9:32 PM
     */
    public void addRegion(int x, int y) {
        Point p = new Point(getValueAfterZoom(window, x, i),
                getValueAfterZoom(window, y, i));
        window.tool.getRegion().selectedRegionLabel[i].setLocation(p);
        window.tool.getRegion().selectedRegionLabel[i].setSize(1, 1);
        window.tool.getRegion().selectedRegionLabel[i].setBorder(BorderFactory
                .createLineBorder(Color.cyan));

//        window.showImgRegionLabel.setLayout(null);
        window.tool.getRegion().selectedRegionLabel[i].setVisible(false);
        window.showImgRegionLabel.add(window.tool.getRegion().selectedRegionLabel[i]);

        if (i == window.counter) {
            window.tool.getRegion().selectedRegionLabel[i].setVisible(true);
        }
        window.tool.getRegion().selectedRegionX[i]
                = window.tool.getRegion().selectedRegionLabel[i].getX();
        window.tool.getRegion().selectedRegionY[i]
                = window.tool.getRegion().selectedRegionLabel[i].getY();
    }

    /**
     * @Description 鼠标拖拽过程中，确定区域大小，同时防止区域超过图片之外
     * @author 卢思文
     * @date 12/11/2023 9:32 PM
     * @version 1.0
     */
    public void setRegionSize(int x, int y) {
        x = getValueAfterZoom(window, x, i);
        y = getValueAfterZoom(window, y, i);
        int width = Math.abs(window.tool.getRegion().selectedRegionX[i] - x);
        int height = Math.abs(window.tool.getRegion().selectedRegionY[i] - y);
        x = Math.min(x, window.tool.getRegion().selectedRegionX[i]);
        y = Math.min(y, window.tool.getRegion().selectedRegionY[i]);
        width = Math.min(getValueAfterZoom(window, window.showImgRegionLabel.getWidth(), i) - x, width);
        height = Math.min(getValueAfterZoom(window, window.showImgRegionLabel.getHeight(), i) - y, height);
        if (x < 0) {
            width += x;
            x = 0;
        }
        if (y < 0) {
            height += y;
            y = 0;
        }
        window.tool.getRegion().selectedRegionLabel[i].setBounds(x, y, width, height);

    }
}
