package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.util.MatUtil.getValueAfterZoom;

public class PasteThread extends Thread {
    public Window window;
    public int i;
    public PasteThread(Window window, int i){
        this.window = window;
        this.i = i;
    }

    @Override
    public void run() {
        MouseInputAdapter mia = new MouseInputAdapter() {
            @Override
            /**
             * @Description: 粘贴状态下，鼠标点击会进行粘贴
             * @author: 卢思文
             * @date: 11/26/2023 9:14 PM
             * @version: 1.0
             **/
            public void mouseClicked(MouseEvent e) {
                int tempX = getValueAfterZoom(window, e.getX(), i);
                int tempY = getValueAfterZoom(window, e.getY(), i);
                if (tempX + window.tool.region.selectedRegionLabel[i].getSize().width > window.showImgRegionLabel.getSize().width
                        || tempY + window.tool.region.selectedRegionLabel[i].getSize().height > window.showImgRegionLabel.getSize().height) {
                    return;
                }

                if (window.pasting) {
                    // 当前property的值入栈，第一层将zoomImg数组入栈（这时仅zoomImg[0]是入栈的，其他的还没更新好）
                    if (i == 0) {
                        window.lastPropertyValue
                                .push(copyPropertyValue(window
                                        .currentPropertyValue));
                        window.last.push(copyImgArray(window.zoomImg));
                        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
                    }
                    Mat img = MatUtil.copy(window.zoomImg[i]);
                    MatUtil.copyToRegion(img,
                            window.copyRegionImg[i],
                            MatUtil.getRect(window.tool.region.selectedRegionLabel[i]));

                    window.zoomImg[i] = MatUtil.copy(img);

                    if (i == window.counter) {
                        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int tempX = getValueAfterZoom(window, e.getX(), i);
                int tempY = getValueAfterZoom(window, e.getY(), i);
                if (window.pasting) {
                    if (tempX + window.tool.region.selectedRegionLabel[i].getSize().width > window.showImgRegionLabel.getSize().width
                            || tempY + window.tool.region.selectedRegionLabel[i].getSize().height > window.showImgRegionLabel.getSize().height) {
                        return;
                    }

                    window.tool.region.selectedRegionLabel[i].setLocation(tempX, tempY);
                    window.tool.region.selectedRegionLabel[i].repaint();
                    window.tool.region.selectedRegionLabel[i].setVisible(false);
                    if (i == window.counter)
                    {
                        window.tool.region.selectedRegionLabel[i].setVisible(true);
                    }
                }
            }
        };

        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }
}
