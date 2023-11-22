package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Undo {
    public JMenuItem undoItem;
    private Window window;

    public Undo(Window window) {
        this.window = window;
        undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undo(evt);
            }
        });
    }

    private void undo (ActionEvent e){
        if (!window.last.isEmpty()) {
            window.next.push(window.img);

            if (!window.tool.region.selectRegionItem.isSelected()) {
                window.img = window.last.pop();
            } else {
                Rect selectedRegionRect = MatUtil.getRect(window.tool.region.selectedRegionLabel);
                Mat newImg = MatUtil.copy(window.img);
                // last.peek()是栈顶Mat，即前一个版本，作用为把上一次改变的区域还原
                window.last.peek().submat(selectedRegionRect).copyTo(newImg.submat(selectedRegionRect));
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
            JOptionPane.showMessageDialog(null, "There's nothing left to undo!");
        }
    }
}
