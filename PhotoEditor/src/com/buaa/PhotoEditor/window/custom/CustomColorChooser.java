package com.buaa.PhotoEditor.window.custom;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.*;

/**
 * ClassName: CustomColorChooser
 * Package: com.buaa.PhotoEditor.window.custom
 * Description:
 *
 * @Author 卢思文
 * @Create 12/10/2023 6:06 PM
 * @Version 1.0
 */
public class CustomColorChooser {
    public Window window;
    public JColorChooser colorChooser;
    public JDialog dialog;
    public JButton OKButton;

    public CustomColorChooser(Window window, JButton OKButton) {
        this.window = window;
        this.OKButton = OKButton;
    }
    public void showDialog(){
        colorChooser = new JColorChooser();

        // Create the dialog
        dialog = new JDialog(window, "Choose a color", true);
        dialog.getContentPane().add(colorChooser);
        dialog.getContentPane().add(OKButton, BorderLayout.SOUTH);
        // Set up and show the dialog
        dialog.pack();
        int x = window.getLocation().x + (window.getWidth() - dialog.getWidth())/2;
        int y = window.getLocation().y + (window.getHeight() - dialog.getHeight())/2;
        // 居中显示
        dialog.setLocation(x,y);
        dialog.setVisible(true);

    }
}