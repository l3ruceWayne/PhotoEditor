
package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
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

    public Window window;
    public JMenuItem addTextItem;

    private Scalar color;
    private int scale;
    private String str;

    public JDialog dialogAddText;
    public JPanel panelTextColor;
    public JLabel labelAddTextContents;
    public JLabel labelAddTextSize;
    public JLabel labelAddTextColor;
    public JSpinner spinnerAddText;
    public JTextField textField;

    public Point point;

    public Text(Window window){
        scale = 1;
        color = new Scalar(0,0,0);
        str = "";

        this.window = window;

        addTextItem = new JMenuItem("Add Text");
        dialogAddText = new JDialog();
        panelTextColor = new JPanel();
        labelAddTextContents = new JLabel("Contents");
        labelAddTextSize = new JLabel("Size: ");
        labelAddTextColor = new JLabel("Color: ");
        spinnerAddText = new JSpinner();
        textField = new JTextField();

        createAddTextPanel();

        addTextItem.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                writeTextActionPerformed(evt);
            }
        });
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
        spinnerAddText.setValue(1);
        setColor(new Scalar(0, 0, 0));
        panelTextColor.setBackground(Color.BLACK);
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

    /*
    * @param:
    * @return
    * @Description:创建文字设置的面板
    * @author: 张旖霜
    * @date: 11/27/2023 12:51 PM
    * @version: 1.0
    */
    public void createAddTextPanel() {
        dialogAddText.setSize(400, 400);
        dialogAddText.setTitle("Text");
        dialogAddText.setResizable(false);
        dialogAddText.setLocationRelativeTo(null);

        panelTextColor.setBackground(new java.awt.Color(0, 0, 0));
        panelTextColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlTextColorMouseClicked(evt);
            }
        });

        // panelTextColor
        GroupLayout pnlTextColorLayout = new javax.swing.GroupLayout(panelTextColor);
        panelTextColor.setLayout(pnlTextColorLayout);
        pnlTextColorLayout.setHorizontalGroup(
                pnlTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 21, Short.MAX_VALUE)
        );
        pnlTextColorLayout.setVerticalGroup(
                pnlTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE)
        );

        spinnerAddText.addChangeListener(new javax.swing.event.ChangeListener() {
            /*
            * @param:
            * @return
            * @Description:设置条件，字体大小不能小于等于0
            * @author: 张旖霜
            * @date: 11/27/2023 12:51 PM
            * @version: 1.0
            */
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                if ((int)spinnerAddText.getValue() > 0) {
                    textScaleStateChanged(evt);
                }else {
                    spinnerAddText.setValue(1);
                }
            }
        });

        // dialogAddText
        GroupLayout addTextLayout = new GroupLayout(dialogAddText.getContentPane());
        dialogAddText.getContentPane().setLayout(addTextLayout);
        addTextLayout.setHorizontalGroup(
                addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTextLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(addTextLayout.createSequentialGroup()
                                                .addComponent(labelAddTextColor)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panelTextColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(labelAddTextContents)
                                        .addComponent(labelAddTextSize))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spinnerAddText, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(91, Short.MAX_VALUE))
        );
        addTextLayout.setVerticalGroup(
                addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addTextLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelAddTextContents)
                                        .addComponent(textField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(17, 17, 17)
                                .addGroup(addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelAddTextSize)
                                        .addComponent(spinnerAddText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelAddTextColor)
                                        .addComponent(panelTextColor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(82, Short.MAX_VALUE))
        );

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setStr(textField.getText());
                writeText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setStr(textField.getText());
                writeText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setStr(textField.getText());
                writeText();

            }
        });
    }

    /*
    * @param:
    * @return
    * @Description:选好位置（框选区域）后，显示字体设置面板
    * @author: 张旖霜
    * @date: 11/27/2023 12:51 PM
    * @version: 1.0
    */
    public void writeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeTextActionPerformed

        if (window.tool.region.selectRegionItem.isSelected()) {
            window.tool.region.removeRegionSelected();
            dialogAddText.setModal(true);
            dialogAddText.setVisible(true);

            window.lastPropertyValue.push(MatUtil.copyPropertyValue(window.currentPropertyValue));
            window.last.push(window.img);
            window.img = window.temp;
            initPanel();
        } else
            JOptionPane.showMessageDialog(null, "Select an area to add text!");
    }

    public void textScaleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_textScaleStateChanged

        setScale((int) spinnerAddText.getValue());
        writeText();
    }

    /*
    * @param:
    * @return
    * @Description:根据字体的设置，在图片上写入文字（之前用的是putText方法，但是这个方法不支持中文，所以只能用drawString方法，将文字“画”在图片上）
    * @author: 张旖霜
    * @date: 11/27/2023 12:52 PM
    * @version: 1.0
    */
    public void writeText() {
        window.temp = MatUtil.copy(window.img);
        int x = window.tool.region.selectedRegionLabel.getX();
        int y = window.tool.region.selectedRegionLabel.getY();


        BufferedImage bufImg = MatUtil.bufferedImg(window.temp);
        Font font = new Font("simhei", Font.PLAIN, getScale()*25);

        Graphics g = bufImg.getGraphics();

        g.setFont(font);
        g.setColor(new Color((int)getColor().val[2], (int)getColor().val[1], (int)getColor().val[0]));
        g.drawString(getStr(), x + 5, y + getScale()*25);

        g.dispose();
        window.temp = MatUtil.bufImgToMat(bufImg);
        MatUtil.show(window.temp, window.showImgRegionLabel);
    }

    /*
    * @param:
    * @return
    * @Description:点击颜色框，显示颜色选择面板
    * @author: 张旖霜
    * @date: 11/27/2023 12:52 PM
    * @version: 1.0
    */
    public void pnlTextColorMouseClicked(java.awt.event.MouseEvent evt) {

        Color color = JColorChooser.showDialog(null,"Color", Color.BLACK);

        setColor(new Scalar(color.getBlue(), color.getGreen(), color.getRed()));
        panelTextColor.setBackground(color);
        writeText();

    }

}
