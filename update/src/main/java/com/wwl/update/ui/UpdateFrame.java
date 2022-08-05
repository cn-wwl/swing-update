package com.wwl.update.ui;

import com.wwl.core.utils.SpringContextUtils;
import com.wwl.update.utils.UpdateService;
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
public class UpdateFrame extends JFrame {

    @Autowired
    public UpdateService updateService;

    public static UpdateFrame initialFrame;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private LoadingPanel contentPanel;
    public UpdateFrame() {
        super();
        super.setUndecorated(true);

        contentPanel = new LoadingPanel();
        contentPanel.setTitle("Java Swing Update");
        contentPanel.setSpeed(5);
        contentPanel.setDescription("客户端程序正在初始化中...");

        this.setContentPane(contentPanel);
        this.setSize(400, 240);
       // this.setAlwaysOnTop(true);

        this.setIconImage(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("icons/logo.png"))).getImage());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        SpringContextUtils.injectService(this);
    }

    public void start() {
        try {
            this.setProgressBar(10, "正在进行版本校验...");
            if (this.updateService.versionVerify()) {
                JOptionPane.showMessageDialog(this, "已经是最新版本");
                System.exit(0);
            }

            this.setProgressBar(20, "正在下载更新文件...");
            if (!updateService.downloadFile()){
                JOptionPane.showMessageDialog(this, "更新文件下载失败");
                System.exit(0);
            }

            this.setProgressBar(50, "正在解压更新文件...");
            if (!updateService.unzipUpdateFile()){
                JOptionPane.showMessageDialog(this, "文件解压失败");
                System.exit(0);
            }
            this.setProgressBar(70, "正在替换程序文件...");
            if (!updateService.updateFileReplace()){
                JOptionPane.showMessageDialog(this, "替换程序文件失败");
                System.exit(0);
            }

            this.setProgressBar(80, "正在更新资源文件内容...");
            if (!updateService.localVersionChange()){
                JOptionPane.showMessageDialog(this, "更新资源文件内容失败");
                System.exit(0);
            }

           // this.setProgressBar(100, "程序升级完毕，即将启动");
            JOptionPane.showMessageDialog( this, "程序升级完毕，确认启动");
            updateService.desktopStart();
        } catch (Exception e) {
            logger.error("start错误",e);
        }
    }

    private void setProgressBar(int speed,String description){
        this.contentPanel.setSpeed(speed);
        this.contentPanel.setDescription(description);
    }


    public static UpdateFrame getInstance(){
        if (initialFrame == null){
            initialFrame = new UpdateFrame();
        }
        return initialFrame;
    }

}
