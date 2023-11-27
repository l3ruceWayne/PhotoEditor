package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tool {

    public Window window;

    public Eraser eraser;
    public Pen pen;
    public Region region;
    public ZoomOut zoomOut;
    public ZoomIn zoomIn;
    public Rotate rotate;
    public Drag drag;

    public int penSize;
    public int eraserSize;
    public int ex, ey;

    public JPanel zoomRegion;

    public Tool (Window window) {
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
                // pending
                /*
                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，当松开的时候我们将上一个状态入栈，然后更改img
                 */
                if (pen.penItem.isSelected()
                        || eraser.eraserItem.isSelected()) {
                    window.isProperty.push(0);
                    window.last.add(window.img);
                    if (window.paintingImg != null) {
                        window.img = MatUtil.copy(window.paintingImg);
                    }
                    // 下面这行代码是必须的，这样可以保证每次绘画的时候是在现在图像的基础上进画
                    // 如果没有这行代码，paintImg保持的只是上一个画好的状态，如果之后做了其他操作，将不会显示
                    window.paintingImg = null;
                }
            }
        });

        window.panel.addMouseMotionListener(new MouseAdapter() {
            // 拖拽的时候将会一直调用该方法
            @Override
            public void mouseDragged(MouseEvent e) {
//                int newX = window.tool.drag.newX;
//                int newY = window.tool.drag.newY;
//                int x = e.getX();
//                int y = e.getY();
//                if (x >= window.img.width() || y >= window.img.height()) return;
                if (region.selectRegionItem.isSelected()) {
                    region.setRegionSize(e.getX(), e.getY());
                    // pending
                } else if (pen.penItem.isSelected()) {
                    penSize = (Integer) window.propertyMenuDialogPenSizeSpinner.getValue();
                    pen.paint(e.getX(), e.getY());
                } else if (eraser.eraserItem.isSelected()) {
                    // pending
                    eraserSize = (Integer) window.propertyMenuDialogPenSizeSpinner.getValue();
                    eraser.erase(e.getX(), e.getY());
                }
            }
        });
    }
}
