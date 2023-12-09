package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
/**
* @Description:
 * 1. 解决了添加widget图片不能为中文的问题，并且支持矢量图
 * 2. 增添了自由改变widget尺寸的功能及对应的光标显示
 * 3. 增添了移动widget的时候的光标显示
* @author: 卢思文
* @date: 11/27/2023 4:35 PM
* @version: 1.0
**/
public class Widget {

    public Window window;
    public JMenuItem widgetItem;
    public JLabel selectedWidgetLabel;

    public final List<JLabel> widgetLabelList;
    public final List<String> WIDGET_SUPPORT_FILE_TYPES;
    public String widgetPath;

    public boolean flag;


    public Widget(Window window) {
        this.window = window;
        widgetItem = new javax.swing.JMenuItem("Widget");
        widgetLabelList = new ArrayList<>();
        WIDGET_SUPPORT_FILE_TYPES = new ArrayList<>();


        WIDGET_SUPPORT_FILE_TYPES.add("JPG");
        WIDGET_SUPPORT_FILE_TYPES.add("JPEG");
        WIDGET_SUPPORT_FILE_TYPES.add("PNG");

        widgetItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWidget(evt);
            }
        });
    }

    public void addWidget(ActionEvent evt) {

        window.tool.region.disableListeners();

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(window)
                == JFileChooser.APPROVE_OPTION) {
            widgetPath = fileChooser.getSelectedFile().getAbsolutePath();
            // pending
            if (WIDGET_SUPPORT_FILE_TYPES.contains(widgetPath
                    .substring(widgetPath.lastIndexOf(".") + 1)
                    .toUpperCase())) {

                Mat widget = MatUtil.readImg(widgetPath);
                ImageIcon widgetIcon = new ImageIcon(widgetPath);
                JLabel widgetLabel = new JLabel(widgetIcon);

                addWidgetListener(widgetLabel, widgetIcon);

                widgetLabel.setBounds(window.getX() / 2, window.getY() / 2,
                        widget.width(), widget.height());

                window.panel.setLayout(null);
                window.panel.add(widgetLabel);


                // 设定各小组件优先级
                window.panel.setComponentZOrder(widgetLabel, 0);
                for (int i = 0; i < widgetLabelList.size(); i++) {
                    window.panel.setComponentZOrder(widgetLabelList.get(i), i + 1);
                }
                window.panel.setComponentZOrder(window.showImgRegionLabel,
                        widgetLabelList.size() + 1);
                window.panel.revalidate();
                window.panel.repaint();
                widgetLabelList.add(widgetLabel);


            } else {
                JOptionPane.showMessageDialog(null,
                        "only support JPG, JPEG and PNG");
            }
        }
    }

    public void removeWidget() {
        widgetLabelList.remove(selectedWidgetLabel);
        window.panel.setLayout(null);
        window.panel.remove(selectedWidgetLabel);
        window.panel.repaint();
        window.panel.revalidate();
    }

    public void addWidgetListener(JLabel widgetLabel, ImageIcon widgetIcon) {
        flag = false;
//        Window win = this;
        int width = widgetIcon.getIconWidth();
        int height = widgetIcon.getIconHeight();
        MouseInputAdapter mia = new MouseInputAdapter() {
            int ex;
            int ey;

            public void mousePressed(MouseEvent e) {
                selectedWidgetLabel = widgetLabel;
                ex = e.getX();
                ey = e.getY();

//                widgetLabel.setBorder(BorderFactory.createLineBorder(Color.CYAN));
            }

            public void mouseReleased(MouseEvent e) {
                flag = false;
//                widgetLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE.brighter()));
            }

            public void mouseDragged(MouseEvent e) {

                int lx = widgetLabel.getX();
                int ly = widgetLabel.getY();
                int x = e.getX() + lx;
                int y = e.getY() + ly;
                if (isInResizeArea(e, widgetLabel)) {
//                    widgetLabel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                    int dx = x - lx;
                    double percent = (double) dx / (double) width;
                    // 同比例缩放
                    dx = (int) (percent * width);
                    int dy = (int) (percent * height);
                    Image originalImage = widgetIcon.getImage();
                    Image scaledImage = originalImage.getScaledInstance(
                            dx,
                            dy,
                            Image.SCALE_SMOOTH
                    );
                    widgetLabel.setIcon(new ImageIcon(scaledImage));
                    widgetLabel.setBounds(lx, ly, dx, dy);
                } else if (!flag) {
                    widgetLabel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    widgetLabel.setLocation(x - ex, y - ey);
                }
                window.panel.revalidate();
                window.panel.repaint();
            }

            public void mouseEntered(MouseEvent e) {
                if (isInResizeArea(e, widgetLabel)) {
                    widgetLabel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!isInResizeArea(e, widgetLabel)) {
                    widgetLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            public void mouseMoved(MouseEvent e) {
                if (isInResizeArea(e, widgetLabel)) {
                    widgetLabel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));

                } else {
                    widgetLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

                }
            }
        };

        widgetLabel.addMouseListener(mia);
        widgetLabel.addMouseMotionListener(mia);

    }
    /**
    * @param e 鼠标事件
    * @param widgetLabel 添加的widget的JLabel对象
    * @return boolean 是否在特定区域内
    * @Description: 判断光标是否在widget的右下角（这里是改变widget大小的功能触发区）
    * @author: 卢思文
    * @date: 11/27/2023 4:38 PM
    * @version: 1.0
    **/
    public static boolean isInResizeArea(MouseEvent e, JLabel widgetLabel) {
        int rx = widgetLabel.getWidth();
        int ry = widgetLabel.getHeight();
        int x = e.getX();
        int y = e.getY();
        if (rx - 20 < x && x < rx + 20 && ry - 20 < y && y < ry + 20) {
            return true;
        }
        return false;
    }

    public void removeWidgetSelection() {

        if (selectedWidgetLabel != null) {
            selectedWidgetLabel.setBorder(null);
            selectedWidgetLabel = null;
        }
    }
}