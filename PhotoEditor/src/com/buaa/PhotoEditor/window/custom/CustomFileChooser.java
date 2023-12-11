package com.buaa.PhotoEditor.window.custom;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.*;

/**
 * ClassName: CustomFileChooser
 * Package: com.buaa.PhotoEditor.window.custom
 * Description:
 *
 * @Author 卢思文
 * @Create 12/10/2023 6:50 PM
 * @Version 1.0
 */
public class CustomFileChooser {
    public Window window;
    public JFileChooser fileChooser;
    public JDialog dialog;
    public JButton OpenButton;

    public CustomFileChooser(Window window, JButton OKButton) {
        this.window = window;
        this.OpenButton = OKButton;
    }
    public void showDialog(){
        fileChooser = new JFileChooser();

        // Create the dialog
        dialog = new JDialog(window, "Choose a photo", true);
        dialog.getContentPane().add(fileChooser);
        dialog.getContentPane().add(OpenButton, BorderLayout.SOUTH);
        // Set up and show the dialog
        dialog.pack();
        int x = window.getLocation().x + (window.getWidth() - dialog.getWidth())/2;
        int y = window.getLocation().y + (window.getHeight() - dialog.getHeight())/2;
        // 居中显示
        dialog.setLocation(x,y);
        dialog.setVisible(true);

    }
}
