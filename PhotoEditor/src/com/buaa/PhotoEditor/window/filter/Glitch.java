package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.modal.EColor;
import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public JButton glitchItemDialogOKButton;
    public Window window;

    public Glitch(Window window) {
        this.window = window;
        glitchItem = new JMenuItem("Glitch");
        glitchItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                glitchItemDialog.setModal(true);
                glitchItemDialog.setVisible(true);
            }
        });

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

        glitchItemDialogOKButton = new JButton("OK");
        // 当确定按钮被按下
        glitchItemDialogOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        setLayout();

    }
    public void OKButtonActionPerformed(ActionEvent evt) {
        if (!offsetLabel.isVisible() && !offsetValueTextField.isVisible())
            glitchItemDialog.dispose();
        else {
            try {
                waveLength = Integer.parseInt(offsetValueTextField.getText());
                /* !! ALERT: improve, make dynamic verification */
                if (green.isSelected()) {
                    color = EColor.GREEN;
                } else if (blue.isSelected()) {
                    color = EColor.BLUE;
                } else if (red.isSelected()) {
                    color = EColor.RED;
                }
                Mat newImg = MatUtil.copy(window.img);
                if (window.tool.region.selectRegionItem.isSelected()) {
                    MatUtil.glitchWave(newImg, waveLength, color, MatUtil.
                            getRect(window.tool.region.selectedRegionLabel));
                    window.tool.region.removeRegionSelected();
                } else {
                    MatUtil.glitchWave(newImg, waveLength, color);
                }

                MatUtil.show(newImg, window.showImgRegionLabel);

                window.last.push(window.img);
                window.img = newImg;
                // 执行完之后再关闭窗口
                glitchItemDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "填写错误！");
            }

        }
    }


    private void setLayout(){
        GroupLayout glitchWaveLayout = new GroupLayout(
                glitchItemDialog.getContentPane());
        glitchItemDialog.getContentPane().setLayout(glitchWaveLayout);
        glitchWaveLayout.setHorizontalGroup(
                glitchWaveLayout.
                        createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(glitchWaveLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(glitchWaveLayout.
                                        createParallelGroup(GroupLayout.
                                                Alignment.LEADING)
                                        .addGroup(glitchWaveLayout.createSequentialGroup()
                                                .addComponent(green)
                                                .addGap(45, 45, 45)
                                                .addComponent(blue)
                                                .addGap(35, 35, 35)
                                        )
                                        .addGroup(javax.swing.GroupLayout
                                                        .Alignment.TRAILING,
                                                glitchWaveLayout.createSequentialGroup()
                                                        .addComponent(offsetLabel)
                                                        .addPreferredGap(javax.swing
                                                                .LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(offsetValueTextField,
                                                                javax.swing
                                                                        .GroupLayout.PREFERRED_SIZE,
                                                                82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.
                                                                LayoutStyle.ComponentPlacement.UNRELATED)
                                        )
                                )
                                .addGroup(glitchWaveLayout.createParallelGroup(javax.swing
                                                .GroupLayout.Alignment.TRAILING)
                                        .addComponent(red)
                                        .addComponent(glitchItemDialogOKButton))
                                .addContainerGap(29, Short.MAX_VALUE)
                        )
        );
        // 设置垂直布局
        glitchWaveLayout.setVerticalGroup(
                glitchWaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(glitchWaveLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(glitchWaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(green)
                                        .addComponent(blue)
                                        .addComponent(red))
                                .addGap(18, 18, 18)
                                .addGroup(glitchWaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(offsetValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(glitchItemDialogOKButton)
                                        .addComponent(offsetLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );


        // 设置GlitchWave弹窗的水平布局
//        GroupLayout.SequentialGroup hGroup = glitchWaveLayout
//                .createSequentialGroup();
//        hGroup.addGap(17);
//        hGroup.addGroup(glitchWaveLayout.createParallelGroup(GroupLayout
//                        .Alignment.LEADING)
//                .addComponent(green));
//        hGroup.addGap(5);
//
//        hGroup.addGroup(glitchWaveLayout.createParallelGroup(GroupLayout
//                        .Alignment
//                        .LEADING)
//                .addComponent(blue)
//                .addComponent(offsetLabel));
//        hGroup.addGap(5);
//
//        hGroup.addGroup(glitchWaveLayout.createParallelGroup(GroupLayout
//                        .Alignment
//                        .LEADING)
//                .addComponent(red)
//                .addComponent(txtxLength,
//                        GroupLayout.PREFERRED_SIZE,
//                        82,
//                        GroupLayout.PREFERRED_SIZE
//                        )
//                .addComponent(okButton));
//        glitchWaveLayout.setHorizontalGroup(hGroup);
//
//        // 设置GlitchWave弹窗的垂直布局
//        GroupLayout.SequentialGroup vGroup = glitchWaveLayout
//                .createSequentialGroup();
//        vGroup.addGap(5);
//        vGroup.addGroup(glitchWaveLayout.createParallelGroup()
//                .addComponent(green)
//                .addComponent(blue)
//                .addComponent(red));
//        vGroup.addGap(5);
//        vGroup.addGroup(glitchWaveLayout.createParallelGroup()
//                .addComponent(offsetLabel)
//                .addComponent(txtxLength,
//                        GroupLayout.PREFERRED_SIZE,
//                        GroupLayout.DEFAULT_SIZE,
//                        GroupLayout.PREFERRED_SIZE)
//                .addComponent(okButton)
//        );
//        vGroup.addGap(5);
//        glitchWaveLayout.setVerticalGroup(vGroup);
    }


}
