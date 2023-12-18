package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static com.buaa.PhotoEditor.window.Constant.ORIGINAL_SIZE_COUNTER;

/**
 * @author 卢思文、张旖霜
 * @version 2.0
 * @Description 1. 解决了添加widget图片不能为中文的问题，并且支持矢量图
 * 2. 增添了自由改变widget尺寸的功能及对应的光标显示
 * 3. 增添了移动widget的时候的光标显示
 * @date 2023/12/1
 */
public class Widget {

    public Window window;
    public JLabel widgetLabel;
    public ImageIcon widgetIcon;
    public JMenuItem widgetItem;
    public JLabel selectedWidgetLabel;
    public final List<String> WIDGET_SUPPORT_FILE_TYPES;
    public String widgetPath;

    public boolean flag;
    public boolean threadStartFlag;

    public Widget(Window window) {
        this.window = window;
        widgetItem = new javax.swing.JMenuItem("Widget");
        WIDGET_SUPPORT_FILE_TYPES = new ArrayList<>();
        threadStartFlag = true;
        widgetLabel = new JLabel();
        WIDGET_SUPPORT_FILE_TYPES.add("JPG");
        WIDGET_SUPPORT_FILE_TYPES.add("JPEG");
        WIDGET_SUPPORT_FILE_TYPES.add("PNG");

        widgetItem.addActionListener(this::addWidget);
    }

    /**
     * @param evt 触发事件
     * @Description 增加小组件
     * @author 张旖霜
     * @date 2023/12/2
     */
    public void addWidget(ActionEvent evt) {
        //如果未选择图片，弹窗提示并return
        if (window.originalImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            window.tool.getRegion().removeRegionSelected(i);
        }
        window.tool.getRegion().disableListeners();

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(window)
                == JFileChooser.APPROVE_OPTION) {

            widgetPath = fileChooser.getSelectedFile().getAbsolutePath();
            if (WIDGET_SUPPORT_FILE_TYPES.contains(widgetPath
                    .substring(widgetPath.lastIndexOf(".") + 1)
                    .toUpperCase())) {
                Mat widget = MatUtil.readImg(widgetPath);
                widgetIcon = new ImageIcon(widgetPath);

                /*
                    增加防范措施，防止小组件的大小大过背景图片
                 */
                if (widgetIcon.getIconWidth() > window.size[window.counter][0]
                        || widgetIcon.getIconHeight() > window.size[window.counter][1]) {
                    JOptionPane.showMessageDialog(null,
                            "Please select a widget smaller than the photo");
                    widgetIcon = null;
                    return;
                }
                window.saveFlag = false;
                widgetLabel = new JLabel(widgetIcon);

                addWidgetListener(widgetIcon);
                widgetLabel.setBounds((window.panel.getWidth() - widget.width()) / 2,
                        (window.panel.getHeight() - widget.height()) / 2,
                        widget.width(), widget.height());
                window.panel.setLayout(null);
                window.panel.add(widgetLabel);

                // 设定各小组件优先级
                window.panel.setComponentZOrder(widgetLabel, 0);
                window.panel.revalidate();
                window.panel.repaint();
            } else {
                JOptionPane.showMessageDialog(null,
                        "only support JPG, JPEG and PNG");
            }
        }
    }

    public void removeWidget() {
        window.panel.setLayout(null);
        window.panel.remove(selectedWidgetLabel);
        window.panel.repaint();
        window.panel.revalidate();
    }

    public void addWidgetListener(ImageIcon widgetIcon) {
        flag = false;
        int width = widgetIcon.getIconWidth();
        int height = widgetIcon.getIconHeight();
        MouseInputAdapter mia = new MouseInputAdapter() {
            int ex;
            int ey;

            public void mousePressed(MouseEvent e) {
                selectedWidgetLabel = widgetLabel;
                ex = e.getX();
                ey = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                flag = false;
            }

            public void mouseDragged(MouseEvent e) {
                int lx = widgetLabel.getX();
                int ly = widgetLabel.getY();
                int x = e.getX() + lx;
                int y = e.getY() + ly;
                if (isInResizeArea(e, widgetLabel)) {
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
     * @param e           鼠标事件
     * @param widgetLabel 添加的widget的JLabel对象
     * @return boolean 是否在特定区域内
     * @Description 判断光标是否在widget的右下角（这里是改变widget大小的功能触发区）
     * @author 卢思文
     * @date 2023/11/27
     */
    public static boolean isInResizeArea(MouseEvent e, JLabel widgetLabel) {
        int rx = widgetLabel.getWidth();
        int ry = widgetLabel.getHeight();
        int x = e.getX();
        int y = e.getY();
        return rx - 20 < x && x < rx + 20 && ry - 20 < y && y < ry + 20;
    }

}