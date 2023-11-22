package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class ContrastAndBrightness {//因为MatUtil里修改contrast和brightness的是同一个函数contrastAndBrightness，要求同时操作，所以把这两个类合并为一个
    public JSlider brightnessSlider;
    public JLabel brightnessLabel;
    public JSlider contrastSlide;
    public JLabel contrastLabel;
    private Window window;

    public ContrastAndBrightness(Window window){
        this.window=window;

        brightnessLabel=new JLabel("Brightness");
        brightnessSlider=new JSlider();
        brightnessSlider.setMinimum(1);
        brightnessSlider.setToolTipText("");
        brightnessSlider.setValue(1);
        brightnessSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightnessSliderStateChanged(evt);
            }
        });

        contrastLabel=new JLabel("Contrast");
        contrastSlide=new JSlider();
        contrastSlide.setMinimum(-100);
        contrastSlide.setToolTipText("");
        contrastSlide.setValue(0);
        contrastSlide.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contrastSlideStateChanged(evt);
            }
        });
    }

    private void brightnessSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_brightnessSliderStateChanged
        contrastAndBrightness();
    }

    private void contrastSlideStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_contrastSlideStateChanged
        contrastAndBrightness();
    }

    private void contrastAndBrightness() {
        window.temp = MatUtil.copy(window.img);
        //修改亮度和对比度
        if (window.region.selectRegionItem.isSelected())
            MatUtil.contrastAndBrightness(window.temp, brightnessSlider.getValue(), -contrastSlide.getValue(),
                    MatUtil.getRect(window.region.selectedRegionLabel));
        else
            MatUtil.contrastAndBrightness(window.temp, brightnessSlider.getValue(), -contrastSlide.getValue());

        MatUtil.show(window.temp, window.showImgRegionLabel);
    }
}
