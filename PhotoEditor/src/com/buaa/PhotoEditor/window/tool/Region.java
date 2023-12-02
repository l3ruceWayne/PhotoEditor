package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.PaintThread;
import com.buaa.PhotoEditor.window.thread.RegionThread;
import org.opencv.core.Mat;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
/*

 * @Description:drag后重新定位的bug已修改
 * 点击选择区域功能后，光标暂且设置成默认
 * （如果上一个状态是画笔，如果不设置成默认，就一直是画笔了）
 * @author: 张旖霜、卢思文
 * @date: 11/27/2023 3:31 PM
 * @version: 1.0
 */

public class Region {
    public RegionThread[] regionThread;
    public Mat copyRegionMat;
    public JLabel[] selectedRegionLabel;
    public Window window;
    public JCheckBoxMenuItem selectRegionItem;
    public int selectedRegionX[], selectedRegionY[];

    public Point pointRegion;

    private boolean flag;

    public Region(Window window) {
        this.window = window;
        selectedRegionX = new int[NUM_FOR_NEW];
        selectedRegionY = new int[NUM_FOR_NEW];
        selectedRegionLabel = new JLabel[NUM_FOR_NEW];
        for (int i = 0; i < NUM_FOR_NEW; i++) {
            selectedRegionLabel[i] = new JLabel();
        }
        regionThread = new RegionThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            regionThread[i] = new RegionThread(window, i);
        }
        selectRegionItem = new JCheckBoxMenuItem("Region");
        selectRegionItem.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                window.tool.pen.penItem.setSelected(false);
                window.tool.eraser.eraserItem.setSelected(false);

                window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                window.tool.drag.dragItem.setSelected(false);


                if (flag == false) {
                    regionListener();
                    flag = true;
                }

            }
        });
    }

    /*
     * @param:
     * @return
     * @Description:监听鼠标状态（从tool类换到这里主要是为了修改drag后无法使用画笔的bug）
     * （在tool类是在window.panel上进行监听，换到Region类，在window.showImgRegionLabel进行监听）
     * @author: 张旖霜
     * @date: 11/27/2023 7:53 PM
     * @version: 1.0
     */
    public void regionListener() {
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            regionThread[i].start();
        }
    }
    public void removeRegionSelected() {
        window.panel.setLayout(null);
        window.showImgRegionLabel.remove(selectedRegionLabel[window.counter]);
        window.panel.revalidate();
        window.panel.repaint();
        selectRegionItem.setSelected(false);
    }

    public void copySelectedRegion(ActionEvent evt) {

        selectRegionItem.setSelected(false);
        // 如果还没有选择区域，弹出对话框
        if (selectedRegionLabel[window.counter].getBorder() == null) {
            // pending 对话框的位置？
            JOptionPane.showMessageDialog(null,
                    "Please select region first");
            return;
        }
        copyRegionMat = window.img.submat(MatUtil.getRect(selectedRegionLabel[window.counter]));

        // 进入pasting模式
        window.pasting = true;
    }


    public void disableListeners() {
        selectRegionItem.setSelected(false);
        window.pasting = false;
    }

}
