package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;
import static com.buaa.PhotoEditor.window.Constant.*;


import org.opencv.core.Size;

import javax.swing.*;

/**
 * @author 罗雨曦
 * @version 1.1
 * @Description 调节图片大小（分辨率）
 * @date 2023/11/27 14:00
 */
public class MySize {
    public javax.swing.JTextField txtHeight;
    public javax.swing.JTextField txtWidth;
    public javax.swing.JLabel sizeLabel;
    private Window window;

    /**
     * @param window 当前窗口
     * @Description 构造方法——生成参数重设区域
     * @author 罗雨曦
     * @date 2023/11/27 14:00
     */

    public MySize(Window window) {
        this.window = window;
        sizeLabel = new JLabel("Size");
        txtWidth = new JTextField();
        txtHeight = new JTextField();
    }

    /**
     * @Description 改变原尺寸图片的大小，可撤销
     * @author 卢思文、罗雨曦
     * @date 11/25/2023 11:41 AM
     */
    public void resize() {
        try {
            window.next.clear();
            window.nextOriginalImg.clear();
            window.nextPropertyValue.clear();
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));

            int newWidth = Integer.parseInt(window.property.getMySize().txtWidth.getText());
            int newHeight = Integer.parseInt(window.property.getMySize().txtHeight.getText());

            window.size[ORIGINAL_SIZE_COUNTER][0] = newWidth;
            window.size[ORIGINAL_SIZE_COUNTER][1] = newHeight;

            MatUtil.resize(window.zoomImg[ORIGINAL_SIZE_COUNTER], new Size(newWidth, newHeight));
            MatUtil.resize(window.originalZoomImg[ORIGINAL_SIZE_COUNTER], new Size(newWidth, newHeight));

            window.saveFlag = false;
            JOptionPane.showMessageDialog(null, "Success");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please prefill the data correctly!");
        }

    }

}