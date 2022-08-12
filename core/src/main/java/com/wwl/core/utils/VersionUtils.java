package com.wwl.core.utils;

public class VersionUtils {

    public static int getVersionInt(String version) {

        // 版本号规则
        // V= x.y.z = x * 100 + y * 10 + z
        String[] versionArray = version.split("\\.");
        return Integer.parseInt(versionArray[0]) * 100 +
                Integer.parseInt(versionArray[1]) * 10
                + Integer.parseInt(versionArray[2]);
    }

}
