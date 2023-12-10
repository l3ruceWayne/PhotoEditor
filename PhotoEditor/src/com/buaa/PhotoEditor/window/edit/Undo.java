package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.CutThread;
import com.buaa.PhotoEditor.window.thread.UndoThread;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import org.opencv.core.Size;


import javax.swing.*;
import java.awt.event.ActionEvent;
import static com.buaa.PhotoEditor.util.MatUtil.copyImgArray;
import static com.buaa.PhotoEditor.util.MatUtil.resize;
import static com.buaa.PhotoEditor.window.Constant.*;


/**
 * @Description: 撤销上一步操作； 目前对于是撤销操作还是恢复图片上一状态还没有设置明确，后将改进
 * @author 罗雨曦
 * @date 2023/11/27 14:06
 * @version: 1.0
 **/
/*
 * @Description:实现了undo的多线程
 * @author: 张旖霜
 * @date: 12/5/2023 9:36 PM
 * @version: 1.0
 */
public class Undo {
    public JMenuItem undoItem;
    private Window window;
    public UndoThread[] undoThread;


    /**
     * @param window 当前窗口
     * @return null
     * @Description:构造方法——生成子菜单项并设置快捷键
     * @author: 罗雨曦
     * @date: 2023/11/27 14:06
     * @version: 1.0
     **/
    public Undo(Window window) {
        this.window = window;
        undoItem = new JMenuItem("Undo");
        undoThread = new UndoThread[NUM_FOR_NEW];

        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            undoThread[i] = new UndoThread(window, i, undoItem);
            undoThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                undoThread[i].join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        });
    }
// conflict
//     /**
//      * @param e 触发操作
//      * @return void
//      * @Description:利用栈操作实现撤销操作
//      * 实现了property值的undo操作
//      * 增加未选择图片弹窗
//      * @author: 罗雨曦 张旖霜
//      * @date: 2023/12/5 3:40
//      * @version: 2.0
//      **/
//     private void undo (ActionEvent e){
//         //如果未选择图片，弹窗提示并return
//         if (window.originalImg == null) {
//             JOptionPane.showMessageDialog(null, "Please open an image first");
//             return;
//         }
//         if (!window.last.isEmpty()) {


//             window.next.push(window.img);
//             window.nextPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));




//             if (!window.tool.region.selectRegionItem.isSelected()) {
//                 window.img = window.last.pop();
//                 // 当前property的值入栈
//                 window.currentPropertyValue = MatUtil.copyPropertyValue(window.lastPropertyValue.pop());

//                 // 还原property的值
//                 window.property.getContrastAndBrightness().contrastSlide.setValue(window.currentPropertyValue[0]);
//                 window.property.getContrastAndBrightness().brightnessSlider.setValue(window.currentPropertyValue[1]);
//                 window.property.getSaturation().saturationSlider.setValue(window.currentPropertyValue[2]);
//                 window.property.getGraininess().grainBar.setValue(window.currentPropertyValue[3]);
//                 window.property.getMySize().txtWidth.setText(window.currentPropertyValue[4]+"");
//                 window.property.getMySize().txtHeight.setText(window.currentPropertyValue[5]+"");

//             } else {
//                 Rect selectedRegionRect = MatUtil.getRect(window.tool.region.selectedRegionLabel[window.counter]);
//                 Mat newImg = MatUtil.copy(window.img);
//                 // last.peek()是栈顶Mat，即前一个版本，作用为把上一次改变的区域还原
//                 window.last.peek().submat(selectedRegionRect).copyTo(newImg.submat(selectedRegionRect));
//                 //更新
//                 window.img = newImg;
//                 //取消区域选择复选框
//                 window.tool.region.removeRegionSelected();
//             }

//             window.showImgRegionLabel.setSize(window.img.width(), window.img.height());

// //            this.window.setSize(window.img.width(), window.img.height());
// //            this.window.setLocationRelativeTo(null);
//             window.property.updateProperty();
//             MatUtil.show(window.img, window.showImgRegionLabel);
//         } else {
//             //个人认为需要保留弹窗，后期可删
//             JOptionPane.showMessageDialog(null, "There's nothing left to undo!");


//         }

//     }

}
