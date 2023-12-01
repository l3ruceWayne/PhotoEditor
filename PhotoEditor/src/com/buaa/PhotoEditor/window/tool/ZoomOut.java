package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
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
                zoomOutActionPerformed(evt);
            }
        });
    }

    public void zoomOutActionPerformed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomOutActionPerformed

        zoomOut(window.img);
    }

   /*
   * @param img: Mat类型图片（当前图片）
   * @return
   * @Description:缩小图片（-10%）后期可以考虑让用户设定放大缩小的比例
   * @author: 张旖霜
   * @date: 11/27/2023 1:01 PM
   * @version: 1.0
   */
    private void zoomOut(Mat img) {
//        if (window.zoomImg == null) {
//            window.zoomImg = MatUtil.copy(img);
//        }
        int width = window.img.width()-(window.img.width()*10/100);
        int height = window.img.height()-(window.img.height()*10/100);
        // 重新设置显示的图片大小
        MatUtil.resize(img, new Size(width, height));
        window.showImgRegionLabel.setSize(width, height);

        MatUtil.show(window.img, window.showImgRegionLabel);
        // 更新eraser需要的原图
        window.originalImg = MatUtil.copy(window.img);
    }
}
