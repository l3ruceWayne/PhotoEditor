package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import javax.swing.*;
import java.nio.file.Path;

/**
 * @author 罗雨曦、卢思文
 * @version 1.0
 * @Description 打开图片
 * @date 2023/11/27 14:08
 */
public class Open {
    public JMenuItem openItem;
    private Window window;

    public Open(Window window) {
        this.window = window;
        this.openItem = new JMenuItem("Open");
        this.openItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        this.openItem.addActionListener(e -> selectImg());

    }

    /**
     * @Description 选择图片，并进行初始化
     * 问题：无法打开中文路径图片
     * 问题原因：定位到OpenCV库的Mat类不支持
     * 修复方法：改写 MatUtil.readImg 方法，先用其他数据结构读取图片，再转化成Mat
     * @author 卢思文、罗雨曦
     * @date 11/26/2023 8:49 PM
     */
    private void selectImg() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this.window)
                == JFileChooser.APPROVE_OPTION) {
            Path selectPath = fileChooser.getSelectedFile().toPath();
            boolean checkFormat = checkImageFormat(selectPath);
            if (!checkFormat) {
                JOptionPane.showMessageDialog(null,
                        "Please select a JPG/JPEG/PNG format image");
                return;
            }
            window.originalImgPath = fileChooser.getSelectedFile().getAbsolutePath();
            window.img = readImg(window.originalImgPath);
            window.nexLayerImg = copy(window.img);
            window.originalImg = copy(window.img);

            initImgSize();
            window.tool.getPen().initPenSize();
            window.tool.getEraser().initEraserSize();

            // 图片显示
            show(window.zoomImg[window.counter], window.showImgRegionLabel);
            window.showImgRegionLabel.setSize(window.zoomImg[window.counter].width(),
                    window.zoomImg[window.counter].height());
            window.panel.setLayout(window.gridBagLayout);
            window.setLocationRelativeTo(null);
            window.showImgRegionLabel.setText("");

            // 清空 undo redo 用到的栈
            window.last.clear();
            window.next.clear();
            window.lastOriginalImg.clear();
            window.nextOriginalImg.clear();
            window.lastPropertyValue.clear();
            window.nextPropertyValue.clear();

            window.property.updateProperty();

        }
    }

    /**
     * @param imagePath 选择图片的路径
     * @return boolean 是否为可处理图片
     * @Description 若是JPEG或JPG或PNG图片，返回true，代表可处理；反之返回false，代表不可处理
     * @author 罗雨曦 张旖霜
     * @date 2023/12/4 21:26
     */
    private static boolean checkImageFormat(Path imagePath) {
        String fileName = imagePath.getFileName().toString();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    /**
     * @Description 尺寸数组的初始化及放大缩小图片的初始化，计算图片的自适应大小，找到图片缩放的大小比例
     * @author 卢思文
     * @date 12/12/2023 9:23 AM
     */
    private void initImgSize() {
        window.size = new int[NUM_FOR_NEW][2];
        window.zoomImg = new Mat[NUM_FOR_NEW];
        window.originalZoomImg = new Mat[NUM_FOR_NEW];

        // 存储原图和图片大小
        int originalWidth = window.originalImg.width();
        int originalHeight = window.originalImg.height();
        window.currentPropertyValue[4] = originalWidth;
        window.currentPropertyValue[5] = originalHeight;
        window.size[ORIGINAL_SIZE_COUNTER] = new int[2];
        window.size[ORIGINAL_SIZE_COUNTER][0] = originalWidth;
        window.size[ORIGINAL_SIZE_COUNTER][1] = originalHeight;
        window.zoomImg[ORIGINAL_SIZE_COUNTER] = copy(window.originalImg);
        window.originalZoomImg[ORIGINAL_SIZE_COUNTER] = copy(window.originalImg);

        // 找到自适应缩放的最佳尺寸
        int autoWidth;
        int autoHeight;
        if (originalHeight * (((double) window.panel.getWidth()) / (double)
                originalWidth) > window.panel.getHeight()) {
            autoHeight = window.panel.getHeight();
            autoWidth = (int) (originalWidth * (((double) window.panel.getHeight()) / (double)
                    originalHeight));
        } else {
            autoWidth = window.panel.getWidth();
            autoHeight = (int) (originalHeight * (((double) window.panel.getWidth()) / (double)
                    originalWidth));
        }
        window.counter = AUTO_SIZE_COUNTER;
        int widthOffset = autoWidth / ZOOM_RATIO;
        int heightOffset = autoHeight / ZOOM_RATIO;

        // 存储自适应大小的图片和自适应大小
        window.size[AUTO_SIZE_COUNTER] = new int[2];
        window.size[AUTO_SIZE_COUNTER][0] = autoWidth;
        window.size[AUTO_SIZE_COUNTER][1] = autoHeight;
        window.zoomImg[AUTO_SIZE_COUNTER] = copy(window.originalImg);
        resize(window.zoomImg[AUTO_SIZE_COUNTER], new Size(window.size[AUTO_SIZE_COUNTER][0],
                window.size[AUTO_SIZE_COUNTER][1]));
        window.originalZoomImg[AUTO_SIZE_COUNTER] = copy(window.zoomImg[AUTO_SIZE_COUNTER]);

        // 0~AUTO_SIZE_COUNTER-1 都是缩小档，存储缩小的每一张图片和大小
        for (int i = 0; i < AUTO_SIZE_COUNTER; i++) {
            window.size[i] = new int[2];
            window.size[i][0] = autoWidth - (AUTO_SIZE_COUNTER - i) * widthOffset;
            window.size[i][1] = autoHeight - (AUTO_SIZE_COUNTER - i) * heightOffset;
            window.zoomImg[i] = copy(window.originalImg);
            resize(window.zoomImg[i], new Size(window.size[i][0],
                    window.size[i][1]));
            window.originalZoomImg[i] = copy(window.zoomImg[i]);
        }

        // AUTO_SIZE_COUNTER + 1 ~ MAX_SIZE_COUNTER 都是放大档，存储放大的每一张图片和大小
        for (int i = AUTO_SIZE_COUNTER + 1; i <= MAX_SIZE_COUNTER; i++) {
            window.size[i] = new int[2];
            window.size[i][0] = autoWidth + (i - AUTO_SIZE_COUNTER) * widthOffset;
            window.size[i][1] = autoHeight + (i - AUTO_SIZE_COUNTER) * heightOffset;
            window.zoomImg[i] = copy(window.originalImg);
            resize(window.zoomImg[i], new Size(window.size[i][0],
                    window.size[i][1]));
            window.originalZoomImg[i] = copy(window.zoomImg[i]);
        }
    }
}