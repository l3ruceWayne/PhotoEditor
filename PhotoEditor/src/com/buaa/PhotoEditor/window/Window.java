/*
Authors: Igor Joaquim dos Santos Lima
GitHub: https://github.com/igor036
 */
package com.buaa.PhotoEditor.window;

import com.buaa.PhotoEditor.modal.EColor;
import com.buaa.PhotoEditor.util.MatUtil;

import java.awt.*;

import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.*;
import javax.swing.event.*;

import com.buaa.PhotoEditor.window.file.Save;
import com.buaa.PhotoEditor.window.file.MyFile;
// add
import com.buaa.PhotoEditor.window.add.Add;
// tool
import com.buaa.PhotoEditor.window.tool.Tool;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

public class Window extends javax.swing.JFrame {


    public Save save;
    // tool
    public Tool tool;
    // add
    public Add add;
    //control of photo
    public Mat img;              //actually
    public Mat originalImg;
    public String originalImgPath;
    public Mat temp;             //temp
    public static Mat copy;
    public Stack<Mat> last; //ctrl+z
    public Stack<Mat> next;     //ctrl+y`


    //glitch wave 毛刺波？
    public int waveLength;
    public EColor color;

    //outer's
    public Mat paintingImg;            //image paint
    public Mat nexLayerImg;           //image use to paint
    public int lastSaturation;

    public static JRadioButton red;
    public static JRadioButton blue;
    public static JRadioButton green;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // 点击工具后出现的菜单栏
    // 一个框架？Img需要和lPhoto相关联


    public JMenuItem blur;
    public JSlider brightnessSlider;
    public JMenu btMasks;
    public JButton btResize;
    public JButton btnVhs;
    public JMenuItem cartoon;
    public ButtonGroup colors;
    public ButtonGroup vhs;

    public boolean flag;
    // 总画板
    // 作用？
    public JPanel panel;
    // 总菜单栏
    public JMenuBar menuBar;
    // 图片显示区域
    public JLabel showImgRegionLabel;

    public JMenu openPhotoMenu;
    // options
    public JMenu optionsMenu;
    public JMenuItem optionsMenuUndoItem;
    public JMenuItem optionsMenuRedoItem;
    public JMenuItem optionsMenuCopyItem;
    public boolean pasting = false;
    public JMenuItem optionsMenuCutItem;
    public JMenuItem focus;


    // Glitch
    public JMenu glitchMenu;
    public JMenuItem glitchMenuWaveItem;
    public JMenuItem glitchMenuVHSItem;
    public JMenuItem glitchMenuAddItem;
    public JMenuItem glitchMenuLilacItem;
    public JDialog glitchMenuLilacItemDialog;
    public JDialog glitchMenuWaveItemDialog;
    public JDialog glitchMenuVHSItemDialog;

    // Property
    public JMenu propertyMenu;
    public JDialog propertyMenuDialog;
    public JLabel propertyMenuDialogContrastLabel;
    public JLabel propertyMenuDialogBrightnessLabel;
    public JLabel propertyMenuDialogSaturationLabel;
    public JSlider propertyMenuDialogSaturationSlider;
    public JSpinner propertyMenuDialogPenSizeSpinner;
    public SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 30, 1);

    public javax.swing.JSlider contrastSlide;

    public javax.swing.JMenuItem dogMask;
    public javax.swing.Box.Filler filler1;
    public javax.swing.JMenu filter;
    public javax.swing.JMenuItem glasses1Mask;

    public javax.swing.ButtonGroup goutTypePen;
    public javax.swing.JMenuItem gray;
    public javax.swing.JMenuItem inversor;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    public javax.swing.JMenuItem jMenuItem1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JSeparator jSeparator1;
    public javax.swing.JSeparator jSeparator2;
    public javax.swing.JSeparator jSeparator3;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JLabel offsetLabel;
    public javax.swing.JLabel lbSize;
    public javax.swing.JLabel lightenLabel1;
    public javax.swing.JMenuItem morphology;
    public javax.swing.JScrollBar noiseBar;
    public javax.swing.JButton glitchWaveOKButton;
    public javax.swing.JPanel penColor;
    public javax.swing.JLabel penColorLabel;
    public javax.swing.JLabel penSizeLabel;
    public javax.swing.JCheckBox rbtBlue;
    public javax.swing.JCheckBox rbtGreen;
    public javax.swing.JCheckBox rbtRed;
    public javax.swing.JMenuItem sepia;
    public javax.swing.JTextField txtHeight;
    public javax.swing.JTextField txtWidth;
    public javax.swing.JTextField txtxLength;
    public javax.swing.JRadioButton vhs_1;
    public javax.swing.JLabel vhs_1_icon;
    public javax.swing.JLabel vhs_1_icon4;
    public javax.swing.JLabel vhs_1_icon5;
    public javax.swing.JRadioButton vhs_2;
    public javax.swing.JLabel vhs_2_icon;
    public javax.swing.JRadioButton vhs_3;
    public javax.swing.JLabel vhs_3_icon;
    public javax.swing.JRadioButton vhs_4;
    public javax.swing.JLabel vhs_4_icon;
    public javax.swing.JRadioButton vhs_date_1;
    public javax.swing.JRadioButton vhs_date_2;

    //control variables of listeners
    // selectRegion是画出一块区域进行单独编辑
