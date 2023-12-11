
package com.buaa.PhotoEditor;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.custom.CustomColorChooser;
import com.buaa.PhotoEditor.window.custom.CustomFileChooser;
import org.opencv.core.Core;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        new Window("Layer 1");
    }

}
