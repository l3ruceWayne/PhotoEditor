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
