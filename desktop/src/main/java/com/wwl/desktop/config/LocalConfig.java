package com.wwl.desktop.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wwl
 * @date 2022/7/27 13:59
 * @desc 本地配置
 */
@Configuration
public class LocalConfig {

    @Autowired
    private SystemConfigProperties systemConfig;

    private JSONObject localData = new JSONObject();


    public void saveLocalInfo(){
        try {
            File file = new File(systemConfig.getLocalDataPath());
            if(!file.exists()){
                file.createNewFile();
            }
            try (FileOutputStream fos = new FileOutputStream(file,false)){
                fos.flush();
                byte[] writeBytes = Base64Utils.encode(JSON.toJSONString(this.localData).getBytes());
                for (int i = 0; i < writeBytes.length; i++) {
                    writeBytes[i] = (byte) (writeBytes[i] - i%5);
                }
                fos.write(writeBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSetting(String key,Object value) {
        this.localData.put(key, value);
        this.saveLocalInfo();
    }

    public Object getSetting(String key) {
        return this.localData.getOrDefault(key, null);
    }

    public void clearLocalInfo(){
        File file = new File(systemConfig.getLocalDataPath());
        file.delete();
    }


}
