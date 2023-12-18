package com.buaa.PhotoEditor.window;

/**
 * @author 卢思文
 * @version 1.0
 * @Description 存放常量
 * @date 2023/12/1
 */

public class Constant {
    /*
        lsw
        原尺寸图片放到最大放大图片的后面
        最佳自适应图片放到中间位置，即5
        如果档位过多，那么同时需要处理的图片也越多，会导致性能问题
        目前只使用画笔测试，11张图片是ok的，后续如果同时处理滤镜导致卡顿，可能会减半档位
     */

    /*
        NUM_FOR_NEW是我们new一系列数组的时候填入的个数，是最大数+1
        ORIGINAL_SIZE_COUNTER是原图存放的下标，不会在放大缩小的过程中显示
        MAX_SIZE_COUNTER是最大尺寸图片存放的下标，是zoomIn的最大档位
        AUTO_SIZE_COUNTER是自适应图片存放的下标
        我们会以自适应图片为中心，进行放大缩小，即缩小图片的下标是0,1,2,3,4
        放大图片的下标是5,6,7,8,9,10
     */
    public static final int NUM_FOR_NEW = 12;
    public static final int ORIGINAL_SIZE_COUNTER = 11;
    public static final int MAX_SIZE_COUNTER = 10;
    public static final int AUTO_SIZE_COUNTER = 5;

    // 缩小和放大份额，例如份额是10，即每次尺寸增加/减小原图尺寸/10
    public static final int ZOOM_RATIO = 6;

    public static final int MAX_PEN_SIZE = 30;
    public static final int MAX_ERASER_SIZE = 60;
    public static final int MIN_PEN_SIZE = 1;
    public static final int MIN_ERASER_SIZE = 1;
    public static final int INIT_ERASER_SIZE = AUTO_SIZE_COUNTER * 3 + 13;
    public static final int INIT_PEN_SIZE = AUTO_SIZE_COUNTER / 2 + 1;
    public static final int PEN_STEP_SIZE = 1;
    public static final int ERASER_STEP_SIZE = 1;
}
