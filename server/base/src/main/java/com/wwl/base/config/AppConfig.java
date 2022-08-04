package com.wwl.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author wwl
 * @date 2022/8/1 10:57
 * @desc TODO
 */
@Configuration
public class AppConfig {

    @Value("${app.project.name}")
    private String projectName;


    @Value("${app.project.version}")
    private String projectVersion;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }
}
