package com.wwl.update.utils;

import com.wwl.core.utils.FileUtils;
import com.wwl.core.utils.ZipUtils;
import com.wwl.update.config.SystemConfig;
import com.wwl.update.config.SystemConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * @author wwl
 * @date 2022/8/3 10:30
 * @desc 更新服务类
 */
@Service
public class UpdateService {

    @Autowired
    private SystemConfigProperties properties;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private RequestUtils requestUtils;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 项目根目录
     */
    private String rootDirPath;

    /**
     * 更新文件 临时目录
     */
    private File updateTempDirFile;

    /**
     * 更新文件路径
     */
    private File updateFile;

    private String currentVersion;
    private String lastVersion;


    /**
     * 版本验证
     */
    public boolean versionVerify() {
        currentVersion = properties.getVersion();
        lastVersion =systemConfig.config().getVersion();
        return currentVersion.equals(lastVersion);
    }

    /**
     * 更新文件下载
     */
    public boolean downloadFile() {
        try {
            // 下载 更新文件

            String configUrl = systemConfig.getConfigProperties().getConfigBase();

            String downLoadFileName = this.getDownloadFileName();

            String fileUrl;
            if (configUrl.endsWith("/") ){
                fileUrl = String.format("%sresources/download/update/%s.zip", configUrl, downLoadFileName);
            }else {
                fileUrl = String.format("%s/resources/download/update/%s.zip", configUrl, downLoadFileName);
            }

            rootDirPath = new File("").getCanonicalPath();
            updateTempDirFile = new File(rootDirPath + "\\UpdateTemp");
            if (!updateTempDirFile.exists()) {
                updateTempDirFile.mkdir();
            }
            updateFile = new File(updateTempDirFile.getAbsoluteFile() + "\\" + systemConfig.config().getVersion() + ".zip");
            if (!updateFile.exists()) {
                updateFile.createNewFile();
            }
            requestUtils.downloadFileToPath(fileUrl, updateFile.getAbsolutePath());
        } catch (Exception exception) {
            logger.error("downloadFile 失败",exception);
            return false;
        }
        return true;
    }

    /**
     * 如果本地版本落后于最新版两个小版本，则直接下载最新版
     * @return
     */
    private String getDownloadFileName() {
        int currentVersionInt = this.getVersionInt(this.currentVersion);
        int lastVersionInt = this.getVersionInt(this.lastVersion);
        if (lastVersionInt - currentVersionInt > 2) {
            return "last";
        }
        return this.lastVersion;
    }

    private int getVersionInt(String version) {

        // 版本号规则
        // V= x.y.z = x * 100 + y * 10 + z
        String[] versionArray = version.split("\\.");
        return Integer.parseInt(versionArray[0]) * 100 +
                Integer.parseInt(versionArray[1]) * 10
                + Integer.parseInt(versionArray[2]);
    }

    /**
     * 解压更新文件
     * @return
     */
    public boolean unzipUpdateFile() {
        try {
            ZipUtils.unzip(updateFile.getAbsolutePath(), updateTempDirFile.getAbsolutePath());

        } catch (Exception exception) {
            logger.error("unzipUpdateFile 失败",exception);
            return false;
        }
        return true;
    }

    /**
     * 替换更新文件
     * @return
     */
    public boolean updateFileReplace() {
        try {

            String filePath = null;
            for (File file : Objects.requireNonNull(updateTempDirFile.listFiles())) {
                if (file.isDirectory()){
                    filePath = file.getAbsolutePath();
                    break;
                }
            }

            FileUtils.copyDirectory(filePath, rootDirPath);
            //删除临时目录
            FileUtils.removeFile(updateTempDirFile);
        } catch (Exception exception) {
            logger.error("updateFileReplace 失败", exception);
            return false;
        }
        return true;
    }


    /**
     * 本地版本号更新
     */
    public boolean localVersionChange(){
        String propertiesFile = rootDirPath+"\\application.properties";
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFile));
            properties.setProperty("com.wwl.desktop.version",systemConfig.config().getVersion());
            //true表示追加打开
            FileOutputStream oFile = new FileOutputStream(propertiesFile);
            properties.store(oFile,"");
            oFile.close();
        } catch (IOException e) {
            logger.error("updateFileReplace 失败", e);
            return false;
        }
        return true;
    }

    public void desktopStart(){
        try {
            String desktopExe = rootDirPath + "\\Desktop.exe";

            Runtime.getRuntime().exec("cmd /k start " + desktopExe);
            System.exit(0);
        } catch (IOException e) {
            logger.error("desktopUpdate 失败", e);
        }
    }
}
