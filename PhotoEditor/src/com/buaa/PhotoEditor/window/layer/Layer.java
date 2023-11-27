package com.buaa.PhotoEditor.window.layer;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: Layer类聚合了单个图层类（PerLayer）和图层窗口类（LayerFrame）
* @author: 卢思文
* @date: 11/26/2023 9:00 PM
* @version: 1.0
**/
public class Layer {
    public JMenuItem layerItem;
    public static LayerFrame layerFrame = new LayerFrame();
    public Window window;
    public PerLayer perLayer;
    public static PerLayer currentLayer;
    public static List<PerLayer> layerList = new ArrayList<>();

    public Layer(Window window) {
        this.window = window;
        perLayer = new PerLayer(window);
        currentLayer = perLayer;
        currentLayer.showWindow();
        layerList.add(perLayer);
        layerItem = new JMenuItem("Layer");
        // 调整间距
//        layerItem.setBorder(BorderFactory.createEmptyBorder(10,-10,10,10));
        layerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                layerActionPerformed(evt);
            }
        });

    }

    public void layerActionPerformed(ActionEvent evt){
        layerFrame.setLayersList(layerList);
        layerFrame.setVisible(true);
    }
    public static void addLayers() {
        // 之前报错原因是当我们点击“添加”之前如果没有导入图片，则获取不到，就报错了
        // 解决方案是，当没有导入图片时，点击后无反应或者后期再添加弹窗提示
        PerLayer layer1 = layerList.get(layerList.size() - 1);
        if(layer1.getImg() == null){
            return;
        }else{
            Mat img = MatUtil.copy(layerList.get(layerList.size() - 1).getImg());
            String title = ("Layer" + (layerList.size() + 1));
            PerLayer layer = new PerLayer(new Window(img, title));
            layerList.add(layer);
            layerFrame.setLayersList(layerList);
            currentLayer.setNextLayerMat(layer.getImg());
        }

    }
    public static void removeLayer(int id) {
        layerList.remove(id);
        layerFrame.setLayersList(layerList);
    }


     public static void alterLayer(int id) {

        currentLayer.hideWindow();
        // 当前层更改为选中的那一个
        currentLayer = layerList.get(id);
//        currentLayer.showWindow();

        if (id >= 0 && id <= layerList.size() - 2) {

            Mat img = layerList.get(id + 1).getImg();
            // setNextLayerMat的意义何在
            currentLayer.setNextLayerMat(img);
        }
    }
}
