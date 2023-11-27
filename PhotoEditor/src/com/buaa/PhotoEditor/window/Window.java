/*
Authors: Igor Joaquim dos Santos Lima
GitHub: https://github.com/igor036
 */
package com.buaa.PhotoEditor.window;

import com.buaa.PhotoEditor.Main;
import com.buaa.PhotoEditor.util.MatUtil;

import java.awt.*;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.*;
import javax.swing.event.*;

import com.buaa.PhotoEditor.window.edit.Edit;
import com.buaa.PhotoEditor.window.file.Save;
import com.buaa.PhotoEditor.window.file.MyFile;

import com.buaa.PhotoEditor.window.property.Property;



// add
import com.buaa.PhotoEditor.window.add.Add;
// tool
import com.buaa.PhotoEditor.window.tool.Tool;


import com.buaa.PhotoEditor.window.filter.Filter;

import com.buaa.PhotoEditor.window.layer.Layer;


import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

public class Window extends javax.swing.JFrame {

    public Property property = new Property(this);

    public Save save;

    // tool
    public Tool tool;
    // add
    public Add add;

    public Filter filter;
    /* Layer类的实例化对象只能有一个（因为不同的图层需要共用一个Layer，这样才能显示所有的图层列表）
        所以是static
    * */
    public static Layer layer;

    //control of photo
    public Mat img;              //actually
    public Mat originalImg;
    public String originalImgPath;
    public Mat temp;             //temp
    public Mat zoomImg;
    public static Mat copy;
    public Stack<Mat> last; //ctrl+z
    public Stack<Mat> next;     //ctrl+y`
    public Stack<Integer> isProperty;
    public Stack<Integer> propertyValue;
    public Stack<Integer> nextIsProperty;
    public Stack<Integer> nextPropertyValue;



    //outer's
    public Mat paintingImg;            //image paint
    public Mat nexLayerImg;           //image use to paint



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // 点击工具后出现的菜单栏
    // 一个框架？Img需要和lPhoto相关联


    public JMenuItem blur;

    public boolean flag;
    // 总画板
    // 作用？
    public JPanel panel;
    // 总菜单栏
    public JMenuBar menuBar;
    // 图片显示区域
    public JLabel showImgRegionLabel;

    // options
    public boolean pasting = false;
    public String title;
    public int imgWidth;
    public int imgHeight;



    // Property
    public JSpinner propertyMenuDialogPenSizeSpinner;
    public SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 30, 1);


    public javax.swing.JMenuItem dogMask;
    public javax.swing.Box.Filler filler1;
    public javax.swing.JMenuItem glasses1Mask;

    public javax.swing.ButtonGroup goutTypePen;

    public javax.swing.JMenuItem jMenuItem1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSeparator jSeparator1;
    public javax.swing.JTextArea jTextArea1;


    public javax.swing.JMenuItem morphology;



    public javax.swing.JPanel penColor;
    public javax.swing.JLabel penColorLabel;
    public javax.swing.JLabel penSizeLabel;
    public javax.swing.JCheckBox rbtBlue;
    public javax.swing.JCheckBox rbtGreen;
    public javax.swing.JCheckBox rbtRed;
    public javax.swing.JMenuItem sepia;
    public javax.swing.JTextField txtHeight;
    public javax.swing.JTextField txtWidth;



    public Window(Mat img, String title) {
        this(title);
        this.title  = title;
        this.img = img;
        this.imgWidth = img.width();
        this.imgHeight = img.height();
        MatUtil.show(img, showImgRegionLabel);
        showImgRegionLabel.setSize(img.width(), img.height());
        this.setSize(img.width(), img.height());
        setResizable(true);
        setLocationRelativeTo(null);
    }

    public Window(String title) {

        this.title  = title;
        initComponents();

        addMouseListeners();
        setResizable(true);
        setLocationRelativeTo(null);
        // 按下每个按键会弹出一个对应窗口
        // 设置窗口的大小



        // 撤销和反撤销操作用的栈
        last = new Stack<>();
        next = new Stack<>();
        isProperty = new Stack<>();
        propertyValue = new Stack<>();
        nextIsProperty = new Stack<>();
        nextPropertyValue = new Stack<>();
        this.setTitle(title);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    public void initComponents() {
        panel = new JPanel(); // 一定要先初始化panel，才能调用tool类构造方法
        save = new Save(this);

        // add
        add = new Add(this);
        // tool
        tool = new Tool(this);

        // 只有一个layer，所以layer赋值之后就不再赋值
        if(layer == null){
            layer = new Layer(this);
        }



       

        penSizeLabel = new javax.swing.JLabel();
        propertyMenuDialogPenSizeSpinner = new javax.swing.JSpinner(model);
        penColorLabel = new javax.swing.JLabel();
        penColor = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        goutTypePen = new javax.swing.ButtonGroup();
        rbtGreen = new javax.swing.JCheckBox();
        rbtBlue = new javax.swing.JCheckBox();
        rbtRed = new javax.swing.JCheckBox();

        showImgRegionLabel = new JLabel();
        filler1 = new javax.swing.Box.Filler(new java.
                awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        menuBar = new javax.swing.JMenuBar();

        dogMask = new javax.swing.JMenuItem();
        glasses1Mask = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        blur = new javax.swing.JMenuItem();
        morphology = new javax.swing.JMenuItem();
        sepia = new javax.swing.JMenuItem();

        // GlitchWave是按下Glitch再按下wave后出现的弹窗
        // colors是GlitchWave中的三个按键
        filter = new Filter(this);










        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);




        // 当点击右上角的叉号时进行什么操作
        // pending 未保存的情况下，弹窗
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        showImgRegionLabel.setText("请选择图像");
        // pending
        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(showImgRegionLabel)
                                .addGap(0, 307, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(showImgRegionLabel)
                                .addContainerGap(306, Short.MAX_VALUE))
        );

        MyFile file = new MyFile(this);
        menuBar.add(file.fileMenu);


        

        
        Edit edit = new Edit(this);
        menuBar.add(edit.editMenu);

        

        
        

        menuBar.add(add.addMenu);

        

        




        dogMask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, java.awt.event.InputEvent.CTRL_MASK));
        dogMask.setText("Cachorro");
        dogMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dogMaskActionPerformed(evt);
            }
        });

        glasses1Mask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        glasses1Mask.setText("Óculos 1");
        glasses1Mask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glasses1MaskActionPerformed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Óculos 2");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });








        morphology.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        morphology.setText("Morfologia");
        morphology.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                morphologyActionPerformed(evt);
            }
        });



        sepia.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        sepia.setText("Sépia");
        sepia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sepiaActionPerformed(evt);
            }
        });

        menuBar.add(filter.filterMenu);



        // 有问题的源代码
        //         点击Glitch再点击Wave一栏之后，执行actionPerformed方法，其中第二行是阻塞的方法
