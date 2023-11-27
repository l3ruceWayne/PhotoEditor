package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import javax.swing.*;
/**
* @Description: 调节图片大小（分辨率）
* @author 罗雨曦
* @date 2023/11/27 11:54
**/
public class MySize {
    //与openCV里的size类冲突，所以改了类名
    public javax.swing.JTextField txtHeight;
    public javax.swing.JTextField txtWidth;
    public javax.swing.JLabel lbSize;
    private Window window;

    public MySize(Window window){
        /**
         * @param window
         * @return null
         * @Description:构造方法——生成参数重设区域
         * @author: 罗雨曦
         * @date: 2023/11/27 11:59
         **/

        this.window=window;
        lbSize = new JLabel("Size");
        txtWidth = new JTextField();
        txtHeight = new JTextField();
    }

    public void btResizeActionPerformed(java.awt.event.ActionEvent evt) {
        /**
         * @param evt
         * @return void
         * @Description:保存图片参数修改
         * @author: 罗雨曦
         * @date: 2023/11/27 11:44
         **/

        try {
            double width = Double.parseDouble(this.txtWidth.getText());
            double height = Double.parseDouble(this.txtHeight.getText());

            Mat newImg = MatUtil.copy(window.temp);
            MatUtil.resize(newImg, new Size(width, height));

            window.last.push(window.img);
            window.img = window.temp = newImg;
            MatUtil.show(window.temp, window.showImgRegionLabel);
            window.updatePropertys();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please prefill the data correctly!");
        }
    }

}
