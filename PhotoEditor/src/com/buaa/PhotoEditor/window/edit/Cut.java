package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;
import org.opencv.core.Size;

import javax.swing.*;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;
import static com.buaa.PhotoEditor.window.Constant.AUTO_SIZE_COUNTER;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @Description: 剪切图片
 * @author 罗雨曦
 * @date 2023/11/27 14:09
 * @version: 1.0
 **/
/*
 * @Description:实现剪切图片的多线程执行
 * @author: 张旖霜
 * @date: 12/9/2023 9:46 AM
 * @version: 1.0
 */
public class Cut {
    public JMenuItem cutItem;
    private Window window;
    // cut的线程
    public CutThread[] cutThread;

    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:10
     * @version: 1.0
     **/
    public Cut(Window window) {
        this.window = window;
        cutItem = new JMenuItem("Cut");
        cutThread = new CutThread[NUM_FOR_NEW];

        // pending
//     /**
//      * @param evt 触发操作
//      * @return void
//      * @Description:利用MatUtil实现图片的剪切与展示
//      * 增加未选择图片弹窗
//      * @author: 罗雨曦
//      * @date: 2023/12/5 3:39
//      * @version: 2.0
//      **/
//     private void cutActionPerformed(ActionEvent evt) {
//         //在原函数基础上修了点bug。原函数为勾选框勾选就不报错，现改为需要勾选框勾选且选择区域才不报错，且在执行cut的同时取消勾选框勾选
//         window.tool.region.selectRegionItem.setSelected(false);
//         //如果未选择图片，弹窗提示并return
//         if (window.originalImg == null) {
//             JOptionPane.showMessageDialog(null, "Please open an image first");
//             return;
//         }
//         // 如果还没有选择区域，弹出提示框
//         if (window.tool.region.selectedRegionLabel[window.counter].getBorder() == null) {
//             JOptionPane.showMessageDialog(null,
//                     "Please select region first");
//         } else {
//             //从window.img图像中裁剪出window.region.selectedRegionLabel[window.counter]标识的区域，并将裁剪后的图像赋值给新的Mat对象newImg
//             Mat newImg = MatUtil.cut(window.img, MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]));
//             MatUtil.show(newImg, window.showImgRegionLabel);
//             //调整图片、窗口大小与窗口位置
//             window.showImgRegionLabel.setSize(newImg.width(),newImg.height());
//             this.window.setSize(newImg.width(), newImg.height());
//             this.window.setLocationRelativeTo(null);

//             // 当前property的值入栈
//             window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
//             // 将当前的window.img压入window.last中，保存上一张图片
//             window.last.push(window.img);
//             //更新
//             window.img = newImg;

//             window.tool.region.removeRegionSelected();

        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            cutThread[i] = new CutThread(window, i, cutItem); // 创建线程
            cutThread[i].start(); // 执行线程
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                cutThread[i].join(); // 等待上一个线程执行完毕才开始下一个线程
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

    }
}
