package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.modal.EColor;

import com.buaa.PhotoEditor.util.MatUtil;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.add.Widget;
import com.buaa.PhotoEditor.window.filter.*;
import org.opencv.core.Mat;

import javax.swing.*;


import static com.buaa.PhotoEditor.util.MatUtil.getRect;

/**
 * ClassName: FilterThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description: 所有与栈有关的代码都先注释掉了
 *
 * @Author 卢思文
 * @Create 12/9/2023 11:25 AM
 * @Version 1.0
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
        blur.blurItemDialogOKButton.addActionListener(evt -> blur());
        invert.invertItem.addActionListener(evt -> invert());
        animize.animizeItem.addActionListener(evt -> animize());
        focus.focusItem.addActionListener(evt -> focus());
        glitch.glitchItemDialogOKButton.addActionListener(evt -> glitch());

    }

    public void gray() {

        if (window.tool.region.selectRegionItem.isSelected()) {

            MatUtil.grayScale(window.zoomImg[i],
                    getRect(window.tool.region
                            .selectedRegionLabel[i]));
            window.tool.region.removeRegionSelected(i);

        } else {
            MatUtil.grayScale(window.zoomImg[i]);
        }

        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);

//        // 当前property的值入栈
//        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//        window.last.push(window.img);
    }
    // lsw 只能输入整数，部分整数例如30,1也不能输入，即有bug待改
    public void blur() {
        if (!blur.blurLevelLabel.isVisible()
                && !blur.blurLevelTextField.isVisible()) {
            blur.blurItemDialog.dispose();
        } else {
            try {
                int blurLevel = Integer.parseInt(blur.blurLevelTextField.getText());
                if (window.tool.region.selectRegionItem.isSelected()) {

                    MatUtil.blur(window.zoomImg[i], blurLevel, getRect(window.tool.region.selectedRegionLabel[i]));
                    window.tool.region.removeRegionSelected(i);

                } else {
                    MatUtil.blur(window.zoomImg[i], blurLevel);
                }
                blur.blurItemDialog.dispose();
                if (i != window.counter) {
                    return;
                }
                MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);

//        // 当前property的值入栈
//        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//        window.last.push(window.img);

                // 当前property的值入栈
//                window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//                window.last.push(window.img);
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

    public void invert() {

        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.inversor(window.zoomImg[i], getRect(window.tool.region.selectedRegionLabel[i]));
            window.tool.region.removeRegionSelected(i);
        } else {
            MatUtil.inversor(window.zoomImg[i]);
        }

        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);

//        // 当前property的值入栈
//        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//        window.last.push(window.img);

    }
    // 这个滤镜计算量很大，耗时长，可以选择一小块区域之后进行测试
    public void animize() {


        if (window.tool.region.selectRegionItem.isSelected()) {
            MatUtil.cartoon(window.zoomImg[i], getRect(window.tool.region.selectedRegionLabel[i]));
            window.tool.region.removeRegionSelected(i);
        } else {
            MatUtil.cartoon(window.zoomImg[i]);
        }

        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);

        // 当前property的值入栈
//        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//        window.last.push(window.img);
//        window.img = window.zoomImg[i];

    }

    public void focus() {

        if (window.tool.region.selectRegionItem.isSelected()) {

            MatUtil.focus(window.zoomImg[i], getRect(window.tool.region.selectedRegionLabel[i]));
            window.tool.region.removeRegionSelected(i);
            if (i != window.counter) {
                return;
            }
            show(window.zoomImg[i], window.showImgRegionLabel);

            // 当前property的值入栈
//            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//            window.last.push(window.img);

        } else {
            if (i != window.counter) {
                return;
            }
            JOptionPane.showMessageDialog(null, "Please select a region first");
        }
    }

    public void glitch() {
        if (!glitch.offsetLabel.isVisible()
                && !glitch.offsetValueTextField.isVisible()) {
            glitch.glitchItemDialog.dispose();
        } else {
            try {
                glitch.waveLength = Integer.parseInt(glitch.offsetValueTextField.getText());
                if (glitch.green.isSelected()) {
                    glitch.color = EColor.GREEN;
                } else if (glitch.blue.isSelected()) {
                    glitch.color = EColor.BLUE;
                } else if (glitch.red.isSelected()) {
                    glitch.color = EColor.RED;
                }
                if (window.tool.region.selectRegionItem.isSelected()) {
                    MatUtil.glitchWave(window.zoomImg[i], glitch.waveLength, glitch.color, MatUtil.
                            getRect(window.tool.region.selectedRegionLabel[i]));
                    window.tool.region.removeRegionSelected(i);
                } else {
                    MatUtil.glitchWave(window.zoomImg[i], glitch.waveLength, glitch.color);
                }
                glitch.glitchItemDialog.dispose();
                if (i != window.counter) {
                    return;
                }
                MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);

                // 当前property的值入栈
//                window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//                window.last.push(window.img);
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
