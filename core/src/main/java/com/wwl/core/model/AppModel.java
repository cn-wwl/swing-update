package com.wwl.core.model;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

/**
 * @author wwl
 * @date 2022/8/1 11:04
 * @desc 应用信息模型
 */
public class AppModel implements Serializable {
    private String projectName;

    private String projectVersion;

    public AppModel() {
    }

    public AppModel(String projectName, String projectVersion) {
        this.projectName = projectName;
        this.projectVersion = projectVersion;
    }

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
