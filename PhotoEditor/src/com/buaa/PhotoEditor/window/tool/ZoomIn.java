package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @Description: 放大图片
 * @author: 张旖霜 卢思文
 * @date: 12/12/2023 11:58 AM
 * @version: 1.0
 */
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

    /**
     * @Description: 无损像素放大图片（换下一个放大比例的图）
     * @author: 卢思文
     * @date: 12/1/2023 3:34 PM
     * @version: 3.0
     */
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
        window.tool.getDrag().dragItem.setSelected(false);
        window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        // 不能再放大了
        if (window.counter == MAX_SIZE_COUNTER) {
            return;
        } else {
            window.counter++;
        }
        if (window.tool.getRegion().selectedRegionLabel[window.counter - 1] != null) {
            window.tool.getRegion().selectedRegionLabel[window.counter - 1].setVisible(false);
        }
        if (window.tool.getRegion().selectedRegionLabel[window.counter] != null) {
            window.tool.getRegion().selectedRegionLabel[window.counter].setVisible(true);
        }

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
