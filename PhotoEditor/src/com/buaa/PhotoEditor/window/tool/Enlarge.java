package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Enlarge {

    public Window window;
    public JMenu zoomInItem;

    public Enlarge(Window window) {
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

        if (window.tool.region.selectRegionItem.isSelected()) {
            window.tool.zoomRegion = new JPanel();
            window.tool.zoomRegion.setBounds(window.tool.region.selectedRegionLabel.getX(),
                    window.tool.region.selectedRegionLabel.getY(),
                    window.tool.region.selectedRegionLabel.getWidth(),
                    window.tool.region.selectedRegionLabel.getHeight());
            window.tool.zoomOut.matZoomOut = MatUtil.copy(window.img);

            window.img = window.img.submat(MatUtil.getRect(window.tool.zoomRegion));

            //pen image - 2nd layer
            if (window.nexLayerImg != null) {

                window.tool.zoomOut.matZoomOutNexLayerImg = MatUtil.copy(window.nexLayerImg);
                window.nexLayerImg = window.nexLayerImg.submat(MatUtil.getRect(window.tool.zoomRegion));
                MatUtil.resize(window.nexLayerImg, window.tool.zoomOut.matZoomOut.size());
            }

            MatUtil.resize(window.img, window.tool.zoomOut.matZoomOut.size());
            MatUtil.show(window.img, window.showImgRegionLabel);
            window.tool.region.removeRegionSelected();

        } else
            JOptionPane.showMessageDialog(null, "Select an area to zoom in!");

    }
}
