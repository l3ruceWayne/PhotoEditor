
package com.buaa.PhotoEditor;

import com.buaa.PhotoEditor.window.layer.PerLayer;
import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.layer.LayerFrame;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.layer.Layer;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.opencv.core.Mat;

public class Main {
    // layer 自己定义的一个类型

//    private static final List<PerLayer> layerList = new ArrayList<>();
//    private static LayerFrame layerFrameWindow;
//    private static PerLayer currentLayer;
    // 所有窗口共用一个Layer，这样才能做到多窗口切换，所以static

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        new Window("Layer 1");

        // 默认开启的时候有一个窗口，重点是window
//        currentLayer = new PerLayer(new Window("Layer 1"));
//        currentLayer = layer.perLayer;

        // 让其显示
//        currentLayer.showWindow();

//        Layer.layerList.add(currentLayer);
        // layersWindow是显示当前有哪些层的窗口
//        layerFrameWindow = layer.layerFrame;
//        layerFrameWindow.setVisible(false);
//        layerFrameWindow.setLayersList(layerList);
    }


//    public static void removeLayer(int id) {
//        layerList.remove(id);
//        layerFrameWindow.setLayersList(layerList);
//    }
//
//    public static void addLayers() {
//        // 报错原因是当我们点击“添加”之前如果没有导入图片，则获取不到，就报错了
//        // 解决方案是，当没有导入图片时，点击后无反应或者后期再添加弹窗提示
//        PerLayer layer1 = layerList.get(layerList.size() - 1);
//        if(layer1.getImg() == null){
//            return;
//        }else{
//            Mat img = MatUtil.copy(layerList.get(layerList.size() - 1).getImg());
//            String title = ("层" + (layerList.size() + 1));
//            PerLayer layer = new PerLayer(new Window(img, title));
//            layerList.add(layer);
//            layerFrameWindow.setLayersList(layerList);
//            currentLayer.setNextLayerMat(layer.getImg());
//        }
//
//    }
//
//    public static void alterLayer(int id) {
//
////        currentLayer.hideWindow();
//        // 当前层更改为选中的那一个
//        currentLayer = layerList.get(id);
//        currentLayer.showWindow();
//
//        if (id >= 0 && id <= layerList.size() - 2) {
//
//            Mat img = layerList.get(id + 1).getImg();
//            // setNextLayerMat的意义何在
//            currentLayer.setNextLayerMat(img);
//        }
//    }
}
