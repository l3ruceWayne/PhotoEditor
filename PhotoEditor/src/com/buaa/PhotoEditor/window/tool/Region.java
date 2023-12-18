package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.thread.RegionThread;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * @Description 选择区域功能
 * @author 张旖霜、卢思文
 * @date 11/27/2023 3:31 PM
 * @version 1.0
 */
public class Region {
    public RegionThread[] regionThread;
    public JLabel[] selectedRegionLabel;
    public Window window;
    public JCheckBoxMenuItem selectRegionItem;
    private int removeRegionCounter = 0;
    public int[] selectedRegionX;
    public int[] selectedRegionY;
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
        // 如果未选择图片，弹窗提示并return
        selectRegionItem.addActionListener(e -> {
            if (window.originalImg == null) {
                JOptionPane.showMessageDialog(null, "Please open an image first");
                selectRegionItem.setSelected(false);
            }
        });
        selectRegionItem.addItemListener(e -> initRegionItem(e));
    }

    /**
     * @param e 事件
     * @Description 初始化regionItem
     * @author 卢思文
     * @date 11/26/2023 8:05 PM
     * @version 1.0
     */
    private void initRegionItem(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            window.tool.getPen().penItem.setSelected(false);
            window.tool.getEraser().eraserItem.setSelected(false);
            window.showImgRegionLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            window.tool.getDrag().dragItem.setSelected(false);

            if (!flag) {
                regionListener();
                flag = true;
            }
        }
    }

    /**
     * @Description 执行多线程，监听鼠标状态
     * @author 张旖霜
     * @date 11/27/2023 7:53 PM
     * @version 1.0
     */
    public void regionListener() {
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            regionThread[i].start();
        }
    }

    /**
     * @Description 取消某图层的region
     * @author 卢思文
     * @date 11/27/2023 7:53 PM
     * @version 1.0
     */
    public void removeRegionSelected(int i) {
        window.panel.setLayout(null);
        window.showImgRegionLabel.remove(selectedRegionLabel[i]);
        selectedRegionLabel[i].setVisible(false);
        window.panel.revalidate();
        window.panel.repaint();
        removeRegionCounter++;
        if (removeRegionCounter == NUM_FOR_NEW) {
            removeRegionCounter = 0;
            selectRegionItem.setSelected(false);
        }
    }

    /**
     * @Description 取消所有图层的region
     * @author 张旖霜
     * @date 11/27/2023 7:53 PM
     * @version 1.0
     */
    public void removeRegionSelected() {
        window.panel.setLayout(null);
        window.showImgRegionLabel.remove(selectedRegionLabel[window.counter]);
        window.panel.revalidate();
        window.panel.repaint();
        selectRegionItem.setSelected(false);
    }

    /**
     * @Description 恢复选择region前的状态
     * @author 张旖霜
     * @date 11/27/2023 7:53 PM
     * @version 1.0
     */
    public void disableListeners() {
        selectRegionItem.setSelected(false);
        window.pasting = false;
    }
}