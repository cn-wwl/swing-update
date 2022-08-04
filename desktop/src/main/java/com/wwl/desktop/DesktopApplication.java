package com.wwl.desktop;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.wwl.desktop.frame.InitialFrame;
import com.wwl.desktop.global.FrameContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

/**
 * @author wwl
 * @date 2022/7/26 11:25
 * @desc 桌面应用
 */
@SpringBootApplication
@ComponentScan("com.wwl.*")
public class DesktopApplication implements ApplicationRunner {
    public static void main(String[] args) {
        try {
            System.setProperty("sun.java2d.noddraw", "true");
            SwingUtilities.invokeAndWait(DesktopApplication::initDesktopSetting);

            // 初始化窗口
            InitialFrame.getInstance().start();
            SpringApplication.run(DesktopApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDesktopSetting(){
        FlatIntelliJLaf.setup();
    }

    @Autowired
    private FrameContext frameContext;

    @Override
    public void run(ApplicationArguments args) {
        frameContext.start();
    }
}
