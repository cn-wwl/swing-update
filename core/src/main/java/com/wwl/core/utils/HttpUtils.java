package com.wwl.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.wwl.core.base.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wwl
 * @date 2022/7/27 14:04
 * @desc http请求工具类
 */
public class HttpUtils {


    private static HttpUtils instance;

    private static org.slf4j.Logger Logger = LoggerFactory.getLogger(HttpUtils.class);

    public HttpUtils(HttpOptions option){
        this.option = option;
    }

    public static HttpUtils loadInstance(){
        if(instance==null){
            instance = new HttpUtils(getDefaultOptions());
        }
        return instance;
    }

    public void destroy(){
        try {
            if(this.httpClient==null) {
                return;
            }
            this.httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private HttpOptions option;

    private CloseableHttpClient httpClient;

    /**
     * 发送web请求
     * @param requestType 请求类型 GET,POST,PUT,DELETE,默认为GET
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     * @param jsonRequest 参数传递类型是否为json
     * @param userName 请求验证信息 用户名（两个都不为空才加验证信息）
     * @param passWord 请求验证信息 密码（两个都不为空才加验证信息）
     * @return 请求结果字节
     * @throws IOException 请求异常
     */
    public byte[] executeRequest(RequestType requestType , String url, Map<String,Object> params, Map<String,String> headers, boolean jsonRequest, String userName, String passWord) throws IOException {
        CloseableHttpClient client = loadHttpClient();
        return getRequestBytes(requestType, url, params, headers, jsonRequest, userName, passWord, client);
    }

    private byte[] getRequestBytes(RequestType requestType, String url, Map<String, Object> params, Map<String, String> headers, boolean jsonRequest, String userName, String passWord, CloseableHttpClient client) throws IOException {
        HttpRequestBase request = loadHttpRequest(requestType,url,jsonRequest,params);
        if(headers!=null)
        {
            inputHeaders(request,headers);
        }

        if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(passWord))
        {
            HttpClientContext context =  HttpClientContext.adapt( new BasicHttpContext());
            context.setRequestConfig(defaultRequestConfig());
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(userName,passWord));
            context.setCredentialsProvider(credsProvider);
            try (CloseableHttpResponse response = client.execute(request,context)){
                if(response.getEntity()==null)
                {
                    return new byte[0];
                }
                return readStream(response.getEntity().getContent());
            }
        }
        try (CloseableHttpResponse response = client.execute(request)){
            if(response.getEntity()==null)
            {
                return new byte[0];
            }
            return readStream(response.getEntity().getContent());
        }
    }


    /**
     * 发送web请求并得到字符串
     * @param requestType 请求类型 GET,POST,PUT,DELETE,默认为GET
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     * @param jsonRequest 参数传递类型是否为json
     * @param userName 请求验证信息 用户名（两个都不为空才加验证信息）
     * @param passWord 请求验证信息 密码（两个都不为空才加验证信息）
     * @return 请求结果字节
     * @throws IOException 请求异常
     */
    public String executeStringRequest(RequestType requestType , String url, Map<String,Object> params, Map<String,String> headers, boolean jsonRequest, String userName, String passWord) throws IOException {
        return inputStream2String(executeRequest(requestType,url,params,headers,jsonRequest,userName,passWord));
    }


