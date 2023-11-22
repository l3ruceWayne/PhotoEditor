package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;

public class MySize {//与openCV里的size类冲突，所以改了类名
    public javax.swing.JTextField txtHeight;
    public javax.swing.JTextField txtWidth;
    public javax.swing.JLabel lbSize;
    private Window window;

    public MySize(Window window){
        this.window=window;
        lbSize = new JLabel("Size");
        txtWidth = new JTextField();
        txtHeight = new JTextField();
    }
}
