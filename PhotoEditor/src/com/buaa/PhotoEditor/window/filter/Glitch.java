package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.modal.EColor;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * @author 卢思文
 * @version 1.0
 * @Description 设置 Glitch 效果
 * @date 11/26/2023 8:58 PM
 */
public class Glitch {
    public int waveLength;
    public ButtonGroup colors;
    public JLabel offsetLabel;
    public JTextField offsetValueTextField;
    public EColor color;
    public static JRadioButton red;
    public static JRadioButton blue;
    public static JRadioButton green;
    public JMenuItem glitchItem;
    public JDialog glitchItemDialog;
    public JButton glitchItemDialogOkButton;
    public Window window;

    public Glitch(Window window) {
        this.window = window;
        glitchItem = new JMenuItem("Glitch");
        glitchItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.SHIFT_DOWN_MASK));
        glitchItem.addActionListener(evt -> glitch());
        initGlitchItem();
    }

    /**
     * @Description 点击glitch后触发，打开glitch设置面板
     * @author 卢思文
     * @date 12/12/2023 10:28 AM
     */
    private void glitch() {
        if (window.zoomImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        glitchItemDialog.setModal(true);
        glitchItemDialog.setVisible(true);
    }

    /**
     * @Description 初始化 glitchItem以及其设置面板
     * @author 卢思文
     * @date 12/12/2023 10:29 AM
     */
    private void initGlitchItem() {
        glitchItemDialog = new JDialog();
        glitchItemDialog.setTitle("Glitch");
        glitchItemDialog.setSize(400, 200);
        glitchItemDialog.setLocationRelativeTo(null);
        glitchItemDialog.setResizable(false);

        red = new JRadioButton("Red");
        blue = new JRadioButton("Blue");
        green = new JRadioButton("Green");

        colors = new ButtonGroup();
        colors.add(blue);
        colors.add(red);
        colors.add(green);

        offsetLabel = new JLabel("Offset:");
        offsetValueTextField = new JTextField();

        glitchItemDialogOkButton = new JButton("OK");

        setLayout();
    }

    /**
     * @Description 设置面板的水平和垂直布局
     * @author 卢思文
     * @date 12/12/2023 10:30 AM
     */
    private void setLayout() {
        GroupLayout glitchDialogLayout = new GroupLayout(
                glitchItemDialog.getContentPane());
        glitchItemDialog.getContentPane().setLayout(glitchDialogLayout);

        // 水平布局
        glitchDialogLayout.setHorizontalGroup(glitchDialogLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(glitchDialogLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(glitchDialogLayout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(glitchDialogLayout.createSequentialGroup()
                                        .addComponent(green)
                                        .addGap(45, 45, 45)
                                        .addComponent(blue)
                                        .addGap(35, 35, 35))
                                .addGroup(GroupLayout.Alignment.TRAILING,
                                        glitchDialogLayout.createSequentialGroup()
                                                .addComponent(offsetLabel)
                                                .addPreferredGap(javax.swing
                                                        .LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(offsetValueTextField,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(glitchDialogLayout.createParallelGroup(javax.swing
                                        .GroupLayout.Alignment.TRAILING)
                                .addComponent(red)
                                .addComponent(glitchItemDialogOkButton))
                        .addContainerGap(29, Short.MAX_VALUE)
                )
        );
        // 设置垂直布局
        glitchDialogLayout.setVerticalGroup(
                glitchDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(glitchDialogLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(glitchDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(green)
                                        .addComponent(blue)
                                        .addComponent(red))
                                .addGap(18, 18, 18)
                                .addGroup(glitchDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(offsetValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(glitchItemDialogOkButton)
                                        .addComponent(offsetLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
}