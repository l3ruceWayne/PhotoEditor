package com.buaa.PhotoEditor.window.thread;


import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;
import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Constant;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.add.Text;

import static com.buaa.PhotoEditor.window.Constant.*;

import com.buaa.PhotoEditor.window.tool.ZoomIn;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.features2d.ORB;

import javax.print.attribute.standard.OrientationRequested;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.SocketTimeoutException;
import java.nio.channels.NonReadableChannelException;

/**
 * ClassName: AddTextThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description:
 *
 * @Author 卢思文
 * @Create 12/9/2023 5:43 PM
 * @Version 1.0
 */
public class AddTextThread extends Thread {
    public Window window;
    public Text text;
    public int i;
    public Mat matForAddText;
    public AddTextThread(Window window, Text text, int i) {
        matForAddText = new Mat();
        this.window = window;
        this.text = text;
        this.i = i;
    }

    @Override
    public void run() {
        text.addTextDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                matForAddText = copy(window.zoomImg[i]);
            }
        });
        text.OKButton.addActionListener(e -> {
            Color color = text.customColorChooser.colorChooser.getColor();
            text.setColor(new Scalar(color.getBlue(), color.getGreen(), color.getRed()));
            text.textColorPanel.setBackground(color);
            if(i == window.counter){
                text.customColorChooser.dialog.dispose();
            }
            writeText(i);
        });
        /*
         * @param:
         * @return
         * @Description:设置条件，字体大小不能小于等于0
         * @author: 张旖霜
         * @date: 11/27/2023 12:51 PM
         * @version: 1.0
         */
        text.addTextSpinner.addChangeListener(evt -> {
            if ((int) text.addTextSpinner.getValue() > 0) {
                textScaleStateChanged();
            } else {
                text.addTextSpinner.setValue(1);
            }
            /*
             等实现了undo之后，如果改变字号大小，利用undo一次+重新绘制新字号的string
             来实现实时改变字号大小
             */
            window.zoomImg[i] = copy(matForAddText);
            writeText(i);
        });
        text.textField.getDocument()
                .addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        text.setStr(text.textField.getText());
                        window.zoomImg[i] = copy(matForAddText);
                        writeText(i);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        text.setStr(text.textField.getText());
                        window.zoomImg[i] = copy(matForAddText);
                        writeText(i);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        text.setStr(text.textField.getText());
                        window.zoomImg[i] = copy(matForAddText);
                        writeText(i);

                    }
                });
    }

    /*
     * @param:
     * @return
     * @Description:根据字体的设置，在图片上写入文字（之前用的是putText方法，但是这个方法不支持中文，所以只能用drawString方法，
     *   将文字“画”在图片上）
     * @author: 张旖霜
     * @date: 11/27/2023 12:52 PM
     * @version: 1.0
     */
    public void writeText(int i) {
        int x = window.tool.region.selectedRegionLabel[i].getX();
        int y = window.tool.region.selectedRegionLabel[i].getY();


        BufferedImage bufImg = MatUtil.bufferedImg(window.zoomImg[i]);
        int size = getValueAfterZoom(window, text.getScale() * 25, i);
        Font font = new Font("simhei", Font.PLAIN, size);

        Graphics g = bufImg.getGraphics();

        g.setFont(font);
        g.setColor(new Color((int) text.getColor().val[2], (int) text.getColor().val[1], (int) text.getColor().val[0]));
        g.drawString(text.getStr(), x + 5, y + size);

        g.dispose();
        window.zoomImg[i] = MatUtil.bufImgToMat(bufImg);
        if (i != window.counter) {
            return;
        }
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
    }

    public void textScaleStateChanged() {
        text.setScale((int) text.addTextSpinner.getValue());
    }
}
