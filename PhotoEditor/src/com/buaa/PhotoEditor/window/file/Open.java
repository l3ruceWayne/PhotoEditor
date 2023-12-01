package com.buaa.PhotoEditor.window.file;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
* @Description: 打开图片（选择需要编辑的图片）； 后续可进行优化——点击后不是进入主界面，而是进入上一次打开所在路径
* 注意，不能打开矢量图
* @author 罗雨曦、卢思文
* @date 2023/11/27 14:08
* @version: 1.0
**/
public class Open {
    public JMenuItem openItem;
    private Window window;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:09
     * @version: 1.0
     **/
    public Open(Window window) {
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

    /**
    * @param e : 事件
    * @Description: 修复了无法打开中文路径图片的问题
     * 问题的原因定位到OpenCV库的Mat类不支持
     * 修复方法是改写MatUtil.readImg方法，先用其他数据结构读取图片，再转化成Mat
     * 注意，不支持打开矢量图
     * 通过改变图片尺寸实现图片大小自适应
     * 实现图片初始位置居中
    * @author: 卢思文、罗雨曦
    * @date: 11/26/2023 8:49 PM
    * @version: 1.0
    **/

    private void selectImg(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this.window)
                == JFileChooser.APPROVE_OPTION) {
            //LYX 实现图片大小自适应屏幕大小
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            window.originalImgPath = fileChooser.getSelectedFile().getAbsolutePath();
            Mat src= MatUtil.readImg(window.originalImgPath);
            window.srcImgWidth=src.width();
            window.srcImgHeight=src.height();
            window.img=reSize(src);

            window.nexLayerImg = MatUtil.copy(window.img);
            window.originalImg = MatUtil.copy(window.img);

            //图片缩放
            MatUtil.show(window.img, window.showImgRegionLabel);
            window.showImgRegionLabel.setSize(window.img.width(), window.img.height());
            //LYX 实现图片居中
            window.panel.setLayout(null);
            window.showImgRegionLabel.setLocation((screenSize.width-window.img.width())/2,((int)(screenSize.height*0.9)-window.img.height())/2);

            // 打开图片后储存图片的原始大小（保存图片原本大小）
            window.imgWidth = window.img.width();
            window.imgHeight = window.img.height();
            this.window.setLocationRelativeTo(null);
            window.last.clear();
            window.next.clear();
            // 清空property值的栈
            window.lastPropertyValue.clear();
            window.nextPropertyValue.clear();
            window.showImgRegionLabel.setText("");

            window.property.updateProperty();

        }
    }
    /**
     * @param src 原图片Mat
     * @return org.opencv.core.Mat
     * @Description: 实现图片大小适应屏幕
     * @author: 罗雨曦
     * @date: 2023/12/1 3:08
     * @version: 1.0
     **/
    private Mat reSize(Mat src){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double ratio = 1.0*src.width()/src.height();
        int dstWidth = (int)(((int) Math.min(screenSize.width, ratio*screenSize.height))*0.9);
        int dstHeight = (int) (dstWidth/ratio);
        Size size = new Size(dstWidth,dstHeight);
        MatUtil.resize(src,size);
        return src;
    }
}
