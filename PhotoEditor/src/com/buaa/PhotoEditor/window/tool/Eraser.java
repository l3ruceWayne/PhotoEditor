package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

/**
* @Description: 为橡皮添加图标和光标，增添调节橡皮擦粗细的设置
 * 存在的bug是和pen的粗细同步调节，原因是共用了一个Tool.model
 * 未来增加一键清除功能
* @author: 卢思文
* @date: 11/26/2023 10:06 PM
* @version: 1.0
**/
public class Eraser {
    public JCheckBoxMenuItem eraserItem;
    public static Cursor eraserCursor;
    public static ImageIcon eraserCursorIcon;
    public static ImageIcon eraserItemIcon;
    public JSpinner eraserSizeSpinner;
    public int eraserSize;
    public Window window;
    private boolean flag;
    static {
        eraserCursorIcon = new ImageIcon(
                "resources/eraserCursorImage.png");
        eraserItemIcon = new ImageIcon(
                "resources/eraserItemImage.png"
        );
        Image image = eraserCursorIcon.getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        eraserCursor = toolkit.createCustomCursor(image, new Point(0, 0), "");
    }

    public Eraser(Window window) {
        this.window = window;
        eraserItem = new JCheckBoxMenuItem(eraserItemIcon);

        eraserItem.addItemListener(new ItemListener() {
            /**
             * @param e : 事件
             * @Description: 点击橡皮即取消了画笔的使用，实现光标从画笔到默认的转变
             * 未来增加橡皮光标
             * @author: 卢思文
             * @date: 11/26/2023 8:19 PM
             * @version: 1.0
             **/
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.tool.region.removeRegionSelected();
                    window.tool.pen.penItem.setSelected(false);

                    window.showImgRegionLabel.setCursor(eraserCursor);

                    window.tool.drag.dragItem.setSelected(false);

                    if (flag == false) {
                        eraserListener();
                        flag = true;
                    }

                }
            }
        });
        eraserSizeSpinner = new JSpinner(Tool.eraserModel);



    }

    /*
     * @param:
     * @return
     * @Description:监听鼠标状态（从tool类换到这里主要是为了修改drag后无法使用画笔的bug）
     * （在tool类是在window.panel上进行监听，换到Eraser类，在window.showImgRegionLabel进行监听）
     * @author: 张旖霜
     * @date: 11/27/2023 7:53 PM
     * @version: 1.0
     */
    public void eraserListener() {
        ImageIcon imgIcon = new ImageIcon(MatUtil.bufferedImg(window.img));
        window.showImgRegionLabel.setIcon(imgIcon);
        MouseInputAdapter mia = new MouseInputAdapter() {

            public void mouseDragged(MouseEvent e) {
                if (eraserItem.isSelected()) {
                    if (e.getX() < 0 || e.getY() < 0 || e.getX()>window.img.width()-5 || e.getY()>window.img.height()-5) {
                        return;
                    }
                    eraserSize = (Integer) eraserSizeSpinner.getValue();
                    erase(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // pending
                /*
                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，当松开的时候我们将上一个状态入栈，然后更改img
                 */
                if (eraserItem.isSelected()) {
                    // 当前property的值入栈
                    window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
                    window.last.add(window.img);
                    if (window.paintingImg[window.counter] != null) {
                        window.zoomImg[window.counter] = MatUtil.copy(window.paintingImg[window.counter]);
                    }
                    // 下面这行代码是必须的，这样可以保证每次绘画的时候是在现在图像的基础上进画
                    // 如果没有这行代码，paintImg保持的只是上一个画好的状态，如果之后做了其他操作，将不会显示
                    window.paintingImg = null;
                }
            }
        };

        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }


    // pending 加一个erase的大小调节功能
    // 一键清除功能
    /*
    * @param x, y:鼠标位置
    * @return
    * @Description: 换成在window.showImgRegionLabel上监听鼠标，所以不需要重新定位
    * @author: 张旖霜
    * @date: 11/27/2023 3:30 PM
    * @version: 1.0
    */

    public void erase(int x, int y) {
        if (window.paintingImg[window.counter] == null) {
            window.paintingImg[window.counter] = MatUtil.copy(window.zoomImg[window.counter]);
        }


        Mat eraseRegion = window.paintingImg[window.counter].submat(new Rect(x, y, window.tool.eraser.eraserSize,
                window.tool.eraser.eraserSize));

        
        Mat originalRegion = window.originalImg.submat(new Rect(x, y, window.tool.eraser.eraserSize, window.tool.eraser.eraserSize));

        // 拿原图覆盖现在正在画的图，就相当于橡皮擦操作
        MatUtil.overlay(eraseRegion, originalRegion);

        MatUtil.show(window.paintingImg[window.counter], window.showImgRegionLabel);

    }
}
