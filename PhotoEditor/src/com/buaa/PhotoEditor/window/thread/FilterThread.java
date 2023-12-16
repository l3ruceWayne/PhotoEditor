package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.modal.EColor;

import com.buaa.PhotoEditor.util.MatUtil;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.filter.*;

import javax.swing.*;


import static com.buaa.PhotoEditor.util.MatUtil.getRect;

/**
 * @Description 滤镜有关的多线程操作
 * @author 卢思文
 * @date 12/9/2023 11:25 AM
 * @version 1.0
 */

public class FilterThread extends Thread {
    public Window window;
    private Gray gray;
    private Blur blur;
    private Invert invert;
    private Animize animize;
    private Focus focus;
    private Glitch glitch;
    public int i;

    public FilterThread(Window window, Gray gray, Blur blur, Invert invert, Animize animize, Focus focus, Glitch glitch, int i) {
        this.window = window;
        this.gray = gray;
        this.blur = blur;
        this.invert = invert;
        this.animize = animize;
        this.focus = focus;
        this.glitch = glitch;
        this.i = i;
    }

    @Override
    public void run() {
        gray.grayItem.addActionListener(evt -> gray());
        blur.blurItemDialogOkButton.addActionListener(evt -> blur());
        invert.invertItem.addActionListener(evt -> invert());
        animize.animizeItem.addActionListener(evt -> animize());
        focus.focusItem.addActionListener(evt -> focus());
        glitch.glitchItemDialogOkButton.addActionListener(evt -> glitch());

    }

