package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import javax.swing.*;
import java.awt.event.ActionEvent;

import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;
import static com.buaa.PhotoEditor.util.MatUtil.resize;
import static com.buaa.PhotoEditor.window.Constant.*;
import static com.buaa.PhotoEditor.window.Constant.AUTO_SIZE_COUNTER;

/**
 * @Description: 调节图片大小（分辨率）
 * @author 罗雨曦
 * @date 2023/11/27 14:00
 * @version: 1.0
 **/
public class MySize {
    //与openCV里的size类冲突，所以改了类名
    public javax.swing.JTextField txtHeight;
    public javax.swing.JTextField txtWidth;
    public javax.swing.JLabel lbSize;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description: 构造方法——生成参数重设区域
     * @author: 罗雨曦
     * @date: 2023/11/27 14:00
     * @version: 1.0
     **/
    public MySize(Window window){
        this.window=window;
        lbSize = new JLabel("Size");
        txtWidth = new JTextField();
        txtHeight = new JTextField();
    }

    /**
     * @param evt : 事件
     * @Description:
     * 只是简单的实现改变尺寸
     * 改变尺寸后，图片显示有bug
     * @author: 卢思文、罗雨曦
     * @date: 11/25/2023 11:41 AM
     * @version: 1.0
     **/
    public void Resize(ActionEvent evt) {
        try {
            int newWidth = Integer.parseInt(window.property.getMySize().txtWidth.getText());
            int newHeight = Integer.parseInt(window.property.getMySize().txtHeight.getText());

            window.size[ORIGINAL_SIZE_COUNTER][0] = newWidth;
            window.size[ORIGINAL_SIZE_COUNTER][1] = newHeight;
            MatUtil.resize(window.zoomImg[ORIGINAL_SIZE_COUNTER], new Size(newWidth, newHeight));
            MatUtil.resize(window.originalZoomImg[ORIGINAL_SIZE_COUNTER], new Size(newWidth, newHeight));


            //图片缩放
            MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
            window.panel.setLayout(window.gridBagLayout);

            window.property.updateProperty();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please prefill the data correctly!");
        }

    }


}
