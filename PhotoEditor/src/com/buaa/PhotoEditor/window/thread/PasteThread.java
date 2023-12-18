package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.util.MatUtil.getValueAfterZoom;
/**
* @Description 粘贴多线程
* @author 张旖霜
* @date 12/11/2023 9:27 PM
* @version 1.0
*/
public class PasteThread extends Thread {
    public Window window;
    public int i;

    public PasteThread(Window window, int i) {
        this.window = window;
        this.i = i;
    }

    @Override
    public void run() {
        MouseInputAdapter mia = new MouseInputAdapter() {
            @Override
            /**
             * @Description 粘贴状态下，鼠标点击会进行粘贴
             * @author 卢思文
             * @date 11/26/2023 9:14 PM
             * @version 1.0
             */
            public void mouseClicked(MouseEvent e) {
                if (window.zoomImg == null) {
                    return;
                }
                int tempX = getValueAfterZoom(window, e.getX(), i);
                int tempY = getValueAfterZoom(window, e.getY(), i);

                if (tempX + window.tool.getRegion().selectedRegionLabel[i].getSize().width
                        > getValueAfterZoom(window, window.showImgRegionLabel.getSize().width, i)
                        || tempY + window.tool.getRegion().selectedRegionLabel[i].getSize().height
                        > getValueAfterZoom(window, window.showImgRegionLabel.getSize().height, i)) {
                    return;
                }
                if (window.pasting) {
                    // 当前property的值入栈，第一层将zoomImg数组入栈（这时仅zoomImg[0]是入栈的，其他的还没更新好）
                    if (i == 0) {
                        window.next.clear();
                        window.nextOriginalImg.clear();
                        window.nextPropertyValue.clear();
                        window.lastPropertyValue
                            .push(copyPropertyValue(window
                            .currentPropertyValue));
                        window.last.push(copyImgArray(window.zoomImg));
                        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
                    }
                    Mat img = MatUtil.copy(window.zoomImg[i]);
                    window.tool.getRegion().selectedRegionLabel[i].setLocation(tempX,tempY);
                    MatUtil.copyToRegion(img,
                            window.copyRegionImg[i],
                            MatUtil.getRect(window.tool.getRegion().selectedRegionLabel[i]));
                    window.zoomImg[i] = MatUtil.copy(img);
                    if (i == window.counter) {
                        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
                    }
                }
            }
            /**
            * @Description 鼠标移动的过程中，显示粘贴框
            * @author 卢思文
            * @date 12/11/2023 9:27 PM
            * @version 1.0
            */
            @Override
            public void mouseMoved(MouseEvent e) {
                if (window.zoomImg == null) {
                    return;
                }
                int tempX = getValueAfterZoom(window, e.getX(), i);
                int tempY = getValueAfterZoom(window, e.getY(), i);
                if (window.pasting) {
                    window.tool.getRegion().selectedRegionLabel[i].setLocation(tempX, tempY);
                    window.tool.getRegion().selectedRegionLabel[i].repaint();
                    window.tool.getRegion().selectedRegionLabel[i].setVisible(false);
                    if (i == window.counter) {
                        window.tool.getRegion().selectedRegionLabel[i].setVisible(true);
                    }
                }
            }

        };
        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }
}
