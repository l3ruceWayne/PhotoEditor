package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
* @Description: 将updateProperty方法整合到了此类里面
* @author: 卢思文
* @date: 11/26/2023 9:30 PM
* @version: 1.0
**/
public class Property {
    public JMenu propertyMenu;
    public JDialog propertyMenuDialog;

    private Window window;
    private ContrastAndBrightness contrastAndBrightness;
    private Saturation saturation;
    private Graininess graininess;
    private MySize mySize;

    public Property(Window window) {
        this.window = window;
        contrastAndBrightness = new ContrastAndBrightness(window);
        saturation = new Saturation(window);
        graininess = new Graininess(window);
        mySize = new MySize(window);

        propertyMenu = new JMenu("Property");
        propertyMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                propertysMouseClicked(evt);
            }
        });

        propertyMenuDialog = new JDialog();
        propertyMenuDialog.setTitle("Property");
        propertyMenuDialog.setSize(400, 400);
        propertyMenuDialog.setLocationRelativeTo(null);
        initLayout();
    }

    public void initLayout() {

        JLabel jLabel3 = new JLabel("Contrast");
        JLabel jLabel4 = new JLabel("Width:");
        JLabel jLabel5 = new JLabel("Height:");
        JLabel jLabel6 = new JLabel("Noise");
        JLabel jLabel7 = new JLabel("Size");

        JButton resizeButton = new JButton("Resize");

        resizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Resize(evt);
            }
        });

        JSeparator jSeparator2 = new JSeparator();
        JSeparator jSeparator3 = new JSeparator();

        GroupLayout PropertysLayout = new javax.swing.GroupLayout(propertyMenuDialog.getContentPane());
        propertyMenuDialog.getContentPane().setLayout(PropertysLayout);
        PropertysLayout.setHorizontalGroup(
                PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PropertysLayout.createSequentialGroup()
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(167, 167, 167)
                                                .addComponent(jLabel3))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                .addGap(17, 17, 17)
                                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                                .addGap(13, 13, 13)
                                                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                                                .addComponent(jLabel4)
                                                                                                .addGap(2, 2, 2)
                                                                                                .addComponent(getMySize().txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(30, 30, 30)
                                                                                                .addComponent(jLabel5))
                                                                                        .addComponent(getMySize().lbSize))
                                                                                .addGap(5, 5, 5)
                                                                                .addComponent(getMySize().txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                ))
                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                .addComponent(getGraininess().grainLabel)
                                                                .addGap(34, 34, 34)
                                                                .addComponent(getGraininess().grainBar, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                ))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(135, 135, 135)
                                                .addComponent(resizeButton))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(167, 167, 167)
                                                .addComponent(jLabel6))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(157, 157, 157)
                                                .addComponent(jLabel7))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(getContrastAndBrightness().contrastLabel)
                                                        .addComponent(getContrastAndBrightness().brightnessLabel)
                                                        .addComponent(getSaturation().saturationLabel))
                                                .addGap(28, 28, 28)
                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(getSaturation().saturationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(getContrastAndBrightness().brightnessSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(getContrastAndBrightness().contrastSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(73, Short.MAX_VALUE))
        );

        PropertysLayout.setVerticalGroup(
                PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PropertysLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addComponent(getContrastAndBrightness().contrastSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(getContrastAndBrightness().brightnessSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addComponent(getContrastAndBrightness().contrastLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(getContrastAndBrightness().brightnessLabel)))
                                .addGap(18, 18, 18)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(getSaturation().saturationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(getSaturation().saturationLabel))
                                .addGap(36, 36, 36)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(getGraininess().grainBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(getGraininess().grainLabel))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(getMySize().lbSize)
                                .addGap(24, 24, 24)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(getMySize().txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(getMySize().txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addComponent(resizeButton)
                                .addGap(42, 42, 42))
        );
    }

    public void propertysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertysMouseClicked
        window.temp = MatUtil.copy(window.img);
        propertyMenuDialog.setModal(true);
        propertyMenuDialog.setVisible(true);
        propertyMenuDialog.setResizable(true);
        window.last.push(window.img);
        window.img = window.temp;
        window.tool.region.removeRegionSelected();
        restartPorpertyComponentsValues();
    }

    private void restartPorpertyComponentsValues() {
        Component[] components = propertyMenuDialog.getContentPane().getComponents();
        for (Component c : components) {
            if (c instanceof JScrollBar) {
                ((JScrollBar) c).setValue(0);
            }
        }
    }

    /**
    * @param evt : 事件
    * @Description:
     * 只是简单的实现改变尺寸
     * 改变尺寸后，图片显示有bug
    * @author: 卢思文
    * @date: 11/25/2023 11:41 AM
    * @version: 1.0
    **/
    private void Resize(ActionEvent evt) {
        try {
            double newWidth = Double.parseDouble(getMySize().txtWidth.getText());
            double newHeight = Double.parseDouble(getMySize().txtHeight.getText());

            Mat newImg = MatUtil.copy(window.temp);
            MatUtil.resize(newImg, new Size(newWidth, newHeight));

            window.last.push(window.img);

            window.img = window.temp = newImg;
            MatUtil.show(window.temp, window.showImgRegionLabel);
            updateProperty();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Please prefill the data correctly!");
        }

    }
    public void updateProperty() {

        getMySize().txtWidth.setText(window.img.width() + "");
        getMySize().txtHeight.setText(window.img.height() + "");
        getMySize().lbSize.setText("Size: " + window.img.width() + "x" + window.img.height());

        int width = window.img.width() >= 200 ? window.img.width() : 200;
        int height = window.img.height() >= 200 ? window.img.height() : 200;

        window.setSize(width, height);
        window.setLocationRelativeTo(null);

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