//    public boolean toolMenuSelectRegionItem.isSelected() = false;


    public Window(Mat img, String title) {
        this(title);
        this.img = img;
        MatUtil.show(img, showImgRegionLabel);
        showImgRegionLabel.setSize(img.width(), img.height());
        this.setSize(img.width(), img.height());
        setResizable(true);
        setLocationRelativeTo(null);

    }

    public Window(String title) {

        initComponents();

        addMouseListeners();
        setResizable(true);
        setLocationRelativeTo(null);
        // 按下每个按键会弹出一个对应窗口
        // 设置窗口的大小
        glitchMenuWaveItemDialog.setSize(400, 200);
        glitchMenuVHSItemDialog.setSize(500, 200);
        glitchMenuLilacItemDialog.setSize(300, 100);
        propertyMenuDialog.setSize(400, 400);

        // 设置弹窗的位置，null指明默认是中央
        glitchMenuWaveItemDialog.setLocationRelativeTo(null);
        glitchMenuVHSItemDialog.setLocationRelativeTo(null);
        glitchMenuLilacItemDialog.setLocationRelativeTo(null);
        propertyMenuDialog.setLocationRelativeTo(null);


        // 撤销和反撤销操作用的栈
        last = new Stack<>();
        next = new Stack<>();
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

        glitchMenuWaveItemDialog = new javax.swing.JDialog();
        glitchWaveOKButton = new javax.swing.JButton();
        blue = new javax.swing.JRadioButton();
        red = new javax.swing.JRadioButton();
        green = new javax.swing.JRadioButton();
        txtxLength = new javax.swing.JTextField();
        offsetLabel = new javax.swing.JLabel();
        colors = new javax.swing.ButtonGroup();
        glitchMenuVHSItemDialog = new javax.swing.JDialog();
        vhs_1 = new javax.swing.JRadioButton();
        vhs_2 = new javax.swing.JRadioButton();
        vhs_3 = new javax.swing.JRadioButton();
        vhs_4 = new javax.swing.JRadioButton();
        vhs_date_1 = new javax.swing.JRadioButton();
        vhs_date_2 = new javax.swing.JRadioButton();
        vhs_1_icon = new javax.swing.JLabel();
        vhs_2_icon = new javax.swing.JLabel();
        vhs_3_icon = new javax.swing.JLabel();
        vhs_4_icon = new javax.swing.JLabel();
        vhs_1_icon4 = new javax.swing.JLabel();
        vhs_1_icon5 = new javax.swing.JLabel();
        btnVhs = new javax.swing.JButton();
        vhs = new javax.swing.ButtonGroup();
        propertyMenuDialog = new javax.swing.JDialog();
        propertyMenuDialogContrastLabel = new javax.swing.JLabel();
        propertyMenuDialogBrightnessLabel = new javax.swing.JLabel();
        noiseBar = new javax.swing.JScrollBar();
        lightenLabel1 = new javax.swing.JLabel();
        penSizeLabel = new javax.swing.JLabel();
        propertyMenuDialogPenSizeSpinner = new javax.swing.JSpinner(model);
        penColorLabel = new javax.swing.JLabel();
        penColor = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lbSize = new javax.swing.JLabel();
        txtWidth = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btResize = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        contrastSlide = new javax.swing.JSlider();
        brightnessSlider = new javax.swing.JSlider();
        propertyMenuDialogSaturationLabel = new javax.swing.JLabel();
        propertyMenuDialogSaturationSlider = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        goutTypePen = new javax.swing.ButtonGroup();
        glitchMenuLilacItemDialog = new javax.swing.JDialog();
        rbtGreen = new javax.swing.JCheckBox();
        rbtBlue = new javax.swing.JCheckBox();
        rbtRed = new javax.swing.JCheckBox();

        showImgRegionLabel = new JLabel();
        filler1 = new javax.swing.Box.Filler(new java.
                awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        menuBar = new javax.swing.JMenuBar();
        openPhotoMenu = new javax.swing.JMenu();
        optionsMenu = new JMenu();
        optionsMenuUndoItem = new javax.swing.JMenuItem();
        optionsMenuRedoItem = new javax.swing.JMenuItem();
        optionsMenuCopyItem = new javax.swing.JMenuItem();
        optionsMenuCutItem = new javax.swing.JMenuItem();
        focus = new javax.swing.JMenuItem();
        btMasks = new javax.swing.JMenu();
        dogMask = new javax.swing.JMenuItem();
        glasses1Mask = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        filter = new javax.swing.JMenu();
        gray = new javax.swing.JMenuItem();
        blur = new javax.swing.JMenuItem();
        inversor = new javax.swing.JMenuItem();
        morphology = new javax.swing.JMenuItem();
        cartoon = new javax.swing.JMenuItem();
        sepia = new javax.swing.JMenuItem();
        glitchMenu = new javax.swing.JMenu();
        glitchMenuWaveItem = new javax.swing.JMenuItem();
        glitchMenuVHSItem = new javax.swing.JMenuItem();
        glitchMenuAddItem = new javax.swing.JMenuItem();
        glitchMenuLilacItem = new javax.swing.JMenuItem();
        propertyMenu = new javax.swing.JMenu();
        // GlitchWave是按下Glitch再按下wave后出现的弹窗
        glitchMenuWaveItemDialog.setTitle("Glitch Wave");
        glitchMenuWaveItemDialog.setResizable(false);
        // colors是GlitchWave中的三个按键
        colors.add(blue);
        blue.setText("蓝色");

        colors.add(red);
        red.setText("红色");

        colors.add(green);
        green.setText("绿色");

        glitchWaveOKButton.setText("确定");
        // 当确定按钮被按下
        glitchWaveOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        offsetLabel.setText("偏移量:");

        GroupLayout glitchWaveLayout = new GroupLayout(glitchMenuWaveItemDialog.getContentPane());
        glitchMenuWaveItemDialog.getContentPane().setLayout(glitchWaveLayout);

        // 设置GlitchWave弹窗的水平布局
//        GroupLayout.SequentialGroup hGroup = glitchWaveLayout
//                .createSequentialGroup();
//        hGroup.addGap(17);
//        hGroup.addGroup(glitchWaveLayout.createParallelGroup(GroupLayout
//                        .Alignment.LEADING)
//                .addComponent(green));
//        hGroup.addGap(5);
//
//        hGroup.addGroup(glitchWaveLayout.createParallelGroup(GroupLayout
//                        .Alignment
//                        .LEADING)
//                .addComponent(blue)
//                .addComponent(offsetLabel));
//        hGroup.addGap(5);
//
//        hGroup.addGroup(glitchWaveLayout.createParallelGroup(GroupLayout
//                        .Alignment
//                        .LEADING)
//                .addComponent(red)
//                .addComponent(txtxLength,
//                        GroupLayout.PREFERRED_SIZE,
//                        82,
//                        GroupLayout.PREFERRED_SIZE
//                        )
//                .addComponent(okButton));
//        glitchWaveLayout.setHorizontalGroup(hGroup);
//
//        // 设置GlitchWave弹窗的垂直布局
//        GroupLayout.SequentialGroup vGroup = glitchWaveLayout
//                .createSequentialGroup();
//        vGroup.addGap(5);
//        vGroup.addGroup(glitchWaveLayout.createParallelGroup()
//                .addComponent(green)
//                .addComponent(blue)
//                .addComponent(red));
//        vGroup.addGap(5);
//        vGroup.addGroup(glitchWaveLayout.createParallelGroup()
//                .addComponent(offsetLabel)
//                .addComponent(txtxLength,
//                        GroupLayout.PREFERRED_SIZE,
//                        GroupLayout.DEFAULT_SIZE,
//                        GroupLayout.PREFERRED_SIZE)
//                .addComponent(okButton)
//        );
//        vGroup.addGap(5);
//        glitchWaveLayout.setVerticalGroup(vGroup);


        glitchWaveLayout.setHorizontalGroup(
                glitchWaveLayout.
                        createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(glitchWaveLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(glitchWaveLayout.
                                        createParallelGroup(GroupLayout.
                                                Alignment.LEADING)
                                        .addGroup(glitchWaveLayout.createSequentialGroup()
                                                .addComponent(green)
                                                .addGap(45, 45, 45)
                                                .addComponent(blue)
                                                .addGap(35, 35, 35)
                                        )
                                        .addGroup(javax.swing.GroupLayout
                                                        .Alignment.TRAILING,
                                                glitchWaveLayout.createSequentialGroup()
                                                        .addComponent(offsetLabel)
                                                        .addPreferredGap(javax.swing
                                                                .LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtxLength,
                                                                javax.swing
                                                                        .GroupLayout.PREFERRED_SIZE,
                                                                82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.
                                                                LayoutStyle.ComponentPlacement.UNRELATED)
                                        )
                                )
                                .addGroup(glitchWaveLayout.createParallelGroup(javax.swing
                                                .GroupLayout.Alignment.TRAILING)
                                        .addComponent(red)
                                        .addComponent(glitchWaveOKButton))
                                .addContainerGap(29, Short.MAX_VALUE)
                        )
        );
        // 设置垂直布局
        glitchWaveLayout.setVerticalGroup(
                glitchWaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(glitchWaveLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(glitchWaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(green)
                                        .addComponent(blue)
                                        .addComponent(red))
                                .addGap(18, 18, 18)
                                .addGroup(glitchWaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtxLength, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(glitchWaveOKButton)
                                        .addComponent(offsetLabel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        glitchMenuVHSItemDialog.setTitle("Glitch VHS");
        glitchMenuVHSItemDialog.setResizable(false);
        // vhs is ButtonGroup
        vhs.add(vhs_1);
        // vhs_1 is RadioButton
        vhs_1.setText("VHS_1");

        vhs.add(vhs_2);
        vhs_2.setText("VHS_2");

        vhs.add(vhs_3);
        vhs_3.setText("VHS_3");

        vhs.add(vhs_4);
        vhs_4.setText("VHS_4");

        vhs.add(vhs_date_1);
        vhs_date_1.setText("VHS_DATE_1");

        vhs.add(vhs_date_2);
        vhs_date_2.setText("VHS_DATE_2");

        btnVhs.setText("应用");
        btnVhs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVhsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout GlitchVHSLayout = new javax.swing.GroupLayout(glitchMenuVHSItemDialog.getContentPane());
        glitchMenuVHSItemDialog.getContentPane().setLayout(GlitchVHSLayout);
        // 水平布局
        GlitchVHSLayout.setHorizontalGroup(
                GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                .addComponent(vhs_1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(vhs_1_icon))
                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                .addComponent(vhs_2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(vhs_2_icon)))
                                .addGap(32, 32, 32)
                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                .addComponent(vhs_3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(vhs_3_icon)
                                                .addGap(30, 30, 30)
                                                .addComponent(vhs_date_2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(vhs_1_icon5))
                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(btnVhs)
                                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                                .addComponent(vhs_4)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(vhs_4_icon)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(vhs_date_1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(vhs_1_icon4)))
                                .addContainerGap(65, Short.MAX_VALUE))
        );
        // 垂直布局
        GlitchVHSLayout.setVerticalGroup(
                GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(vhs_1)
                                                        .addComponent(vhs_1_icon))
                                                .addGap(18, 18, 18)
                                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(vhs_2)
                                                        .addComponent(vhs_2_icon)))
                                        .addGroup(GlitchVHSLayout.createSequentialGroup()
                                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(vhs_3)
                                                                .addComponent(vhs_3_icon))
                                                        .addComponent(vhs_date_2)
                                                        .addComponent(vhs_1_icon5))
                                                .addGap(15, 15, 15)
                                                .addGroup(GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(vhs_4)
                                                        .addComponent(vhs_4_icon)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, GlitchVHSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(vhs_date_1)
                                                                .addComponent(vhs_1_icon4)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addComponent(btnVhs)
                                .addContainerGap())
        );

        propertyMenuDialog.setTitle("属性");

        propertyMenuDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                if(copy == null) return;
