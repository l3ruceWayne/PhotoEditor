package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Constant;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import static com.buaa.PhotoEditor.window.Constant.*;
import javax.swing.*;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


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

    /*

     */
    public Point lastPoint = null;
    /*
        实现画笔颜色选择：
            JPanel其实就是一个画板，我们让它显示颜色，然后画画的时候从中提取颜色作为画笔
            的颜色
            “提取颜色”的代码见本类的paint方法中的new int[]{
                        penColorPanel.getBackground().getBlue(),
                        penColorPanel.getBackground().getGreen(),
                        penColorPanel.getBackground().getRed()}
     */

    public JPanel penColorPanel;
    public JSpinner penSizeSpinner;
    public int penSize;
    public static Cursor penCursor;
    public static ImageIcon penCursorIcon;
    public static ImageIcon penItemIcon;


    private boolean flag;

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

                    if (flag == false) {
                        penListener();
                        flag = true;
                    }

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

        // 初始化
        penSize = (int)penSizeSpinner.getValue();
        /*
            lsw
            增加事件捕获器
         */
        penSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                penSize = (int)penSizeSpinner.getValue();
            }
        });

    }
    

    /*
     * @param:
     * @return
     * @Description:监听鼠标状态（从tool类换到这里主要是为了修改drag后无法使用画笔的bug）
     * （在tool类是在window.panel上进行监听，换到Pen类，在window.showImgRegionLabel进行监听）
     * @author: 张旖霜
     * @date: 11/27/2023 7:53 PM
     * @version: 1.0
     */
    public void penListener() {
        ImageIcon imgIcon = new ImageIcon(MatUtil.bufferedImg(window.zoomImg[window.counter]));
        window.showImgRegionLabel.setIcon(imgIcon);
        MouseInputAdapter mia = new MouseInputAdapter() {

            public void mouseDragged(MouseEvent e) {
                if (penItem.isSelected()) {
                    if (e.getX() < 0 || e.getY() < 0 || e.getX() > window.img.width() - 5 || e.getY() > window.img.height() - 5) {
                        return;
                    }
                    penSize = (Integer) penSizeSpinner.getValue();
                    paint(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // pending
                /*
                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，当松开的时候我们将上一个状态入栈，然后更改img
                 */
                if (penItem.isSelected()) {
                    // 当前property的值入栈
                    window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
                    window.last.add(window.zoomImg[window.counter]);
                    for (int i = 0; i <= MAX_SIZE_COUNTER; i++) {
                        if (window.paintingImg[i] != null) {
                            window.zoomImg[i] = MatUtil.copy(window.paintingImg[i]);
                        }
                        // 下面这行代码是必须的，这样可以保证每次绘画的时候是在现在图像的基础上进画
                        // 如果没有这行代码，paintImg保持的只是上一个画好的状态，如果之后做了其他操作，将不会显示
                        window.paintingImg[i] = null;
                    }
                }
            }
        };

        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }

   
     * @Description:
        * 实现画笔功能，原理是将指定像素块染成指定的颜色
        * 解决了“快速移动导致笔迹断续”的问题，思想：在离散的点之间插值
        * newX和newY是drag后的重新定位

     * @author: 张旖霜、卢思文
     * @date: 11/27/2023 3:30 PM
     * @version: 1.0
     */

    public void paint(int x, int y) {

        for (int i = 0; i <= MAX_SIZE_COUNTER; i++) {
            if (window.paintingImg[i] == null) {
                window.paintingImg[i] = MatUtil.copy(window.zoomImg[i]);
            }
        }
        System.out.println(x);
        System.out.println(y);
        for (int i = 0; i <= MAX_SIZE_COUNTER; i++) {
            System.out.print(i + " ");
            System.out.print(MatUtil.getPointAfterZoom(window, x, window.counter, i) + " ");
            System.out.println(MatUtil.getPointAfterZoom(window, y, window.counter, i));
            MatUtil.paint(new int[]{

                            penColorPanel.getBackground().getBlue(),
                            penColorPanel.getBackground().getGreen(),
                            penColorPanel.getBackground().getRed()},
                    penSize,
                    penSize,
                    MatUtil.getPointAfterZoom(window, x, window.counter, i),
                    MatUtil.getPointAfterZoom(window, y, window.counter, i),

                    window.paintingImg[i]);
        }

        MatUtil.show(window.paintingImg[window.counter], window.showImgRegionLabel);
      // 12/1
        /* lsw 解决了画到图片外报错的问题：让width和height分别减去penSize,
            因为x只是鼠标的点，而画的时候是penSize大小的矩形块
            所以当x在图片边界的时候，画的区域已经超过了边界，导致报错
         */
        // 下面这行代码作用是，当所画区域在图片外，return，这样避免报错
        if (x > (window.img.width() - penSize)+newX ||
                x < newX ||
                y > (window.img.height()-penSize)+newY ||
                y < newY) {
            // 如果画在区域外，上一个点要置null
            lastPoint = null;
            return;
        }

        if (window.paintingImg == null) {
            window.paintingImg = MatUtil.copy(window.img);
        }

        int[] color = {penColorPanel.getBackground().getBlue(),
                penColorPanel.getBackground().getGreen(),
                penColorPanel.getBackground().getRed()
        };
        /*
            lsw
            将当前点与上一个点连线，顺利解决笔迹断续的问题！
         */
        if(lastPoint != null){
            MatUtil.drawLine(lastPoint.x,
                            lastPoint.y,
                            x - newX,
                            y - newY,
                            color,
                            penSize, window.paintingImg);
        }else{
            MatUtil.paint(color, penSize, penSize, x - newX, y - newY, window.paintingImg);
        }
        MatUtil.show(window.paintingImg, window.showImgRegionLabel);
        // 更新上一个点为当前点
        lastPoint = new Point(x, y);


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
