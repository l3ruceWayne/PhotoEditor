package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;
import static com.buaa.PhotoEditor.window.Constant.*;
import com.buaa.PhotoEditor.window.thread.RotateThread;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
* @Description:图片旋转功能（顺时针旋转90°），后期可能可以添加图片翻转功能（镜面）
* @author: 卢思文，张旖霜
* @date: 11/27/2023 12:59 PM
* @version: 1.0
*/
public class Rotate {
    public Window window;
    public JMenu rotateItem;
    public RotateThread[] rotateThread;

    public Rotate(Window window) {
        this.window = window;
        rotateItem = new JMenu("Rotate");
// conflict

//         rotateItem.addMouseListener(new MouseAdapter() {
//             public void mouseClicked(MouseEvent evt) {
//                 rotateActionPerformed();
//             }
//         });
//     }

//     public void rotateActionPerformed() {
//         rotate(window.img);
//     }

//     /*
//      * @param img: Mat类型图片（当前图片）
//      * @return
//      * @Description:图片旋转（bug：旋转后的height和width设置有问题）
//      * @author: 张旖霜
//      * @date: 11/27/2023 12:59 PM
//      * @version: 1.0
//      */
//     private void rotate(Mat img) {
//         // 如果未选择图片，弹窗提示并return
//         if (window.originalImg == null) {
//             JOptionPane.showMessageDialog(null, "Please open an image first");
//             return;
//         }
//         int temp;
//         center = new Point(img.width() / 2, img.height() / 2);
//         angle = 90;
//         scale = 1.0;

//         rotation_matrix = Imgproc.getRotationMatrix2D(center, angle, scale);
//         Mat rotateImg = new Mat();
//         Mat newImg = new Mat();

//         Core.transpose(img, rotateImg);
//         Core.flip(rotateImg, newImg, 0);

//         // 旋转后，宽高大小交换
//         temp = window.imgWidth;
//         window.imgWidth = window.imgHeight;
//         window.imgHeight = temp;
//         // 重新设置图片大小
//         MatUtil.resize(newImg, new Size(window.img.height(), window.img.width()));

//         MatUtil.show(newImg, window.showImgRegionLabel);

//         // 当前property的值入栈
//         window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//         window.last.push(window.img);
//         window.img = newImg;

//         // 更新eraser需要的原图
//         window.originalImg = MatUtil.copy(window.img);
        rotateThread = new RotateThread[NUM_FOR_NEW];
        for(int i = 0;i<=ORIGINAL_SIZE_COUNTER;i++){
            rotateThread[i] = new RotateThread(window, rotateItem, i);
            rotateThread[i].start();
        }
    }
}