//                previous.push(img);
//                copy.copyTo(img);
//                MatUtil.show(img, lPhoto);
//                copy = null;
            }
        });

        propertyMenuDialogContrastLabel.setText("对比度：");

        propertyMenuDialogBrightnessLabel.setText("亮度：");

        noiseBar.setMaximum(255);
        noiseBar.setOrientation(JScrollBar.HORIZONTAL);
//        noiseBar.addMouseListener(new MouseAdapter() {
//            // 当鼠标松开的时候，即是调整结束的时候，此时应用调整内容
//            public void mouseReleased(MouseEvent evt) {
//                noiseBarMouseReleased(evt);
//            }
//        });
        noiseBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent evt) {
                noiseBarAdjustmentValueChanged(evt);
            }
        });

        lightenLabel1.setText("噪声:");

        penSizeLabel.setText("笔刷大小:");

        penColorLabel.setText("画笔颜色:");

        penColor.setBackground(new java.awt.Color(0, 0, 0));
        penColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                penColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout penColorLayout = new javax.swing.GroupLayout(penColor);
        penColor.setLayout(penColorLayout);
        penColorLayout.setHorizontalGroup(
                penColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 21, Short.MAX_VALUE)
        );
        penColorLayout.setVerticalGroup(
                penColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 20, Short.MAX_VALUE)
        );

        jLabel2.setText("Pincel");

        jLabel3.setText("Contraste");

        lbSize.setText("Tamannho(LxA): ");

        jLabel4.setText("Largura:");

        jLabel5.setText("Altura:");

        btResize.setText("Redimensionar");
        btResize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btResizeActionPerformed(evt);
            }
        });

        jLabel6.setText("Ruído:");

        jLabel7.setText("Tamanho:");

        contrastSlide.setMinimum(-100);
        contrastSlide.setToolTipText("");
        contrastSlide.setValue(0);
        contrastSlide.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contrastSlideStateChanged(evt);
            }
        });

        brightnessSlider.setMinimum(1);
        brightnessSlider.setToolTipText("");
        brightnessSlider.setValue(1);
        brightnessSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightnessSliderStateChanged(evt);
            }
        });


        propertyMenuDialogSaturationLabel.setText("饱和度：");
        propertyMenuDialogSaturationSlider.setMinimum(-100);
        propertyMenuDialogSaturationSlider.setMaximum(100);
        propertyMenuDialogSaturationSlider.setToolTipText("");
        propertyMenuDialogSaturationSlider.setValue(0);
        propertyMenuDialogSaturationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                changeSaturation(evt);
            }
        });


        javax.swing.GroupLayout PropertysLayout = new javax.swing.GroupLayout(propertyMenuDialog.getContentPane());
        propertyMenuDialog.getContentPane().setLayout(PropertysLayout);
        PropertysLayout.setHorizontalGroup(
                PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PropertysLayout.createSequentialGroup()
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(167, 167, 167)
                                                .addComponent(jLabel3))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                .addGap(17, 17, 17)
                                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                                .addGap(13, 13, 13)
                                                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                                                .addComponent(jLabel4)
                                                                                                .addGap(2, 2, 2)
                                                                                                .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(30, 30, 30)
                                                                                                .addComponent(jLabel5))
                                                                                        .addComponent(lbSize))
                                                                                .addGap(5, 5, 5)
                                                                                .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(penSizeLabel)
                                                                                        .addComponent(penColorLabel))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(propertyMenuDialogPenSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(penColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                .addComponent(lightenLabel1)
                                                                .addGap(34, 34, 34)
                                                                .addComponent(noiseBar, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                                .addGap(148, 148, 148)
                                                                .addComponent(jLabel2))))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(135, 135, 135)
                                                .addComponent(btResize))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(167, 167, 167)
                                                .addComponent(jLabel6))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addGap(157, 157, 157)
                                                .addComponent(jLabel7))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(propertyMenuDialogContrastLabel)
                                                        .addComponent(propertyMenuDialogBrightnessLabel)
                                                        .addComponent(propertyMenuDialogSaturationLabel))
                                                .addGap(28, 28, 28)
                                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(propertyMenuDialogSaturationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(brightnessSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(contrastSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(73, Short.MAX_VALUE))
        );
        PropertysLayout.setVerticalGroup(
                PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PropertysLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addComponent(contrastSlide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(brightnessSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PropertysLayout.createSequentialGroup()
                                                .addComponent(propertyMenuDialogContrastLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(propertyMenuDialogBrightnessLabel)))
                                .addGap(18, 18, 18)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(propertyMenuDialogSaturationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(propertyMenuDialogSaturationLabel))
                                .addGap(36, 36, 36)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(noiseBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lightenLabel1))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(4, 4, 4)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(penSizeLabel)
                                        .addComponent(propertyMenuDialogPenSizeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(penColorLabel)
                                        .addComponent(penColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lbSize)
                                .addGap(24, 24, 24)
                                .addGroup(PropertysLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addComponent(btResize)
                                .addGap(42, 42, 42))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        glitchMenuLilacItemDialog.setTitle("Glitch Lilás");

        rbtGreen.setText("Verde");

        rbtBlue.setText("Azul");

        rbtRed.setText("Vermelho");

        javax.swing.GroupLayout GlitchLilacLayout = new javax.swing.GroupLayout(glitchMenuLilacItemDialog.getContentPane());
        glitchMenuLilacItemDialog.getContentPane().setLayout(GlitchLilacLayout);
        GlitchLilacLayout.setHorizontalGroup(
                GlitchLilacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GlitchLilacLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(rbtGreen)
                                .addGap(18, 18, 18)
                                .addComponent(rbtBlue)
                                .addGap(34, 34, 34)
                                .addComponent(rbtRed)
                                .addContainerGap(38, Short.MAX_VALUE))
        );
        GlitchLilacLayout.setVerticalGroup(
                GlitchLilacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GlitchLilacLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(GlitchLilacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(rbtGreen)
                                        .addComponent(rbtBlue)
                                        .addComponent(rbtRed))
                                .addContainerGap(19, Short.MAX_VALUE))
        );

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

        MyFile file=new MyFile(this);
        menuBar.add(file.fileMenu);

        optionsMenu.setText("Options");

        optionsMenuUndoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        optionsMenuUndoItem.setText("Undo");
        optionsMenuUndoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undo(evt);
            }
        });
        optionsMenu.add(optionsMenuUndoItem);

        optionsMenuRedoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                InputEvent.CTRL_MASK));
        optionsMenuRedoItem.setText("Redo");
        optionsMenuRedoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redo(evt);
            }
        });
        optionsMenu.add(optionsMenuRedoItem);

        optionsMenuCopyItem
                .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                        InputEvent.CTRL_MASK));
        // 点复制之前需要选择好区域
        optionsMenuCopyItem.setText("Copy");
        // 点击复制之后选择的区域会被复制，点击鼠标会在鼠标位置粘贴复制的内容
        optionsMenuCopyItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tool.region.copySelectedRegion(evt);
            }
        });
        optionsMenu.add(optionsMenuCopyItem);

        optionsMenuCutItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        optionsMenuCutItem.setText("Cut");
        optionsMenuCutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cutActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsMenuCutItem);

        optionsMenu.add(save.saveItem);
        optionsMenu.add(save.saveAsItem);

        focus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        focus.setText("Focar");
        focus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                focusActionPerformed(evt);
            }
        });
        optionsMenu.add(focus);

        menuBar.add(optionsMenu);

        menuBar.add(add.addMenu);

        btMasks.setText("Mascaras");

        dogMask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, java.awt.event.InputEvent.CTRL_MASK));
        dogMask.setText("Cachorro");
        dogMask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dogMaskActionPerformed(evt);
            }
        });
        btMasks.add(dogMask);

        glasses1Mask.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        glasses1Mask.setText("Óculos 1");
        glasses1Mask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glasses1MaskActionPerformed(evt);
            }
        });
        btMasks.add(glasses1Mask);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Óculos 2");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        btMasks.add(jMenuItem1);

        menuBar.add(btMasks);

        filter.setText("Filtros");

        gray.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        gray.setText("Escala de Cinza");
        gray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grayActionPerformed(evt);
            }
        });
        filter.add(gray);

        blur.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        blur.setText("Desfocar");
        blur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blurActionPerformed(evt);
            }
        });
        filter.add(blur);

        inversor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        inversor.setText("Inverter");
        inversor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inversorActionPerformed(evt);
            }
        });
        filter.add(inversor);

        morphology.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        morphology.setText("Morfologia");
        morphology.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                morphologyActionPerformed(evt);
            }
        });
        filter.add(morphology);

        cartoon.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        cartoon.setText("Desenho");
        cartoon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cartoonActionPerformed(evt);
            }
        });
        filter.add(cartoon);

        sepia.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        sepia.setText("Sépia");
        sepia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sepiaActionPerformed(evt);
            }
        });
        filter.add(sepia);

        menuBar.add(filter);

        glitchMenu.setText("Glitch");

        glitchMenuWaveItem.setAccelerator(javax.swing.KeyStroke.
                getKeyStroke(java.awt.event.KeyEvent.VK_W,
                        java.awt.event.InputEvent.SHIFT_MASK));
        glitchMenuWaveItem.setText("Wave");

        glitchMenuWaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                glitchMenuWaveItemDialog.setModal(true);
                glitchMenuWaveItemDialog.setVisible(true);
            }
        });
        // 有问题的源代码
        //         点击Glitch再点击Wave一栏之后，执行actionPerformed方法，其中第二行是阻塞的方法
