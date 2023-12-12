package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description: 修改后图片的保存和另存为功能
 * @author: 卢思文
 * @date: 11/27/2023 4:44 PM
 * @version: 2.0
 */
public class Save {
    public JMenuItem saveItem;
    public JMenuItem saveAsItem;
    private Window window;
    private String path;

    public Save(Window window) {
        this.window = window;

        saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveImg());

        saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK
                        | KeyEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> saveAsNewImg());
    }


    /**
     * @Description: 保存图片，如果用户点击了save as，保存路径改为save as的保存路径
     * 通过流以字节单位写图片，既能保证图片质量也能控制图片文件的大小
     * @author: 张旖霜 卢思文 罗雨曦
     * @date: 12/5/2023 3:58 AM
     * @version: 3.0
     */
    private void saveImg() {
        if (window.zoomImg == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }
        updateStack();
        mergeWidget();

        if (!window.flagForWidget) {
            return;
        }

        // 若用户没有点击过 save as，则保存路径为原图路径
        if (path == null) {
            path = window.originalImgPath;
        }
        saveMatToJpg(path, window.zoomImg[ORIGINAL_SIZE_COUNTER]);
        JOptionPane.showMessageDialog(null,
                "Success");
    }

    /**
     * @Description: 对修改后的图片进行另存为操作
     * @author: 卢思文 罗雨曦
     * @date: 12/5/2023 2:52 PM
     * @version: 2.0
     */
    private void saveAsNewImg() {
        int userSelection;
        if (window.zoomImg == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg", "png"));
        userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();
            String jpgExtension = ".jpg";
            if (!path.contains(jpgExtension)) {
                path += jpgExtension;
            }
            updateStack();
            mergeWidget();
            if (!window.flagForWidget) {
                return;
            }
            saveMatToJpg(path, window.zoomImg[ORIGINAL_SIZE_COUNTER]);
            JOptionPane.showMessageDialog(null,
                    "Success");
        }
    }

    /**
     * @Description: 更新 undo redo 用到的栈
     * @author: 卢思文 张旖霜
     * @date: 2023/12/5 4:03
     * @version: 1.0
     */
    private void updateStack() {
        window.next.clear();
        window.nextOriginalImg.clear();
        window.nextPropertyValue.clear();
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(copyImgArray(window.zoomImg));
        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
    }

    /**
     * @Description: 处理添加了 widget 的情况
     * @author: 卢思文，罗雨曦
     * @date: 12/5/2023 4:18 PM
     * @version: 3.0
     */
    private void mergeWidget() {
        int x;
        int y;

        if (window.add.widget.widgetIcon == null) {
            return;
        }

        window.flagForWidget = true;
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            x = (int) (window.add.widget.widgetLabel.getX() - ((double) window.panel.getWidth() - window.showImgRegionLabel.getWidth()) / 2);
            y = (int) (window.add.widget.widgetLabel.getY() - ((double) window.panel.getHeight() - window.showImgRegionLabel.getHeight()) / 2);
            MatUtil.widget(window.zoomImg[i],
                    MatUtil.readImg(window.add.widget.widgetLabel.getIcon().toString()),
                    x, y, i, window);
            if (!window.flagForWidget) {
                if (i == window.counter) {
                    JOptionPane.showMessageDialog(null, "Save failed\nPlease remove widget completely inside the photo");
                }
                continue;
            }
            if (i == window.counter) {
                MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
                window.panel.remove(window.add.widget.widgetLabel);
            }
        }
        if (window.flagForWidget) {
            window.add.widget.widgetIcon = null;
        }
    }

    /**
     * @param imgPath  图片要保存到的路径的字符串形式
     * @param dstImage 要保存的图片
     * @return boolean 是否保存成功
     * @Description: 重新写将Mat保存为JPG图片的方法, 以避开Matutil.save保存路径中含有中文就无法正确保存的bug
     * @author: 罗雨曦
     * @date: 2023/12/5 4:01
     * @version: 1.0
     */
    private static boolean saveMatToJpg(String imgPath, Mat dstImage) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", dstImage, mob);
        byte[] imageByte = mob.toArray();
        return writeFileByBytes(imgPath, imageByte, false);
    }

    /**
     * @param fileName 图片要保存到的路径的字符串形式
     * @param bytes Mat图片转为byte[]形式
     * @param append 此处恒为false，即FileOutputStream都是从头开始输出图片，设置目的为与FileOutputStream参数保持一致
     * @return boolean 操作是否成功
     * @Description: 将byte[]形式的图片通过流输入到指定路径
     * @author: 罗雨曦
     * @date: 2023/12/5 4:03
     * @version: 1.0
     */
    private static boolean writeFileByBytes(String fileName, byte[] bytes, boolean append) {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName, append))) {
            out.write(bytes);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return false;
    }
}