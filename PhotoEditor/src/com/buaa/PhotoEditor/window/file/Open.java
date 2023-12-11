package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;

import org.opencv.core.Mat;
import org.opencv.core.Size;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;


/*
 * @Description: 打开图片（选择需要编辑的图片）； 后续可进行优化——点击后不是进入主界面，而是进入上一次打开所在路径
 * 注意，不能打开矢量图
 * @author 罗雨曦、卢思文
 * @date 2023/11/27 14:08
 * @version: 1.0
 **/
public class Open {
    public JMenuItem openItem;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:09
     * @version: 1.0
     **/
    public Open(Window window) {
        this.window = window;
        openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImg(e);
            }
        });

    }

    /**
     * @param e : 事件
     * @Description: 修复了无法打开中文路径图片的问题
     * 问题的原因定位到OpenCV库的Mat类不支持
     * 修复方法是改写MatUtil.readImg方法，先用其他数据结构读取图片，再转化成Mat
     * 注意，不支持打开矢量图
     * @author: 卢思文、罗雨曦
     * @date: 11/26/2023 8:49 PM
     * @version: 1.0
     **/


    private void selectImg(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this.window)
                == JFileChooser.APPROVE_OPTION) {
            //LYX 判断是否为JPG图片
            Path selectPath= fileChooser.getSelectedFile().toPath();
            boolean checkFormat=checkImageFormat(selectPath);
            // 增加了png
            if(!checkFormat){
                JOptionPane.showMessageDialog(null,
                        "Please select a JPG/JPEG/PNG format image");
                return;
            }
            window.originalImgPath = fileChooser.getSelectedFile().getAbsolutePath();
            window.img = MatUtil.readImg(window.originalImgPath);
            window.nexLayerImg = MatUtil.copy(window.img);
            window.originalImg = MatUtil.copy(window.img);

            /*
               尺寸数组的初始化及放大缩小图片的初始化
             */
            // 因为OpenCV库的resize要求size精确到double，而JLabel的resize是int，不情愿的转化成int
            window.size = new int[NUM_FOR_NEW][2];
            window.zoomImg = new Mat[NUM_FOR_NEW];
            window.originalZoomImg = new Mat[NUM_FOR_NEW];
            // 原图
            int originalWidth = window.originalImg.width();
            int originalHeight = window.originalImg.height();
            window.size[ORIGINAL_SIZE_COUNTER] = new int[2];
            window.size[ORIGINAL_SIZE_COUNTER][0] = originalWidth;
            window.size[ORIGINAL_SIZE_COUNTER][1] = originalHeight;
            window.zoomImg[ORIGINAL_SIZE_COUNTER] = MatUtil.copy(window.originalImg);
            window.originalZoomImg[ORIGINAL_SIZE_COUNTER] = MatUtil.copy(window.originalImg);
            // 找到自适应缩放的最佳尺寸
            int autoWidth, autoHeight;
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
            window.size[AUTO_SIZE_COUNTER] = new int[2];
            window.size[AUTO_SIZE_COUNTER][0] = autoWidth;
            window.size[AUTO_SIZE_COUNTER][1] = autoHeight;
            window.zoomImg[AUTO_SIZE_COUNTER] = MatUtil.copy(window.originalImg);
            resize(window.zoomImg[AUTO_SIZE_COUNTER], new Size(window.size[AUTO_SIZE_COUNTER][0],
                    window.size[AUTO_SIZE_COUNTER][1]));
            window.originalZoomImg[AUTO_SIZE_COUNTER] = MatUtil.copy(window.zoomImg[AUTO_SIZE_COUNTER]);
            window.counter = AUTO_SIZE_COUNTER;
            int widthOffset = autoWidth / ZOOM_RATIO;
            int heightOffset = autoHeight / ZOOM_RATIO;

            // 0~AUTO_SIZE_COUNTER-1 都是缩小档
            for (int i = 0; i < AUTO_SIZE_COUNTER; i++) {
                window.size[i] = new int[2];
                window.size[i][0] = autoWidth - (AUTO_SIZE_COUNTER - i) * widthOffset;
                window.size[i][1] = autoHeight - (AUTO_SIZE_COUNTER - i) * heightOffset;
                window.zoomImg[i] = MatUtil.copy(window.originalImg);
                resize(window.zoomImg[i], new Size(window.size[i][0],
                        window.size[i][1]));
                window.originalZoomImg[i] = MatUtil.copy(window.zoomImg[i]);
            }
            // AUTO_SIZE_COUNTER + 1 ~ MAX_SIZE_COUNTER 都是放大档
            for (int i = AUTO_SIZE_COUNTER + 1; i <= MAX_SIZE_COUNTER; i++) {
                window.size[i] = new int[2];
                window.size[i][0] = autoWidth + (i - AUTO_SIZE_COUNTER) * widthOffset;
                window.size[i][1] = autoHeight + (i - AUTO_SIZE_COUNTER) * heightOffset;
                window.zoomImg[i] = MatUtil.copy(window.originalImg);
                MatUtil.resize(window.zoomImg[i], new Size(window.size[i][0],
                        window.size[i][1]));
                window.originalZoomImg[i] = MatUtil.copy(window.zoomImg[i]);
            }
            window.tool.pen.initPenSize();
            window.tool.eraser.initEraserSize();

            //图片缩放
            MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
            window.showImgRegionLabel.setSize(window.zoomImg[window.counter].width(),
                    window.zoomImg[window.counter].height());
            window.panel.setLayout(window.gridBagLayout);
            // 打开图片后储存图片的原始大小（保存图片原本大小）
            window.imgWidth = window.img.width();
            window.imgHeight = window.img.height();
            this.window.setLocationRelativeTo(null);
            window.last.clear();
            window.next.clear();
            // 清空存储originalImg的栈
            window.lastOriginalImg.clear();
            window.nextOriginalImg.clear();
            // 清空property值的栈
            window.lastPropertyValue.clear();
            window.nextPropertyValue.clear();
            window.showImgRegionLabel.setText("");

            // 将当前的property的初始值暂存起来
            window.currentPropertyValue[4] = originalWidth;
            window.currentPropertyValue[5] = originalHeight;

            window.property.updateProperty();

        }
    }
    /**
     * @param imagePath 选择文件的路径
     * @return boolean 是否为可处理图片
     * @Description: 若是JPEG或JPG图片，返回true，代表可处理；反之返回false，代表不可处理
     * @author: 罗雨曦
     * @date: 2023/12/4 21:26
     * @version: 1.0
     **/
    public static boolean checkImageFormat(Path imagePath) {
        String fileName = imagePath.getFileName().toString();
        // 增加了png
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }
}
