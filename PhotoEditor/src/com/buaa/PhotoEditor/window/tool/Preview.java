package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description: 查看编辑成果，长按preview按钮显示图片原本大小的效果，松开恢复
 * @author: 张旖霜
 * @date: 12/9/2023 9:59 AM
 * @version: 3.0
 */
public class Preview {
    public Window window;
    public JCheckBoxMenuItem previewItem;

    public Preview(Window window) {
        this.window = window;
        previewItem = new JCheckBoxMenuItem("Preview");
        previewItem.addActionListener(e -> {
            if (window.originalImg == null) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
                previewItem.setSelected(false);
                return;
            }
            if(window.add.getWidget().widgetIcon != null){

                JOptionPane.showMessageDialog(null,
                        "Please handle widget first");
                return;
            }
            if (previewItem.isSelected()) {
                showOriginalImg();
            } else {
                showZoomImg();
            }
        });
    }

    /**
     * @Description: 长按显示原图大小编辑成果
     * @author: 张旖霜
     * @date: 12/9/2023 10:00 AM
     * @version: 3.0
     */
    public void showOriginalImg() {
        System.out.println("no");
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            window.tool.getRegion().removeRegionSelected(i);
        }
        window.tool.getPen().penItem.setSelected(false);
        window.tool.getEraser().eraserItem.setSelected(false);
        window.tool.getDrag().dragItem.setSelected(false);
        int counter = window.counter;
        int width = window.size[ORIGINAL_SIZE_COUNTER][0];
        int height = window.size[ORIGINAL_SIZE_COUNTER][1];
        int panelWidth = window.panel.getWidth();
        int panelHeight = window.panel.getHeight();
        window.panel.setLayout(null);
        window.showImgRegionLabel.setSize(width, height);
        MatUtil.show(window.zoomImg[ORIGINAL_SIZE_COUNTER], window.showImgRegionLabel);
        if (width > panelWidth
                || height > panelHeight) {
            window.showImgRegionLabel.setLocation((panelWidth - width) / 2,
                    (panelHeight - height) / 2);
        } else {
            window.panel.setLayout(window.gridBagLayout);
        }
    }

    /**
     * @Description: 松开显示counter大小的图片（恢复）
     * @author: 张旖霜
     * @date: 12/9/2023 10:00 AM
     * @version: 3.0
     */
    public void showZoomImg() {
        System.out.println("yes");
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
