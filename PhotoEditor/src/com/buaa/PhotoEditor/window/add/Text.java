
package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.window.Constant.*;

import com.buaa.PhotoEditor.window.thread.AddTextThread;
import org.opencv.core.Scalar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/*
 * @Description:选择区域后写入文字（特别说明：用户选择区域后才能写字，写文字后不能修改位置）
 * （但是其实不用到选择一个区域，因为是根据选择时点击的第一个位置来定位的，不受选择框限制）
 * @author: 张旖霜
 * @date: 11/27/2023 3:17 PM
 * @version: 1.0
 */
public class Text {
    public AddTextThread[] addTextThread;
    public Window window;
    public JMenuItem addTextItem;
    private Scalar color;
    private int scale;
    private String str;

    public JDialog addTextDialog;
    public JPanel textColorPanel;
    public JLabel addTextContentsLabel;
    public JLabel addTextSizeLabel;
    public JLabel addTextColorLabel;
    public JSpinner addTextSpinner;
    public JTextField textField;
    public boolean flag;

    public Text(Window window) {
        scale = 1;
        color = new Scalar(0, 0, 0);
        str = "";
        addTextThread = new AddTextThread[NUM_FOR_NEW];
        this.window = window;
        addTextItem = new JMenuItem("Text");
        addTextDialog = new JDialog();
        textColorPanel = new JPanel();
        addTextContentsLabel = new JLabel("Contents: ");
        addTextSizeLabel = new JLabel("Size: ");
        addTextColorLabel = new JLabel("Color: ");
        addTextSpinner = new JSpinner();
        textField = new JTextField();

        initAddTextDialog();

        flag = true;
        addTextItem.addActionListener(evt -> writeTextActionPerformed());

    }


    /*
     * @param:
     * @return
     * @Description:创建文字设置的面板
     * @author: 张旖霜
     * @date: 11/27/2023 12:51 PM
     * @version: 1.0
     */
    public void initAddTextDialog() {
        addTextDialog.setSize(280, 200);
        addTextDialog.setTitle("Text");
        addTextDialog.setResizable(false);
        addTextDialog.setLocationRelativeTo(null);

        textColorPanel.setBackground(new Color(0, 0, 0));

        // panelTextColor
        GroupLayout pnlTextColorLayout = new javax.swing.GroupLayout(textColorPanel);
        textColorPanel.setLayout(pnlTextColorLayout);
        pnlTextColorLayout.setHorizontalGroup(
                pnlTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 21, Short.MAX_VALUE)
        );
        pnlTextColorLayout.setVerticalGroup(
                pnlTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE)
        );

        // dialogAddText
        GroupLayout addTextLayout = new GroupLayout(addTextDialog.getContentPane());
        addTextDialog.getContentPane().setLayout(addTextLayout);
        addTextLayout.setHorizontalGroup(
                addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTextLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(addTextLayout.createSequentialGroup()
                                                .addComponent(addTextColorLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(textColorPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(addTextContentsLabel)
                                        .addComponent(addTextSizeLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addTextLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addTextSpinner, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(91, Short.MAX_VALUE))
        );
        addTextLayout.setVerticalGroup(
                addTextLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(addTextLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(addTextLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(addTextContentsLabel)
                                        .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(addTextLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(addTextSizeLabel)
                                        .addComponent(addTextSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addTextLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(addTextColorLabel)
                                        .addComponent(textColorPanel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(82, Short.MAX_VALUE))
        );
    }

    /*
     * @param:
     * @return
     * @Description:选好位置（框选区域）后，显示字体设置面板
     * @author: 张旖霜
     * @date: 11/27/2023 12:51 PM
     * @version: 1.0
     */
    public void writeTextActionPerformed() {
        if (window.tool.region.selectRegionItem.isSelected()) {
            for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                window.tool.region.removeRegionSelected(i);
            }
            if (flag) {
                flag = false;
                for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
                    addTextThread[i] = new AddTextThread(window, this, i);
                    addTextThread[i].start();
                }
            }
            addTextDialog.setModal(true);
            addTextDialog.setVisible(true);

            window.lastPropertyValue
                    .push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            window.last.push(window.img);
            window.img = window.temp;
            initPanel();

        } else {
            JOptionPane.showMessageDialog(null, "Select an area to add text!");
        }
    }


    /*
     * @param:
     * @return
     * @Description: 初始化文字设置
     * @author: 张旖霜
     * @date: 11/27/2023 12:48 PM
     * @version: 1.0
     */
    public void initPanel() {
        setStr("");
        textField.setText(str);
        addTextSpinner.setValue(1);
        setColor(new Scalar(0, 0, 0));
        textColorPanel.setBackground(Color.BLACK);
    }

    public Scalar getColor() {
        return color;
    }

    public void setColor(Scalar color) {
        this.color = color;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}
