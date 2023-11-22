package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Property {
    public JMenu propertyMenu;
    public JDialog propertyMenuDialog;

    private Window window;
    private ContrastAndBrightness contrastAndBrightness;
    private Saturation saturation;
    private Graininess graininess;
    private MySize mySize;

    public Property(Window window){
        this.window=window;
        contrastAndBrightness=new ContrastAndBrightness(window);
        saturation=new Saturation(window);
        graininess=new Graininess(window);
        mySize=new MySize(window);

        propertyMenu=new JMenu("Property");
        propertyMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                propertysMouseClicked(evt);
            }
        });

        propertyMenuDialog=new JDialog();
        propertyMenuDialog.setTitle("Property");
    }

    public void propertysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertysMouseClicked
        window.temp = MatUtil.copy(window.img);
        propertyMenuDialog.setModal(true);
        propertyMenuDialog.setVisible(true);
        propertyMenuDialog.setResizable(true);
        window.last.push(window.img);
        window.img = window.temp;
        window.removeRegionSelected();
        restartPorpertyComponentsValues();
    }
    public void restartPorpertyComponentsValues() {
        Component[] components = propertyMenuDialog.getContentPane().getComponents();
        for (Component c : components) {
            if (c instanceof JScrollBar) {
                ((JScrollBar) c).setValue(0);
            }
        }
    }
    public Window getWindow() {
        return window;
    }
    public ContrastAndBrightness getContrastAndBrightness() {
        return contrastAndBrightness;
    }
    public Saturation getSaturation() {
        return saturation;
    }

    public Graininess getGraininess() {
        return graininess;
    }

    public MySize getMySize() {
        return mySize;
    }
}
