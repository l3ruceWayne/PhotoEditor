package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.EraserThread;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
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
    public EraserThread[] eraserThread;
    public JCheckBoxMenuItem eraserItem;
    public static Cursor eraserCursor;
    public static ImageIcon eraserCursorIcon;
    public static ImageIcon eraserItemIcon;
    public JSpinner eraserSizeSpinner;
    public int[] eraserSize;
    public Window window;
    private boolean flag;
    public boolean clearScreen;

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

        eraserThread = new EraserThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            eraserThread[i] = new EraserThread(window, i);
        }
        clearScreen = false;
        eraserItem = new JCheckBoxMenuItem(eraserItemIcon);
        // 调整布局（原本图标后面会多一块无意义空白）
        Dimension preferredSize = new Dimension(40, 40);
        eraserItem.setPreferredSize(preferredSize);
        // 如果未选择图片，弹窗提示并return
        eraserItem.addActionListener(e -> {
            if (window.originalImg == null) {
                eraserItem.setSelected(false);
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
        });

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

                } else {
                    // 恢复默认光标
                    window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }


        });

        eraserSizeSpinner = new JSpinner(Tool.eraserModel);
        // 将Spinner设置为内嵌样式
        eraserSizeSpinner.setBorder(BorderFactory.createLoweredBevelBorder());
        // 橡皮尺寸初始化

        /*
            lsw
            增加事件捕获器
         */
        eraserSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //LYX 如果未选择图片，弹窗提示并使数值不可编辑
                if (window.originalImg == null) {
                    //LYX 设置编辑框为不可编辑，但暂未找到使增加减小按钮失效的方法
                    JComponent editor = eraserSizeSpinner.getEditor();
                    JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                    textField.setEditable(false);
                    JOptionPane.showMessageDialog(null, "Please open an image first");
                    return;
                }
                int size = (int) window.tool.eraser.eraserSizeSpinner.getValue();
                int offset = size - window.tool.eraser.eraserSize[window.counter];
                for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                    eraserSize[i] += offset;
                    if (eraserSize[i] > MAX_ERASER_SIZE) {
                        eraserSize[i] = MAX_ERASER_SIZE;
                    } else if (eraserSize[i] < MIN_ERASER_SIZE) {
                        eraserSize[i] = MIN_ERASER_SIZE;
                    }
                }
            }
        });


    }

    public void initEraserSize(){
        eraserSize = new int[NUM_FOR_NEW];
        for (int i = 0; i < ORIGINAL_SIZE_COUNTER; i++) {
            eraserSize[i] = i * 3  + 13;
        }
        // 从最大的开始，找到第一个小于它的
        for (int i = MAX_SIZE_COUNTER; i >= 0; i--) {
            if (window.size[i][0] < window.size[ORIGINAL_SIZE_COUNTER][0]){
                eraserSize[ORIGINAL_SIZE_COUNTER]
                        = Math.min(MAX_PEN_SIZE, eraserSize[i] + 2);
                break;
            }
        }
        // 如果没找到, 那么它是最小的
        if(eraserSize[ORIGINAL_SIZE_COUNTER] == 0){
            eraserSize[ORIGINAL_SIZE_COUNTER]
                    = Math.max(MIN_PEN_SIZE, eraserSize[0] - 2);
        }
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
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            eraserThread[i].start();

            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                eraserThread[i].join();
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

}
