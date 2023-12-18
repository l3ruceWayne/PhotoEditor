package com.buaa.PhotoEditor.window.custom;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.*;

/**
 * @author 卢思文
 * @version 1.0
 * @Description 字体颜色选择
 * @date 2023/12/10
 */
public class CustomColorChooser {
    public Window window;
    public JColorChooser colorChooser;
    public JDialog dialog;
    public JButton okButton;

    public CustomColorChooser(Window window, JButton okButton) {
        this.window = window;
        this.okButton = okButton;
    }

    /**
     * @Description 弹框
     * @author 卢思文
     * @date 2023/12/11
     */
    public void showDialog() {
        colorChooser = new JColorChooser();

        // Create the dialog
        dialog = new JDialog(window, "Choose a color", true);
        dialog.getContentPane().add(colorChooser);
        dialog.getContentPane().add(okButton, BorderLayout.SOUTH);

        // Set up and show the dialog
        dialog.pack();
        int x = window.getLocation().x + (window.getWidth() - dialog.getWidth()) / 2;
        int y = window.getLocation().y + (window.getHeight() - dialog.getHeight()) / 2;

        // 居中显示
        dialog.setLocation(x, y);
        dialog.setVisible(true);
    }
}