    /**
     * 上传多媒体
     * @param url 上传地址
     * @param headers 头信息
     * @param mapDatas 文件信息
     * @return 上传结果
     */
    public Result uploadDatas(String url, Map<String,String> headers,Map<String,byte[]> mapDatas) throws IOException {
        CloseableHttpClient client = loadHttpClient();
        HttpPost request = new HttpPost(url);
        if(headers!=null)
        {
            inputHeaders(request,headers);
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        mapDatas.forEach((key,datas)->{
            builder.addBinaryBody(key,datas,ContentType.APPLICATION_OCTET_STREAM,key);
        });
        request.setEntity( builder.build());
        return executeUpload(client, request);
    }
    /**
     * 分段上传多媒体
     * @param url 上传地址
     * @param headers 头信息
     * @param mapDatas 文件信息
     * @param chunkSize 已上传大小
     * @return 上传结果
     */
    public Result uploadDatas(String url,int chunkSize, Map<String,String> headers,Map<String,byte[]> mapDatas) throws IOException {
        CloseableHttpClient client = loadHttpClient();
        HttpPost request = new HttpPost(url);
        if(headers!=null)
        {
            inputHeaders(request,headers);
        }
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        mapDatas.forEach((key,datas)->{
            builder.addBinaryBody(key,datas, ContentType.APPLICATION_OCTET_STREAM,key);
        });
        builder.addTextBody("chunk",chunkSize+"");
        request.setEntity( builder.build());
        return executeUpload(client, request);
    }

    private Result executeUpload(CloseableHttpClient client, HttpPost request) throws IOException {
        try (CloseableHttpResponse response = client.execute(request)){
            if(response.getEntity()==null)
            {
                return Result.success();
            }
            return JSONObject.parseObject(inputStream2String(readStream(response.getEntity().getContent())),Result.class);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("请求上传出错:",e);
            return Result.fail();
        }
    }

    /**
     * 下载文件
     * @param fileUrl 文件地址
     * @return 文件字节流
     * @throws IOException 文件不存在或者其他异常
     */
    public byte[] downLoadFile(String fileUrl) throws IOException {
        int bytesum = 0;
        int byteread;
        URL url = new URL(fileUrl);
        try {
            URLConnection conn = url.openConnection();
            try(InputStream inStream = conn.getInputStream(); ByteArrayOutputStream outSteam = new ByteArrayOutputStream()) {                ;
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    outSteam.write(buffer, 0, byteread);
                }
                return outSteam.toByteArray();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 下载文件
     * @param fileUrl 文件地址
     * @param outputStream 要写入的文件流
     * @return 文件字节流
     * @throws IOException 文件不存在或者其他异常
     */
    public void downLoadFileIntoStream(String fileUrl, OutputStream outputStream) throws IOException {
        int byteRead;
        URL url = new URL(fileUrl);
        try {
            URLConnection conn = url.openConnection();
            try(InputStream inStream = conn.getInputStream()) {                ;
                byte[] buffer = new byte[1024];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteRead);
                }
                outputStream.flush();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private HttpRequestBase loadHttpRequest(RequestType requestType ,String url,boolean jsonRequest, Map<String,Object> params)
    {
        switch (requestType)
        {
            case POST:
                HttpPost post = new HttpPost(url);
                if(params!=null)
                {
                    post.setEntity(stringEntity(jsonRequest,params));
                }
                return post;
            case PUT:
                HttpPut put = new HttpPut(url);
                if(params!=null) {
                    put.setEntity(stringEntity(jsonRequest, params));
                }
                return put;
            case DELETE:
                return new HttpDelete(appendRequestParam(url,params));
            default:
                return new HttpGet(appendRequestParam(url,params));
        }
    }

    private void inputHeaders(HttpRequestBase client,Map<String,String> headers)
    {
        for (Map.Entry<String, String> entry:headers.entrySet()){
            client.addHeader(entry.getKey(),entry.getValue());
        }
    }


    private AbstractHttpEntity stringEntity(boolean jsonRequest, Map<String,Object> stringObjectMap)
    {
        if(jsonRequest)
        {
            return   new StringEntity( stringObjectMap.containsKey("")? JSONObject.toJSONString(stringObjectMap.get("")):JSONObject.toJSONString(stringObjectMap),ContentType.APPLICATION_JSON);
        }
        return  formEntity(stringObjectMap);
    }
    private UrlEncodedFormEntity formEntity(Map<String,Object> stringObjectMap)
    {
        List<NameValuePair> nvps = new ArrayList<>();
        for (Map.Entry<String, Object> entry:stringObjectMap.entrySet()){
            nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue() == null ? null :ConvertUtils.getString(entry.getValue())));
        }
        return new UrlEncodedFormEntity(nvps, Consts.UTF_8);
    }

    private String appendRequestParam(String fixUrl,Map<String,Object> stringObjectMap)
    {
        if(stringObjectMap==null)
        {
            return fixUrl;
        }
        StringBuilder builder = new StringBuilder(fixUrl);
        if(!fixUrl.contains("?"))
        {
            builder.append("?lmtimestamp=").append(System.currentTimeMillis()+"");
        };
        for (Map.Entry<String,Object> entry:stringObjectMap.entrySet())
        {
            builder.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue()+""));
        }
        return builder.toString();
    }

