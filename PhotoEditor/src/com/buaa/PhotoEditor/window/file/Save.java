package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Save {
    public JMenuItem saveItem;
    public JMenuItem saveAsItem;
    private Window window;

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
        for (JLabel widgetLabel : window.widget.widgetLabelList) {
            MatUtil.widget(newImg,
                    MatUtil.readImg(widgetLabel.getIcon().toString()),
                    widgetLabel.getX(), widgetLabel.getY());
            window.panel.remove(widgetLabel);
        }
        // 显示融为一体的图片
        MatUtil.show(newImg, window.showImgRegionLabel);
        if (window.last.size() != 0 && window.img != window.last.peek()) {
            window.last.push(window.img);
        }
        window.img = newImg;
    }

    private void saveImg(ActionEvent e) {
        if (window.img == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }

        getNewImg();

        MatUtil.save(window.originalImgPath,
                window.img);
        JOptionPane.showMessageDialog(null,
                "Success");
    }

    private void saveAsNewImg(ActionEvent e) {
        if (window.img == null) {
            JOptionPane.showMessageDialog(null,
                    "Please open an image and edit it first");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images",
                "png"));

        int userSelection = fileChooser.showSaveDialog(null);
        // 如果用户点击保存
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getPath();
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
