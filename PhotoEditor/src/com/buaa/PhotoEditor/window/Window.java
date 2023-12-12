/*
Authors: Igor Joaquim dos Santos Lima
GitHub: https://github.com/igor036
 */
package com.buaa.PhotoEditor.window;


import static com.buaa.PhotoEditor.util.MatUtil.*;


import static com.buaa.PhotoEditor.window.Constant.*;


import java.awt.*;
import java.awt.event.*;

import java.util.Stack;
import javax.swing.*;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.edit.Edit;
import com.buaa.PhotoEditor.window.file.Save;
import com.buaa.PhotoEditor.window.file.MyFile;

import com.buaa.PhotoEditor.window.property.Property;


// add
import com.buaa.PhotoEditor.window.add.Add;
// tool
import com.buaa.PhotoEditor.window.tool.Tool;


import com.buaa.PhotoEditor.window.filter.Filter;


import org.opencv.core.Mat;
import org.opencv.dnn.Layer;


/**
 * @Description: menuBar的显示效果不好（各项之间的距离等），需改进
 * 后续在关闭窗口的时候检查是否保存，未保存则提醒。相关代码如下
 * setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 * 通过插入自适应块与分割线改进menuBar布局
 * @author: 卢思文、罗雨曦
 * @date: 12/5/2023 3:49 AM
 * @version: 2.0
 **/
public class Window extends JFrame {
    // 掌管尺寸的计数器
    public int counter;
    // 掌管尺寸的数组 在打开图片后进行初始化
    public int[][] size;
    // 掌管放大缩小图片的数组和对应尺寸的原图数组
    public Mat[] zoomImg;
    public Mat[] originalZoomImg;
    public Mat[] paintingImg;
    // 存放copy的区域
    public Mat[] copyRegionImg;
    public Property property;
    // 为设置panel的布局增添的布局管理器
    public GridBagLayout gridBagLayout;
    public boolean flagForWidget;

    public Save save;
    // tool
    public Tool tool;
    // add
    public Add add;
    public Filter filter;

    public MyFile myFile;
    public Edit edit;
    /* Layer类的实例化对象只能有一个（因为不同的图层需要共用一个Layer，这样才能显示所有的图层列表）
        所以是static
    * */
    //control of photo
    public Mat img;              //actually
    // 谨慎更改originalImg
    public Mat originalImg;
    public String originalImgPath;
    // temp的作用？
    public Mat temp;             //temp
    public static Mat copy;
    public Stack<Mat[]> last; //ctrl+z
    public Stack<Mat[]> next;     //ctrl+y`

    // 存储originalImg的栈
    public Stack<Mat[]> lastOriginalImg;
    public Stack<Mat[]> nextOriginalImg;

    // 存储property值的栈
    public Stack<int[]> lastPropertyValue;
    public Stack<int[]> nextPropertyValue;
    public int[] currentPropertyValue;

    public Mat nexLayerImg;           //image use to paint
    public int cnt;


    public JPanel panel;
    // 总菜单栏
    public JMenuBar menuBar;
    // 图片显示区域
    public JLabel showImgRegionLabel;
    // options
    public boolean pasting = false;

    public String title;
    //新增UI菜单
    public UI ui;


    public Window(Mat img, String title) {
        this(title);
        this.title = title;
        this.img = img;
        cnt = 0;

        // 将当前的property的初始值暂存起来（如同img）
        currentPropertyValue[4] = Integer.parseInt(property.getMySize().txtWidth.getText());
        currentPropertyValue[5] = Integer.parseInt(property.getMySize().txtHeight.getText());

//        this.imgWidth = img.width();
//        this.imgHeight = img.height();
        MatUtil.show(img, showImgRegionLabel);
        showImgRegionLabel.setSize(img.width(), img.height());
        this.setSize(img.width(), img.height());
        setResizable(true);
        setLocationRelativeTo(null);
    }