//        glitchWave.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                System.out.println("abcd");
//                glitchWaveActionPerformed(evt);
//            }
//        });
        glitchMenu.add(glitchMenuWaveItem);

        glitchMenuVHSItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        glitchMenuVHSItem.setText("VHS");
        glitchMenuVHSItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        glitchMenu.add(glitchMenuVHSItem);

        // bug
        glitchMenuAddItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        glitchMenuAddItem.setText("add");
        glitchMenuAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        glitchMenu.add(glitchMenuAddItem);

        glitchMenuLilacItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                InputEvent.CTRL_MASK));
        glitchMenuLilacItem.setText("lilac");
        glitchMenuLilacItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                lilacActionPerformed(evt);
            }
        });
        glitchMenu.add(glitchMenuLilacItem);

        menuBar.add(glitchMenu);


        // 点击选择区域之后开始按键监听
//        toolMenuSelectRegionItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                clickToolMenuSelectRegionItem(evt);
//            }
//        });

        propertyMenu.setText("Property");
        propertyMenu.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                propertysMouseClicked(evt);
            }
        });
        menuBar.add(propertyMenu);

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

    public void grayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grayActionPerformed

        Mat newImg = MatUtil.copy(img);

        if (tool.region.selectRegionItem.isSelected()) {

            MatUtil.grayScale(newImg, MatUtil.getRect(tool.region.selectedRegionLabel));
            tool.region.removeRegionSelected();

        } else {
            MatUtil.grayScale(newImg);
        }

        MatUtil.show(newImg, showImgRegionLabel);

        last.push(img);
        img = newImg;
    }//GEN-LAST:event_grayActionPerformed

    public void blurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blurActionPerformed

        int blurLevel = Integer.parseInt(JOptionPane.showInputDialog(null, "Nível de desfoque", JOptionPane.WARNING_MESSAGE));

        Mat newImg = MatUtil.copy(img);

        if (tool.region.selectRegionItem.isSelected()) {

            MatUtil.blur(newImg, blurLevel, MatUtil.getRect(tool.region.selectedRegionLabel));
            tool.region.removeRegionSelected();

        } else {
            MatUtil.blur(newImg, blurLevel);
        }

        MatUtil.show(newImg, showImgRegionLabel);

        last.push(img);
        img = newImg;

    }//GEN-LAST:event_blurActionPerformed

    public void inversorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inversorActionPerformed

        Mat newImg = MatUtil.copy(img);

        if (tool.region.selectRegionItem.isSelected()) {
            MatUtil.inversor(newImg, MatUtil.getRect(tool.region.selectedRegionLabel));
            tool.region.removeRegionSelected();
        } else
            MatUtil.inversor(newImg);

        MatUtil.show(newImg, showImgRegionLabel);

        last.push(img);
        img = newImg;

    }//GEN-LAST:event_inversorActionPerformed

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

    public void glitchWaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_glitchWaveActionPerformed

