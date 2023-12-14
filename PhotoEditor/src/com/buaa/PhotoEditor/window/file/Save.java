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
        /*
            handleWidget()的作用是：
            1. 处理栈（这块代码请不要分离出来，否则反而麻烦）
            2. 如果添加了widget但是没有"enter"，保存操作会自动"enter"
             并且会检测widget是否超出图片外
         */
        handleWidget();
        /*
            如果widget超出范围，handleWidget()会做标记，即window.flagForWidget
            超出范围，自然保存不成功，之后的代码就不用执行了，所以return
         */
        if (!window.flagForWidget) {
            return;
        }
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
            /*
                下面4行代码的作用同saveImg中的注释所描述的那样
             */
            handleWidget();
            if (!window.flagForWidget) {
                return;
            }
            saveMatToJpg(path, window.zoomImg[ORIGINAL_SIZE_COUNTER]);
            JOptionPane.showMessageDialog(null,
                    "Success");
        }
    }

    /**
     * @Description: 处理添加了 widget 的情况，同时进行栈相关的处理
     * @author: 卢思文，罗雨曦
     * @date: 12/5/2023 4:18 PM
     * @version: 3.0
     */
    private void handleWidget() {
        if (window.add.getWidget().widgetIcon == null) {
            return;
        }
        window.next.clear();
        window.nextOriginalImg.clear();
        window.nextPropertyValue.clear();
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(copyImgArray(window.zoomImg));
        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        window.flagForWidget = true;
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            int x = (int) (window.add.getWidget().widgetLabel.getX() - ((double) window.panel.getWidth() - window.showImgRegionLabel.getWidth()) / 2);
            int y = (int) (window.add.getWidget().widgetLabel.getY() - ((double) window.panel.getHeight() - window.showImgRegionLabel.getHeight()) / 2);
            MatUtil.widget(window.zoomImg[i],
                    MatUtil.readImg(window.add.getWidget().widgetLabel.getIcon().toString()),
                    x, y, i, window);
            if (!window.flagForWidget) {
                if (i == window.counter) {
                    JOptionPane.showMessageDialog(null, "Save failed\nPlease remove widget completely inside the photo");
                }
                continue;
            }
            if (i == window.counter) {
                MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
                window.panel.remove(window.add.getWidget().widgetLabel);
            }
        }
        if (window.flagForWidget) {
            window.add.getWidget().widgetIcon = null;
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
     * @param bytes    Mat图片转为byte[]形式
     * @param append   此处恒为false，即FileOutputStream都是从头开始输出图片，设置目的为与FileOutputStream参数保持一致
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