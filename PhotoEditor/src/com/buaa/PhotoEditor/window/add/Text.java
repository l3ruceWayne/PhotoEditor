
package com.buaa.PhotoEditor.window.add;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import org.opencv.core.Scalar;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;


public class Text {

    public Window window;
    public JMenuItem addTextItem;
    
    private Scalar color;
    private int scale;
    private String str;

    // dialog
    public JDialog dialogAddText;
    public JPanel panelAddTextColor;
    public JLabel labelAddTextContents;
    public JLabel labelAddTextSize;
    public JLabel labelAddTextColor;
    public JSpinner spinnerAddText;
    public JTextField textField;

    public Text(Window window){
        scale = 1;
        color = new Scalar(0,0,0);
        str = "";

        this.window = window;

        addTextItem = new JMenuItem("Add Text");
        dialogAddText = new JDialog();
        panelAddTextColor = new JPanel();
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

    public void createAddTextPanel() {
        dialogAddText.setSize(400, 400);
        dialogAddText.setTitle("Text");
        dialogAddText.setResizable(false);
        dialogAddText.setLocationRelativeTo(null);

        spinnerAddText.setValue(1);

        panelAddTextColor.setBackground(new java.awt.Color(0, 0, 0));
        panelAddTextColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlTextColorMouseClicked(evt);
            }
        });

        // panelAddTextColor
        GroupLayout pnlTextColorLayout = new javax.swing.GroupLayout(panelAddTextColor);
        panelAddTextColor.setLayout(pnlTextColorLayout);
        pnlTextColorLayout.setHorizontalGroup(
                pnlTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 21, Short.MAX_VALUE)
        );
        pnlTextColorLayout.setVerticalGroup(
                pnlTextColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 15, Short.MAX_VALUE)
        );

        spinnerAddText.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                textScaleStateChanged(evt);
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
                                                .addComponent(panelAddTextColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(panelAddTextColor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    public void writeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_writeTextActionPerformed

        if (window.region.selectRegionItem.isSelected()) {

            dialogAddText.setModal(true);
            dialogAddText.setVisible(true);

            window.last.push(window.img);
            window.img = window.temp;

            window.region.removeRegionSelected();
        } else
            JOptionPane.showMessageDialog(null, "Select an area to add text!");

    }

    public void textScaleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_textScaleStateChanged

        setScale((int) spinnerAddText.getValue());
        writeText();
    }

    public void writeText() {
        window.temp = MatUtil.copy(window.img);
        MatUtil.writeText(this, window.temp, MatUtil.getRect(window.region.selectedRegionLabel));
        MatUtil.show(window.temp, window.showImgRegionLabel);
    }

    public void pnlTextColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTextColorMouseClicked

        Color color = JColorChooser.showDialog(null,
                "选择颜色",
                Color.BLACK);

        setColor(new Scalar(color.getBlue(), color.getGreen(), color.getRed()));
        panelAddTextColor.setBackground(color);
        writeText();

    }
    
}
