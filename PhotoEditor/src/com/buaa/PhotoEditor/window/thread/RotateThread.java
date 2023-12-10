package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.buaa.PhotoEditor.util.MatUtil.show;
import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.transpose;

/**
 * ClassName: RotateThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description: 与栈有关的代码没完成
 *
 * @Author 卢思文
 * @Create 12/9/2023 11:06 AM
 * @Version 1.0
 */
public class RotateThread extends Thread {
    public Window window;
    public int i;
    public JMenu rotateItem;

    public RotateThread(Window window, JMenu rotateItem, int i) {
        this.window = window;
        this.rotateItem = rotateItem;
        this.i = i;
    }

    @Override
    public void run() {
        rotateItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                rotate();
            }
        });
    }

    private void rotate() {
        if (window.zoomImg == null) {
            if(i == window.counter){
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        /*
            transpose是矩阵转置，window.zoomImg[i]是Mat的实例化对象，而Mat是用
            矩阵来存储图像信息的，所以对该矩阵转置就是对图像进行旋转。
         */
        transpose(window.zoomImg[i], window.zoomImg[i]);
        /*
            而因为矩阵转置两次会回到原矩阵，这无法达到使图像每次旋转90°的目的
            需要进一步对图像进行翻转，即flip操作
            flipCode = 1作用是指定水平方向翻转
             = 0则是竖直方向翻转
             而要达到顺时针旋转90°的目的，需要 = 1
         */
        flip(window.zoomImg[i], window.zoomImg[i], 1);

        // 旋转后，宽高大小交换
        int temp = window.size[i][0];
        window.size[i][0] = window.size[i][1];
        window.size[i][1] = temp;

        // 原图也需要做同样的处理
        transpose(window.originalZoomImg[i], window.originalZoomImg[i]);
        flip(window.originalZoomImg[i], window.originalZoomImg[i], 1);

        //
        if (i != window.counter) {
            return;
        }
        // 布局重新设置，类似于ZoomIn中的相关代码
        window.panel.setLayout(null);
        window.showImgRegionLabel.setSize(window.size[i][0],
                window.size[i][1]);
        show(window.zoomImg[i], window.showImgRegionLabel);

        int width = window.size[i][0];
        int height = window.size[i][1];
        int panelWidth = window.panel.getWidth();
        int panelHeight = window.panel.getHeight();
        // 如果图片大小超出panel的大小，需要取消布局管理器的权限，自己介入布局管理，否则无法居中
        if (width > panelWidth
                || height > panelHeight) {
            window.showImgRegionLabel.setLocation((panelWidth - width) / 2,
                    (panelHeight - height) / 2);
            // 没超过，仍使用布局管理器
        } else {
            window.panel.setLayout(window.gridBagLayout);
        }


        // 当前property的值入栈
//        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//        window.last.push(window.img);
//        window.img = newImg;
//
//

    }
}