    private  CloseableHttpClient loadHttpClient()
    {
        if(httpClient!=null)
        {
            return httpClient;
        }
        httpClient = buildHttpClient();
        return httpClient;
    }

    private  CloseableHttpClient buildHttpClient() {
        ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        RegistryBuilder<ConnectionSocketFactory> registryBuilder =  RegistryBuilder.<ConnectionSocketFactory> create().register("https",PlainConnectionSocketFactory.getSocketFactory()).register("http", plainSocketFactory);
        PoolingHttpClientConnectionManager manager= new PoolingHttpClientConnectionManager(registryBuilder.build());
        manager.setMaxTotal(this.option.getMaxConnect());
        manager.setDefaultMaxPerRoute(this.option.getMaxPreRoute());
        return HttpClients.custom().setConnectionManager(manager).setRetryHandler((exception, executionCount, context) -> false).setDefaultRequestConfig(defaultRequestConfig()).build();
    }

    private RequestConfig defaultRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(this.option.getConnectTimeout())
                .setConnectionRequestTimeout(this.option.getConnectRequestTimeout())
                .setSocketTimeout(this.option.getConnectSocketTimeout())
                .build();
    }

    public static byte[] readStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    public static class HttpOptions{

        public HttpOptions(){

        }

        private int maxConnect;

        private int maxPreRoute;

        private int connectTimeout;

        private int connectRequestTimeout;

        private int connectSocketTimeout;

        public int getMaxConnect() {
            return maxConnect;
        }

        public HttpOptions setMaxConnect(int maxConnect) {
            this.maxConnect = maxConnect;
            return this;
        }

        public int getMaxPreRoute() {
            return maxPreRoute;
        }

        public HttpOptions setMaxPreRoute(int maxPreRoute) {
            this.maxPreRoute = maxPreRoute;
            return this;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public HttpOptions setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public int getConnectRequestTimeout() {
            return connectRequestTimeout;
        }

        public HttpOptions setConnectRequestTimeout(int connectRequestTimeout) {
            this.connectRequestTimeout = connectRequestTimeout;
            return this;
        }

        public int getConnectSocketTimeout() {
            return connectSocketTimeout;
        }

        public HttpOptions setConnectSocketTimeout(int connectSocketTimeout) {
            this.connectSocketTimeout = connectSocketTimeout;
            return this;
        }

    }

    private static HttpOptions defaultOption;

    public static HttpOptions getDefaultOptions(){
        if(defaultOption==null) {
            defaultOption = new HttpOptions().setMaxConnect(10).setMaxPreRoute(3).setConnectRequestTimeout(10000).setConnectTimeout(60000).setConnectSocketTimeout(60000);
        }
        return defaultOption;
    }

    public static   String inputStream2String(InputStream is)   throws   IOException{
        ByteArrayOutputStream baos   =   new ByteArrayOutputStream();
        int   i=-1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }

    public static   String   inputStream2String(byte[] is)   throws   IOException{
        ByteArrayOutputStream baos   =   new ByteArrayOutputStream(is.length);
        baos.write(is);
        return new String(baos.toByteArray(), Charset.forName("UTF-8"))   ;
    }

    private static SSLContext emptySSLContext() {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
        } catch (Exception e) {
            return null;
        }
        return  ctx;
    }


    public enum RequestType{
        GET,
        POST,
        PUT,
        DELETE
    }
}
