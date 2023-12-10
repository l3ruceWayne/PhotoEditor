package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

public class Animize {
    public JMenuItem animizeItem;
    public Window window;

    public Animize(Window window) {
        this.window = window;
        animizeItem = new JMenuItem("Animize");
        animizeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
    }
}
