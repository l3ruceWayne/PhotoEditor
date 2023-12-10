package com.buaa.PhotoEditor.window.filter;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Gray {
    public JMenuItem grayItem;
    public Window window;

    public Gray(Window window) {
        this.window = window;
        grayItem = new JMenuItem("Gray");

        grayItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                InputEvent.CTRL_MASK));

// conflict
//         grayItem.addActionListener(new ActionListener() {
//             public void actionPerformed(java.awt.event.ActionEvent evt) {
//                 grayActionPerformed(evt);
//             }
//         });
//     }
//     public void grayActionPerformed(ActionEvent evt) {
//         // 如果未选择图片，弹窗提示并return
//         if (window.zoomImg[ORIGINAL_SIZE_COUNTER]==null) {
//             JOptionPane.showMessageDialog(null, "Please open an image first");
//             return;
//         }

//         Mat newImg = copy(window.zoomImg[window.counter]);

//         if (window.tool.region.selectRegionItem.isSelected()) {

//             MatUtil.grayScale(newImg,
//                     getRect(window.tool.region
//                             .selectedRegionLabel[window.counter]));
//             window.tool.region.removeRegionSelected();

    }

}
