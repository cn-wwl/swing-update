package com.wwl.update;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.wwl.update.ui.UpdateFrame;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

/**
 * @author wwl
 * @date 2022/8/3 10:20
 * @desc 更新程序
 */
@SpringBootApplication
@ComponentScan("com.wwl.*")
public class UpdateApplication implements ApplicationRunner {

    public static void main(String[] args) {
        try {
            System.setProperty("sun.java2d.noddraw", "true");
            SwingUtilities.invokeAndWait(UpdateApplication::initDesktopSetting);

            SpringApplication.run(UpdateApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDesktopSetting(){
        FlatIntelliJLaf.setup();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 更新窗口
        UpdateFrame.getInstance().start();
    }
}
