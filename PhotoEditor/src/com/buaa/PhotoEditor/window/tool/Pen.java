package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @Description: 增添选择画笔颜色/选择画笔粗细/选择画笔后光标变成画笔样式
 * penColorPanel是选择画笔颜色的入口，点击后弹出选择窗口，
 * 功能实现见penColorPanel.addMouseListener
 * @author: 卢思文
 * @date: 11/26/2023 4:14 PM
 * @version: 1.0
 **/
public class Pen {
    public JCheckBoxMenuItem penItem;
    public Window window;
    public JPanel penColorPanel;
    public JSpinner penSizeSpinner;
    public int penSize;
    public static Cursor penCursor;
    public static ImageIcon penCursorIcon;
    public static ImageIcon penItemIcon;

    /**
     * @Description: 加载画笔图标，因为共用，所以static
     * 画笔图片的路径暂时放在那里，到最后我们调整一下目录结构，使其更规范
     * @author: 卢思文
     * @date: 11/26/2023 5:41 PM
     * @version: 1.0
     **/
    static {
        penCursorIcon = new ImageIcon(
                "resources/penCursorImage.png");
        penItemIcon = new ImageIcon(
                "resources/penItemImage.png"
        );
        Image image = penCursorIcon.getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        penCursor = toolkit.createCustomCursor(image, new Point(10, 0), "");

    }

    public Pen(Window window) {
        this.window = window;
        // menubar

        penItem = new JCheckBoxMenuItem(penItemIcon);
        // shortcut
//        penItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
//                InputEvent.ALT_MASK
//                        | InputEvent.SHIFT_MASK
//                        | InputEvent.CTRL_MASK)
//        );
        // onclick event


        penItem.addItemListener(new ItemListener() {
            /**
            * @param e : 事件
            * @Description: 点击画笔之后，取消使用选择区域、橡皮功能，光标变成画笔
             * 取消画笔之后，光标恢复原样
            * @author: 卢思文
            * @date: 11/26/2023 8:05 PM
            * @version: 1.0
            **/
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.tool.region.removeRegionSelected();
                    window.tool.eraser.eraserItem.setSelected(false);
                    window.showImgRegionLabel.setCursor(penCursor);
                    window.tool.drag.dragItem.setSelected(false);
                } else {
                    window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        penColorPanel = new JPanel();
        penColorPanel.setBackground(new java.awt.Color(0, 0, 0));
        penColorPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                selectPenColor(evt);
            }
        });
        penSizeSpinner = new JSpinner(Tool.penModel);


    }
    
    /*
     * @param x, y:鼠标位置
     * @return
     * @Description: newX和newY是drag后的重新定位
     * 实现画笔功能，原理是将指定像素块染成指定的颜色
     * @author: 张旖霜、卢思文
     * @date: 11/27/2023 3:30 PM
     * @version: 1.0
     */
    public void paint(int x, int y) {
        // 调整界面大小，让图片填充后可以删掉
        // pending
        int newX = window.tool.drag.newX;
        int newY = window.tool.drag.newY;
//        if (x > window.img.width()+newX || x < newX || y > window.img.height()+newY || y < newY) return;
        // pending

        if (window.paintingImg == null) {
            window.paintingImg = MatUtil.copy(window.img);
        }
        
        MatUtil.paint(new int[]{

                        penColorPanel.getBackground().getBlue(),
                        penColorPanel.getBackground().getGreen(),
                        penColorPanel.getBackground().getRed()},
                penSize,
                penSize,
                x - newX,
                y - newY,

                window.paintingImg);

        MatUtil.show(window.paintingImg, window.showImgRegionLabel);

    }
    /**
    * @param evt : 事件
    * @Description: 有bug，修改方法是用栈
     * 需要记录上一次选择的是什么颜色，下次打开选择窗口后默认是该颜色
    * @author: 卢思文
    * @date: 11/26/2023 8:09 PM
    * @version: 1.0
    **/
    public void selectPenColor(MouseEvent evt) {
        Color color = JColorChooser
                .showDialog(null,
                        "Select Color",
                        // bug
                        Color.BLACK);

        penColorPanel.setBackground(color);
    }

}
