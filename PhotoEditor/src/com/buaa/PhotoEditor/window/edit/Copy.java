package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @Description: 选择某个选区后复制该选区，通过在画布上点击鼠标实现该选区的剪贴
* @author 罗雨曦
* @date 2023/11/27 14:12
* @version: 1.0
**/
public class Copy {
    public JMenuItem copyItem;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:13
     * @version: 1.0
     **/
    public Copy(Window window) {
        this.window = window;
        copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        copyItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copySelectedRegion(evt);
            }
        });
    }
    /**
     * @param e 触发操作
     * @return void
     * @Description:获取选区并将pasting状态置1
     * @author: 罗雨曦
     * @date: 2023/11/27 14:14
     * @version: 1.0
     **/
    private void copySelectedRegion(ActionEvent e) {
        window.tool.region.selectRegionItem.setSelected(false);
        // 如果还没有选择区域，弹出提示框
        if (window.tool.region.selectedRegionLabel[window.counter].getBorder() == null) {
            JOptionPane.showMessageDialog(null, "Please select region first");
            return;
        }
        window.tool.region.copyRegionMat = window.img.submat(MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
        // pasting状态置1
        window.pasting = true;
    }
}
