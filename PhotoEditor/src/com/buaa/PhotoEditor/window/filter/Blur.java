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

        // 水平布局
        blurDialogLayout.setHorizontalGroup(blurDialogLayout
                .createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(blurDialogLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(blurDialogLayout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.TRAILING,
                                        blurDialogLayout.createSequentialGroup()
                                                .addComponent(blurLevelLabel)
                                                .addPreferredGap(javax.swing
                                                        .LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(blurLevelTextField,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(blurDialogLayout.createParallelGroup(javax.swing
                                        .GroupLayout.Alignment.TRAILING)
                                .addComponent(blurItemDialogOKButton))
                        .addContainerGap(29, Short.MAX_VALUE)
                )
        );
        // 设置垂直布局
        blurDialogLayout.setVerticalGroup(
                blurDialogLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(blurDialogLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGap(18, 18, 18)
                                .addGroup(blurDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(blurLevelTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(blurItemDialogOKButton)
                                        .addComponent(blurLevelLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

    }
}
