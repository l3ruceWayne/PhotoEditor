package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private void zoomIn(Mat img) {
//        if (window.zoomImg == null) {
//            window.zoomImg = MatUtil.copy(img);
//        }
        int width = window.img.width()+(window.img.width()*10/100);
        int height = window.img.height()+(window.img.height()*10/100);
        // 重新设置显示的图片大小
        MatUtil.resize(img, new Size(width, height));
        window.showImgRegionLabel.setSize(width, height);

        MatUtil.show(window.img, window.showImgRegionLabel);
        // 更新eraser需要的原图
        window.originalImg = MatUtil.copy(window.img);
    }
}
