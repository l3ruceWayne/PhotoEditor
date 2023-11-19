
package com.buaa.PhotoEditor;

import com.buaa.PhotoEditor.modal.Layer;
import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Layers;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.opencv.core.Mat;

public class Main {
    // layer 自己定义的一个类型

    private static final List<Layer> layerList = new ArrayList<>();
    private static Layers layersWindow;
    private static Layer currentLayer;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();

        }
        // 默认开启的时候有一个窗口，重点是window
        currentLayer = new Layer(new Window("层1"));

        // 让其显示
        currentLayer.showWindow();

        layerList.add(currentLayer);
        // layersWindow是显示当前有哪些层的窗口
        layersWindow = new Layers();
        layersWindow.setVisible(true);
        layersWindow.setLayersList(layerList);
    }

    public static List<Layer> getLayerList() {
        return layerList;
    }

    public static void removeLayer(int id) {
        layerList.remove(id);
        layersWindow.setLayersList(layerList);
    }

    public static void addLayers() {
        // 报错原因是当我们点击“添加”之前如果没有导入图片，则获取不到，就报错了
        // 解决方案是，当没有导入图片时，点击后无反应或者后期再添加弹窗提示
        Layer layer1 = layerList.get(layerList.size() - 1);
        if(layer1.getImg() == null){
            return;
        }else{
            Mat img = MatUtil.copy(layerList.get(layerList.size() - 1).getImg());
            String title = ("层" + (layerList.size() + 1));
            Layer layer = new Layer(new Window(img, title));
            layerList.add(layer);
            layersWindow.setLayersList(layerList);
            currentLayer.setNextLayerMat(layer.getImg());
        }

    }

    public static void alterLayer(int id) {

//        currentLayer.hideWindow();
        // 当前层更改为选中的那一个
        currentLayer = layerList.get(id);
        currentLayer.showWindow();

        if (id >= 0 && id <= layerList.size() - 2) {

            Mat img = layerList.get(id + 1).getImg();
            // setNextLayerMat的意义何在
            currentLayer.setNextLayerMat(img);
        }
    }
}
