package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

                }
            }
        });
        eraserSizeSpinner = new JSpinner(Tool.eraserModel);

    }


    // pending 加一个erase的大小调节功能
    // 一键清除功能
    /*
    * @param x, y:鼠标位置
    * @return
    * @Description: newX和newY是drag后的重新定位
    * @author: 张旖霜
    * @date: 11/27/2023 3:30 PM
    * @version: 1.0
    */

    public void erase(int x, int y) {
        // pending
        int newX = window.tool.drag.newX;
        int newY = window.tool.drag.newY;
        if (x > window.img.width()+newX || x < newX || y > window.img.height()+newY || y < newY) return;

        if (window.paintingImg == null) {
            window.paintingImg = MatUtil.copy(window.img);
        }


        Mat eraseRegion = window.paintingImg.submat(new Rect(x-newX, y-newY, window.tool.eraser.eraserSize,
                window.tool.eraser.eraserSize));

        
        Mat originalRegion = window.originalImg.submat(new Rect(x-newX, y-newY, window.tool.eraser.eraserSize, window.tool.eraser.eraserSize));

        // 拿原图覆盖现在正在画的图，就相当于橡皮擦操作
        MatUtil.overlay(eraseRegion, originalRegion);

        MatUtil.show(window.paintingImg, window.showImgRegionLabel);

    }
}
