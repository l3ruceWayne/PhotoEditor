package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
/**
* @Description: 调节图片颗粒度（噪声）
* @author 罗雨曦
* @date 2023/11/27 11:44
**/
public class Graininess {
    public JLabel grainLabel;
    public JScrollBar grainBar;
    private Window window;

    public Graininess(Window window){
        /**
         * @param window
         * @return null
         * @Description:构造方法——生成噪声调节滚动条
         * @author: 罗雨曦
         * @date: 2023/11/27 11:45
         **/

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

    private void grainBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
        /**
         * @param evt
         * @return void
         * @Description:调用执行重设噪声参数的操作
         * @author: 罗雨曦
         * @date: 2023/11/27 11:46
         **/

        applyNoise(grainBar.getValue());
    }

    private void applyNoise(int level) {
        /**
         * @param level
         * @param replace
         * @return void
         * @Description:重设噪声参数
         * @author: 罗雨曦
         * @date: 2023/11/27 11:47
         **/

        window.copy = MatUtil.copy(window.img);
        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.noise(window.copy, level, MatUtil.getRect(window.tool.region.selectedRegionLabel));
        } else {
            MatUtil.noise(window.copy, level);
        }
        // 需要再加一个确定键，确定之后img入栈，然后替换为当前调整后的内容
        MatUtil.show(window.copy, window.showImgRegionLabel);
    }
}
