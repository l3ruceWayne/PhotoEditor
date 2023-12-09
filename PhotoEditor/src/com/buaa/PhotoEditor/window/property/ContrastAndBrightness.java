package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

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
        brightnessSlider.setValue(1);
        brightnessSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightnessSliderStateChanged(evt);
            }
        });

        contrastLabel = new JLabel("Contrast");
        contrastSlide = new JSlider();
        contrastSlide.setMinimum(-100);
        contrastSlide.setToolTipText("");
        contrastSlide.setValue(0);
        contrastSlide.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contrastSlideStateChanged(evt);
            }
        });
    }
    /**
     * @param evt 触发操作
     * @return void
     * @Description:调用执行重设亮度参数的操作
     * @author: 罗雨曦
     * @date: 2023/11/27 13:54
     * @version: 1.0
     **/
    private void brightnessSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        contrastAndBrightness();
    }

    /**
     * @param evt 触发操作
     * @return void
     * @Description:调用执行重设对比度参数的操作
     * @author: 罗雨曦
     * @date: 2023/11/27 13:55
     * @version: 1.0
     **/
    private void contrastSlideStateChanged(javax.swing.event.ChangeEvent evt) {
        contrastAndBrightness();
    }

    /**
     * @param
     * @return void
     * @Description:重设对比度与亮度参数
     * @author: 罗雨曦
     * @date: 2023/11/27 13:55
     * @version: 1.0
     **/
    private void contrastAndBrightness() {
        window.temp = MatUtil.copy(window.img);
        //修改亮度和对比度
        if (window.tool.region.selectRegionItem.isSelected())
            MatUtil.contrastAndBrightness(window.temp, brightnessSlider.getValue(), -contrastSlide.getValue(),
                    MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
        else
            MatUtil.contrastAndBrightness(window.temp, brightnessSlider.getValue(), -contrastSlide.getValue());

        MatUtil.show(window.temp, window.showImgRegionLabel);
    }
}
