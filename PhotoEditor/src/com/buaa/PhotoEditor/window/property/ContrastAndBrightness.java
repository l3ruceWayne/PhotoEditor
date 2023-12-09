package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.tool.ZoomIn;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.lang.invoke.ConstantCallSite;

/**
* @Description: 调节图片对比度与亮度
* @author 罗雨曦
* @date 2023/11/27 13:49
* @version: 1.0
**/
public class ContrastAndBrightness {
    public JSlider brightnessSlider;
    public JLabel brightnessLabel;
    public JSlider contrastSlide;
    public JLabel contrastLabel;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成对比度与亮度调节滚动条
     * @author: 罗雨曦
     * @date: 2023/11/27 13:51
     * @version: 1.0
     **/
    public ContrastAndBrightness(Window window) {
        this.window = window;

        brightnessLabel = new JLabel("Brightness");
        brightnessSlider = new JSlider();
        brightnessSlider.setMinimum(1);
        brightnessSlider.setToolTipText("");
        brightnessSlider.setValue(10);
        brightnessSlider.setMaximum(20);
        contrastLabel = new JLabel("Contrast");
        contrastSlide = new JSlider();
        contrastSlide.setMinimum(-10);
        contrastSlide.setToolTipText("");
        contrastSlide.setValue(0);
        contrastSlide.setMaximum(10);
    }


}
