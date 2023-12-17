package com.buaa.PhotoEditor.window;

import javax.swing.*;

/**
 * @author 罗雨曦
 * @Description 更改编辑器的UI风格
 * @date 2023/12/5
 * @version: 1.0
 */
public class UI {
    public JMenu uiMenu;
    public Window window;

    /**
     * @param window 当前窗口
     * @Description 构造函数，改变UI种类
     * @author 罗雨曦
     * @date 2023/12/5
     * @version: 1.0
     */
    public UI(Window window) {
        this.window = window;
        uiMenu = new JMenu("UI");
        JMenuItem lightMode = new JMenuItem("Light Mode");
        JMenuItem darkMode = new JMenuItem("Dark Mode");
        JMenuItem metalMode = new JMenuItem("Metal Mode");
        JMenuItem classicMode = new JMenuItem("Classic Mode");
        JMenuItem motifMode = new JMenuItem("Motif Mode");
        JMenuItem nimbusMode = new JMenuItem("Nimbus Mode");


        lightMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException ex) {
                throw new RuntimeException(ex);
            }
        });

        darkMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException ex) {
                throw new RuntimeException(ex);
            }
        });

        metalMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        classicMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        motifMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        nimbusMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });


        uiMenu.add(lightMode);
        uiMenu.add(darkMode);
        uiMenu.add(metalMode);
        uiMenu.add(classicMode);
        uiMenu.add(nimbusMode);
        uiMenu.add(motifMode);
    }
}
