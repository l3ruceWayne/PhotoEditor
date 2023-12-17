package com.buaa.PhotoEditor.window.property;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;


import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;


import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

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
        mySize = new MySize(window);
        propertyMenu = new JMenu("Property");
        propertyMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                propertyMouseClicked();
            }
        });
        propertyMenuDialog = new JDialog();
        propertyMenuDialog.setTitle("Property");
        propertyMenuDialog.setSize(400, 175);
        propertyMenuDialog.setLocationRelativeTo(null);
        initLayout();
    }

    /**
     * @return void
     * @Description:设置面板界面布局
     * @author: 罗雨曦，卢思文
     * @date: 2023/11/27 14:03
     * @version: 2.0
     **/
    public void initLayout() {
        JLabel widthLabel = new JLabel("Width:");
        JLabel heightLabel = new JLabel("Height:");
        JLabel sizeLabel = new JLabel("Size");
        JButton resizeButton = new JButton("Resize");
        resizeButton.addActionListener(evt -> window.property.mySize.resize());
        GroupLayout propertyLayout = new GroupLayout(propertyMenuDialog.getContentPane());
        propertyMenuDialog.getContentPane().setLayout(propertyLayout);
        GroupLayout layout = new GroupLayout(propertyMenuDialog.getContentPane());
        propertyMenuDialog.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sizeLabel)
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addContainerGap(100, Short.MAX_VALUE)
            .addComponent(widthLabel)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(MySize.txtWidth, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(heightLabel)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(MySize.txtHeight, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(100, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(resizeButton)
            .addGap(0, 0, Short.MAX_VALUE)))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(sizeLabel)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
            .addComponent(widthLabel)
            .addComponent(mySize.txtWidth, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(heightLabel)
            .addComponent(mySize.txtHeight, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGap(18, 18, 18)
            .addComponent(resizeButton)
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }
    /**
     * @return void
     * @Description:点击触发，完成面板有关参数的初始化设置 触发后，把当前修改的新值保存到currentPropertyValue中（方便undo和redo的操作）
     * @author: 罗雨曦 张旖霜
     * @date: 2023/11/27 14:03
     * @version: 1.0
     **/
    public void propertyMouseClicked() {
        // 如果未选择图片，弹窗提示并return
        if (window.originalImg == null) {
            JOptionPane.showMessageDialog(null,
            "Please open an image first");
            return;
        }
        // 入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(
                window.currentPropertyValue));
        window.last.push(copyImgArray(window.zoomImg));
        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
        propertyMenuDialog.setModal(true);
        propertyMenuDialog.setVisible(true);
        propertyMenuDialog.setResizable(true);
        // 更新property现在的值
        window.currentPropertyValue[4] = Integer.parseInt(MySize.txtWidth.getText());
        window.currentPropertyValue[5] = Integer.parseInt(MySize.txtHeight.getText());
        window.tool.getRegion().removeRegionSelected();
    }
    /**
    * @Description: 更新property面板显示的图片参数
    * @author: 罗雨曦
    * @date: 12/11/2023 8:45 PM
    * @version: 1.0
    **/
    public void updateProperty() {
        MySize.txtWidth.setText(window.size[ORIGINAL_SIZE_COUNTER][0] + "");
        MySize.txtHeight.setText(window.size[ORIGINAL_SIZE_COUNTER][1] + "");
        getMySize().sizeLabel.setText("Size: "
            + window.size[ORIGINAL_SIZE_COUNTER][0] + "x"
            + window.size[ORIGINAL_SIZE_COUNTER][1]);
        window.setLocationRelativeTo(null);
    }

    public Window getWindow() {
        return window;
    }
    
    public MySize getMySize() {
        return mySize;
    }
}
