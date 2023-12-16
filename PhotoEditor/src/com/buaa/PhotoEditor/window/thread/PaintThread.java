package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.util.MatUtil.*;


import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.util.MatUtil.getValueAfterZoom;

/**
 * @Description 实现画笔的多线程操作
 * i是与zoomImg[]有关的下标
 * @author 卢思文
 * @date 12/2/2023 9:31 AM
 * @version 1.3
 */

public class PaintThread extends Thread {
    public Window window;
    public int i;

    public PaintThread(Window window, int i) {
        this.window = window;
        this.i = i;
    }

    @Override
    public void run() {
        MouseInputAdapter mia = new MouseInputAdapter() {
            /**
            * @Description 利用画笔进行点击时，画出点迹，需要进行点击事件的捕捉
            * @author 卢思文
            * @date 12/11/2023 9:12 PM
            * @version: 1.0
            **/
            @Override
            public void mouseClicked(MouseEvent e) {
                if (window.tool.getPen().penItem.isSelected()) {
                    window.saveFlag = false;
                    paint(e.getX(), e.getY(), true);
                    initLastPoint();
                }
            }
            /**
            * @Description 画笔曲线的绘制，鼠标拖拽事件的捕捉
            * @author 卢思文
            * @date 12/11/2023 9:12 PM
            * @version: 2.0
            **/
            @Override
            public void mouseDragged(MouseEvent e) {
                if (window.tool.getPen().penItem.isSelected()) {
                    window.saveFlag = false;
                    paint(e.getX(), e.getY(), false);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                /*
                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，
                当松开的时候我们将上一个状态入栈，然后更改img
                 */
                if (window.tool.getPen().penItem.isSelected()) {
                    // 在第一个线程入栈
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
                    initLastPoint();
                }
            }
        };
        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }

    /**
     * @param x : 鼠标 x 坐标
     * @param y ：鼠标 y 坐标
     * @param flag : true则是画点，否则画线
     * @Description
     * 实现画笔功能，原理是将指定像素块染成指定的颜色
     * 解决了“快速移动导致笔迹断续”的问题，思想：在离散的点之间插值
     * newX和newY是drag后的重新定位
     * @author 张旖霜、卢思文
     * @date 12/5/2023 3:42 AM
     * @version: 2.0
     */
    public void paint(int x, int y, boolean flag) {
        if (window.paintingImg[i] == null) {
            window.paintingImg[i] = copy(window.zoomImg[i]);
        }
        int[] color = {window.tool.getPen().penColorPanel.getBackground().getBlue(),
                window.tool.getPen().penColorPanel.getBackground().getGreen(),
                window.tool.getPen().penColorPanel.getBackground().getRed()
        };
        int tempX = getValueAfterZoom(window, x, i);
        int tempY = getValueAfterZoom(window, y, i);
        if (window.tool.getPen().lastPoint[i] != null && !flag) {
            /*
                将当前点与上一个点连线，解决笔迹断续的问题
            */
            MatUtil.drawLine(window.tool.getPen().lastPoint[i].x,
                    window.tool.getPen().lastPoint[i].y,
                    tempX,
                    tempY,
                    color,
                    window.tool.getPen().penSize[i], window.paintingImg[i]);
        } else {
            /*
                解决无法画到边界的问题
             */
            if (tempX + window.tool.getPen().penSize[i] > window.paintingImg[i].width()) {
                tempX = window.paintingImg[i].width() - window.tool.getPen().penSize[i];
            }
            if (tempY + window.tool.getPen().penSize[i] > window.paintingImg[i].height()) {
                tempY = window.paintingImg[i].height() - window.tool.getPen().penSize[i];
            }
            MatUtil.paint(color,
                    window.tool.getPen().penSize[i],
                    window.tool.getPen().penSize[i],
                    tempX,
                    tempY,
                    window.paintingImg[i]);
        }
        if (i == window.counter) {
            show(window.paintingImg[window.counter], window.showImgRegionLabel);
        }
        // 更新上一个点为当前点
        window.tool.getPen().lastPoint[i] = new Point(getValueAfterZoom(window, x, i),
                getValueAfterZoom(window, y, i));

    }

    private void initLastPoint() {
        window.tool.getPen().lastPoint[i] = null;
    }
}
