package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Save {
    public JMenuItem saveItem;
    public JMenuItem saveAsItem;
    private Window window;

    private String path;

    public Save(Window window) {
        this.window = window;

        saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImg(e);
            }
        });

        saveAsItem = new JMenuItem("Save As");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK
                | KeyEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsNewImg(e);
            }
        });
    }

    private void getNewImg() {
        Mat newImg = MatUtil.copy(window.img);
        // 保存后，小组件和图片融为一体，所以把小组件删除
        for (JLabel widgetLabel : window.add.widget.widgetLabelList) {
            MatUtil.widget(newImg,
                    MatUtil.readImg(widgetLabel.getIcon().toString()),
                    widgetLabel.getX(), widgetLabel.getY());
            window.panel.remove(widgetLabel);
        }
        // 显示融为一体的图片
        MatUtil.show(newImg, window.showImgRegionLabel);
        if (window.last.size() != 0 && window.img != window.last.peek()) {
            window.isProperty.push(0);
            window.last.push(window.img);
        }
        window.img = newImg;
    }

    /*
    * @param:
    * @return
    * @Description:保存图片，如果用户点击了save as，保存路径改为save as的保存路径
    * @author: 张旖霜
    * @date: 11/27/2023 12:54 PM
    * @version: 1.0
    */
    private void saveImg(ActionEvent e) {
        if (window.img == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }
        MatUtil.resize(window.img, new Size(window.imgWidth, window.imgHeight));
        getNewImg();

        // zys 4
        if (path == null) {
            path = window.originalImgPath;
        }
        MatUtil.save(path, window.img);
        JOptionPane.showMessageDialog(null,
                "Success");
    }

    private void saveAsNewImg(ActionEvent e) {
        if (window.img == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }
        // 为了避免保存到zoom的图片，所以保存前先resize回之前的大小
        MatUtil.resize(window.img, new Size(window.imgWidth, window.imgHeight));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images",
                "png"));

        int userSelection = fileChooser.showSaveDialog(null);
        // 如果用户点击保存
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // zys 1
            path = fileChooser.getSelectedFile().getPath();
            if (!path.contains(".png")) {
                path += ".png";
            }
            getNewImg();
            MatUtil.save(path, window.img);
//                ImageIO.write(MatUtil.bufferedImg(window.img), "png", fileToSave);
            JOptionPane.showMessageDialog(null,
                    "Success");
        }
        // 否则就是取消另存为

    }
}
