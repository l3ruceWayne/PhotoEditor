package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Eraser {
    public JCheckBoxMenuItem eraserItem;
    public int eraserSize;
    public Window window;
    public Eraser(Window window) {
        this.window = window;
        eraserItem = new JCheckBoxMenuItem("Eraser");
        eraserItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    window.region.removeRegionSelected();
                    window.pen.penItem.setSelected(false);
                }
            }
        });
    }
}
