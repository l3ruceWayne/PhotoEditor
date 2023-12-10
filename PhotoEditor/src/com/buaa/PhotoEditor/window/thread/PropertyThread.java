package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.property.ContrastAndBrightness;
import com.buaa.PhotoEditor.window.property.Saturation;

import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;
import static com.buaa.PhotoEditor.window.Constant.*;
import org.opencv.core.Mat;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * ClassName: PropertyThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description: 改整体的属性，不支持选择区域
 *
 * @Author 卢思文
 * @Create 12/9/2023 2:35 PM
 * @Version 1.0
 */
public class PropertyThread extends Thread {
    public Window window;
    public ContrastAndBrightness contrastAndBrightness;
    public Saturation saturation;
    public int i;

    public PropertyThread(Window window, ContrastAndBrightness contrastAndBrightness, Saturation saturation, int i) {
        this.window = window;
        this.contrastAndBrightness = contrastAndBrightness;
        this.saturation = saturation;
        this.i = i;
    }

    @Override
    public void run() {
//        saturation.saturationSlider.addChangeListener(evt -> changeSaturation());
//        contrastAndBrightness.brightnessSlider.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent evt) {
//                contrastAndBrightness();
//            }
//        });

//        contrastAndBrightness.contrastSlide.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent evt) {
//                contrastAndBrightness();
//            }
//        });
    }

    /**
     * @return void
     * @Description:重设饱和度参数
     * @author: 罗雨曦
     * @date: 2023/11/27 13:57
     * @version: 1.0
     **/
    private void changeSaturation() {
        if (i == ORIGINAL_SIZE_COUNTER) {
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        }
        int saturationValue = saturation.saturationSlider.getValue();
        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.saturation(window.zoomImg[i], saturationValue,
                    MatUtil.getRect(window.tool.region.selectedRegionLabel[i]));
        } else {
            MatUtil.saturation(window.zoomImg[i], saturationValue);
        }
        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
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
        if (i == ORIGINAL_SIZE_COUNTER) {
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        }
        //修改亮度和对比度
        int contrastValue = contrastAndBrightness.contrastSlide.getValue();
        int brightnessValue = contrastAndBrightness.brightnessSlider.getValue();
        if (window.tool.region.selectRegionItem.isSelected()){
            MatUtil.contrastAndBrightness(window.zoomImg[i], brightnessValue,
                    contrastValue,
                    MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
        }
        else{
            MatUtil.contrastAndBrightness(window.zoomImg[i], brightnessValue,
                    contrastValue);
        }
        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
    }
}
