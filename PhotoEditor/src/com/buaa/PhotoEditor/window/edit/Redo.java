package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Redo {
    public JMenuItem redoItem;
    private Window window;

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

    private void redo(ActionEvent e) {
        if (!window.next.isEmpty()) {
            window.last.push(window.img);

            if (!window.tool.region.selectRegionItem.isSelected()) {
                window.img = window.next.pop();
            } else {//现在的效果是
                Rect selectedRegionRect = MatUtil.getRect(window.tool.region.selectedRegionLabel);
                Mat newImg = MatUtil.copy(window.img);
                // last.peek()是栈顶Mat，即上一次撤销前的版本，作用为恢复上一次撤销前改变的区域
                window.next.peek().submat(selectedRegionRect).copyTo(newImg.submat(selectedRegionRect));
                //更新
                window.img = newImg;
                //取消区域选择复选框
                window.tool.region.removeRegionSelected();
            }

            window.showImgRegionLabel.setSize(window.img.width(), window.img.height());
            this.window.setSize(window.img.width(), window.img.height());
            this.window.setLocationRelativeTo(null);

            MatUtil.show(window.img, window.showImgRegionLabel);
        } else {
            //个人认为需要保留弹窗，后期可删
            JOptionPane.showMessageDialog(null, "There's nothing else to redo!");
        }
    }
}
