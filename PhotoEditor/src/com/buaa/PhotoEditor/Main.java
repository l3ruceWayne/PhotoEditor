package com.buaa.PhotoEditor;

import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Core;

import javax.swing.*;

/**
 * @author 卢思文、罗雨曦
 * @version 1.0
 * @Description 主方法
 * @date 12/11/2023 10:18 PM
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        new Window("");
    }
}