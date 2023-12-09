package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/*
* @Description:查看编辑成果，长按preview按钮显示图片原本大小的效果，松开恢复
* @author: 张旖霜
* @date: 12/9/2023 9:59 AM
* @version: 1.0
*/
public class Preview {
    public Window window;
    public JMenu previewItem;

    public Preview(Window window) {
        this.window = window;
        previewItem = new JMenu("Preview");
        previewItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                showOriginalImg();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                showZoomImg();
            }
        });
    }

    /*
     * @param
     * @return
     * @Description: 长按显示原图大小编辑成果
     * @author: 张旖霜
     * @date: 12/9/2023 10:00 AM
     * @version: 1.0
     */
    public void showOriginalImg(){
        window.tool.region.removeRegionSelected();
        window.tool.pen.penItem.setSelected(false);
        window.tool.eraser.eraserItem.setSelected(false);
        window.tool.drag.dragItem.setSelected(false);
        MatUtil.show(window.zoomImg[ORIGINAL_SIZE_COUNTER], window.showImgRegionLabel);
        window.showImgRegionLabel.setSize(window.zoomImg[ORIGINAL_SIZE_COUNTER].width(),
                window.zoomImg[ORIGINAL_SIZE_COUNTER].height());
        window.panel.setLayout(window.gridBagLayout);
    }

    /*
     * @param
     * @return
     * @Description: 松开显示counter大小的图片（恢复）
     * @author: 张旖霜
     * @date: 12/9/2023 10:00 AM
     * @version: 1.0
     */
    public void showZoomImg()
    {
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
