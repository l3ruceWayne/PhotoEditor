package com.buaa.PhotoEditor.window.filter;

import com.buaa.PhotoEditor.window.Window;

import static com.buaa.PhotoEditor.window.Constant.*;

import com.buaa.PhotoEditor.window.thread.FilterThread;

import javax.swing.*;

/**
 * @Description 滤镜类，只需要在Window类里面new Filter(),即可实现所有滤镜相关内容初始化
 * @author 卢思文
 * @date 11/26/2023 8:58 PM
 * @version: 3.0
 */
public class Filter {
    public JMenu filterMenu;
    public Window window;
    private Gray gray;
    private Blur blur;
    private Invert invert;
    private Animize animize;
    private Focus focus;
    private Glitch glitch;
    public FilterThread[] filterThread;

    public Filter(Window window) {
        this.window = window;
        filterMenu = new JMenu("Filter");
        gray = new Gray(window);
        blur = new Blur(window);
        invert = new Invert(window);
        animize = new Animize(window);
        focus = new Focus(window);
        glitch = new Glitch(window);
        filterMenu.add(gray.grayItem);
        filterMenu.add(blur.blurItem);
        filterMenu.add(invert.invertItem);
        filterMenu.add(animize.animizeItem);
        filterMenu.add(focus.focusItem);
        filterMenu.add(glitch.glitchItem);
        // 执行多线程
        filterThread = new FilterThread[NUM_FOR_NEW];
        for (int i = 0; i <= ORIGINAL_SIZE_COUNTER; i++) {
            filterThread[i] = new FilterThread(window,
                gray,
                blur,
                invert,
                animize,
                focus,
                glitch,
                i);
            filterThread[i].start();
            // 等待线程完成，让线程可以顺序执行（方便线程中的操作）
            try {
                filterThread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
