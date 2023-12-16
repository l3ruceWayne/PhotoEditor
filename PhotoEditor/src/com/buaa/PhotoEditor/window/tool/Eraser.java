package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.EraserThread;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * @Description 橡皮（为橡皮添加图标和光标，增添调节橡皮擦粗细的设置）
 * @author 卢思文
 * @date 11/26/2023 10:06 PM
 * @version: 1.0
 */
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

    // 加载橡皮图标，因为共用，所以static
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
        eraserThread = new EraserThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            eraserThread[i] = new EraserThread(window, i);
        }

        // 调整布局（原本图标后面会多一块无意义空白）
        Dimension preferredSize = new Dimension(40, 30);
        eraserItem.setPreferredSize(preferredSize);

        // 如果未选择图片，弹窗提示并return
        eraserItem.addActionListener(e -> {
            if (window.originalImg == null) {
                eraserItem.setSelected(false);
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
        });

        // 将Spinner设置为内嵌样式
        eraserSizeSpinner = new JSpinner(Tool.eraserModel);
        eraserSizeSpinner.setBorder(BorderFactory.createLoweredBevelBorder());

        eraserItem.addItemListener(e -> initEraserItem(e));
        eraserSizeSpinner.addChangeListener(e -> setEraserSize());
    }

    /**
     * @param e : 事件
     * @Description 初始化 eraserItem，若已选择，开始监听滑鼠
     * @author 卢思文
     * @date 11/26/2023 8:19 PM
     * @version: 1.0
     */
    private void initEraserItem(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            window.tool.getRegion().removeRegionSelected();
            window.tool.getPen().penItem.setSelected(false);
            window.tool.getDrag().dragItem.setSelected(false);
            window.showImgRegionLabel.setCursor(eraserCursor);
            if (!flag) {
                eraserListener();
                flag = true;
            }

        } else {
            // 恢复默认光标
            window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * @Description 橡皮尺寸初始化
     * @author 卢思文
     * @date 11/26/2023 8:19 PM
     * @version: 1.0
     */
    public void initEraserSize() {
        eraserSize = new int[NUM_FOR_NEW];
        for (int i = 0; i < ORIGINAL_SIZE_COUNTER; i++) {
            eraserSize[i] = i * 3 + 13;
        }
        // 从最大的开始，找到第一个小于它的
        for (int i = MAX_SIZE_COUNTER; i >= 0; i--) {
            if (window.size[i][0] < window.size[ORIGINAL_SIZE_COUNTER][0]) {
                eraserSize[ORIGINAL_SIZE_COUNTER]
                        = Math.min(MAX_PEN_SIZE, eraserSize[i] + 2);
                break;
            }
        }
        // 如果没找到, 那么它是最小的
        if (eraserSize[ORIGINAL_SIZE_COUNTER] == 0) {
            eraserSize[ORIGINAL_SIZE_COUNTER]
                    = Math.max(MIN_PEN_SIZE, eraserSize[0] - 2);
        }
    }

    /**
     * @Description 根据用户设置，更新橡皮的大小
     * @author 卢思文
     * @date 11/26/2023 8:19 PM
     * @version: 1.0
     */
    private void setEraserSize() {
        // 如果未选择图片，弹窗提示并使数值不可编辑
        if (window.originalImg == null) {
            // 设置编辑框为不可编辑
            JComponent editor = eraserSizeSpinner.getEditor();
            JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setEditable(false);
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        int size = (int) window.tool.getEraser().eraserSizeSpinner.getValue();
        int offset = size - window.tool.getEraser().eraserSize[window.counter];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            eraserSize[i] += offset;
            if (eraserSize[i] > MAX_ERASER_SIZE) {
                eraserSize[i] = MAX_ERASER_SIZE;
            } else if (eraserSize[i] < MIN_ERASER_SIZE) {
                eraserSize[i] = MIN_ERASER_SIZE;
            }
        }
    }

    /**
     * @Description 执行多线程，监听鼠标状态
     * @author 张旖霜
     * @date 11/27/2023 7:53 PM
     * @version: 1.0
     */
    public void eraserListener() {
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            eraserThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                eraserThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
