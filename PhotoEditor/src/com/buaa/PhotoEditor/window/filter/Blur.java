package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @Description 设置 Blur 效果
 * @author 卢思文
 * @date 11/26/2023 8:58 PM
 * @version 1.0
 */
public class Blur {
    public JMenuItem blurItem;
    public Window window;
    public JLabel blurLevelLabel;
    public JTextField blurLevelTextField;
    public JDialog blurItemDialog;
    public JButton blurItemDialogOkButton;

    public Blur(Window window) {
        this.window = window;
        blurItem = new JMenuItem("Blur");
        blurItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_DOWN_MASK));
        blurItem.addActionListener(evt -> blur());
        initBlurItem();
    }

    /**
     * @Description 点击blur后触发，打开blur设置面板
     * @author 卢思文
     * @date 12/12/2023 10:28 AM
     * @version 1.0
     */
    private void blur() {
        if (window.zoomImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        blurItemDialog.setModal(true);
        blurItemDialog.setVisible(true);
    }

    /**
     * @Description 初始化 blurItem以及其设置面板
     * @author 卢思文
     * @date 12/12/2023 10:29 AM
     * @version 1.0
     */
    private void initBlurItem() {
        blurItemDialog = new JDialog();
        blurItemDialog.setTitle("Blur");
        blurItemDialog.setSize(270, 150);
        blurItemDialog.setLocationRelativeTo(null);
        blurItemDialog.setResizable(false);

        blurLevelLabel = new JLabel("Blur Level:");
        blurLevelTextField = new JTextField();

        blurItemDialogOkButton = new JButton("OK");

        setLayout();
    }

    /**
     * @Description 设置面板的水平和垂直布局
     * @author 卢思文
     * @date 12/12/2023 10:30 AM
     * @version 1.0
     */
    private void setLayout() {
        GroupLayout blurDialogLayout = new GroupLayout(
                blurItemDialog.getContentPane());

        blurItemDialog.getContentPane().setLayout(blurDialogLayout);

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
                                .addComponent(blurItemDialogOkButton))
                        .addContainerGap(29, Short.MAX_VALUE)
                )
        );
        blurDialogLayout.setVerticalGroup(
                blurDialogLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(blurDialogLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGap(18, 18, 18)
                                .addGroup(blurDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(blurLevelTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(blurItemDialogOkButton)
                                        .addComponent(blurLevelLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
}