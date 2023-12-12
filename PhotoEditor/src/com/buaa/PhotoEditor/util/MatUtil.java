package com.buaa.PhotoEditor.util;

import com.buaa.PhotoEditor.modal.EColor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.*;

import javax.swing.*;

import com.buaa.PhotoEditor.window.Window;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @author 卢思文、张旖霜、罗雨曦
 * @version 2.0
 * @Description Mat的一些编辑与处理操作
 * @date 2023/12/11
 */
public abstract class MatUtil extends JFrame {
    /**
     * @param x u时的坐标值
     * @param i 转换成i时的坐标值
     * @return zoom操作后的坐标值
     * @Description 计算zoom操作后的坐标值
     * @author 卢思文、张旖霜
     * @date 2023/12/1
     * @version: 1.0
     **/
    public static int getValueAfterZoom(Window window, double x, int i) {
        if (i == window.counter) {
            return (int) x;
        }
        double iWidth = window.zoomImg[i].width();
        double uWidth = window.zoomImg[window.counter].width();
        return (int) Math.round(x * iWidth / uWidth);
    }

    /**
     * @param img    编辑后的图片
     * @param jLabel 展示图片的JLabel
     * @Description 将编辑后的图片实时展示
     * @author 卢思文、罗雨曦
     * @date 2023/12/11
     * @version: 2.0
     */
    public static void show(Mat img, JLabel jLabel) {
        ImageIcon imgIcon = new ImageIcon(bufferedImg(img));
        jLabel.setIcon(imgIcon);
    }

    /**
     * @param img 要转化为bufferedImg的Mat图片
     * @return java.awt.image.BufferedImage
     * @Description Mat类型的img转化为BufferedImage类型的对象
     * 为什么要转化？因为Mat类型的对象适合图像处理，但是不适合展示处理好的结果
     * 而BufferedImage则相反
     * 所以在利用Mat处理完图像之后，转化为BufferedImage进行展示
     * 此方法不能用来处理矢量图片
     * @author 卢思文、罗雨曦
     * @date 2023/12/1
     * @version: 2.0
     */
    public static BufferedImage bufferedImg(Mat img) {
        int type = (img.channels() > 1) ? BufferedImage.TYPE_3BYTE_BGR :
                BufferedImage.TYPE_BYTE_GRAY;
        int bufferSize = img.channels() * img.cols() * img.rows();
        byte[] buffer = new byte[bufferSize];

        img.get(0, 0, buffer);
        BufferedImage bfImg = new BufferedImage(img.cols(), img.rows(), type);
        byte[] targetPixels = ((DataBufferByte) bfImg.getRaster()
                .getDataBuffer()).getData();

        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return bfImg;
    }

    /**
     * @param img    图片
     * @param widget 添加组件
     * @param wx     添加处x坐标
     * @param wy     添加处y坐标
     * @param i      当前图片序列号
     * @param window 当前窗口
     * @Description 添加小组件
     * @author 卢思文
     * @date 2023/12/1
     * @version: 1.0
     */
    public static void widget(Mat img, Mat widget, int wx, int wy, int i, Window window) {
        int lx = getValueAfterZoom(window, wx, i);
        int ly = getValueAfterZoom(window, wy, i);
        int width = getValueAfterZoom(window, widget.width(), i);
        int height = getValueAfterZoom(window, widget.height(), i);
        int rx = lx + width;
        int ry = ly + height;
        if ((lx < 0 || rx > getValueAfterZoom(window, window.showImgRegionLabel.getWidth(), i))) {
            window.flagForWidget = false;
            return;
        }
        if ((ly < 0 || ry > getValueAfterZoom(window, window.showImgRegionLabel.getHeight(), i))) {
            window.flagForWidget = false;
            return;
        }
        Point a = new Point(lx, ly);
        Point b = new Point(rx, ry);
        Mat widgetRegion = img.submat(new Rect(a, b));
        Mat mat = copy(widget);
        resize(mat, new Size(width, height));

        overlay(widgetRegion, mat);
    }

