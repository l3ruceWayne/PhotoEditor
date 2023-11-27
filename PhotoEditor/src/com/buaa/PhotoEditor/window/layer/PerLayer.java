/*
Authors: Igor Joaquim dos Santos Lima
GitHub: https://github.com/igor036
*/
package com.buaa.PhotoEditor.window.layer;

import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

public class PerLayer {
    // Window 自己定义的类型
    
    private Window window;
    
    public PerLayer(){}
    
    public PerLayer(Window window){
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setNextLayerMat(Mat img) {
        this.window.setNextLayerMat(img);
    }
    
    public void showWindow(){
        window.setVisible(true);
    }
    
    public void hideWindow(){
        window.setVisible(false);
    }
    
    public Mat getImg(){
        return window.getImg();
    }

    /*
    * @param:
    * @return
    * @Description:获取当前图层名字（可以添加让用户为图层取名的功能）
    * @author: 张旖霜
    * @date: 11/27/2023 12:56 PM
    * @version: 1.0
    */
    public String getTitle() {
        return window.title;
    }
}
