package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.util.MatUtil.getValueAfterZoom;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * ClassName: paintThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description: i 是 下标
 *
 * @Author 卢思文
 * @Create 12/2/2023 9:31 AM
 * @Version 1.0
 */
public class PaintThread extends Thread{
    public Window window;
    public int i;
    public PaintThread(Window window, int i){
        this.window = window;
        this.i = i;
    }

    @Override
    public void run() {
        MouseInputAdapter mia = new MouseInputAdapter() {
            /*
                lsw
                新添，绘画点击的笔迹
                应该还需要添加橡皮擦除的点击事件处理
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (window.tool.pen.penItem.isSelected()) {
                    paint(e.getX(), e.getY(), true);
                    initLastPoint();
                }
            }

            public void mouseDragged(MouseEvent e) {
                if (window.tool.pen.penItem.isSelected()) {
                    paint(e.getX(), e.getY(), false);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                /*
                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，
                当松开的时候我们将上一个状态入栈，然后更改img
                 */
                if (window.tool.pen.penItem.isSelected()) {
                    // 当前property的值入栈，第一层将zoomImg数组入栈（这时仅zoomImg[0]是入栈的，其他的还没更新好）
                    if (i == 0) {
                        window.lastPropertyValue
                                .push(copyPropertyValue(window
                                        .currentPropertyValue));
                        window.last.push(copyImgArray(window.zoomImg));
                        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
                    }
                    // 这时其他的值更新好了，就入栈（修改栈顶的值）
//                    else
//                    {
//                        window.last.peek()[i] = copy(window.zoomImg[i]);
//                    }

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

    /*
     * @param flag : true则是画点，否则画线
     * @Description:
     * 实现画笔功能，原理是将指定像素块染成指定的颜色
     * 解决了“快速移动导致笔迹断续”的问题，思想：在离散的点之间插值
     * newX和newY是drag后的重新定位
     * @author: 张旖霜、卢思文
     * @date: 11/27/2023 3:30 PM
     * @version: 1.0
     */
        /*  lsw
            解决了画到图片外报错的问题：让width和height分别减去penSize,
            因为x只是鼠标的点，而画的时候是penSize大小的矩形块
            所以当x在图片边界的时候，画的区域已经超过了边界，导致报错
         */


    public void paint(int x, int y, boolean flag) {
        if (window.paintingImg[i] == null) {
            window.paintingImg[i] = copy(window.zoomImg[i]);
        }
        int[] color = {window.tool.pen.penColorPanel.getBackground().getBlue(),
                window.tool.pen.penColorPanel.getBackground().getGreen(),
                window.tool.pen.penColorPanel.getBackground().getRed()
        };
        int tempX = getValueAfterZoom(window, x, i);
        int tempY = getValueAfterZoom(window, y, i);
        if (window.tool.pen.lastPoint[i] != null && !flag) {
            /*
                lsw
                将当前点与上一个点连线，解决笔迹断续的问题
            */
            MatUtil.drawLine(window.tool.pen.lastPoint[i].x,
                    window.tool.pen.lastPoint[i].y,
                    tempX,
                    tempY,
                    color,
                    window.tool.pen.penSize[i], window.paintingImg[i]);
        } else {
            /*
                解决无法画到边界的问题
             */
            if (tempX + window.tool.pen.penSize[i] > window.paintingImg[i].width()) {
                tempX = window.paintingImg[i].width() - window.tool.pen.penSize[i];
            }
            if (tempY + window.tool.pen.penSize[i] > window.paintingImg[i].height()) {
                tempY = window.paintingImg[i].height() - window.tool.pen.penSize[i];
            }
            MatUtil.paint(color,
                    window.tool.pen.penSize[i],
                    window.tool.pen.penSize[i],
                    tempX,
                    tempY,
                    window.paintingImg[i]);
        }
        if (i == window.counter) {
            show(window.paintingImg[window.counter], window.showImgRegionLabel);
        }

        // 更新上一个点为当前点
        window.tool.pen. lastPoint[i] = new Point(getValueAfterZoom(window, x, i),
                getValueAfterZoom(window, y, i));


    }
    private void initLastPoint() {
        window.tool.pen.lastPoint[i] = null;
    }
}
