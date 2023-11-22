package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class Graininess {
    public JLabel grainLabel;
    public JScrollBar grainBar;
    private Window window;

    public Graininess(Window window){
        this.window=window;
        grainBar=new JScrollBar();
        grainBar.setMaximum(255);
        grainBar.setOrientation(JScrollBar.HORIZONTAL);
        grainLabel=new JLabel("Graininess");
        grainBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                grainBarAdjustmentValueChanged(evt);
            }
        });
    }

    public void grainBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_noiseBarAdjustmentValueChanged
        applyNoise(grainBar.getValue(), false);
    }

    public void applyNoise(int level, boolean replace) {
        window.copy = MatUtil.copy(window.img);
        if (window.region.selectRegionItem.isSelected()) {
            MatUtil.noise(window.copy, level, MatUtil.getRect(window.region.selectedRegionLabel));
        } else {
            MatUtil.noise(window.copy, level);
        }
        // 需要再加一个确定键，确定之后img入栈，然后替换为当前调整后的内容
        MatUtil.show(window.copy, window.showImgRegionLabel);

        if (replace) {
            window.last.push(window.img);
            window.img = window.copy;
        }

    }
}
