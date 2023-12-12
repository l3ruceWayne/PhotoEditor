package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ClassName: EraserThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description: 橡皮多线程，实现多张图片的同时擦除操作
 * @Author 卢思文
 * @Create 12/2/2023 7:28 PM
 * @Version 1.0
 */
public class EraserThread extends Thread {
    public Window window;
    public int i;

    public EraserThread(Window window, int i) {
        this.window = window;
        this.i = i;
    }
    /**
    * @Description: 线程实现监听
    * @author: 卢思文
    * @date: 12/11/2023 9:01 PM
    * @version: 1.0
    **/
    @Override
    public void run() {
        // 增加计时器，当长按橡皮2000毫秒后，清除全屏
        Timer timer = new Timer(2000, e -> {
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
            window.paintingImg[i] = MatUtil.copy(window.originalZoomImg[i]);
            // 更新“上一个图片”img
            window.zoomImg[i] = MatUtil.copy(window.originalZoomImg[i]);
            if (i == window.counter) {
                show(window.paintingImg[i], window.showImgRegionLabel);
            }
            window.paintingImg[i] = null;
        });
        timer.setRepeats(false);
        /*
            添加长按清屏功能 按压开始计时，松开计时结束，如果计时超过2s，则清屏
            否则无事发生
            待实现：清除全屏后，所有属性参数需要恢复成原始状态
        */
        window.tool.eraser.eraserItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                timer.start();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                timer.stop();
            }
        });

        MouseInputAdapter mia = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                erase(e.getX(), e.getY());
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (window.tool.eraser.eraserItem.isSelected()) {
                    erase(e.getX(), e.getY());
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                /*
                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，当松开的时候我们将上一个状态入栈，然后更改img
                 */
                if (window.tool.eraser.eraserItem.isSelected()) {
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
                    if (window.paintingImg[i] != null) {
                        window.zoomImg[i] = copy(window.paintingImg[i]);
                    }
                    // 下面这行代码是必须的，这样可以保证每次绘画的时候是在现在图像的基础上进画
                    // 如果没有这行代码，paintImg保持的只是上一个画好的状态，如果之后做了其他操作，将不会显示
                    window.paintingImg[i] = null;
                }
            }
        };
        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }
    /**
     * @param x, y:鼠标位置
     * @return
     * @Description: 换成在window.showImgRegionLabel上监听鼠标，所以不需要重新定位
     * @author: 张旖霜、卢思文
     * @date: 11/27/2023 3:30 PM
     * @version: 2.0
     */
    public void erase(int x, int y) {
        if (window.paintingImg[i] == null) {
            window.paintingImg[i] = copy(window.zoomImg[i]);
        }
        int tempX = getValueAfterZoom(window, x, i);
        int tempY = getValueAfterZoom(window, y, i);
        if (tempX + window.tool.eraser.eraserSize[i] > window.paintingImg[i].width()) {
            tempX = window.paintingImg[i].width() - window.tool.eraser.eraserSize[i];
        }
        if (tempY + window.tool.eraser.eraserSize[i] > window.paintingImg[i].height()) {
            tempY = window.paintingImg[i].height() - window.tool.eraser.eraserSize[i];
        }
        tempX = Math.max(tempX, window.tool.eraser.eraserSize[i]);
        tempY = Math.max(tempY, window.tool.eraser.eraserSize[i]);
        Mat eraseRegion = window.paintingImg[i].submat(new Rect(
            tempX, tempY,
            window.tool.eraser.eraserSize[i],
            window.tool.eraser.eraserSize[i]));

        Mat originalRegion = window.originalZoomImg[i]
            .submat(new Rect(tempX, tempY,
            window.tool.eraser.eraserSize[i],
            window.tool.eraser.eraserSize[i]));
        // 拿原图覆盖现在正在画的图，就相当于橡皮擦操作
        MatUtil.overlay(eraseRegion, originalRegion);
        if (i == window.counter) {
            show(window.paintingImg[i], window.showImgRegionLabel);
        }
    }
}
