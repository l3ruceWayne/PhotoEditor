package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.window.Constant.*;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.PathIterator;
import java.util.zip.CheckedOutputStream;

public class ZoomIn {

    public Window window;
    public JMenuItem zoomInItem;

    public ZoomIn(Window window) {
        this.window = window;
        zoomInItem = new JMenu("Zoom+");


        zoomInItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                zoomIn();
            }
        });
    }

    /*
     * @param img: 当前图片
     * @return
     * @Description:放大图片（+10%）后期可以考虑让用户设定放大缩小的比例
     * @author: 张旖霜
     * @date: 11/27/2023 1:00 PM
     * @version: 1.0
     */

    /**
     * @param
     * @return
     * @Description: 无损像素
     * @author: 卢思文
     * @date: 12/1/2023 3:34 PM
     * @version: 1.0
     **/
    public void zoomIn() {
        // 如果未选择图片，弹窗提示并return
        if (window.originalImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        if(window.add.getWidget().widgetIcon != null){
            JOptionPane.showMessageDialog(null,
                    "Please handle widget first");
            return;
        }
        // 取消 drag
        window.tool.drag.dragItem.setSelected(false);
        window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        // 不能再放大了，return，后期加弹窗
        if (window.counter == MAX_SIZE_COUNTER) {
            return;
        } else {
            window.counter++;
        }
        if (window.tool.region.selectedRegionLabel[window.counter - 1] != null) {
            window.tool.region.selectedRegionLabel[window.counter - 1].setVisible(false);
        }
        if (window.tool.region.selectedRegionLabel[window.counter] != null) {
            window.tool.region.selectedRegionLabel[window.counter].setVisible(true);
        }
//        if (window.add.widget.widgetLabel[window.counter - 1] != null) {
//            window.add.widget.widgetLabel[window.counter - 1].setVisible(false);
//        }
//        if (window.add.widget.widgetLabel[window.counter] != null) {
//            window.add.widget.widgetLabel[window.counter].setVisible(true);
//        }

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
            window.showImgRegionLabel.setLocation((panelWidth - width)/2,
                    (panelHeight - height)/2);
        } else{
            window.panel.setLayout(window.gridBagLayout);
        }
    }
}
