package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
* @Description: 重做上一步操作； 目前对于是重做操作还是恢复图片下一状态还没有设置明确，后将改进
* @author 罗雨曦
* @date 2023/11/27 14:05
* @version: 1.0
**/
public class Redo {
    public JMenuItem redoItem;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:05
     * @version: 1.0
     **/
    public Redo(Window window) {
        this.window = window;
        redoItem = new JMenuItem("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        redoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                redo(e);
            }
        });
    }

    /**
     * @param e 触发操作
     * @return void
     * @Description:利用栈操作实现重做操作

     * 实现了property值的redo操作
     * @author: 罗雨曦 张旖霜
     * @date: 2023/11/27 14:05
     * @version: 1.0
     **/
    private void redo(ActionEvent e) {
        if (!window.next.isEmpty()) {
            window.last.push(window.img);
            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));

            if (!window.tool.region.selectRegionItem.isSelected()) {
                window.img = window.next.pop();


                // 还原property的值
                window.currentPropertyValue = MatUtil.copyPropertyValue(window.nextPropertyValue.pop());
                window.property.getContrastAndBrightness().contrastSlide.setValue(window.currentPropertyValue[0]);
                window.property.getContrastAndBrightness().brightnessSlider.setValue(window.currentPropertyValue[1]);
                window.property.getSaturation().saturationSlider.setValue(window.currentPropertyValue[2]);
                window.property.getGraininess().grainBar.setValue(window.currentPropertyValue[3]);
                window.property.getMySize().txtWidth.setText(window.currentPropertyValue[4]+"");
                window.property.getMySize().txtHeight.setText(window.currentPropertyValue[5]+"");

            } else {


                Rect selectedRegionRect = MatUtil.getRect(window.tool.region.selectedRegionLabel);


                Mat newImg = MatUtil.copy(window.img);
                // last.peek()是栈顶Mat，即上一次撤销前的版本，作用为恢复上一次撤销前改变的区域
                window.next.peek().submat(selectedRegionRect)
                        .copyTo(newImg.submat(selectedRegionRect));
                //更新
                window.img = newImg;
                //取消区域选择复选框
                window.tool.region.removeRegionSelected();
            }

            window.showImgRegionLabel.setSize(window.img.width(), window.img.height());

            // 不需要重新设置窗口大小
//            this.window.setSize(window.img.width(), window.img.height());
//            this.window.setLocationRelativeTo(null);

            window.property.updateProperty();
            MatUtil.show(window.img, window.showImgRegionLabel);
        } else {
            //个人认为需要保留弹窗，后期可删
            JOptionPane.showMessageDialog(null, "There's nothing else to redo!");
        }
    }
}
