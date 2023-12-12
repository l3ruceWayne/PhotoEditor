package com.buaa.PhotoEditor.modal;

/**
 * @author 罗雨曦
 * @version 1.0
 * @Description 关于颜色的枚举类，在涉及颜色参数调整时使用
 * @date 2023/12/11
 */
public enum EColor {

    GREEN(0),
    BLUE(1),
    RED(2);

    private final int color;

    EColor(int color) {
        this.color = color;
    }

    public int colorValue() {
        return this.color;
    }

}
