package com.buaa.PhotoEditor.window.thread;

import com.buaa.PhotoEditor.util.MatUtil;
import com.buaa.PhotoEditor.window.Window;
import com.buaa.PhotoEditor.window.add.Text;

import javax.swing.*;

/**
 * ClassName: CopyThread
 * Package: com.buaa.PhotoEditor.window.thread
 * Description:
 *
 * @Author 卢思文
 * @Create 12/10/2023 10:33 AM
 * @Version 1.0
 */
public class CopyThread extends Thread {
    public Window window;
    public JMenuItem copyItem;
    public int i;

    public CopyThread(Window window, JMenuItem copyItem, int i) {
        this.window = window;
        this.copyItem = copyItem;
        this.i = i;
    }

    @Override
    public void run() {
        copyItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        copyItem.addActionListener(evt -> copySelectedRegion());
    }
    /**
     * @return void
     * @Description:获取选区并将pasting状态置1 增加未选择图片弹窗
     * @author: 罗雨曦
     * @date: 12/5/2023 3:28 PM
     * @version: 2.0
     **/
    private void copySelectedRegion() {
        window.tool.region.selectRegionItem.setSelected(false);
        //如果未选择图片，弹窗提示并return
        if (window.zoomImg == null) {
            if(i == window.counter){
                JOptionPane.showMessageDialog(null, "Please open an image first");
            }
            return;
        }
        // 如果还没有选择区域，弹出提示框
        if (window.tool.region.selectedRegionLabel[i].getBorder() == null) {
            if(i == window.counter){
                JOptionPane.showMessageDialog(null, "Please select region first");
            }
            return;
        }
        window.copyRegionImg[i] = window.zoomImg[i].submat(MatUtil.getRect(window.tool.region.selectedRegionLabel[i]));
        // pasting状态置1
        window.pasting = true;
    }

}
