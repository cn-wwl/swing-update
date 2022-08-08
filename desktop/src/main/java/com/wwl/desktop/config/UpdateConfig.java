package com.wwl.desktop.config;

import java.io.Serializable;
import java.util.List;

public class UpdateConfig implements Serializable {

    private String project;

    private List<Version> versions;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public static class Version{
        private String version;
        private String content;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
