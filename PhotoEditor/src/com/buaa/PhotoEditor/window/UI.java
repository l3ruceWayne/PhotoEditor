package com.buaa.PhotoEditor.window;

import javax.swing.*;

/**
* @Description: 更改编辑器的UI风格
* @author 罗雨曦
* @date 2023/12/5 1:39
* @version: 1.0
**/
public class UI {
    public JMenu uiMenu;
    public Window window;
    private JMenuItem lightMode;
    private JMenuItem darkMode;
    private JMenuItem systemMode;
    private JMenuItem javaMode;

    /**
     * @param window 当前窗口
     * @return null
     * @Description: 构造函数，改变UI种类
     * @author: 罗雨曦
     * @date: 2023/12/5 1:40
     * @version: 1.0
     **/
    public UI(Window window) {
        this.window = window;
        uiMenu = new JMenu("UI");
        lightMode = new JMenuItem("Light Mode");
        darkMode = new JMenuItem("Dark Mode");
        systemMode = new JMenuItem("System Mode");
        javaMode = new JMenuItem("Java Mode");

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

        systemMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        javaMode.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(window);
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                     IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        uiMenu.add(lightMode);
        uiMenu.add(darkMode);
        uiMenu.add(systemMode);
        uiMenu.add(javaMode);
    }
}
