package com.buaa.PhotoEditor.window;

import static com.buaa.PhotoEditor.util.MatUtil.*;
import static com.buaa.PhotoEditor.window.Constant.*;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.edit.Edit;
import com.buaa.PhotoEditor.window.file.Save;
import com.buaa.PhotoEditor.window.file.MyFile;
import com.buaa.PhotoEditor.window.property.Property;
import com.buaa.PhotoEditor.window.add.Add;
import com.buaa.PhotoEditor.window.tool.Tool;
import com.buaa.PhotoEditor.window.filter.Filter;

import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import javax.swing.*;

import org.opencv.core.Mat;

/**
 * @author 卢思文、罗雨曦
 * @version 2.0
 * @Description menuBar的显示效果不好（各项之间的距离等），需改进
 * 后续在关闭窗口的时候检查是否保存，未保存则提醒。相关代码如下
 * setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 * 通过插入自适应块与分割线改进menuBar布局
 * @date 2023/12/11
 */
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
    public Tool tool;
    public Add add;
    public Filter filter;
    public MyFile myFile;
    public Edit edit;
    // control of photo
    public Mat img;
    // 谨慎更改originalImg
    public Mat originalImg;
    public String originalImgPath;
    // temp的作用？
    public Mat temp;
    public static Mat copy;
    // ctrl+z
    public Stack<Mat[]> last;
    // ctrl+y`
    public Stack<Mat[]> next;

    // 存储originalImg的栈
    public Stack<Mat[]> lastOriginalImg;
    public Stack<Mat[]> nextOriginalImg;

    // 存储property值的栈
    public Stack<int[]> lastPropertyValue;
    public Stack<int[]> nextPropertyValue;
    public int[] currentPropertyValue;

    public Mat nexLayerImg;
    public int cnt;
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

        setLocationRelativeTo(null);
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
                keyPress(evt);
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
        add = new Add(this);
        showImgRegionLabel = new JLabel();
        tool = new Tool(this);
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
        panel.add(showImgRegionLabel);

        // LYX 通过插入自适应块与分割线调整menuBar布局，新增UI菜单
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
        menuBar.add(tool.region.selectRegionItem);
        tool.region.selectRegionItem.setMaximumSize(new Dimension(0, tool.region.selectRegionItem.getPreferredSize().height));
        separateMenu(menuBar);
        menuBar.add(tool.pen.penItem);
        tool.pen.penItem.setMaximumSize(new Dimension(0, tool.pen.penItem.getPreferredSize().height));
        menuBar.add(tool.pen.penColorPanel);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(tool.pen.penSizeSpinner);
        separateMenu(menuBar);
        menuBar.add(tool.eraser.eraserItem);
        menuBar.add(tool.eraser.eraserSizeSpinner);
        separateMenu(menuBar);
        menuBar.add(tool.rotate.rotateItem);
        separateMenu(menuBar);
        menuBar.add(tool.zoomIn.zoomInItem);
        separateMenu(menuBar);
        menuBar.add(tool.zoomOut.zoomOutItem);
        separateMenu(menuBar);
        menuBar.add(tool.drag.dragItem);
        tool.drag.dragItem.setMaximumSize(new Dimension(0, tool.drag.dragItem.getPreferredSize().height));
        separateMenu(menuBar);
        menuBar.add(tool.preview.previewItem);
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
     * @param evt 键盘事件
     * @Description ESC按键的功能设置
     * @author 卢思文
     * @date 2023/11/26
     * @version: 1.0
     */
    public void keyPress(KeyEvent evt) {
        if (zoomImg == null) {
            JOptionPane.showMessageDialog(null, "Please open an image first");
            return;
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            add.widget.widgetIcon = null;
            if (tool.region.selectRegionItem.isSelected()) {
                tool.region.removeRegionSelected();
            }
            if (add.widget.selectedWidgetLabel != null) {
                add.widget.removeWidget();
            }
            if (pasting) {
                pasting = false;
                for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                    this.tool.region.removeRegionSelected(i);
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
            if (flagForWidget) {
                add.widget.widgetIcon = null;
            }
        }
    }

    /**
     * @param menuBar 当前菜单栏
     * @Description 因为会多次复用，故将插入自适应块和垂直分隔线进行简单封装
     * @author 罗雨曦
     * @date 2023/12/12
     * @version: 1.0
     */
    public void separateMenu(JMenuBar menuBar) {
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(new JSeparator(SwingConstants.VERTICAL));
    }
}
