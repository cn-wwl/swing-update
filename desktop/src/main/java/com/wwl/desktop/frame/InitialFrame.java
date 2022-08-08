package com.wwl.desktop.frame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author wwl
 * @date 2022/7/26 13:32
 * @desc 加载窗体
 */
public class InitialFrame extends JFrame {

    public static InitialFrame initialFrame;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private InitialPanel contentPanel;
    public InitialFrame() {
        super();
        super.setUndecorated(true);

        contentPanel = new InitialPanel();
        contentPanel.setTitle("Java Swing Desktop");
        contentPanel.setSpeed(5);
        contentPanel.setDescription("客户端程序正在初始化中...");

        this.setContentPane(contentPanel);
        this.setSize(400, 240);
       // this.setAlwaysOnTop(true);

        this.setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("icons/logo.png"))).getImage());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void start() {
        this.setProgressBar(10,"应用正在启动...");
    }

    public void setProgressBar(int speed,String description){
        this.contentPanel.setSpeed(speed);
        this.contentPanel.setDescription(description);
    }

    public static InitialFrame getInstance(){
        if (initialFrame == null){
            initialFrame = new InitialFrame();
        }
        return initialFrame;
    }

}
