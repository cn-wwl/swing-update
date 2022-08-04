package com.wwl.update.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wwl.core.base.*;
import com.wwl.core.utils.ConvertUtils;
import com.wwl.core.utils.HttpUtils;
import com.wwl.update.config.SystemConfigProperties;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * @author wwl
 * @date 2022/7/27 14:03
 * @desc 网络请求工具类
 */
@Component
public class RequestUtils {


    private HttpUtils httpUtils = HttpUtils.loadInstance();

    @Autowired
    private SystemConfigProperties systemConfig;

    @Value("${com.wwl.page.index:pageIndex}")
    private String pageIndexKey="pageIndex";

    @Value("${com.wwl.page.size:pageSize}")
    private String pageSizeKey="pageSize";

    @Value("${com.wwl.page.sort:sortField}")
    private String sortFieldKey="sortField";

    private final static int UPLOAD_PAGE_SIZE = 2 * 1024*1024;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String loginToken;

    private Map<String, JSONObject> pluginConfigs = new HashMap<>();

    public Result<String> userLogin(String userName,String userPwd) {
        Map<String,Object> paras = new HashMap<>();
        paras.put("userName",userName);
        paras.put("userPwd",userPwd);
        try {
            String result =   this.httpUtils.executeStringRequest(HttpUtils.RequestType.POST,systemConfig.getGateway()+"authorization-server/login",paras,null,false,null,null);
            Result loginResult = JSON.parseObject(result,Result.class);
            if(loginResult.isSuccess()) {
                this.loginToken = loginResult.getData()+"";
            }
            return loginResult;
        } catch (IOException e) {
            this.logger.error("登陆请求异常",e);
            return Result.fail(ErrorType.SYSTEM_ERROR,"登陆请求异常");
        }
    }

