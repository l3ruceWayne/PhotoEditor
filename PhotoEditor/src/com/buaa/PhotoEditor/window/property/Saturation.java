package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
* @Description: 调节图片饱和度
* @author 罗雨曦
* @date 2023/11/27 13:56
* @version: 1.0
**/
public class Saturation {
    public JLabel saturationLabel;
    public JSlider saturationSlider;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成饱和度调节滚动条
     * @author: 罗雨曦
     * @date: 2023/11/27 13:56
     * @version: 1.0
     **/
    public Saturation(Window window){
        this.window=window;
        saturationLabel=new JLabel("Saturation");
        saturationSlider=new JSlider();
        saturationSlider.setMinimum(-100);
        saturationSlider.setMaximum(100);
        saturationSlider.setToolTipText("");
        saturationSlider.setValue(0);
    }

}
