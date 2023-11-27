package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
* @Description: 打开图片（选择需要编辑的图片）
* @author 罗雨曦
* @date 2023/11/27 11:27
**/
public class Open {
    public JMenuItem openItem;
    private Window window;

    public Open(Window window) {
        /**
         * @param window 当前窗口
         * @return null
         * @Description:构造方法——生成子菜单项并设置快捷键
         * @author: 罗雨曦
         * @date: 2023/11/27 11:28
         **/

        this.window = window;
        openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectImg(e);
            }
        });
    }

    private void selectImg(ActionEvent e) {
        /**
         * @param e
         * @return void
         * @Description:利用JFileChooser实现图片的选择，并将图片展示在当前窗口
         * @author: 罗雨曦
         * @date: 2023/11/27 11:29
         **/

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this.window)
                == JFileChooser.APPROVE_OPTION) {
            window.originalImgPath = fileChooser.getSelectedFile().getAbsolutePath();
            window.img = MatUtil.readImg(window.originalImgPath);
            window.nexLayerImg = MatUtil.copy(window.img);
            window.originalImg = MatUtil.copy(window.img);
            //图片缩放
            MatUtil.show(window.img, window.showImgRegionLabel);
            window.showImgRegionLabel.setSize(window.img.width(), window.img.height());
            this.window.setSize(window.img.width(), window.img.height());
            this.window.setLocationRelativeTo(null);
            window.last.clear();
            window.next.clear();
            window.showImgRegionLabel.setText("");
            window.updatePropertys();
        }
    }
}
