package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author 张旖霜
 * @version 1.0
 * @Description 实现了undo的多线程执行 实现了undo originalImg的操作
 * 将栈修改成保存zoomImg数组，Undo操作是撤销每一张图片的操作，把数组中的图片都换成上一步
 * 因为cut后，原图大小也不一样了，所以undo后，如果上一步是cut，就要恢复原图cut前的originalImg大小（存在lastOriginalImg的栈中）
 * @date 12/5/2023 9:36 PM
 */
public class UndoThread extends Thread {
    public Window window;
    public int i;
    public JMenuItem undoItem;

    public UndoThread(Window window, int i, JMenuItem undoItem) {
        this.window = window;
        this.i = i;
        this.undoItem = undoItem;
    }

    /**
     * @Description 实现对undo的多线程监听
     * @author 张旖霜
     * @date 12/11/2023 9:36 PM
     */
    @Override
    public void run() {
        undoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        undoItem.addActionListener(new ActionListener() {
            /**
             * @param e 触发操作
             * @Description 利用栈操作实现撤销操作
             * 实现了property值的undo操作
             * @author 罗雨曦 张旖霜
             * @date 2023/11/27 14:07
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (window.zoomImg == null) {
                    if (i == window.counter) {
                        JOptionPane.showMessageDialog(null, "Please open an image first");
                    }
                    return;
                }
                if (window.last.isEmpty()) {
                    if (i == window.counter) {
                        JOptionPane.showMessageDialog(null, "There's nothing left to undo!");
                    }
                    return;
                }
                // 取消 drag
                window.tool.getDrag().dragItem.setSelected(false);
//                window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                if (!window.last.isEmpty()) {
                    if (i == ORIGINAL_SIZE_COUNTER) {
                        window.next.push(copyImgArray(window.zoomImg));
                        window.nextOriginalImg.push(copyImgArray(window.originalZoomImg));
                        window.nextPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
                    }
                    if (!window.tool.getRegion().selectRegionItem.isSelected()) {
                        /*
                        将栈顶的上一步每个大小的图片复制到当前图片zoomImg中（undo操作）
                        将栈顶的上一步每个大小的原图复制到当前大小的原图OriginalImg中（undo操作）
                         */
                        if (i == ORIGINAL_SIZE_COUNTER) {
                            window.zoomImg = copyImgArray(window.last.peek());
                            window.originalZoomImg = copyImgArray(window.lastOriginalImg.peek());
                            // 当前property的值入栈
                            window.currentPropertyValue = MatUtil.copyPropertyValue(window.lastPropertyValue.pop());
                            // 还原property的值
                            window.property.getMySize().txtWidth.setText(window.currentPropertyValue[4] + "");
                            window.property.getMySize().txtHeight.setText(window.currentPropertyValue[5] + "");
                        }
                        // 在线程执行最后一步时，出栈
                        if (i == 0) {
                            window.last.pop();
                            window.lastOriginalImg.pop();
                        }
                        // 当前大小恢复成undo后的图片大小（因为cut会改变图片大小）
                        window.size[i][0] = window.zoomImg[i].width();
                        window.size[i][1] = window.zoomImg[i].height();
                    } else {
                        // last.peek()是栈顶Mat，即前一个版本，作用为把上一次改变的区域还原
                        Rect selectedRegionRect = MatUtil.getRect(window.tool.getRegion().selectedRegionLabel[i]);
                        Mat img = MatUtil.copy(window.zoomImg[i]);
                        window.last.peek()[i].submat(selectedRegionRect).copyTo(img.submat(selectedRegionRect));
                        window.zoomImg[i] = MatUtil.copy(img);
                        window.tool.getRegion().removeRegionSelected(i);
                    }
                    if (i == window.counter) {
                        window.property.updateProperty();
                        // 显示当前大小的图片
                        window.panel.setLayout(null);
                        window.showImgRegionLabel.setSize(window.zoomImg[window.counter].width(),
                                window.zoomImg[window.counter].height());
                        MatUtil.show(window.zoomImg[window.counter], window.showImgRegionLabel);
                        window.panel.setLayout(window.gridBagLayout);
                    }
                } else {
                    if (i == window.counter) {
                        JOptionPane.showMessageDialog(null, "There's nothing left to undo!");
                    }
                }
            }
        });
    }
}