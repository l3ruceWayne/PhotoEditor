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
* @Description: 主菜单栏上的一级菜单，整合参数设置相关操作，下设ContrastAndBrightness Graininess MySize Saturation四个功能子菜单； 目前面板布局存在问题，后需改进
* 将updateProperty方法整合到了此类里面
* @author 罗雨曦、卢思文
* @date 2023/11/27 14:02
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

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成参数设置面板
     * @author: 罗雨曦
     * @date: 2023/11/27 14:03
     * @version: 1.0
     **/
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
        // lsw 高度改为600，让界面显示完全
        propertyMenuDialog.setSize(400, 600);
        propertyMenuDialog.setLocationRelativeTo(null);
        initLayout();
    }

    /**
     * @param
     * @return void
     * @Description:设置面板界面布局
     * @author: 罗雨曦
     * @date: 2023/11/27 14:03
     * @version: 1.0
     **/
    public void initLayout() {
        JLabel jLabel3 = new JLabel("Contrast");
        JLabel jLabel4 = new JLabel("Width:");
        JLabel jLabel5 = new JLabel("Height:");
//        JLabel jLabel6 = new JLabel("Noise");
        JLabel jLabel6 = new JLabel("");
        JLabel jLabel7 = new JLabel("Size");


        JButton resizeButton = new JButton("Resize");

        resizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                window.property.mySize.Resize(evt);

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

    /**
     * @param evt 触发操作
     * @return void
     * @Description:点击触发，完成面板有关参数的初始化设置
     * 触发后，把当前修改的新值保存到currentPropertyValue中（方便undo和redo的操作）
     * @author: 罗雨曦 张旖霜
     * @date: 2023/11/27 14:03
     * @version: 1.0
     **/
    public void propertysMouseClicked(java.awt.event.MouseEvent evt) {
        window.temp = MatUtil.copy(window.img);
        propertyMenuDialog.setModal(true);
        propertyMenuDialog.setVisible(true);
        propertyMenuDialog.setResizable(true);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.zoomImg);

        // 更新property现在的值
        window.currentPropertyValue[0] = contrastAndBrightness.contrastSlide.getValue();
        window.currentPropertyValue[1] = contrastAndBrightness.brightnessSlider.getValue();
        window.currentPropertyValue[2] = saturation.saturationSlider.getValue();
        window.currentPropertyValue[3] = graininess.grainBar.getValue();
        window.currentPropertyValue[4] = Integer.parseInt(mySize.txtWidth.getText());
        window.currentPropertyValue[5] = Integer.parseInt(mySize.txtHeight.getText());

        window.img = window.temp;

        window.tool.region.removeRegionSelected();
        // 为什么需要每次点击property后初始化滚动条呢？感觉不需要
//        restartPorpertyComponentsValues();
    }

    /**
     * @param
     * @return void
     * @Description:初始化滚动条参数
     * @author: 罗雨曦
     * @date: 2023/11/27 14:04
     * @version: 1.0
     **/
    private void restartPorpertyComponentsValues() {
        Component[] components = propertyMenuDialog.getContentPane().getComponents();
        for (Component c : components) {
            if (c instanceof JScrollBar) {
                ((JScrollBar) c).setValue(0);
            }
        }
    }



    public void updateProperty() {

        getMySize().txtWidth.setText(window.img.width() + "");
        getMySize().txtHeight.setText(window.img.height() + "");
        getMySize().lbSize.setText("Size: " + window.img.width() + "x" + window.img.height());

//        int width = window.img.width() >= 200 ? window.img.width() : 200;
//        int height = window.img.height() >= 200 ? window.img.height() : 200;

//        window.setSize(width, height);

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
