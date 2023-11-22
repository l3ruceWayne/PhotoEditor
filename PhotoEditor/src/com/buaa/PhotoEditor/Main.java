
package com.buaa.PhotoEditor;

import com.buaa.PhotoEditor.window.layer.PerLayer;
import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.layer.LayerFrame;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.layer.Layer;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.opencv.core.Mat;

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
