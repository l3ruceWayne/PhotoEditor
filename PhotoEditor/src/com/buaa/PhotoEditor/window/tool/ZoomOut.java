package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ZoomOut {

    public Window window;
    public JMenu zoomOutItem;

    public Mat matZoomOut;
    public Mat matZoomOutNexLayerImg;

    public ZoomOut(Window window) {
        this.window = window;
        zoomOutItem = new JMenu("Zoom -");

//        zoomOutItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.SHIFT_MASK));
        zoomOutItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                zoomOut();
            }
        });
    }


    /*
     * @param img: Mat类型图片（当前图片）
     * @return
     * @Description:缩小图片（-10%）后期可以考虑让用户设定放大缩小的比例
     * @author: 张旖霜
     * @date: 11/27/2023 1:01 PM
     * @version: 1.0
     */
    public void zoomOut() {
// 不能再放大了，return，后期加弹窗
        if (window.counter == 0) {
            return;
        } else {
            window.counter--;
        }
        window.tool.region.selectedRegionLabel[window.counter + 1].setVisible(false);
        window.tool.region.selectedRegionLabel[window.counter].setVisible(true);
        int counter = window.counter;
        int width = window.size[counter][0];
        int height = window.size[counter][1];
        int panelWidth = window.panel.getWidth();
        int panelHeight = window.panel.getHeight();
        window.panel.setLayout(null);
        window.showImgRegionLabel.setSize(width, height);
        MatUtil.show(window.zoomImg[counter], window.showImgRegionLabel);
        if (width > panelWidth
                || height > panelHeight) {
            window.showImgRegionLabel.setLocation((panelWidth - width) / 2,
                    (panelHeight - height) / 2);
        } else {
            window.panel.setLayout(window.gridBagLayout);
        }
    }
}
