package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @Description: 剪切图片
* @author 罗雨曦
* @date 2023/11/27 14:09
* @version: 1.0
**/
public class Cut {
    public JMenuItem cutItem;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:10
     * @version: 1.0
     **/
    public Cut(Window window) {
        this.window = window;
        cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        cutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cutActionPerformed(evt);
            }
        });
    }

    /**
     * @param evt 触发操作
     * @return void
     * @Description:利用MatUtil实现图片的剪切与展示
     * @author: 罗雨曦
     * @date: 2023/11/27 14:11
     * @version: 1.0
     **/
    private void cutActionPerformed(ActionEvent evt) {
        //在原函数基础上修了点bug。原函数为勾选框勾选就不报错，现改为需要勾选框勾选且选择区域才不报错，且在执行cut的同时取消勾选框勾选
        window.tool.region.selectRegionItem.setSelected(false);
        // 如果还没有选择区域，弹出提示框
        if (window.tool.region.selectedRegionLabel[window.counter].getBorder() == null) {
            JOptionPane.showMessageDialog(null,
                    "Please select region first");
        } else {
            //从window.img图像中裁剪出window.region.selectedRegionLabel[window.counter]标识的区域，并将裁剪后的图像赋值给新的Mat对象newImg
            Mat newImg = MatUtil.cut(window.img, MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
            MatUtil.show(newImg, window.showImgRegionLabel);
            //调整图片、窗口大小与窗口位置
            window.showImgRegionLabel.setSize(newImg.width(),newImg.height());
            this.window.setSize(newImg.width(), newImg.height());
            this.window.setLocationRelativeTo(null);

            // 当前property的值入栈
            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            // 将当前的window.img压入window.last中，保存上一张图片
            window.last.push(window.img);
            //更新
            window.img = newImg;
            // pending
            window.tool.region.removeRegionSelected();
        }
    }
}