    public void gray() {
        if (window.zoomImg == null) {
            if (i == window.counter) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        if (i == ORIGINAL_SIZE_COUNTER) {
            window.saveFlag = false;
            window.next.clear();
            window.nextOriginalImg.clear();
            window.nextPropertyValue.clear();
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        }
        if (window.tool.getRegion().selectRegionItem.isSelected()) {

            MatUtil.grayScale(window.zoomImg[i],
                    getRect(window.tool.getRegion()
                            .selectedRegionLabel[i]));
            window.tool.getRegion().removeRegionSelected(i);

        } else {
            MatUtil.grayScale(window.zoomImg[i]);
        }

        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);

    }
    /**
    * @Description blur滤镜的实现
    * @author 卢思文
    * @date 12/11/2023 9:06 PM
    * @version: 1.0
    **/
    public void blur() {
        if (!blur.blurLevelLabel.isVisible()&& !blur.blurLevelTextField.isVisible()) {
            blur.blurItemDialog.dispose();
        } else {
            if (i == ORIGINAL_SIZE_COUNTER) {
                window.saveFlag = false;
                window.next.clear();
                window.nextOriginalImg.clear();
                window.nextPropertyValue.clear();
                // 当前property的值入栈
                window.lastPropertyValue.push(MatUtil
                    .copyPropertyValue(window.currentPropertyValue));
                // 将当前的window.img压入window.last中，保存上一张图片
                window.last.push(copyImgArray(window.zoomImg));
                window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
            }
            try {
                int blurLevel = Integer.parseInt(blur.blurLevelTextField.getText());
                if (window.tool.getRegion().selectRegionItem.isSelected()) {

                    MatUtil.blur(window.zoomImg[i], blurLevel, getRect(window.tool.getRegion().selectedRegionLabel[i]));
                    window.tool.getRegion().removeRegionSelected(i);

                } else {
                    MatUtil.blur(window.zoomImg[i], blurLevel);
                }
                blur.blurItemDialog.dispose();
                if (i != window.counter) {
                    return;
                }
                MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
            } catch (Exception ex) {
                if (i != window.counter) {
                    return;
                }
                JOptionPane.showMessageDialog(null,
                        "Wrong input");
            }
        }
    }
    /**
    * @Description invert滤镜的实现
    * @author 卢思文
    * @date 12/11/2023 9:08 PM
    * @version: 1.0
    **/
    public void invert() {
        if (window.zoomImg == null) {
            if (i == window.counter) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        if (i == ORIGINAL_SIZE_COUNTER) {
            window.saveFlag = false;
            window.next.clear();
            window.nextOriginalImg.clear();
            window.nextPropertyValue.clear();
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        }
        if (window.tool.getRegion().selectRegionItem.isSelected()) {
            MatUtil.inversion(window.zoomImg[i], getRect(window.tool.getRegion().selectedRegionLabel[i]));
            window.tool.getRegion().removeRegionSelected(i);
        } else {
            MatUtil.inversion(window.zoomImg[i]);
        }
        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
    }

    /**
    * @Description animize(动漫化)滤镜的实现，该滤镜计算量比较大，实现时间比较长
    * @author 卢思文
    * @date 12/11/2023 9:09 PM
    * @version: 1.0
    **/
    public void animize() {
        if (window.zoomImg == null) {
            if (i == window.counter) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        if (i == ORIGINAL_SIZE_COUNTER) {
            window.saveFlag = false;
            window.next.clear();
            window.nextOriginalImg.clear();
            window.nextPropertyValue.clear();
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        }
        if (window.tool.getRegion().selectRegionItem.isSelected()) {
            MatUtil.cartoon(window.zoomImg[i], getRect(window.tool.getRegion().selectedRegionLabel[i]));
            window.tool.getRegion().removeRegionSelected(i);
        } else {
            MatUtil.cartoon(window.zoomImg[i]);
        }
        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
    }
    /**
    * @Description focus(聚焦)滤镜的实现，操作前需要选定区域作为焦点
    * @author 卢思文
    * @date 12/11/2023 9:10 PM
    * @version: 1.0
    **/
    public void focus() {
        if (window.zoomImg == null) {
            if (i == window.counter) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        if (i == ORIGINAL_SIZE_COUNTER) {
            window.saveFlag = false;
            window.next.clear();
            window.nextOriginalImg.clear();
            window.nextPropertyValue.clear();
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(copyImgArray(window.zoomImg));
            window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        }
        if (window.tool.getRegion().selectRegionItem.isSelected()) {
            MatUtil.focus(window.zoomImg[i], getRect(window.tool.getRegion().selectedRegionLabel[i]));
            window.tool.getRegion().removeRegionSelected(i);
            if (i != window.counter) {
                return;
            }
            show(window.zoomImg[i], window.showImgRegionLabel);
        } else {
            if (i != window.counter) {
                return;
            }
            JOptionPane.showMessageDialog(null, "Please select a region first");
        }
    }
    /**
    * @Description glitch(故障风格, 例如抖音图标的风格)滤镜的实现
    * @author 卢思文
    * @date 12/11/2023 9:10 PM
    * @version: 1.0
    **/
    public void glitch() {
        if (!glitch.offsetLabel.isVisible()
                && !glitch.offsetValueTextField.isVisible()) {
            glitch.glitchItemDialog.dispose();
        } else {
            if (i == ORIGINAL_SIZE_COUNTER) {
                window.saveFlag = false;
                window.next.clear();
                window.nextOriginalImg.clear();
                window.nextPropertyValue.clear();
                // 当前property的值入栈
                window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
                // 将当前的window.img压入window.last中，保存上一张图片
                window.last.push(copyImgArray(window.zoomImg));
                window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
            }
            try {
                glitch.waveLength = Integer.parseInt(glitch.offsetValueTextField.getText());
                if (Glitch.green.isSelected()) {
                    glitch.color = EColor.GREEN;
                } else if (Glitch.blue.isSelected()) {
                    glitch.color = EColor.BLUE;
                } else if (Glitch.red.isSelected()) {
                    glitch.color = EColor.RED;
                }
                if (window.tool.getRegion().selectRegionItem.isSelected()) {
                    MatUtil.glitchWave(window.zoomImg[i], glitch.waveLength, glitch.color, MatUtil.
                            getRect(window.tool.getRegion().selectedRegionLabel[i]));
                    window.tool.getRegion().removeRegionSelected(i);
                } else {
                    MatUtil.glitchWave(window.zoomImg[i], glitch.waveLength, glitch.color);
                }
                glitch.glitchItemDialog.dispose();
                if (i != window.counter) {
                    return;
                }
                MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
                // 执行完之后再关闭窗口
            } catch (Exception ex) {
                if (i != window.counter) {
                    return;
                }
                JOptionPane.showMessageDialog(null,
                        "Wrong input");
            }
        }
    }
}