    public static void overlay(Mat img, Mat widget) {
        for (int x = 0; x < widget.rows(); x++) {
            for (int y = 0; y < widget.cols(); y++) {

                double[] pixel1 = img.get(x, y);
                double[] pixel2 = widget.get(x, y);

                //PNG WITH OPACITY
                if (pixel2.length == 4) {

                    double alpha = pixel2[3] / 255f;

                    for (int i = 0; i < 3; i++) {
                        pixel1[i] = pixel2[i] * alpha + pixel1[i] * (1 - alpha);
                    }

                }
                //PHOTO WITHOUT OPACITY
                else {
                    System.arraycopy(pixel2, 0, pixel1, 0, 3);
                }
                img.put(x, y, pixel1);
            }
        }
    }

    public static void glitchWave(Mat img, int waveLength, EColor color) {
        Mat sub = copy(img);

        for (int x = 0; x < sub.rows(); x++) {
            for (int y = 0; y < sub.cols(); y++) {

                double[] pixel1 = img.get(x, y);

                int xWave = x - waveLength >= 0 ? x - waveLength : waveLength - x;
                int yWave = y - waveLength >= 0 ? y - waveLength : waveLength - y;

                double[] pixel2 = sub.get(xWave, yWave);
                pixel1[color.colorValue()] = pixel2[color.colorValue()];

                img.put(x, y, pixel1);
            }
        }
    }

    public static void cartoon(Mat img) {
        Mat process = new Mat();

        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGRA2BGR);
        Imgproc.pyrMeanShiftFiltering(img.clone(), img, 25, 40);
        Imgproc.cvtColor(img, process, Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(process, process, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 9, 2);
        Imgproc.cvtColor(process, process, Imgproc.COLOR_GRAY2RGB);

        for (int x = 0; x < img.rows(); x++) {
            for (int y = 0; y < img.cols(); y++) {

                double[] pixel = process.get(x, y);

                if (pixel[0] == 0 && pixel[1] == 0 && pixel[2] == 0) {
                    img.put(x, y, pixel);
                }
            }
        }
    }

    public static void focus(Mat img, Rect region) {
        Mat old = copy(img).submat(region);
        Mat focusRegion = copy(img).submat(region);
        Mat kernel = Imgproc.getStructuringElement(2, new Size(23, 23));

        //pre-process
        Imgproc.cvtColor(focusRegion, focusRegion, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Sobel(focusRegion, focusRegion, -1, 1, 0);
        Imgproc.threshold(focusRegion, focusRegion, 35, 255, Imgproc.THRESH_BINARY);
        Imgproc.morphologyEx(focusRegion, focusRegion, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.GaussianBlur(focusRegion, focusRegion, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);

        //focus
        blur(img, 9);
        Mat copy = img.submat(region);

        for (int x = 0; x < copy.rows(); x++) {
            for (int y = 0; y < copy.cols(); y++) {

                double[] pixel = focusRegion.get(x, y);
                if (pixel[0] == 255) {
                    copy.put(x, y, old.get(x, y));
                }
            }
        }
    }

    public static void inversion(Mat img) {
        byte[] buffer = toByteArray(img);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] *= -1;
        }

        img.put(0, 0, buffer);
    }

