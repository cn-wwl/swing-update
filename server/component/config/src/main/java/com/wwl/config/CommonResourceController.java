package com.wwl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * @author wwl
 * @date 2022/7/25 17:34
 * @desc 普通资源文件
 */
@Controller
public class CommonResourceController {

     @Value("${spring.cloud.config.server.native.search-locations}")
    private String searchPath;

    @RequestMapping(path = "/resources/download/{label}/{fileName}")
    public ResponseEntity<FileSystemResource> downLoadFile(@PathVariable String label, @PathVariable String fileName){
        File file =new File(searchPath.replace("file:","")+label+"/"+fileName);
        if(file.exists()){
            return export(file);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping(path = "/resources/upload/{label}/{fileName}")
    public ResponseEntity<String> uploadFile(@PathVariable String label,@PathVariable String fileName,@RequestParam("file") MultipartFile file
            ,@RequestParam(value = "md5",required = false,defaultValue = "xxx") String md5
            ,@RequestParam(value = "chunk",required = false,defaultValue = "0") String chunk
    ) throws IOException {
        File originFile =new File(searchPath.replace("file:","")+label+"/"+fileName);
        File originFileDic =new File(searchPath.replace("file:","")+label);
        if(!originFileDic.exists()){
            originFileDic.mkdirs();
        }
        int startIndex = Math.max(0,Integer.parseInt(chunk));
        if(!originFile.exists()){
            originFile.createNewFile();
        }
        if(startIndex>0 && startIndex>originFile.length()){
            return  contentBody("{\"code\":500,\"message\":\"参数传递出错\"}");
        }
        try(RandomAccessFile randomFile = new RandomAccessFile(originFile,"rw")) {
            randomFile.seek(startIndex);
            byte[] contentData = file.getBytes();
            randomFile.write(contentData);
        }catch (IOException ex){
            originFile.delete();
            return  contentBody("{\"code\":500,\"message\":\"上传出错\"}");
        }
        return contentBody("{\"code\":200,\"message\":\"上传成功\"}");
    }

    private ResponseEntity<String> contentBody(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(content.getBytes().length)
                .contentType(MediaType.parseMediaType("application/json"))
                .body(content);
    }
    private ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }





}
