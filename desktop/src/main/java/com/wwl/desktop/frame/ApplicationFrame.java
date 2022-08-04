package com.wwl.desktop.frame;

import com.wwl.core.utils.SpringContextUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * @author wwl
 * @date 2022/7/26 11:36
 * @desc 应用窗口
 */
public class ApplicationFrame extends JFrame {

    public ApplicationFrame(){
        super();
        this.setTitle("Java Swing Desktop");
        this.setSize(1000,650);
        this.setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("icons/logo.png"))).getImage());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        initListener();
    }

    private void initUi(){


    }


    private void initListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                // int option = JOptionPane.showConfirmDialog(null, "确定退出吗?", "提示", JOptionPane.YES_NO_OPTION);
                //
                // if (option == JOptionPane.CLOSED_OPTION) {
                //     setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                //     return;
                // } else if (option == JOptionPane.NO_OPTION) {
                //     setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                //     return;
                // }
                System.exit(0);
            }
        });
    }
}
