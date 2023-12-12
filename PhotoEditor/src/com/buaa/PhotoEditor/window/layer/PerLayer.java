package com.buaa.PhotoEditor.window.layer;

/**
 * ClassName: PerLayer
 * Package: com.buaa.photoeditor.window.layer
 * Description:
 *
 * @Author 卢思文
 * @Create 12/11/2023 10:28 PM
 * @Version 1.0
 */
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

public class PerLayer {

    private Window window;

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
        window.nexLayerImg = img;
    }

    public void showWindow(){
        window.setVisible(true);
    }

    public Mat getImg(){
        return window.img;
    }

    public String getTitle() {
        return window.title;
    }
}