    public static void inversion(Mat img, Rect region) {
        Mat sub = img.submat(region);
        byte[] buffer = toByteArray(sub);
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] *= -1;
        }

        sub.put(0, 0, buffer);
    }


    /**
     * @param propertyValue: 当前要使用的property的值
     * @return 以新的数组返回（其中的元素是property的值）
     * @Description: 为了让栈里保存的是新的数组，而不仅仅是对currentPropertyValue数组的引用
     * @author: 张旖霜
     * @date 2023/11/30
     * @version: 1.0
     */
    public static int[] copyPropertyValue(int[] propertyValue) {
        int[] newPropertyValue = new int[10];
        System.arraycopy(propertyValue, 0, newPropertyValue, 0, 6);
        return newPropertyValue;
    }

    /**
     * @param imgArray: 当前要使用的img数组
     * @return 以新的数组返回（其中的元素是imgArray的值）
     * @Description 为了让栈里保存的是新的数组，而不仅仅是对zoomImg数组的引用
     * @author 张旖霜
     * @date 2023/12/5
     * @version: 1.0
     */
    public static Mat[] copyImgArray(Mat[] imgArray) {
        Mat[] newImgArray = new Mat[12];
        for (int i = 0; i <= 11; i++) {
            newImgArray[i] = copy(imgArray[i]);
        }
        return newImgArray;
    }

    public static Mat copy(Mat img) {
        Mat imgCopy = new Mat();
        img.copyTo(imgCopy);
        return imgCopy;
    }

    public static byte[] toByteArray(Mat mat) {
        int size = (int) (mat.total() * mat.channels());
        byte[] buffer = new byte[size];

        mat.get(0, 0, buffer);

        return buffer;
    }

    public static void blur(Mat mat, int size) {
        if (size % 3 != 0) {
            size += 3;
        }
        Imgproc.GaussianBlur(mat, mat, new Size(size, size), Core.BORDER_DEFAULT);
    }

    public static void blur(Mat mat, int size, Rect region) {
        if (size % 3 != 0) {
            size += 3;
        }
        Imgproc.GaussianBlur(mat.submat(region), mat.submat(region), new Size(size, size), Core.BORDER_DEFAULT);
    }

    public static void grayScale(Mat img, Rect region) {
        Mat sub = img.submat(region);
        Imgproc.cvtColor(sub, sub, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(sub, sub, Imgproc.COLOR_GRAY2BGR);

        sub.copyTo(img.submat(region));
    }

    public static void resize(Mat img) {
        Size size = new Size(img.width() * 0.05, img.height() * 0.05);
        Imgproc.resize(img, img, size);
    }

    public static void grayScale(Mat img) {
        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(img, img, Imgproc.COLOR_GRAY2BGR);
    }

    public static void resize(Mat img, Size size) {
        Imgproc.resize(img, img, size);
    }

    /**
     * @param url 图片地址
     * @return 包含图片信息的Mat类实例化对象
     * @Description 修复了无法打开中文路径图片的问题
     * @author 卢思文
     * @date 2023/11/26
     * @version: 1.1
     */
    public static Mat readImg(String url) {
        byte[] data;
        try {
            data = Files.readAllBytes(Paths.get(url));
            return Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Mat cut(Mat img, Rect region) {
        return img.submat(region);
    }

    public static void paint(int[] color, int width, int height, int x, int y, Mat img) {
        img.submat(new Rect(x, y, width, height)).setTo(new Scalar(color[0], color[1], color[2]));
    }

    /**
     * @param x1      上一个点的x坐标
     * @param y1      上一个点的y坐标
     * @param x2      当前点的x坐标
     * @param y2      当前点的y坐标
     * @param color   画笔颜色
     * @param penSize 画笔粗细
     * @param mat     被画的图片
     * @Description 以画笔的颜色、画笔的粗细在mat上画出当前点和上一个点之间的连线
     * @author 卢思文
     * @date 2023/11/29
     * @version: 1.0
     */
    public static void drawLine(int x1, int y1, int x2, int y2, int[] color, int penSize, Mat mat) {
        Scalar scalarColor = new Scalar(color[0], color[1], color[2]);
        Imgproc.line(mat, new Point(x1, y1), new Point(x2, y2), scalarColor, penSize);
    }

    public static void copyToRegion(Mat img, Mat copy, Rect region) {
        // srcMat.copyTo(dstMat);
        copy.copyTo(img.submat(region));
    }

    public static void cartoon(Mat img, Rect region) {
        cartoon(img.submat(region));
    }

    public static void glitchWave(Mat img, int waveLength, EColor color, Rect region) {
        glitchWave(img.submat(region), waveLength, color);
    }


    /**
     * @param bufImg 要被转换的bufferedImage类型图片
     * @return Mat类型图片
     * @Description bufferedImage类型转换成Mat类型（主要用在writeText）
     * @author 张旖霜
     * @date 2023/11/27
     * @version: 1.0
     */
    public static Mat bufImgToMat(BufferedImage bufImg) {
        byte[] pixels = ((DataBufferByte) bufImg.getRaster().getDataBuffer())
                .getData();

        // Create a Matrix the same size of image
        Mat image = new Mat(bufImg.getHeight(), bufImg.getWidth(), CvType.CV_8UC3);
        // Fill Matrix with image values
        image.put(0, 0, pixels);

        return image;
    }

    public static Rect getRect(JComponent c) {
        int x = c.getX();
        int y = c.getY();
        int width = c.getWidth();
        int height = c.getHeight();

        return new Rect(x, y, width, height);
    }
}
