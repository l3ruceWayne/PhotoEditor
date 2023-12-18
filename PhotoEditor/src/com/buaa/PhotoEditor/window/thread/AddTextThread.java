package com.buaa.PhotoEditor.window.thread;

import static com.buaa.PhotoEditor.util.MatUtil.*;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.add.Text;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * @author 卢思文
 * @version 1.5
 * @Description 添加文本的多线程操作，实现同时在多张图片上添加文本
 * @date 12/9/2023 5:43 PM
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
        text.okButton.addActionListener(e -> {
            Color color = text.customColorChooser.colorChooser.getColor();
            text.setColor(new Scalar(color.getBlue(), color.getGreen(), color.getRed()));
            text.textColorPanel.setBackground(color);
            if (i == window.counter) {
                text.customColorChooser.dialog.dispose();
            }
            writeText(i);
        });
        /*
         * @Description设置字体大小，设置时文本同时进行显示
         * @author 张旖霜
         * @date 11/27/2023 12:51 PM
         * @version 1.0
         */
        text.addTextSpinner.addChangeListener(evt -> {
            if ((int) text.addTextSpinner.getValue() > 0) {
                textScaleChanged();
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
                    /**
                     * @Description 插入文本时，文本同步显示
                     * @author 卢思文、张旖霜
                     * @date 12/11/2023 8:52 PM
                     */
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        text.setStr(text.textField.getText());
                        window.zoomImg[i] = copy(matForAddText);
                        writeText(i);
                    }

                    /**
                     * @Description 删除文本时，文本同步显示
                     * @author 卢思文、张旖霜
                     * @date 12/11/2023 8:52 PM
                     */
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        text.setStr(text.textField.getText());
                        window.zoomImg[i] = copy(matForAddText);
                        writeText(i);
                    }

                    /**
                     * @Description 改变文本，文本同步显示
                     * @author 卢思文、张旖霜
                     * @date 12/11/2023 8:53 PM
                     */
                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        text.setStr(text.textField.getText());
                        window.zoomImg[i] = copy(matForAddText);
                        writeText(i);
                    }
                });
    }

    /**
     * @param i 是有关于zoomImg[]的下标索引
     * @Description 根据字体的设置，在图片上写入文字
     * @author 张旖霜、卢思文
     * @date 11/27/2023 12:52 PM
     */
    public void writeText(int i) {
        int x = window.tool.getRegion().selectedRegionLabel[i].getX();
        int y = window.tool.getRegion().selectedRegionLabel[i].getY();
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
        window.saveFlag = false;
        MatUtil.show(window.zoomImg[i], window.showImgRegionLabel);
    }

    /**
     * @Description 获取改变后的字号大小
     * @author 张旖霜
     * @date 11/27/2023 12:52 PM
     */
    public void textScaleChanged() {
        text.setScale((int) text.addTextSpinner.getValue());
    }
}