    public JSONObject userInfo(String userName){
        Map<String,Object> paras = new HashMap<>();
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        paras.put("account",userName);
        try {
            return JSONObject.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.POST,systemConfig.getGateway()+"authorization-server/system/user/current",paras,authInfo,false,null,null));
        } catch (IOException e) {
            this.logger.error("获取用户信息异常",e);
            return null;
        }
    }

    public JSONObject loadConfig(String plugin,String name,String password){
        String requestUrl = this.systemConfig.getConfigUrl().replace("{plugin}",plugin);
        try {
            if(pluginConfigs.containsKey(plugin)) {
                return pluginConfigs.get(plugin);
            }
            Map<String,String> authInfo = new HashMap<>();
            String authorInfo = String.format("%s:%s",name,password);
            Base64 base64 = new Base64();
            String base64Info = base64.encodeToString(authorInfo.getBytes());
            authInfo.put("Authorization","Basic "+ base64Info);

            String aa=  this.httpUtils.executeStringRequest(HttpUtils.RequestType.GET,requestUrl,null,authInfo,false,null,null);

            JSONObject object = JSONObject.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.GET,requestUrl,null,authInfo,false,null,null));
            pluginConfigs.put(plugin,object);
            return object;
        } catch (IOException e) {
            this.logger.error("插件配置加载异常",e);
            return new JSONObject();
        }
    }

    public  Result postData(String subPath, Map<String,Object> paras,boolean isJsonRequest) {
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.POST,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,isJsonRequest,null,null),Result.class);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    public <T> T postData(String subPath, Map<String,Object> paras,boolean isJsonRequest,Class<T> tClass) {
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.POST,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,isJsonRequest,null,null),tClass);
        } catch (Exception e) {
            return null;
        }
    }


    public Result putData(String subPath, Map<String,Object> paras,boolean isJsonRequest) {
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.PUT,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,isJsonRequest,null,null),Result.class);
        } catch (Exception e) {
            return null;
        }
    }


    public  Result doGetData(String subPath, Map<String,Object> paras) {
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.GET,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,false,null,null),Result.class);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    public <T> T doGetData(String subPath, Map<String,Object> paras,Class<T> tClass) {
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.GET,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,false,null,null),tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> doGetListData(String subPath, Map<String,Object> paras, Class<T> tClass) {

        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            List<T> result = new ArrayList<>();
            JSON.parseArray(this.httpUtils.executeStringRequest(HttpUtils.RequestType.GET,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,false,null,null)).forEach(to->{
                result.add(((JSONObject)to).toJavaObject(tClass));
            });
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> doPostListData(String subPath, Map<String,Object> paras, Class<T> tClass) {

        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        if(paras==null){
            paras = new HashMap<>();
        }
        try {
            List<T> result = new ArrayList<>();
            JSONArray jsonArray=  JSON.parseArray(this.httpUtils.executeStringRequest(HttpUtils.RequestType.POST,subPath.contains("http://")?
                    subPath : systemConfig.getGateway()+subPath,paras,authInfo,true,null,null));

            jsonArray.forEach(to->{
                result.add((T)to);
            });
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public Result doDeleteData(String subPath, Map<String,Object> paras){
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.DELETE,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,true,null,null),Result.class);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    public Result doDeleteData(String subPath, Map<String,Object> paras,boolean isJsonRequest){
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            return JSON.parseObject(this.httpUtils.executeStringRequest(HttpUtils.RequestType.DELETE,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,isJsonRequest,null,null),Result.class);
        } catch (Exception e) {
            return Result.fail();
        }
    }

    public <T extends Serializable> PageResult<T> doGetPageData(String subPath, Map<String,Object> paras, int pageIndex, int pageSize, Class<T> tClass) {

        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        if(paras==null){
            paras = new HashMap<>();
        }
        paras.put(pageIndexKey,pageIndex);
        paras.put(pageSizeKey,pageSize);
        try {
            String result = this.httpUtils.executeStringRequest(HttpUtils.RequestType.GET,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,false,null,null);
            JSONObject jsonObject = JSON.parseObject(result);
            if(jsonObject.containsKey("code") && !Objects.equals(jsonObject.getString("code"), "200")){
                return PageResult.of(new ArrayList<>(),0);
            }
            return PageResult.of(jsonObject.getJSONArray("pageData").toJavaList(tClass),jsonObject.getInteger("totalCount"));
        } catch (Exception e) {
            return null;
        }
    }

    public <T extends Serializable> PageResult<T> doPostPageData(String subPath, Map<String,Object> paras,boolean jsonRequest,int pageIndex,int pageSize, Class<T> tClass) {
        subPath += ( subPath.contains("?")?"&":"?" ) + pageIndexKey + "="+pageIndex+"&"+pageSizeKey+"="+pageSize;
        Map<String,String> authInfo = new HashMap<>();
        authInfo.put("X-Requested-With","token="+this.loginToken);
        try {
            String jsonBody = this.httpUtils.executeStringRequest(HttpUtils.RequestType.POST,subPath.contains("http://")? subPath : systemConfig.getGateway()+subPath,paras,authInfo,jsonRequest,null,null);
            JSONObject jsonObject = JSONObject.parseObject(jsonBody);
            if(!Objects.equals(jsonObject.getString("code"), Result.SUCCESSFUL_CODE)){
                return PageResult.fail(Result.FAIL_CODE,jsonObject.getString("error"));
            }else{
                PageHelper pageHelper=new PageHelper(Integer.parseInt(jsonObject.get("pageIndex").toString()),
                        Integer.parseInt(jsonObject.get("pageSize").toString()),
                        Integer.parseInt(jsonObject.get("totalCount").toString())
                );

                return PageResult.of(jsonObject.getJSONArray("pageData").toJavaList(tClass),pageHelper);
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
        return PageResult.fail("获取出错");
    }

    public byte[] downloadFile(String fileUrl){
        try {
            fileUrl = fileUrl.contains("http://")?fileUrl:systemConfig.getGateway()+fileUrl;
            return this.httpUtils.downLoadFile(fileUrl);
        } catch (IOException e) {
            return new byte[0];
        }
    }
    public void downloadFileToPath(String fileUrl,String filePath){
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
            this.httpUtils.downLoadFileIntoStream(fileUrl,fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Result<String> uploadFile(String url,byte[] datas){
        url = url.contains("http://")?url:systemConfig.getGateway()+url;
        Map<String,byte[]> uploadDatas = new HashMap<>();
        uploadDatas.put("file",datas);
        try {
            this.httpUtils.uploadDatas(url,new HashMap<>(),uploadDatas);
        } catch (IOException e) {
            return Result.fail(ErrorType.BUSINESS_ERROR,"无法上传文件");
        }
        return Result.success("上传成功");
    }

    public Result<String> uploadPatchFile(String url,int chunkSize,byte[] datas){
        url = url.contains("http://")?url:systemConfig.getGateway()+url;
        Map<String,byte[]> uploadDatas = new HashMap<>();
        uploadDatas.put("file",datas);
        try {
            this.httpUtils.uploadDatas(url,chunkSize,new HashMap<>(),uploadDatas);
        } catch (IOException e) {
            return Result.fail(ErrorType.BUSINESS_ERROR,"无法上传文件");
        }
        return Result.success("上传成功");
    }

    public Result<String> uploadFile(String url,String filePath){
        if(new File(filePath).length()>2*1024*1024){
            return biggerFileUpload(url, filePath);
        }
        try (FileInputStream inputStream = new FileInputStream(filePath)){
            byte[] uploadData = new byte[inputStream.available()];
            inputStream.read(uploadData,0,inputStream.available());
            return this.uploadFile(url,uploadData);
        }catch (Exception ex){
            return Result.fail("文件处理出错");
        }
    }

    public Result<String> biggerFileUpload(String url,String filePath){
        try (FileInputStream inputStream = new FileInputStream(filePath)){
            int sendLength = 0;
            while (inputStream.available()>0){
                byte[] uploadData = new byte[Math.min(UPLOAD_PAGE_SIZE,inputStream.available())];
                inputStream.read(uploadData);
                Result uploadResult = this.uploadPatchFile(url,sendLength,uploadData);
                if(uploadResult.isFail()){
                    System.out.println("url = [" + url + "], filePath = [" + filePath + "]");
                    return Result.fail();
                }
                sendLength += uploadData.length;
            }
            return Result.success("");
        }catch (Exception ex){
            return Result.fail("文件处理出错");
        }
    }
    public void asyncBiggerFileUpload(String url, String filePath, Consumer<Double> onProgress) {

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            int sendLength = 0;
            int totalLength = inputStream.available();
            while (inputStream.available() > 0) {
                byte[] uploadData = new byte[Math.min(UPLOAD_PAGE_SIZE, inputStream.available())];
                inputStream.read(uploadData);
                Result uploadResult = this.uploadPatchFile(url, sendLength, uploadData);
                if (uploadResult.isFail()) {
                    if (onProgress != null) {
                        onProgress.invoke(-1.0);
                        break;
                    }
                }
                sendLength += uploadData.length;
                if (onProgress != null) {
                    onProgress.invoke(ConvertUtils.round(sendLength * 100.0 / (totalLength * 1.0), 2));
                }
            }
        } catch (Exception ex) {
        }
    }


}
