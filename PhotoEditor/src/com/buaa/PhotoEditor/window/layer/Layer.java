package com.buaa.PhotoEditor.window.layer;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/*
Layer类聚合了单个图层(PerLayer)和图层窗口(LayerFrame)
 */

/*
* @param:
* @return
* @Description: 每个按钮触发都增加了弹窗提醒
* @author: 张旖霜
* @date: 11/27/2023 12:55 PM
* @version: 1.0
*/
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

        if(currentLayer.getImg() == null){
            JOptionPane.showMessageDialog(null, "Please open an image");
            layerFrame.disposeLayerFrame(); // 关闭窗口
            return;
        }else{
            Mat img = MatUtil.copy(layerList.get(layerList.size() - 1).getImg());
            String title = ("Layer " + (layerList.size() + 1));
            PerLayer layer = new PerLayer(new Window(img, title));
            layerList.add(layer);
            layerFrame.setLayersList(layerList);
            currentLayer.setNextLayerMat(layer.getImg());
        }

    }
    public static void removeLayer(int id) {
        if (id == -1) {
            JOptionPane.showMessageDialog(null, "Please choose a layer to remove");
            return;
        }
        if(layerList.size() == 1){
            JOptionPane.showMessageDialog(null, "cannot remove layer");
            layerFrame.disposeLayerFrame(); // 关闭窗口
            return;
        }else{
            layerList.remove(id);
            layerFrame.setLayersList(layerList);
        }
    }


     public static void alterLayer(int id) {
         if (id == -1) {
             JOptionPane.showMessageDialog(null, "Please choose a layer to switch");
             return;
         }
        currentLayer.hideWindow();
         if (currentLayer == layerList.get(id))
         {
             currentLayer.showWindow(); // 打开当前图层
         } else {
             // 当前层更改为选中的那一个
             currentLayer = layerList.get(id);
             currentLayer.showWindow();
         }

        if (id >= 0 && id <= layerList.size() - 2) {

            Mat img = layerList.get(id + 1).getImg();
            // setNextLayerMat的意义何在
            currentLayer.setNextLayerMat(img);
        }
         layerFrame.disposeLayerFrame(); // 关闭窗口
    }
}
