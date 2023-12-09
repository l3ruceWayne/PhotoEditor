package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.window.Window;
import static com.buaa.PhotoEditor.util.MatUtil.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * ClassName: RegionThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description:
 *
 * @Author 卢思文
 * @Create 12/2/2023 8:56 PM
 * @Version 1.0
 */
public class RegionThread extends Thread{
    public Window window;
    public int i;
    public RegionThread(Window window, int i){
        this.window = window;
        this.i = i;
    }

    @Override
    public void run() {
        MouseInputAdapter mia = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (window.tool.region.selectRegionItem.isSelected()) {
                    addRegion(e.getX(),e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (window.tool.region.selectRegionItem.isSelected()) {
                    setRegionSize(e.getX(), e.getY());
                }
            }
        };

        window.showImgRegionLabel.addMouseListener(mia);
        window.showImgRegionLabel.addMouseMotionListener(mia);
    }
    public void addRegion(int x,int y) {
        Point p = new Point(getValueAfterZoom(window, x, i),
                getValueAfterZoom(window, y, i));
        window.tool.region.selectedRegionLabel[i].setLocation(p);
        window.tool.region.selectedRegionLabel[i].setSize(1, 1);
        window.tool.region.selectedRegionLabel[i].setBorder(BorderFactory
                .createLineBorder(Color.cyan));

//        window.showImgRegionLabel.setLayout(null);
        window.tool.region.selectedRegionLabel[i].setVisible(false);
        window.showImgRegionLabel.add(window.tool.region.selectedRegionLabel[i]);
        if(i == window.counter){
            window.tool.region.selectedRegionLabel[i].setVisible(true);
        }
//        // 可能过后改bug会用
//        // 在panel的z轴视角上设置各组件的优先级/遮盖关系：index小的，优先级高
//        window.showImgRegionLabel.setComponentZOrder(selectedRegionLabel[i], 0);
//        for (int i = 0; i < window.add.widget.widgetLabelList.size(); i++) {
//            window.panel.setComponentZOrder(window.add.widget.widgetLabelList.get(i), i + 1);
//        }
//        window.panel.setComponentZOrder(window.showImgRegionLabel,
//                window.add.widget.widgetLabelList.size() + 1);
//
//        window.showImgRegionLabel.revalidate();
//        window.showImgRegionLabel.repaint();

        window.tool.region.selectedRegionX[i]
                = window.tool.region.selectedRegionLabel[i].getX();
        window.tool.region.selectedRegionY[i]
                = window.tool.region.selectedRegionLabel[i].getY();
    }
    public void setRegionSize(int x, int y) {
        x = getValueAfterZoom(window, x ,i);
        y = getValueAfterZoom(window, y, i);
        int width = Math.abs(window.tool.region.selectedRegionX[i] - x);
        int height = Math.abs(window.tool.region.selectedRegionY[i] - y);
        x = Math.min(x, window.tool.region.selectedRegionX[i]);
        y = Math.min(y, window.tool.region.selectedRegionY[i]);
        window.tool.region.selectedRegionLabel[i].setBounds(x, y, width, height);
//        window.tool.region.pointRegion = new Point(x+width, y-height);
    }
    public void removeRegionSelected() {
        window.panel.setLayout(null);
        window.tool.region.selectedRegionLabel[i].setBorder(null);
        window.showImgRegionLabel.remove(window.tool.region.selectedRegionLabel[i]);
        window.panel.revalidate();
        window.panel.repaint();
        window.tool.region.selectRegionItem.setSelected(false);
    }
}
