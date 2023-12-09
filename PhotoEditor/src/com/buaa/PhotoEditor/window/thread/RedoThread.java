package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.buaa.PhotoEditor.window.Constant.*;
/*
 * @Description:实现了redo的多线程执行
 * 实现了redo originalImg的操作
 * 将栈修改成保存zoomImg数组，Redo操作是恢复每一张图片的操作，把数组中的图片都换成redo后的图（next栈顶的图）
 * 因为cut后，原图大小也不一样了，所以redo后，就要恢复原图cut前的originalImg大小（存在nextOriginalImg的栈中）
 * @author: 张旖霜
 * @date: 12/5/2023 9:36 PM
 * @version: 1.0
 */
public class RedoThread extends Thread {
    public Window window;
    public int i;
    public JMenuItem redoItem;

    public RedoThread(Window window, int i, JMenuItem redoItem) {
        this.window = window;
        this.i = i;
        this.redoItem = redoItem;
    }

    @Override
    public void run() {
        // 进行监听

        redoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        redoItem.addActionListener(new ActionListener() {
            /**
             * @param e 触发操作
             * @return void
             * @Description:利用栈操作实现重做操作
             * 实现了property值的redo操作
             * @author: 罗雨曦 张旖霜
             * @date: 2023/11/27 14:05
             * @version: 1.0
             **/
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!window.next.isEmpty()) {
                    if (i == window.counter) {
                        window.last.push(copyImgArray(window.zoomImg));
                        window.lastOriginalImg.push(copyImgArray(window.originalZoomImg));
                        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
                    }

                    if (!window.tool.region.selectRegionItem.isSelected()) {
                        // 将栈顶的每个大小的图片复制到当前图片zoomImg中（redo操作）
                        window.zoomImg[i] = copy(window.next.peek()[i]);
                        // 将栈顶的上一步每个大小的原图复制到当前大小的原图OriginalImg中（undo操作）
                        window.originalZoomImg[i] = copy(window.nextOriginalImg.peek()[i]);
                        // 在线程执行最后一步时，出栈
                        if (i == 0) {
                            window.next.pop();
                            window.nextOriginalImg.pop();
                            // 当前property的值入栈
                            window.currentPropertyValue = MatUtil.copyPropertyValue(window.nextPropertyValue.pop());

                            // 还原property的值
                            window.property.getContrastAndBrightness().contrastSlide.setValue(window.currentPropertyValue[0]);
                            window.property.getContrastAndBrightness().brightnessSlider.setValue(window.currentPropertyValue[1]);
                            window.property.getSaturation().saturationSlider.setValue(window.currentPropertyValue[2]);
                            window.property.getGraininess().grainBar.setValue(window.currentPropertyValue[3]);
                            window.property.getMySize().txtWidth.setText(window.currentPropertyValue[4] + "");
                            window.property.getMySize().txtHeight.setText(window.currentPropertyValue[5] + "");
                        }
                        // 当前大小恢复成redo后的图片大小（因为cut会改变图片大小）
                        window.size[i][0] = window.zoomImg[i].width();
                        window.size[i][1] = window.zoomImg[i].height();
                    } else {
                        // last.peek()是栈顶Mat，即前一个版本，作用为把上一次改变的区域还原
                        Rect selectedRegionRect = MatUtil.getRect(window.tool.region.selectedRegionLabel[i]);
                        Mat img = MatUtil.copy(window.zoomImg[i]);
                        window.next.peek()[i].submat(selectedRegionRect).copyTo(img.submat(selectedRegionRect));
                        window.zoomImg[i] = MatUtil.copy(img);
                    }
                    if (i == window.counter)
                    {
                        window.property.updateProperty();
                        // 显示当前大小的图片
                        MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
                        window.showImgRegionLabel.setSize(window.zoomImg[window.counter].width(),
                                window.zoomImg[window.counter].height());
                        window.panel.setLayout(window.gridBagLayout);
                        //取消区域选择复选框
                        window.tool.region.removeRegionSelected();
                    }

                } else {
                    //个人认为需要保留弹窗，后期可删
                    if (i == window.counter)
                    {
                        JOptionPane.showMessageDialog(null, "There's nothing left to redo!");
                    }
                }
            }
        });
    }
}