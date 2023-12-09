package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Blur {
    public JMenuItem blurItem;
    public Window window;
    public JLabel blurLevelLabel;
    public JTextField blurLevelTextField;
    public JDialog blurItemDialog;
    public JButton blurItemDialogOKButton;

    public Blur(Window window) {
        this.window = window;
        blurItem = new JMenuItem("Blur");
        blurItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_MASK));
        blurItem.addActionListener(evt -> {
            blurItemDialog.setModal(true);
            blurItemDialog.setVisible(true);
        });

        blurItemDialog = new JDialog();
        blurItemDialog.setTitle("Blur");
        blurItemDialog.setSize(270, 150);
        blurItemDialog.setLocationRelativeTo(null);
        blurItemDialog.setResizable(false);
        
        blurLevelLabel = new JLabel("Blur Level:");
        blurLevelTextField = new JTextField();
        
        blurItemDialogOKButton = new JButton("OK");
        
        setLayout();
        
    }
    
    public void setLayout(){
        GroupLayout blurDialogLayout = new GroupLayout(
                blurItemDialog.getContentPane());

        blurItemDialog.getContentPane().setLayout(blurDialogLayout);

        Mat newImg = MatUtil.copy(window.img);

        if (window.tool.region.selectRegionItem.isSelected()) {

            MatUtil.blur(newImg, blurLevel, MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
            window.tool.region.removeRegionSelected();

        } else {
            MatUtil.blur(newImg, blurLevel);
        }

        MatUtil.show(newImg, window.showImgRegionLabel);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.zoomImg);
        window.img = newImg;

    }
}
