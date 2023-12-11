package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.layer.Layer;
import com.buaa.PhotoEditor.window.thread.PaintThread;


import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;

import static com.buaa.PhotoEditor.util.MatUtil.getValueAfterZoom;
import static com.buaa.PhotoEditor.util.MatUtil.widget;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.security.KeyStore;
import java.util.Arrays;
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
    public PaintThread[] paintThread;
    /*

     */
    public Point[] lastPoint;
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
    public int[] penSize;
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

        paintThread = new PaintThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            paintThread[i] = new PaintThread(window, i);
        }
        lastPoint = new Point[NUM_FOR_NEW];
        // menubar

        // 调整布局（原本图标后面会多一块无意义空白）
        penItem = new JCheckBoxMenuItem(penItemIcon);
        Dimension preferredSize = new Dimension(40, 40);
        penItem.setPreferredSize(preferredSize);
        // 如果未选择图片，弹窗提示并return
        penItem.addActionListener(e -> {
            if (window.originalImg == null) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
                penItem.setSelected(false);
            }
        });
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
        // 颜色选框（panel）美化:内嵌加白边（因为背景颜色为黑，为了在黑色背景下突出边界所以选择白色）
        Border b1 = BorderFactory.createLineBorder(Color.white, 1);  //组合边框
        Border b2 = BorderFactory.createLoweredBevelBorder();
        penColorPanel.setBorder(BorderFactory.createCompoundBorder(b1, b2) );
        penColorPanel.setBackground(new java.awt.Color(0, 0, 0));
        penColorPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                selectPenColor(evt);
            }
        });
        penSizeSpinner = new JSpinner(Tool.penModel);
        // 将Spinner设置为内嵌样式
        penSizeSpinner.setBorder(BorderFactory.createLoweredBevelBorder());
        /*
            lsw
            增加事件捕获器,更改画笔尺寸
         */
        penSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //LYX 如果未选择图片，弹窗提示并使数值不可编辑
                if (window.originalImg == null) {
                    //LYX 设置编辑框为不可编辑，但暂未找到使增加减小按钮失效的方法
                    JComponent editor = penSizeSpinner.getEditor();
                    JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                    textField.setEditable(false);
                    JOptionPane.showMessageDialog(null, "Please open an image first");
                    return;
                }
                int size = (int) window.tool.pen.penSizeSpinner.getValue();
                int offset = size - window.tool.pen.penSize[window.counter];
                for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                    penSize[i] += offset;
                    if (penSize[i] > MAX_PEN_SIZE) {
                        penSize[i] = MAX_PEN_SIZE;
                    } else if (penSize[i] < MIN_PEN_SIZE) {
                        penSize[i] = MIN_PEN_SIZE;
                    }
                }
            }
        });

    }

    public void initPenSize() {
        // 注意Spinner模型边界和MAX_SIZE_COUNTER的限制
        penSize = new int[NUM_FOR_NEW];
        for (int i = 0; i < ORIGINAL_SIZE_COUNTER; i++) {
            penSize[i] = i / 2 + 1;
        }
        // 从最大的开始，找到第一个小于它的
        for (int i = MAX_SIZE_COUNTER; i >= 0; i--) {
            if (window.size[i][0] < window.size[ORIGINAL_SIZE_COUNTER][0]){
                penSize[ORIGINAL_SIZE_COUNTER] = Math.min(MAX_PEN_SIZE, penSize[i] + 2);
                break;
            }
        }
        // 如果没找到, 那么它是最小的
        if(penSize[ORIGINAL_SIZE_COUNTER] == 0){
            penSize[ORIGINAL_SIZE_COUNTER] = Math.max(MIN_PEN_SIZE, penSize[0] - 2);
        }

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
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            paintThread[i].start();

            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                paintThread[i].join();
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
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
        // 如果未选择图片，弹窗提示并return
        if (window.originalImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        Color color = JColorChooser
                .showDialog(null,
                        "Select Color",
                        // bug
                        Color.BLACK);

        penColorPanel.setBackground(color);
    }

}
