package com.wwl.desktop.global;

import com.wwl.core.utils.VersionUtils;
import com.wwl.desktop.config.SystemConfig;
import com.wwl.desktop.config.SystemConfigProperties;
import com.wwl.desktop.frame.ApplicationFrame;
import com.wwl.desktop.frame.InitialFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author wwl
 * @date 2022/7/26 13:57
 * @desc 窗体上下文
 */
@Component
public class FrameContext {

    @Autowired
    private SystemConfig systemConfig;

    private static ApplicationFrame applicationFrame;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void start() {
        try {
            this.versionVerify();

            Thread.sleep(500);
            InitialFrame.getInstance().setProgressBar(25,"正在加载插件...");

            Thread.sleep(500);
            InitialFrame.getInstance().setProgressBar(50, "正在启动xxx引擎...");

            Thread.sleep(500);
            InitialFrame.getInstance().setProgressBar(75, "正在加载数据...");

            Thread.sleep(500);
            InitialFrame.getInstance().setProgressBar(100, "应用加载完成...");

            if (applicationFrame == null) {
                applicationFrame = new ApplicationFrame();
            }
            InitialFrame.getInstance().setVisible(false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 版本验证
     */
    private void versionVerify() {
        int currentVersionInt = VersionUtils.getVersionInt(systemConfig.properties().getVersion());
        int lastVersionInt = VersionUtils.getVersionInt(systemConfig.config().getVersion());

        if (currentVersionInt == lastVersionInt){
            return;
        }else if (currentVersionInt < lastVersionInt){
            JOptionPane.showMessageDialog( InitialFrame.getInstance(), "发现新的应用版本需要更新");
            this.desktopUpdate();
        }else {
            JOptionPane.showMessageDialog(InitialFrame.getInstance(), "本地版本配置错误");
            System.exit(0);
        }
    }



    private void desktopUpdate(){
        try {
            File rootFile = new File("");
            String updateExe = rootFile.getAbsolutePath() + "\\update.exe";

            Runtime.getRuntime().exec("cmd /k start " + updateExe);
            System.exit(0);
        } catch (IOException e) {
            logger.error("desktopUpdate 错误",e);
        }
    }

    public static ApplicationFrame getApplicationFrame() {
        return applicationFrame;
    }
}