//        br.com.ySelf.JDialog.glitchWaveDialog glitchWaveDialog1 = new glitchWaveDialog();
//        glitchWaveDialog1.main();
        glitchMenuWaveItemDialog.setModal(true);
        glitchMenuWaveItemDialog.setVisible(true);
        // 获取一个当前状态的copy，对copy进行更改，便于ctrl z 回退
        // 如果点击的是x，就不执行下面的内容了
        if (!glitchWaveOKButton.isSelected()) return;
        Mat newImg = MatUtil.copy(img);
        if (tool.region.selectRegionItem.isSelected()) {
            MatUtil.glitchWave(newImg, waveLength, color, MatUtil.getRect(tool.region.selectedRegionLabel));
            tool.region.removeRegionSelected();
        } else {
            MatUtil.glitchWave(newImg, waveLength, color);
        }

        MatUtil.show(newImg, showImgRegionLabel);

        last.push(img);
        img = newImg;

    }

    public void okButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (!offsetLabel.isVisible() && !txtxLength.isVisible())
            glitchMenuWaveItemDialog.dispose();
        else {
            try {
                waveLength = Integer.parseInt(txtxLength.getText());
                /* !! ALERT: improve, make dynamic verification */
                if (green.isSelected()) {
                    color = EColor.GREEN;
                } else if (blue.isSelected()) {
                    color = EColor.BLUE;
                } else if (red.isSelected()) {
                    color = EColor.RED;
                }
                Mat newImg = MatUtil.copy(img);
                if (tool.region.selectRegionItem.isSelected()) {
                    MatUtil.glitchWave(newImg, waveLength, color, MatUtil.
                            getRect(tool.region.selectedRegionLabel));
                    tool.region.removeRegionSelected();
                } else {
                    MatUtil.glitchWave(newImg, waveLength, color);
                }

                MatUtil.show(newImg, showImgRegionLabel);

                last.push(img);
                img = newImg;
                // 执行完之后再关闭窗口
                glitchMenuWaveItemDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "填写错误！");
            }

        }
    }//GEN-LAST:event_okButtonActionPerformed

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

    public void btnVhsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVhsActionPerformed
        glitchMenuVHSItemDialog.dispose();
    }//GEN-LAST:event_btnVhsActionPerformed

    public void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        try {

            glitchMenuVHSItemDialog.setModal(true);
            glitchMenuVHSItemDialog.setVisible(true);

            Mat newImg = MatUtil.copy(img);

            /* !! ALERT: improve, make dynamic verification */
            if (vhs_1.isSelected()) {
                MatUtil.vhs(newImg, MatUtil.VHS_1);
            } else if (vhs_2.isSelected()) {
                MatUtil.vhs(newImg, MatUtil.VHS_2);
            } else if (vhs_3.isSelected()) {
                MatUtil.vhs(newImg, MatUtil.VHS_3);
            } else if (vhs_4.isSelected()) {
                MatUtil.vhs(newImg, MatUtil.VHS_4);
            } else if (vhs_date_1.isSelected()) {
                MatUtil.vhs(newImg, MatUtil.VHS_DATE_1);
            } else {
                MatUtil.vhs(newImg, MatUtil.VHS_DATE_2);
            }

            MatUtil.show(newImg, showImgRegionLabel);

            last.push(img);
            img = newImg;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Tente novamente!");
            System.out.println("ERRO: " + ex.getMessage());
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    public void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            Mat newImg = MatUtil.copy(img);
            MatUtil.overlay(newImg, MatUtil.readImg(fileChooser.getSelectedFile().getAbsolutePath()));
            MatUtil.show(newImg, showImgRegionLabel);

            last.push(img);
            img = newImg;

        }

    }

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

    public void propertysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertysMouseClicked
        temp = MatUtil.copy(img);
        propertyMenuDialog.setModal(true);
        propertyMenuDialog.setVisible(true);
        propertyMenuDialog.setResizable(true);
        last.push(img);
        img = temp;
        tool.region.removeRegionSelected();
        restartPorpertyComponentsValues();
    }//GEN-LAST:event_propertysMouseClicked

    public void cartoonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cartoonActionPerformed

        Mat newImg = MatUtil.copy(img);

        if (tool.region.selectRegionItem.isSelected()) {
            MatUtil.cartoon(newImg, MatUtil.getRect(tool.region.selectedRegionLabel));
            tool.region.removeRegionSelected();
        } else
            MatUtil.cartoon(newImg);

        MatUtil.show(newImg, showImgRegionLabel);

        last.push(img);
        img = newImg;

    }//GEN-LAST:event_cartoonActionPerformed

