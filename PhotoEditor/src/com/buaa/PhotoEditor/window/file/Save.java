package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description: 负责修改后图片的保存和另存为功能
 * @author: 卢思文
 * @date: 11/27/2023 4:44 PM
 * @version: 1.0
 **/
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
        saveItem.addActionListener(e -> saveImg(e));

        saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK
                        | KeyEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> saveAsNewImg(e));
    }

    /**
     * @Description: 更新window.img为保存后的img，以便后续再进行编辑

     * 根据设计更改更新newImg的获取对象与相关细节操作
     * @author: 卢思文，罗雨曦
     * @date: 12/5/2023 4:18 PM
     * @version: 2.0
     **/
    private void getNewImg() {
        Mat newImg = MatUtil.copy(window.zoomImg[ORIGINAL_SIZE_COUNTER]);
        // 保存后，小组件和图片融为一体，所以把小组件删除
        for (JLabel widgetLabel : window.add.widget.widgetLabelList) {
            MatUtil.widget(newImg,
                    MatUtil.readImg(widgetLabel.getIcon().toString()),
                    widgetLabel.getX(), widgetLabel.getY());
            window.panel.remove(widgetLabel);
        }
        // LYX 因为不再使用resize实现编辑时的放大缩小，故不需要更新屏幕显示内容
        // 显示融为一体的图片
//        MatUtil.show(newImg, window.showImgRegionLabel);
        // pending
//        if (window.last.size() != 0 && window.img != window.last.peek()) {
//            // 当前property的值入栈
//            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//            window.last.push(window.zoomImg);
//        }
        // window.img弃用，暂时注释掉下行
//        window.img = newImg;
    }


    /*
     * @param:
     * @return
     * @Description:保存图片，如果用户点击了save as，保存路径改为save as的保存路径

     * 修复save后显示出错的bug
     * 修复保存路径中含有中文而无法正确保存的bug
     * 同时，通过流以字节单位写图片，既能保证图片质量也能控制图片文件的大小
     * @author: 张旖霜、卢思文、罗雨曦
     * @date: 12/5/2023 3:58 AM
     * @version: 1.0
     */

    private void saveImg(ActionEvent e) {
        if (window.zoomImg == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }

        //LYX 因为图片放缩相关功能修改过，所以以下代码已经过时了
        // 为了避免保存到zoom的图片，所以保存前先resize回之前的大小
//        MatUtil.resize(window.img, new Size(window.imgWidth, window.imgHeight));

        getNewImg();

        if (path == null) {
            path = window.originalImgPath;
        }
        //LYX 修复保存路径中含有中文而无法正确保存的bug；同时，通过流以字节单位写图片，既能保证图片质量也能控制图片文件的大小
//        MatUtil.save(path, window.img);
        SaveMatToJpg(path, window.zoomImg[ORIGINAL_SIZE_COUNTER]);
        JOptionPane.showMessageDialog(null,
                "Success");


        /* 打开图片，添加小组件，保存，然后再选择区域，会报错，加上下面这行代码才行
            详细原因等待了解
         */

        window.add.widget.widgetLabelList.clear();

    }

    /**
     * @param e : 事件
     * @Description: 对修改后的图片进行另存为操作
     * 修改因为图片放缩、显示相关功能修改而过时的resize代码
     * 将输出文件格式修改为JPG，与原文件格式相同
     * 修复保存路径中含有中文而无法正确保存的bug
     * 同时，通过流以字节单位写图片，既能保证图片质量也能控制图片文件的大小
     * @author: 卢思文，罗雨曦
     * @date: 12/5/2023 2:52 PM
     * @version: 2.0
     **/
    private void saveAsNewImg(ActionEvent e) {
        if (window.zoomImg == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }
        // 为了避免保存到zoom的图片，所以保存前先resize回之前的大小
        // bug
        //因为图片放缩相关功能修改过，所以以下这行代码已经过时了
//        MatUtil.resize(window.img, new Size(window.imgWidth, window.imgHeight));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        //LYX 将输出文件格式限定改为JPG，与原文件格式相同
//        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG file", "jpg", "jpeg"));
        int userSelection = fileChooser.showSaveDialog(null);
        // 如果用户点击保存
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();
            //LYX 将输出文件格式修改为JPG，与原文件格式相同
//            if (!path.contains(".png")) {
//                path += ".png";
//            }
            if (!path.contains(".jpg")) {
                path += ".jpg";
            }
            getNewImg();
            //LYX 修复保存路径中含有中文而无法正确保存的bug，同时，通过流以字节单位写图片，既能保证图片质量也能控制图片文件的大小
//            MatUtil.save(path, window.img);
            SaveMatToJpg(path, window.zoomImg[ORIGINAL_SIZE_COUNTER]);
//                ImageIO.write(MatUtil.bufferedImg(window.img), "png", fileToSave);
            JOptionPane.showMessageDialog(null,
                    "Success");
        }
        // 否则就是取消另存为

    }

    /**
     * @param imgPath  图片要保存到的路径的字符串形式
     * @param dstImage 要保存的图片
     * @return boolean 是否保存成功
     * @Description: 重新写将Mat保存为JPG图片的方法, 以避开Matutil.save保存路径中含有中文就无法正确保存的bug
     * @author: 罗雨曦
     * @date: 2023/12/5 4:01
     * @version: 1.0
     **/
    public static boolean SaveMatToJpg(String imgPath, Mat dstImage) {
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
     **/
    public static boolean writeFileByBytes(String fileName, byte[] bytes, boolean append) {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName, append))) {
            out.write(bytes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