//        glitchWave.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                System.out.println("abcd");
//                glitchWaveActionPerformed(evt);
//            }
//        });



        menuBar.add(filter.filterMenu);


        // 点击选择区域之后开始按键监听
//        toolMenuSelectRegionItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                clickToolMenuSelectRegionItem(evt);
//            }
//        });


        menuBar.add(property.propertyMenu);

        
        menuBar.add(layer.layerItem);


        menuBar.add(tool.region.selectRegionItem);
        menuBar.add(tool.pen.penItem);
        menuBar.add(tool.eraser.eraserItem);
        menuBar.add(tool.rotate.rotateItem);
        menuBar.add(tool.zoomIn.zoomInItem);
        menuBar.add(tool.zoomOut.zoomOutItem);
        menuBar.add(tool.drag.dragItem);




        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
        );

        pack();

    }

    public void dogMaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dogMaskActionPerformed

        Mat newImg = MatUtil.copy(img);

        MatUtil.dog(newImg);
        MatUtil.show(newImg, showImgRegionLabel);

        last.push(img);
        img = newImg;
        tool.region.removeRegionSelected();
    }//GEN-LAST:event_dogMaskActionPerformed









    public void glasses1MaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glasses1MaskActionPerformed

        try {

            Mat newImg = MatUtil.copy(img);

            MatUtil.glasses(newImg, MatUtil.GLASSES_1);
            MatUtil.show(newImg, showImgRegionLabel);

            last.push(img);
            img = newImg;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Combinação de efeitos inválida!");
        }
    }//GEN-LAST:event_glasses1MaskActionPerformed

    public void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        try {

            Mat newImg = MatUtil.copy(img);

            MatUtil.glasses(newImg, MatUtil.GLASSES_2);
            MatUtil.show(newImg, showImgRegionLabel);

            last.push(img);
            img = newImg;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Combinação de efeitos inválida!");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed




    public void morphologyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_morphologyActionPerformed

        try {

            int morph_size = Integer.parseInt(JOptionPane.showInputDialog(null, "Tamanho"));

            Mat newImg = MatUtil.copy(img);

            if (tool.region.selectRegionItem.isSelected()) {
                MatUtil.morphology(newImg, morph_size, MatUtil.getRect(tool.region.selectedRegionLabel));
                tool.region.removeRegionSelected();
            } else {
                MatUtil.morphology(newImg, morph_size);
            }
            MatUtil.show(newImg, showImgRegionLabel);

            last.push(img);
            img = newImg;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Tente novamente!");
        }

    }//GEN-LAST:event_morphologyActionPerformed




    // pending ESC
    public void formKeyPressed(KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {

            if (tool.region.selectRegionItem.isSelected()) {
                tool.region.removeRegionSelected();
            } else if (add.widget.selectedWidgetLabel != null) {
                add.widget.removeWidget();
            } else if (pasting) {
                pasting = false;
                disablePasteMode();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_F12) {
            MatUtil.show(nexLayerImg, "");
        }
    }






    public void penColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_penColorMouseClicked
        // 阻塞式选择画笔颜色
        // 窗口弹出后一直到选好颜色点击确定，才会执行下一行代码
        Color color = JColorChooser
                .showDialog(null,
                        "选择颜色",
                        // 默认颜色为黑色
                        Color.BLACK);

        penColor.setBackground(color);

    }



    public void sepiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sepiaActionPerformed

        Mat newImg = MatUtil.copy(img);

        if (tool.region.selectRegionItem.isSelected()) {

            MatUtil.sepia(newImg, MatUtil.getRect(tool.region.selectedRegionLabel));
            tool.region.removeRegionSelected();

        } else
            MatUtil.sepia(newImg);


        last.push(img);
        img = newImg;
        MatUtil.show(img, showImgRegionLabel);

    }//GEN-LAST:event_sepiaActionPerformed





    public void addMouseListeners() {

        panel.addMouseListener(new MouseAdapter() {

            // pending
            @Override
            public void mouseClicked(MouseEvent e) {
                // pending 下面的代码是让组件边界高亮消失的，但是我把组件边界高亮取消了
                // 所以现在没必要
//                removeWidgetSelection();
                if (pasting) {
                    paste();
                }
            }

//            @Override
//            public void mousePressed(MouseEvent e) {
//                if (region.selectRegionItem.isSelected()) {
//                    region.addRegion(e.getPoint());
//                }
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                // pending
//                /*
//                画笔的时候，鼠标按下->拖拽->松开是一个画画行为的完成，当松开的时候我们将上一个状态入栈，然后更改img
//                 */
//                if (pen.penItem.isSelected()
//                        || eraser.eraserItem.isSelected()) {
//                    last.add(img);
//                    if (paintingImg != null) {
//                        img = MatUtil.copy(paintingImg);
//                    }
//                    // 下面这行代码是必须的，这样可以保证每次绘画的时候是在现在图像的基础上进画
//                    // 如果没有这行代码，paintImg保持的只是上一个画好的状态，如果之后做了其他操作，将不会显示
//                    paintingImg = null;
//                }
//            }

        });

        panel.addMouseMotionListener(new MouseAdapter() {
//            // 拖拽的时候将会一直调用该方法
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                if (region.selectRegionItem.isSelected()) {
//                    region.setRegionSize(e.getX(), e.getY());
//                    // pending
//                } else if (pen.penItem.isSelected()) {
//                    penSize = (Integer) propertyMenuDialogPenSizeSpinner.getValue();
//                    pen.paint(e.getX(), e.getY());
//                } else if (eraser.eraserItem.isSelected()) {
//                    // pending
//                    eraserSize = (Integer) propertyMenuDialogPenSizeSpinner.getValue();
//                    eraser.erase(e.getX(), e.getY());
//                }
//            }

            // pending
            @Override
            public void mouseMoved(MouseEvent e) {
                // 粘贴模式下，粘贴框随鼠标一起移动
                if (pasting) {
                    // REGION就是将要复制的区域大小，是一个轮廓
                    tool.region.selectedRegionLabel.setLocation(e.getPoint());
                    tool.region.selectedRegionLabel.repaint();
                }
            }
        });

        ActionListener cbx = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean[] colors = {
                        rbtBlue.isSelected(),
                        rbtGreen.isSelected(),
                        rbtRed.isSelected()
                };

                temp = MatUtil.copy(img);

                MatUtil.lilac(temp, colors);
                MatUtil.show(temp, showImgRegionLabel);
            }
        };


        rbtBlue.addActionListener(cbx);
        rbtGreen.addActionListener(cbx);
        rbtRed.addActionListener(cbx);
    }

    public void paste() {

        Mat newImg = MatUtil.copy(img);
        // newImg 的 selectRegion 被 copyRegionMat的内容覆盖
        MatUtil.copyToRegion(newImg,
                tool.region.copyRegionMat,
                MatUtil.getRect(tool.region.selectedRegionLabel));

        MatUtil.show(newImg, showImgRegionLabel);

        isProperty.push(0);
        last.push(img);
        img = newImg;
    }




    public void disablePasteMode() {
        tool.region.removeRegionSelected();
        tool.region.selectedRegionLabel.removeAll();
        tool.region.copyRegionMat = null;
    }

    public Mat getImg() {
        return img;
    }

    public void setNextLayerMat(Mat img) {
        this.nexLayerImg = img;
    }

    public void updatePropertys() {

        property.getMySize().txtWidth.setText(img.width() + "");
        property.getMySize().txtHeight.setText(img.height() + "");
        property.getMySize().lbSize.setText("Size: " + img.width() + "x" + img.height());

        int width = img.width() >= 200 ? img.width() : 200;
        int height = img.height() >= 200 ? img.height() : 200;

        this.setSize(width, height);
        this.setLocationRelativeTo(null);

    }
}
