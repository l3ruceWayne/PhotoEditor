package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Saturation {
    public JLabel saturationLabel;
    public JSlider saturationSlider;
    public int lastSaturation;
    private Window window;

    public Saturation(Window window){
        this.window=window;
        saturationLabel=new JLabel("Saturation");
        saturationSlider=new JSlider();
        saturationSlider.setMinimum(-100);
        saturationSlider.setMaximum(100);
        saturationSlider.setToolTipText("");
        saturationSlider.setValue(0);
        saturationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                changeSaturation(evt);
            }
        });
    }

    private void changeSaturation(ChangeEvent evt) {
        window.temp = MatUtil.copy(window.img);

        int saturation = saturationSlider.getValue();
        lastSaturation = saturationSlider.getValue();

        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.saturation(window.temp, saturation, MatUtil.getRect(window.tool.region.selectedRegionLabel));
        } else {
            MatUtil.saturation(window.temp, saturation);
        }

        MatUtil.show(window.temp, window.showImgRegionLabel);
    }
}
