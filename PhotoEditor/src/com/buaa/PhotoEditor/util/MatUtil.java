/*
Authors: Igor Joaquim dos Santos Lima
GitHub: https://github.com/igor036
 */
package com.buaa.PhotoEditor.util;

import com.buaa.PhotoEditor.modal.EColor;
import com.buaa.PhotoEditor.modal.ESloopFaceDirection;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;


import com.buaa.PhotoEditor.window.Window;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public abstract class MatUtil extends JFrame {

    private static final int ADJUSTMENT_X_WIDTH_GLASSES = 30;

    /* URL IMAGES
     *-------------------------------------------------------->
     */
    //dog
    public static final String DOG_SNOUT = "img\\Dog\\dog_snout.png";
    public static final String DOG_LEFT_EAR = "img\\Dog\\dog_left_ear.png";
    public static final String DOG_RIGHT_EAR = "img\\Dog\\dog_right_ear.png";

    //glasses
    public static final String GLASSES_1 = "img\\glasses\\glasses_1.png";
    public static final String GLASSES_2 = "img\\glasses\\glasses_2.png";

    //vhs
    public static final String VHS_1 = "img\\VHS\\1.jpg";
    public static final String VHS_2 = "img\\VHS\\2.jpg";
    public static final String VHS_3 = "img\\VHS\\3.jpg";
    public static final String VHS_4 = "img\\VHS\\4.jpg";

    //vhs date
    public static final String VHS_DATE_1 = "img\\VHS\\vhs_date1.png";
    public static final String VHS_DATE_2 = "img\\VHS\\vhs_date2.jpg";

    /*-------------------------------------------------------->
     *END URL IMAGES
     */
    /**
    * @param x : u时的坐标值
    * @param i : 转换成i时的坐标值
    * @return
    * @Description:
    * @author: 卢思文
    * @date: 12/1/2023 4:15 PM
    * @version: 1.0
    **/
    public static int getValueAfterZoom(Window window, double x, int i){
        if(i == window.counter)return (int)x;
        double iWidth = window.zoomImg[i].width();
        double uWidth = window.zoomImg[window.counter].width();
        return (int)Math.round(x * iWidth / uWidth);
    }
    public static void show(Mat img, String title) {

        JLabel lbImg = new JLabel("");
        JFrame window = new JFrame(title);
        ImageIcon imgIcon = new ImageIcon(bufferedImg(img));

        lbImg.setIcon(imgIcon);
        window.setBounds(0, 0, (int) img.size().width, (int) img.size().height);
        window.add(lbImg);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);

    }

    // 将图片img显示在页lbImg上
    public static void show(Mat img, JLabel jLabel) {
//        jLabel.setBounds(10, 10, (int) img.size().width, (int) img.size().height);
        // pending
        ImageIcon imgIcon = new ImageIcon(bufferedImg(img));
        jLabel.setIcon(imgIcon);
    }
    /* Mat类型的img转化为BufferedImage类型的对象
    为什么要转化？因为Mat类型的对象适合图像处理，但是不适合展示处理好的结果
    而BufferedImage则相反
    所以在利用Mat处理完图像之后，转化为BufferedImage进行展示
    此方法不能用来处理矢量图片
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

    public static void dog(Mat img) {

        Rect[] faces = Detection.rectOfFace(img);

        Mat dog_snout = readImg(DOG_SNOUT);
        Mat dog_left_ear = readImg(DOG_LEFT_EAR);
        Mat dog_right_ear = readImg(DOG_RIGHT_EAR);

        for (Rect fr : faces) {

            Mat face = img.submat(fr);

            int width = (int) (fr.width * 0.35);
            int height = (int) (fr.height * 0.35);

            int x = fr.x;
            int y = Math.abs(fr.y - (int) (fr.width * 0.15));
            double slopOfFace = Detection.slopOfFace(face);

            Size size = new Size(width, height);

            //resize
            Imgproc.resize(dog_left_ear, dog_left_ear, size);
            Imgproc.resize(dog_right_ear, dog_right_ear, size);
            Imgproc.resize(dog_snout, dog_snout, size);

            //rotation
            ESloopFaceDirection direction = ESloopFaceDirection.get(slopOfFace);

            if (!direction.isMid()) {
                rotate(dog_left_ear, slopOfFace);
                rotate(dog_right_ear, slopOfFace);
                rotate(dog_snout, slopOfFace);
            }

            //right ear
            int xOverlayRightEar = x;
            int yOverlayRightEar = y;

            Mat region_right_ear = img.submat(new Rect(xOverlayRightEar, yOverlayRightEar, width, height));
            overlay(region_right_ear, dog_right_ear);

            //left ear
            int xOverlayLeftEar = (int) (x + (fr.width * 0.7) + slopOfFace * direction.getInvFlag());
            int yOverlayLeftEar = yOverlayRightEar + (int) (fr.height * 0.1 * direction.getInvFlag());

            Mat region_left_ear = img.submat(new Rect(xOverlayLeftEar, yOverlayLeftEar, width, height));
            overlay(region_left_ear, dog_left_ear);

            //snout       
            int xOverlaySnout = fr.x + Detection.anchorPointX(face, slopOfFace, direction);
            int yOverlaySnout = fr.y + Detection.anchorPointY(face);

            Mat region_snout = img.submat(new Rect(xOverlaySnout, yOverlaySnout, width, height));
            overlay(region_snout, dog_snout);
        }
    }

    public static void rotate(Mat img, double angle) {

        Point center = new Point(img.cols() / 2, img.rows() / 2);
        Mat rot_mat = Imgproc.getRotationMatrix2D(center, angle, 1);

        Imgproc.warpAffine(img, img, rot_mat, img.size(), Imgproc.INTER_CUBIC);
        Imgproc.warpAffine(img, rot_mat, rot_mat, rot_mat.size());
    }

    public static void glasses(Mat img, String png) {

        if (!fileExist(png))
            throw new RuntimeException("Mascara GLASSES não encontrada!");


        Mat sub = readImg(png);
        Rect[] faces = Detection.rectOfFace(img);

        for (Rect fr : faces) {

            Rect[] eyes = Detection.rectsOfEyes(img);
            Rect leftEye = eyes[1];

            Point a = new Point(fr.x + ADJUSTMENT_X_WIDTH_GLASSES, leftEye.y);
            Point b = new Point(fr.x + fr.width - ADJUSTMENT_X_WIDTH_GLASSES, leftEye.y + leftEye.height);
            Rect eyesRegion = new Rect(a, b);

            double sloopOfFace = Detection.slopOfFace(img.submat(eyesRegion));
            Mat face = img.submat(eyesRegion);

            Imgproc.resize(sub, sub, face.size());
            rotate(sub, sloopOfFace);
            overlay(face, sub);
        }
    }

    public static void widget(Mat img, Mat widget, int wx, int wy) {

        final int width = wx + widget.width() > img.width() ? img.width() : wx + widget.width();
        final int heigth = wy + widget.height() > img.height() ? img.height() : wy + widget.height();

        Point a = new Point(wx, wy);
        Point b = new Point(width, heigth);

        Mat widgetRegion = img.submat(new Rect(a, b));

        overlay(widgetRegion, widget);
    }

    // pending
    // widget覆盖img
    public static void overlay(Mat img, Mat widget) {

        for (int x = 0; x < widget.rows(); x++) {
            for (int y = 0; y < widget.cols(); y++) {

                double[] pixel1 = img.get(x, y);
                double[] pixel2 = widget.get(x, y);

                //PNG WITH OPACITY
                if (pixel2.length == 4) {

                    double alpha = pixel2[3] / 255f;

                    for (int i = 0; i < 3; i++)
                        pixel1[i] = pixel2[i] * alpha + pixel1[i] * (1 - alpha);

                } //PHOTO WITHOUT OPACITY
                else {
                    for (int i = 0; i < 3; i++) {
                        pixel1[i] = pixel2[i];
                    }
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


    public static void sumMat(Mat img, Mat mask) {

        Imgproc.resize(mask, mask, img.size());

        for (int x = 0; x < img.rows(); x++) {
            for (int y = 0; y < img.cols(); y++) {

                double[] pixel1 = img.get(x, y);
                double[] pixel2 = mask.get(x, y);

                for (int rgb = 0; rgb < 3; rgb++)
                    pixel1[rgb] += pixel2[rgb];

                img.put(x, y, pixel1);
            }
        }
    }

    public static void noise(Mat img, int noise) {

        if (noise != 0) {

            Random random = new Random();

            for (int x = 0; x < img.rows(); x++) {
                for (int y = 0; y < img.cols(); y++) {

                    double[] pixel = img.get(x, y);
                    int value = random.nextInt(noise);

                    for (int i = 0; i < pixel.length; i++)
                        pixel[i] += value;

                    img.put(x, y, pixel);

                }
            }
        }
    }

    public static void cartoon(Mat img) {

        Mat process = new Mat();
        Mat hierarchy = new Mat();

        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGRA2BGR);
        Imgproc.pyrMeanShiftFiltering(img.clone(), img, 25, 40);
        Imgproc.cvtColor(img, process, Imgproc.COLOR_RGB2GRAY);
        Imgproc.adaptiveThreshold(process, process, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 9, 2);
        Imgproc.cvtColor(process, process, Imgproc.COLOR_GRAY2RGB);

        for (int x = 0; x < img.rows(); x++) {
            for (int y = 0; y < img.cols(); y++) {

                double[] pixel = process.get(x, y);

                if (pixel[0] == 0 && pixel[1] == 0 && pixel[2] == 0)
                    img.put(x, y, pixel);
            }
        }
    }

    public static void sepia(Mat img) {

        /* R = pixel[2] / tr = 0.393R + 0.769G + 0.189B
         * G = pixel[1] / tg = 0.349R + 0.686G + 0.168B
         * B = pixel[0] / tb = 0.0272R + 0.534G + 0.131B
         */

        for (int x = 0; x < img.rows(); x++) {
            for (int y = 0; y < img.cols(); y++) {


                double R = img.get(x, y)[2],
                        G = img.get(x, y)[1],
                        B = img.get(x, y)[0];

                double[] data = {
                        0.272 * R + 0.534 * G + 0.131 * B,
                        0.349 * R + 0.686 * G + 0.168 * B,
                        0.393 * R + 0.769 * G + 0.189 * B
                };

                img.put(x, y, data);
            }
        }
    }

    public static void lilac(Mat img, boolean[] colors) {

        for (int x = 0; x < img.rows(); x++) {
            for (int y = 0; y < img.cols(); y++) {


                double b = img.get(x, y)[0],
                        g = img.get(x, y)[1],
                        r = img.get(x, y)[2];

                double[] data = {
                        colors[0] ? 0.5f * (255 + g + r) : b,
                        colors[1] ? 0.25 * (510 + r + g - 2 * b) : g,
                        colors[2] ? 0.5f * 1.0f / 3.0f * (r + g + b) : r
                };

                img.put(x, y, data);
            }
        }
    }


    public static void contrastAndBrightness(Mat img, double alpha, double beta) {

        alpha *= 0.4;
        beta *= 0.4;

        for (int x = 0; x < img.rows(); x++) {
            for (int y = 0; y < img.cols(); y++) {

                double[] pixel = img.get(x, y);

                for (int i = 0; i < 3; i++)
                    pixel[i] = (int) (alpha * (pixel[i] + beta));

                img.put(x, y, pixel);
            }
        }
    }


    public static void saturation(Mat img, double saturation) {

        Mat hsv = new Mat();
        // 将img转换为HSV色彩空间，并存储到‘hsv’中
        Imgproc.cvtColor(img, hsv, Imgproc.COLOR_RGB2HSV);

        for (int x = 0; x < hsv.rows(); x++) {
            for (int y = 0; y < hsv.cols(); y++) {
                // 获取x,y处的像素值，这是一个数组，分别包括色调、饱和度、亮度
                double[] pixel = hsv.get(x, y);
                // 计算新的饱和度
                double newSaturation = pixel[1] + saturation;
                if (newSaturation <= 255) {
                    pixel[1] = newSaturation;
                    // 更新后的像素值放回hsv中
                    hsv.put(x, y, pixel);
                }
            }
        }
        Imgproc.cvtColor(hsv, img, Imgproc.COLOR_HSV2RGB);
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
                if (pixel[0] == 255)
                    copy.put(x, y, old.get(x, y));
            }
        }
    }

    public static void inversor(Mat img) {

        byte[] buffer = toByteArray(img);

        for (int i = 0; i < buffer.length; i++)
            buffer[i] *= -1;

        img.put(0, 0, buffer);
    }

    public static void inversor(Mat img, Rect region) {

        Mat sub = img.submat(region);
        byte[] buffer = toByteArray(sub);

        for (int i = 0; i < buffer.length; i++)
            buffer[i] *= -1;

        sub.put(0, 0, buffer);
    }


    /*
    * @param propertyValue: 当前要使用的property的值
    * @return 以新的数组返回（其中的元素是property的值）
    * @Description: 为了让栈里保存的是新的数组，而不仅仅是对currentPropertyValue数组的引用
    * @author: 张旖霜
    * @date: 11/30/2023 5:35 PM
    * @version: 1.0
    */
    public static int[] copyPropertyValue(int[] propertyValue)
    {
        int[] newPropertyValue = new int[10];
        for (int i=0; i<=5; i++)
        {
            newPropertyValue[i] = propertyValue[i];
        }
        return newPropertyValue;
    }
    /*
     * @param imgArray: 当前要使用的img数组
     * @return 以新的数组返回（其中的元素是imgArray的值）
     * @Description:为了让栈里保存的是新的数组，而不仅仅是对zoomImg数组的引用
     * @author: 张旖霜
     * @date: 12/5/2023 10:53 PM
     * @version: 1.0
     */
    public static Mat[] copyImgArray(Mat[] imgArray)
    {
        Mat[] newImgArray = new Mat[12];
        for (int i=0; i<=11; i++)
        {
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

    public static void vhs(Mat img, String VHS) {

        if (!fileExist(VHS))
            throw new RuntimeException("Mascara VHS não encontrada!");

        Mat vhs = readImg(VHS);
        sumMat(img, vhs);
    }

    public static void blur(Mat mat, int size) {

        if (size % 3 != 0)
            size += 3;

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

    public static void morphology(Mat img, int morph_size) {

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(morph_size, morph_size));
        Imgproc.morphologyEx(img, img, Imgproc.MORPH_OPEN, element);

    }

    public static void morphology(Mat img, int morph_size, Rect region) {

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(morph_size, morph_size));
        Imgproc.morphologyEx(img.submat(region), img.submat(region), Imgproc.MORPH_OPEN, element);

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

    public static void delete(Mat img, Rect region) {
        img.submat(region).setTo(new Scalar(0, 0, 0));
    }

    public static void delete(Mat img, int x, int y, int size) {
        Rect region = new Rect(x, y, size, size);
        img.submat(region).setTo(new Scalar(0, 0, 0));
    }
    /**
    * @param url : 图片地址
    * @return mat : 包含图片信息的Mat类实例化对象
    * @Description: 修复了无法打开中文路径图片的问题
    * @author: 卢思文
    * @date: 11/26/2023 8:56 PM
    * @version: 1.1
    **/
    public static Mat readImg(String url) {
        byte[] data = null;
        try {
            data = Files.readAllBytes(Paths.get(url));
            return Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean fileExist(String url) {
        return new File(url).exists();
    }

    public static Mat cut(Mat img, Rect region) {
        return img.submat(region);
    }

    public static void save(String path, Mat img) {
        Imgcodecs.imwrite(path, img);
    }

    public static void paint(int[] color,
                             int width,
                             int height,
                             int x,
                             int y,
                             Mat img) {
        img.submat(new Rect(x, y, width, height))
                .setTo(new Scalar(color[0], color[1], color[2]));

    }
    /**
    * @param x1 : 上一个点的x坐标
    * @param y1 : 上一个点的y坐标
    * @param x2 : 当前点的x坐标
    * @param y2 : 当前点的y坐标
    * @param color : 画笔颜色
    * @param penSize : 画笔粗细
    * @param mat : 被画的图片
    * @return
    * @Description: 以画笔的颜色、画笔的粗细在mat上画出当前点和上一个点之间的连线
    * @author: 卢思文
    * @date: 11/29/2023 4:12 PM
    * @version: 1.0
    **/
    public static void drawLine(int x1, int y1, int x2, int y2, int[] color, int penSize, Mat mat) {
        Scalar scalarColor = new Scalar(color[0], color[1], color[2]);
        Imgproc.line(mat, new Point(x1, y1), new Point(x2, y2), scalarColor, penSize);

    }

    public static double[] pixel(Mat img, int x, int y) {
        return img.get(x, y);
    }

    public static void copyToRegion(Mat img, Mat copy, Rect region) {
        // srcMat.copyTo(dstMat);
        // 下面的代码会导致img的region区域的内容被copy覆盖，而img的其他区域内容不变
        copy.copyTo(img.submat(region));
    }

    public static void cartoon(Mat img, Rect region) {
        cartoon(img.submat(region));
    }

    public static void glitchWave(Mat img, int waveLength, EColor color, Rect region) {
        glitchWave(img.submat(region), waveLength, color);
    }

    public static void noise(Mat img, int noise, Rect region) {
        noise(img.submat(region), noise);
    }

    public static void sepia(Mat img, Rect region) {
        sepia(img.submat(region));
    }


//    public static void writeText(Text text, Mat img, Rect region ) {
//        img = img.submat(region);
//        Point point = new Point(0, text.getScale()*25);
//        Imgproc.putText(img, text.getStr(), point, Core.FONT_HERSHEY_SIMPLEX, text.getScale(), text.getColor(),text.getScale());
//    }


    /*
    * @param bufImg: 要被转换的bufferedImage类型图片
    * @return Mat类型图片
    * @Description:bufferedImage类型转换成Mat类型（主要用在writeText）
    * @author: 张旖霜
    * @date: 11/27/2023 12:45 PM
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


    public static void contrastAndBrightness(Mat img, double alpha, double beta, Rect region) {
        contrastAndBrightness(img.submat(region), alpha, beta);
    }

    public static void saturation(Mat img, double saturation, Rect region) {
        saturation(img.submat(region), saturation);
    }

    public static Rect getRect(JComponent c) {

        int x = c.getX();
        int y = c.getY();

        int width = c.getWidth();
        int height = c.getHeight();

        return new Rect(x, y, width, height);
    }
}