    public Window(String title) {

        this.title = title;
        initComponents();
        setBounds(0, 0, getToolkit().getScreenSize().width, getToolkit().getScreenSize().height);
        setResizable(false);
        setVisible(true);


//          pending
//         addMouseListeners();

        setLocationRelativeTo(null);
        // 按下每个按键会弹出一个对应窗口
        // 设置窗口的大小
        // 撤销和反撤销操作用的栈
        last = new Stack<>();
        next = new Stack<>();
        // 初始化originalImg的undo redo用的栈
        lastOriginalImg = new Stack<>();
        nextOriginalImg = new Stack<>();
        // 初始化property的undo redo的栈
        lastPropertyValue = new Stack<>();
        nextPropertyValue = new Stack<>();
        currentPropertyValue = new int[10];
        this.setTitle(title);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                KeyPress(evt);
            }
        });
    }

    public void initComponents() {
        counter = AUTO_SIZE_COUNTER;

        // 对应zoomImg
        paintingImg = new Mat[NUM_FOR_NEW];

        copyRegionImg = new Mat[NUM_FOR_NEW];


        // 一定要先初始化panel，之后再调用tool类构造方法
        panel = new JPanel();
        save = new Save(this);
        gridBagLayout = new GridBagLayout();
        // add
        add = new Add(this);

        showImgRegionLabel = new JLabel();
        // tool
        tool = new Tool(this);

        // 只有一个layer，所以layer赋值之后就不再赋值

        menuBar = new JMenuBar();

        filter = new Filter(this);

        myFile = new MyFile(this);
        edit = new Edit(this);
        property = new Property(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ui = new UI(this);

        showImgRegionLabel.setText("Please select photo");

        /*
            让showImgRegionLabel显示在panel的中央，新添了下面3行代码，注释了原布局代码
            但是这会让程序刚启动的时候界面很小
         */
        panel.setLayout(gridBagLayout);
//        GridBagConstraints gbc = new GridBagConstraints();
        panel.add(showImgRegionLabel);
//        GroupLayout panelLayout = new GroupLayout(panel);
//        panel.setLayout(panelLayout);
//        panelLayout.setHorizontalGroup(
//                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(panelLayout.createSequentialGroup()
//                                .addComponent(showImgRegionLabel)
//                                .addGap(0, 307, Short.MAX_VALUE))
//        );
//        panelLayout.setVerticalGroup(
//                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(panelLayout.createSequentialGroup()
//                                .addComponent(showImgRegionLabel)
//                                .addContainerGap(306, Short.MAX_VALUE))
//        );
        //LYX 通过插入自适应块与分割线调整menuBar布局，新增UI菜单
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(myFile.fileMenu);
        separateMenu(menuBar);
        menuBar.add(edit.editMenu);
        separateMenu(menuBar);
        menuBar.add(add.addMenu);
        separateMenu(menuBar);
        menuBar.add(filter.filterMenu);
        menuBar.add(filter.filterMenu);
        separateMenu(menuBar);
        menuBar.add(property.propertyMenu);
        separateMenu(menuBar);
        menuBar.add(ui.uiMenu);
        separateMenu(menuBar);
        menuBar.add(tool.getRegion().selectRegionItem);
        tool.getRegion().selectRegionItem.setMaximumSize(new Dimension(0, tool.getRegion().selectRegionItem.getPreferredSize().height));
        separateMenu(menuBar);
        menuBar.add(tool.getPen().penItem);
        tool.getPen().penItem.setMaximumSize(new Dimension(0, tool.getPen().penItem.getPreferredSize().height));
        menuBar.add(tool.getPen().penColorPanel);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(tool.getPen().penSizeSpinner);
        separateMenu(menuBar);
        menuBar.add(tool.getEraser().eraserItem);
        menuBar.add(tool.getEraser().eraserSizeSpinner);
        separateMenu(menuBar);
        menuBar.add(tool.getRotate().rotateItem);
        separateMenu(menuBar);
        menuBar.add(tool.getZoomIn().zoomInItem);
        separateMenu(menuBar);
        menuBar.add(tool.getZoomOut().zoomOutItem);
        separateMenu(menuBar);
        menuBar.add(tool.getDrag().dragItem);
        tool.getDrag().dragItem.setMaximumSize(new Dimension(0, tool.getDrag().dragItem.getPreferredSize().height));
        separateMenu(menuBar);
        menuBar.add(tool.getPreview().previewItem);
        separateMenu(menuBar);
        menuBar.add(Box.createHorizontalGlue());
        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(panel,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
        );

        pack();

    }

    /**
     * @param evt : 键盘事件
     * @Description: ESC按键的功能设置
     * 未来：选择copy功能后，将selectRegion置false
     * @author: 卢思文
     * @date: 11/26/2023 9:09 PM
     * @version: 1.0
     **/
    public void KeyPress(KeyEvent evt) {
        if (zoomImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            add.widget.widgetIcon = null;
            if (tool.getRegion().selectRegionItem.isSelected()) {
                // pending
                tool.getRegion().removeRegionSelected();
            }
            if (add.widget.selectedWidgetLabel != null) {
                add.widget.removeWidget();
            }
            // else if 写成if
            if (pasting) {
                pasting = false;
//                edit.getPaste().disablePasteMode();
                for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                    this.tool.getRegion().removeRegionSelected(i);
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (add.widget.widgetIcon == null) {
                return;
            }
            next.clear();
            nextOriginalImg.clear();
            nextPropertyValue.clear();
            lastPropertyValue.push(MatUtil.copyPropertyValue(currentPropertyValue));
            last.push(copyImgArray(zoomImg));
            lastOriginalImg.push(copyImgArray(originalZoomImg));
            flagForWidget = true;
            for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                int x = (int) (add.widget.widgetLabel.getX() - ((double) panel.getWidth() - showImgRegionLabel.getWidth()) / 2);
                int y = (int) (add.widget.widgetLabel.getY() - ((double) panel.getHeight() - showImgRegionLabel.getHeight()) / 2);
                MatUtil.widget(zoomImg[i],
                        MatUtil.readImg(add.widget.widgetLabel.getIcon().toString()),
                        x, y, i, this);
                if (!flagForWidget) {
                    if (i == counter) {
                        JOptionPane.showMessageDialog(null, "Please remove widget completely inside the photo");
                    }
                    continue;
                }
                if (i == counter) {
                    MatUtil.show(zoomImg[counter], showImgRegionLabel);
                    panel.remove(add.widget.widgetLabel);
                }
            }
            if(flagForWidget){
                add.widget.widgetIcon = null;
            }
        }
    }

//     pending
//      待转成多线程


//     pending
//      待转成多线程


//     public void addMouseListeners() {


//         panel.addMouseListener(new MouseAdapter() {
//             /**
//             * @Description: 粘贴状态下，鼠标点击会进行粘贴
//             * @author: 卢思文
//             * @date: 11/26/2023 9:14 PM
//             * @version: 1.0
//             **/
//             @Override
//             public void mouseClicked(MouseEvent e) {

//                 if (pasting) {
//                     edit.getPaste().paste();
//                 }
//             }


//          pending
//         panel.addMouseMotionListener(new MouseAdapter() {
//             /**
//              * @Description: 粘贴模式下，粘贴框随鼠标一起移动
//              * @author: 卢思文
//              * @date: 11/26/2023 9:14 PM
//              * @version: 1.0
//              **/
//             @Override
//             public void mouseMoved(MouseEvent e) {
//                 if (pasting) {
//                     tool.region.selectedRegionLabel[counter].setLocation(e.getPoint());
//                     tool.region.selectedRegionLabel[counter].repaint();
//                 }
//             }
//         });

//         });


//         panel.addMouseMotionListener(new MouseAdapter() {
//             /**
//             * @Description: 粘贴模式下，粘贴框随鼠标一起移动
//             * @author: 卢思文
//             * @date: 11/26/2023 9:14 PM
//             * @version: 1.0
//             **/
//             @Override
//             public void mouseMoved(MouseEvent e) {
//                 if (pasting) {
//                     tool.region.selectedRegionLabel[counter].setLocation(e.getPoint());
//                     tool.region.selectedRegionLabel[counter].repaint();
//                 }
//             }
//         });


//     }


    /**
     * @param menuBar 当前菜单栏
     * @return void
     * @Description: 因为会多次复用，故将插入自适应块和垂直分隔线进行简单封装
     * @author: 罗雨曦
     * @date: 2023/12/5 3:51
     * @version: 1.0
     **/
    public void separateMenu(JMenuBar menuBar) {
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JSeparator(SwingConstants.VERTICAL));
    }


}
