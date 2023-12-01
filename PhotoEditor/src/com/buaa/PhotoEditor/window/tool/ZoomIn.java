package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import static com.buaa.PhotoEditor.window.Constant.*;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowStateListener;
import java.util.zip.CheckedOutputStream;

public class ZoomIn {

    public Window window;
    public JMenu zoomInItem;

    public ZoomIn(Window window) {
        this.window = window;
        zoomInItem = new JMenu("Zoom +");

//        zoomInItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.SHIFT_MASK));
        zoomInItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                zoomInActionPerformed(evt);
            }
        });
    }

    public void zoomInActionPerformed(
            java.awt.event.MouseEvent evt) {
        zoomIn(window.img);
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
    private void zoomIn(Mat img) {
        // 不能再放大了，return，后期加弹窗
        if (window.counter == MAX_SIZE_COUNTER) {
            return;
        } else {
            window.counter++;
        }
        int counter = window.counter;
        int width = window.size[counter][0];
        int height = window.size[counter][1];
        window.showImgRegionLabel.setSize(width, height);
        MatUtil.show(window.zoomImg[counter], window.showImgRegionLabel);
        window.panel.setLayout(window.gridBagLayout);

    }
}
