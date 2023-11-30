package com.buaa.PhotoEditor.window.edit;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

/**
 * @Description: 实现粘贴功能的Paste类
 * @Author 卢思文
 * @Create 11/26/2023 9:22 PM
 * @Version 1.0
 */
public class Paste {
    private Window window;

    public Paste(Window window) {
        this.window = window;
    }
    public void disablePasteMode() {
        window.tool.region.removeRegionSelected();
        window.tool.region.selectedRegionLabel.removeAll();
        window.tool.region.copyRegionMat = null;
    }
    public void paste() {

        Mat newImg = MatUtil.copy(window.img);
        // newImg 的 selectRegion 被 copyRegionMat的内容覆盖
        MatUtil.copyToRegion(newImg,
                window.tool.region.copyRegionMat,
                MatUtil.getRect(window.tool.region.selectedRegionLabel));

        MatUtil.show(newImg, window.showImgRegionLabel);

        // 当前property的值入栈
        window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
        window.last.push(window.img);
        window.img = newImg;
    }
}
