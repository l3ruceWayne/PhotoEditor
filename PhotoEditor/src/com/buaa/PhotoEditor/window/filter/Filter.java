package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.add.Widget;

import javax.swing.*;
import java.awt.event.WindowStateListener;

public class Filter {
    public JMenu filterMenu;
    public Window window;
    private Gray gray;
    private Blur blur;
    private Invert invert;
    private Animize animize;
    private Focus focus;
    private Glitch glitch;
    public Filter(Window window) {
        this.window = window;
        filterMenu = new JMenu("Filter");
        gray = new Gray(window);
        blur = new Blur(window);
        invert = new Invert(window);
        animize = new Animize(window);
        focus = new Focus(window);
        glitch = new Glitch(window);
        filterMenu.add(gray.grayItem);
        filterMenu.add(blur.blurItem);
        filterMenu.add(invert.invertItem);
        filterMenu.add(animize.animizeItem);
        filterMenu.add(focus.focusItem);
        filterMenu.add(glitch.glitchItem);
    }
}
