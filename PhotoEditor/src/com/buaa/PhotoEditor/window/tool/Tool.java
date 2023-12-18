package com.buaa.PhotoEditor.window.tool;

import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.window.Constant.*;

import javax.swing.*;
import java.awt.event.InputEvent;

/**
 * @author 卢思文
 * @version 1.0
 * @Description 主菜单栏上的一级菜单，整合工具类相关操作
 * @date 11/26/2023 8:21 PM
 */
public class Tool {
    public Window window;
    private Eraser eraser;
    private Pen pen;
    private Region region;
    private ZoomOut zoomOut;
    private ZoomIn zoomIn;
    private Rotate rotate;
    private Drag drag;
    private Preview preview;

    public static SpinnerNumberModel penModel
            = new SpinnerNumberModel(INIT_PEN_SIZE,
            MIN_PEN_SIZE,
            MAX_PEN_SIZE,
            PEN_STEP_SIZE);

    public static SpinnerNumberModel eraserModel
            = new SpinnerNumberModel(INIT_ERASER_SIZE,
            MIN_ERASER_SIZE,
            MAX_ERASER_SIZE,
            ERASER_STEP_SIZE);

    public Tool(Window window) {
        this.window = window;
        this.pen = new Pen(window);
        this.eraser = new Eraser(window);
        this.region = new Region(window);
        this.zoomOut = new ZoomOut(window);
        this.zoomIn = new ZoomIn(window);
        this.rotate = new Rotate(window);
        this.drag = new Drag(window);
        this.preview = new Preview(window);
        window.panel.addMouseWheelListener(e -> {
            if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                if (e.getWheelRotation() < 0) {
                    zoomIn.zoomIn();
                } else {
                    zoomOut.zoomOut();
                }
            }
        });
    }

    public Eraser getEraser() {
        return eraser;
    }

    public Pen getPen() {
        return pen;
    }

    public Region getRegion() {
        return region;
    }

    public ZoomOut getZoomOut() {
        return zoomOut;
    }

    public ZoomIn getZoomIn() {
        return zoomIn;
    }

    public Rotate getRotate() {
        return rotate;
    }

    public Drag getDrag() {
        return drag;
    }

    public Preview getPreview() {
        return preview;
    }
}