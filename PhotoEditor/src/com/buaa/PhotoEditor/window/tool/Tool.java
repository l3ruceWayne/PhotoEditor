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

    public static SpinnerNumberModel penModel = new SpinnerNumberModel(5, 1, 30, 1);
    public static SpinnerNumberModel eraserModel = new SpinnerNumberModel(5, 1, 30, 1);

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
        // 静态鼠标事件捕获：点击、按压、释放
        window.panel.addMouseListener(new MouseAdapter() {
            /*
                lsw
                新添，绘画点击的笔迹
                应该还需要添加橡皮擦除的点击事件处理
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pen.penItem.isSelected()) {
                    pen.penSize = (Integer) pen.penSizeSpinner.getValue();
                    pen.paint(e.getX(), e.getY());
                    pen.lastPoint = null;
                }
            }

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
                        || eraser.eraserItem.isSelected()
                        ) {
                    window.isProperty.push(0);
                    // 有问题，画笔之后使用一键清空，撤销无法恢复，反撤销才恢复
                    window.last.add(window.img);
                    if (window.paintingImg != null) {
                        window.img = MatUtil.copy(window.paintingImg);
                        System.out.println("lsw");
                    }
                    /*
                    lsw
                     松开鼠标之后，意味着一次画笔行为的完成，将lastPoint置为null
                     否则下一次笔迹将和上一次笔迹相连
                     */
                    pen.lastPoint = null;
                    window.paintingImg = null;
                }
            }
        });
        // 动态鼠标事件捕获：拖拽
        /*
            lsw
            移动了pen.penSize = 和 eraser.eraserSize = 的代码位置
            因为这两行代码只有当我们改变xxxSize大小之后才需要执行，拖拽时执行做了很多重复工作
         */
        window.panel.addMouseMotionListener(new MouseAdapter() {
            /**
             * @param e : 鼠标事件
             * @Description: 当按下选择区域或者画笔或者橡皮后，鼠标拖拽时会触发下列动作
             * @author: 卢思文
             * @date: 11/26/2023 8:25 PM
             * @version: 1.0
             **/
            // 移动了pen.penSize = 和eraser.eraserSize =的位置，因为不用每次都获取粗细大小
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
                    pen.paint(e.getX(), e.getY());
                } else if (eraser.eraserItem.isSelected()) {
                    eraser.erase(e.getX(), e.getY());
                }
            }
        });
    }
}
