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
                "PhotoEditor/resources/eraserCursorImage.png");
        eraserItemIcon = new ImageIcon(
                "PhotoEditor/resources/eraserItemImage.png"
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
                }
            }
        });
        eraserSizeSpinner = new JSpinner(Tool.eraserModel);

    }


    public void erase(int x, int y) {
        // pending
        if (x > window.img.width() || y > window.img.height()) return;

        if (window.paintingImg == null) {
            window.paintingImg = MatUtil.copy(window.img);
        }

        Mat eraseRegion = window.paintingImg.submat(
                new Rect(x, y, window.tool.eraser.eraserSize,
                window.tool.eraser.eraserSize));

        Mat originalRegion = window.originalImg
                .submat(new Rect(x, y, window.tool.eraser.eraserSize,
                        window.tool.eraser.eraserSize));
        // 拿原图覆盖现在正在画的图，就相当于橡皮擦操作
        MatUtil.overlay(eraseRegion, originalRegion);

        MatUtil.show(window.paintingImg, window.showImgRegionLabel);

    }
}
