package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Shrink {

    public Window window;
    public JMenu zoomOutItem;

    public Mat matZoomOut;
    public Mat matZoomOutNexLayerImg;

    public Shrink(Window window) {
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

//        zoomOutItem.setEnabled(false);

        MatUtil.resize(window.img, new Size(window.zoomRegion.getWidth(), window.zoomRegion.getHeight()));

        window.img.copyTo(matZoomOut.submat(MatUtil.getRect(window.zoomRegion)));
        window.img = matZoomOut;

        MatUtil.show(window.img, window.showImgRegionLabel);
        window.nexLayerImg = matZoomOutNexLayerImg;

    }
}
