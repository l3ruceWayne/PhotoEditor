package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * @Description: 使用画笔的时候，鼠标按下->拖拽->松开是一个行为的完成，
 * 当松开的时候我们将上一个状态入栈，然后更改img
 * 每次操作完之后需要window.paintingImg = null，
 * 才能保证每次绘画开始的时候是在现在图像的基础上进画(当其为null的时候才会copy现在的图片)
 * 如果没有上面那行代码，paintImg保持的只是上一个画好的状态，如果之后做了其他操作，将不会显示
 * @author: 卢思文
 * @date: 11/26/2023 8:21 PM
 * @version: 1.0
 **/
public class Tool {

    public Window window;

    public Eraser eraser;
    public Pen pen;
    public Region region;
    public ZoomOut zoomOut;
    public ZoomIn zoomIn;
    public Rotate rotate;
    public Drag drag;

    public static  SpinnerNumberModel penModel = new SpinnerNumberModel(5, 1, 30, 1);
    public static  SpinnerNumberModel eraserModel = new SpinnerNumberModel(5, 1, 30, 1);

    public int ex, ey;


    public JPanel zoomRegion;

    public Tool(Window window) {
        this.window = window;
        this.pen = new Pen(window);
        this.eraser = new Eraser(window);
        this.region = new Region(window);
        this.zoomOut = new ZoomOut(window);
        this.zoomIn = new ZoomIn(window);
        this.rotate = new Rotate(window);
        this.drag = new Drag(window);

        addMouseListeners();
    }

    public void addMouseListeners() {
        window.panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//                int newX = window.tool.drag.newX;
//                int newY = window.tool.drag.newY;
                ex = e.getX();
                ey = e.getY();
//                if (x < window.img.width()+newX || x > newX || y < window.img.height()+newY || y > newY) return;
                if (region.selectRegionItem.isSelected()) {
                    region.addRegion(e.getPoint());
                }
            }


            @Override
            public void mouseReleased(MouseEvent e) {

                if (pen.penItem.isSelected()
                        || eraser.eraserItem.isSelected()) {
                    window.isProperty.push(0);
                    window.last.add(window.img);
                    if (window.paintingImg != null) {
                        window.img = MatUtil.copy(window.paintingImg);
                    }

                    window.paintingImg = null;
                }
            }
        });

        window.panel.addMouseMotionListener(new MouseAdapter() {
            /**
             * @param e : 鼠标事件
             * @Description: 当按下选择区域或者画笔或者橡皮后，鼠标拖拽时会触发下列动作
             * @author: 卢思文
             * @date: 11/26/2023 8:25 PM
             * @version: 1.0
             **/
            @Override
            public void mouseDragged(MouseEvent e) {
//                int newX = window.tool.drag.newX;
//                int newY = window.tool.drag.newY;
//                int x = e.getX();
//                int y = e.getY();
//                if (x >= window.img.width() || y >= window.img.height()) return;
                if (region.selectRegionItem.isSelected()) {
                    region.setRegionSize(e.getX(), e.getY());
                } else if (pen.penItem.isSelected()) {
                    pen.penSize = (Integer) pen.penSizeSpinner.getValue();
                    pen.paint(e.getX(), e.getY());
                } else if (eraser.eraserItem.isSelected()) {
                    eraser.eraserSize = (Integer) eraser.eraserSizeSpinner.getValue();
                    eraser.erase(e.getX(), e.getY());
                }
            }
        });
    }
}
