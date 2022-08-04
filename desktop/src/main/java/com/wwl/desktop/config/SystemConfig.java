package com.wwl.desktop.config;

import com.wwl.desktop.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author wwl
 * @date 2022/8/1 15:38
 * @desc 客户端配置
 */
@Configuration
public class SystemConfig {

    @Autowired
    private RequestUtils requestUtils;

    @Autowired
    private LocalConfig localConfig;

    private DesktopConfig desktopConfig;

    @Autowired
    private SystemConfigProperties configProperties;

    public DesktopConfig config(){
        if(this.desktopConfig == null){
            this.desktopConfig = this.requestUtils.loadConfig("main",configProperties.getConfigName(),configProperties.getConfigPassWord()).toJavaObject(DesktopConfig.class);
        }
        return this.desktopConfig;
    }

    public SystemConfigProperties getConfigProperties() {
        return configProperties;
    }

    public void setConfigProperties(SystemConfigProperties configProperties) {
        this.configProperties = configProperties;
    }
}
