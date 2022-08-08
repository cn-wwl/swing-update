package com.wwl.desktop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wwl
 * @date 2022/7/27 14:00
 * @desc 系统配置属性
 */
@Configuration
@ConfigurationProperties(prefix = "com.wwl.desktop")
public class SystemConfigProperties {

    private String localDataPath="user.dat";

    private String gateway ;
    private String configUrl;

    private String updateUrl;
    private String configBase;

    private String version;

    @Value("${com.wwl.config.server.username}")
    private String configName;
    @Value("${com.wwl.config.server.password}")
    private String configPassWord;

    public String getLocalDataPath() {
        return localDataPath;
    }

    public void setLocalDataPath(String localDataPath) {
        this.localDataPath = localDataPath;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getConfigUrl() {
        return configUrl;
    }

    public void setConfigUrl(String configUrl) {
        this.configUrl = configUrl;
    }

    public String getConfigBase() {
        return configBase;
    }

    public void setConfigBase(String configBase) {
        this.configBase = configBase;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigPassWord() {
        return configPassWord;
    }

    public void setConfigPassWord(String configPassWord) {
        this.configPassWord = configPassWord;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