//    public void noiseBarMouseReleased(MouseEvent evt) {
//        applyNoise(noiseBar.getValue(), true);
//    }

    public void noiseBarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_noiseBarAdjustmentValueChanged
        applyNoise(noiseBar.getValue(), false);
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

//    public void saveAsNewImage()
    public void cutActionPerformed(ActionEvent evt) {
        // pending 弹窗的位置
        if (!tool.region.selectRegionItem.isSelected()) {
            JOptionPane.showMessageDialog(null,
                    "Please select region first");
        } else {

            Mat newImg = MatUtil.cut(img, MatUtil.getRect(tool.region.selectedRegionLabel));

            MatUtil.show(newImg, showImgRegionLabel);
            showImgRegionLabel.setSize(newImg.width(),newImg.height());
            this.setSize(newImg.width(), newImg.height());
            this.setLocationRelativeTo(null);

            last.push(img);
            img = newImg;

            tool.region.removeRegionSelected();
        }
    }
    // pending1

    public void redo(ActionEvent evt) {

        if (!next.isEmpty()) {

            last.push(img);

            if (!tool.region.selectRegionItem.isSelected()) {

                img = next.pop();

            } else {

                Rect selectedRegionRect = MatUtil.getRect(tool.region.selectedRegionLabel);
                Mat newImg = MatUtil.copy(img);

                next.peek().submat(selectedRegionRect)
                        .copyTo(newImg.submat(selectedRegionRect));
                img = newImg;
                tool.region.removeRegionSelected();
            }
            showImgRegionLabel.setSize(img.width(),img.height());
            this.setSize(img.width(),img.height());
            this.setLocationRelativeTo(null);

            MatUtil.show(img, showImgRegionLabel);

        }
        // pending 认为不需要
//        else {
//            JOptionPane.showMessageDialog(null, "Não há mais oque refazer!");
//        }
    }

    public void undo(ActionEvent evt) {

        if (!last.isEmpty()) {
            next.push(img);
            if (!tool.region.selectRegionItem.isSelected()) {

                img = last.pop();

            } else {

                Rect selectedRegionRect = MatUtil.getRect(tool.region.selectedRegionLabel);
                Mat newImg = MatUtil.copy(img);
                // last.peek()是想要返回的最近的那一个版本
                last.peek().submat(selectedRegionRect)
                        .copyTo(newImg.submat(selectedRegionRect));
//                last.push(img);
                img = newImg;
                tool.region.removeRegionSelected();
            }
            showImgRegionLabel.setSize(img.width(),img.height());
            this.setSize(img.width(), img.height());
            this.setLocationRelativeTo(null);

            MatUtil.show(img, showImgRegionLabel);

        }
        // pending  感觉不需要加弹窗
//        else {
//            JOptionPane.showMessageDialog(null, "Não há mais oque desfazer!");
//        }
    }

    public void btResizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btResizeActionPerformed

        try {

            double width = Double.parseDouble(txtWidth.getText());
            double height = Double.parseDouble(txtHeight.getText());

            Mat newImg = MatUtil.copy(temp);
            MatUtil.resize(newImg, new Size(width, height));

            last.push(img);
            img = temp = newImg;

            MatUtil.show(temp, showImgRegionLabel);

            updatePropertys();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Pre-encha corretamente os dados!");
        }

    }//GEN-LAST:event_btResizeActionPerformed

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

    public void lilacActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_lilacActionPerformed

        glitchMenuLilacItemDialog.setModal(true);
        glitchMenuLilacItemDialog.setVisible(true);

        if (rbtBlue.isSelected() || rbtGreen.isSelected() || rbtRed.isSelected()) {
            Mat newImg = MatUtil.copy(img);
            last.push(newImg);
            img = temp;
        }
    }//GEN-LAST:event_lilacActionPerformed

    public void contrastSlideStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_contrastSlideStateChanged
        contrastAndBrightness();
    }//GEN-LAST:event_contrastSlideStateChanged

    public void brightnessSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_brightnessSliderStateChanged
        contrastAndBrightness();
    }//GEN-LAST:event_brightnessSliderStateChanged

    public void focusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_focusActionPerformed

        if (tool.region.selectRegionItem.isSelected()) {

            Mat newImg = MatUtil.copy(img);
            MatUtil.focus(newImg, MatUtil.getRect(tool.region.selectedRegionLabel));
            MatUtil.show(newImg, showImgRegionLabel);

            last.push(img);
            img = newImg;

            tool.region.removeRegionSelected();

        } else
            JOptionPane.showMessageDialog(null, "Selecione a área que deseja focar!");
    }

    public void changeSaturation(ChangeEvent evt) {

        temp = MatUtil.copy(img);

        int saturation = propertyMenuDialogSaturationSlider.getValue();
        lastSaturation = propertyMenuDialogSaturationSlider.getValue();

        if (tool.region.selectRegionItem.isSelected()) {
            MatUtil.saturation(temp, saturation, MatUtil.getRect(tool.region.selectedRegionLabel));
        } else {
            MatUtil.saturation(temp, saturation);
        }

        MatUtil.show(temp, showImgRegionLabel);

    }

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

        last.push(img);
        img = newImg;
    }

    public void contrastAndBrightness() {

        temp = MatUtil.copy(img);

        if (tool.region.selectRegionItem.isSelected())
            MatUtil.contrastAndBrightness(temp, brightnessSlider.getValue(), -contrastSlide.getValue(),
                    MatUtil.getRect(tool.region.selectedRegionLabel));
        else
            MatUtil.contrastAndBrightness(temp, brightnessSlider.getValue(), -contrastSlide.getValue());


        MatUtil.show(temp, showImgRegionLabel);
    }

    public void applyNoise(int level, boolean replace) {
        copy = MatUtil.copy(img);
        if (tool.region.selectRegionItem.isSelected()) {
            MatUtil.noise(copy, level, MatUtil.getRect(tool.region.selectedRegionLabel));
        } else {
            MatUtil.noise(copy, level);
        }
        // 需要再加一个确定键，确定之后img入栈，然后替换为当前调整后的内容
        MatUtil.show(copy, showImgRegionLabel);

        if (replace) {
            last.push(img);
            img = copy;
        }

    }

    public void restartPorpertyComponentsValues() {
        Component[] components = propertyMenuDialog.getContentPane().getComponents();
        for (Component c : components) {
            if (c instanceof JScrollBar) {
                ((JScrollBar) c).setValue(0);
            }
        }
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

        txtWidth.setText(img.width() + "");
        txtHeight.setText(img.height() + "");
        lbSize.setText("Tamanho (LxA): " + img.width() + "x" + img.height());

        int width = img.width() >= 200 ? img.width() : 200;
        int height = img.height() >= 200 ? img.height() : 200;

        this.setSize(width, height);
        this.setLocationRelativeTo(null);

    }
}
