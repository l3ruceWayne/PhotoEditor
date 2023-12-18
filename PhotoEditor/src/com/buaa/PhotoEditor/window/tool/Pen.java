package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.PaintThread;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author 卢思文
 * @version 1.0
 * @Description 画笔（增添选择画笔颜色/选择画笔粗细/选择画笔后光标变成画笔样式）
 * @date 11/26/2023 4:14 PM
 */
public class Pen {
    public JCheckBoxMenuItem penItem;
    public Window window;
    public PaintThread[] paintThread;
    public Point[] lastPoint;

    public JPanel penColorPanel;
    public JSpinner penSizeSpinner;
    public int[] penSize;
    public static Cursor penCursor;
    public static ImageIcon penCursorIcon;
    public static ImageIcon penItemIcon;

    private boolean flag;

    // 加载画笔图标，因为共用，所以static
    static {
        penCursorIcon = new ImageIcon(
                "PhotoEditor/resources/penCursorImage.png");
        penItemIcon = new ImageIcon(
                "PhotoEditor/resources/penItemImage.png"
        );
        Image image = penCursorIcon.getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        penCursor = toolkit.createCustomCursor(image, new Point(10, 0), "");
    }

    public Pen(Window window) {
        this.window = window;
        penItem = new JCheckBoxMenuItem(penItemIcon);
        lastPoint = new Point[NUM_FOR_NEW];
        paintThread = new PaintThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            paintThread[i] = new PaintThread(window, i);
        }

        // 调整布局（原本图标后面会多一块无意义空白）
        Dimension preferredSize = new Dimension(50, 35);
        penItem.setPreferredSize(preferredSize);

        // 如果未选择图片，弹窗提示并return
        penItem.addActionListener(e -> {
            if (window.originalImg == null) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
                penItem.setSelected(false);
            }
        });

        initPenColorPanel();
        penColorPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                selectPenColor();
            }
        });

        // 将Spinner设置为内嵌样式
        penSizeSpinner = new JSpinner(Tool.penModel);
        penSizeSpinner.setBorder(BorderFactory.createLoweredBevelBorder());

        penItem.addItemListener(e -> initPenItem(e));
        penSizeSpinner.addChangeListener(e -> setPenSize());
    }

    /**
     * @param e : 事件
     * @Description 初始化penItem；点击画笔之后，取消使用选择区域、橡皮功能，光标变成画笔；取消画笔之后，光标恢复原样
     * @author 卢思文
     * @date 11/26/2023 8:05 PM
     */
    private void initPenItem(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            window.tool.getRegion().removeRegionSelected();
            window.tool.getEraser().eraserItem.setSelected(false);
            window.tool.getDrag().dragItem.setSelected(false);
            window.showImgRegionLabel.setCursor(penCursor);
            if (!flag) {
                penListener();
                flag = true;
            }

        } else {
            window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * @Description 初始化 penColorPanel
     * @author 卢思文
     * @date 11/26/2023 8:05 PM
     */
    private void initPenColorPanel() {
        // 颜色选框（panel）美化:内嵌加白边
        penColorPanel = new JPanel();
        Border b1 = BorderFactory.createLineBorder(Color.white, 1);
        Border b2 = BorderFactory.createLoweredBevelBorder();
        penColorPanel.setBorder(BorderFactory.createCompoundBorder(b1, b2));
        penColorPanel.setBackground(new java.awt.Color(0, 0, 0));
    }

    /**
     * @Description 画笔尺寸初始化
     * @author 卢思文
     * @date 11/26/2023 8:19 PM
     */
    public void initPenSize() {
        penSize = new int[NUM_FOR_NEW];
        for (int i = 0; i < ORIGINAL_SIZE_COUNTER; i++) {
            penSize[i] = i / 2 + 1;
        }
        // 从最大的开始，找到第一个小于它的
        for (int i = MAX_SIZE_COUNTER; i >= 0; i--) {
            if (window.size[i][0] < window.size[ORIGINAL_SIZE_COUNTER][0]) {
                penSize[ORIGINAL_SIZE_COUNTER] = Math.min(MAX_PEN_SIZE, penSize[i] + 2);
                break;
            }
        }
        // 如果没找到, 那么它是最小的
        if (penSize[ORIGINAL_SIZE_COUNTER] == 0) {
            penSize[ORIGINAL_SIZE_COUNTER] = Math.max(MIN_PEN_SIZE, penSize[0] - 2);
        }
    }

    /**
     * @Description 根据用户设置，更新画笔的大小
     * @author 卢思文
     * @date 11/26/2023 8:19 PM
     */
    private void setPenSize() {
        // 如果未选择图片，弹窗提示并使数值不可编辑
        if (window.originalImg == null) {
            // 设置编辑框为不可编辑
            JComponent editor = penSizeSpinner.getEditor();
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        int size = (int) window.tool.getPen().penSizeSpinner.getValue();
        int offset = size - window.tool.getPen().penSize[window.counter];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            penSize[i] += offset;
            if (penSize[i] > MAX_PEN_SIZE) {
                penSize[i] = MAX_PEN_SIZE;
            } else if (penSize[i] < MIN_PEN_SIZE) {
                penSize[i] = MIN_PEN_SIZE;
            }
        }
    }

    /**
     * @Description 执行多线程，监听鼠标状态
     * @author 张旖霜
     * @date 11/27/2023 7:53 PM
     */
    public void penListener() {
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            paintThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                paintThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * @Description 选择画笔颜色
     * @author 卢思文
     * @date 11/26/2023 8:09 PM
     */
    public void selectPenColor() {
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
        // 解决更换颜色后画笔光标消失但此时仍能绘画的问题
        if (penItem.isSelected()) {
            penItem.setSelected(false);
            penItem.doClick();
        }
    }

}
