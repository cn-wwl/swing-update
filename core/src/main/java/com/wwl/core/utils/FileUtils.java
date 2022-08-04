package com.wwl.core.utils;

import java.io.*;
import java.nio.file.FileSystemException;
import java.util.Objects;

/**
 * @author wwl
 * @date 2022/8/3 13:54
 * @desc 文件
 */
public class FileUtils {

    public static void moveFile(String sourcePath,String targetPath) throws FileSystemException {
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        if (sourceFile.exists() && targetFile.exists()) {
            for (File file : Objects.requireNonNull(sourceFile.listFiles())) {

                boolean result = file.renameTo(new File(targetFile.getAbsoluteFile() + "\\" + file.getName()));
                if (!result) {
                    throw new FileSystemException(String.format("移动失败: \n\t源文件：%s \n\t 目标目录：%s", file.getAbsolutePath(), targetPath));
                }
            }
        }
    }

    public static void removeFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                //将目录逐层拆分成数组元素
                File[] files = file.listFiles();
                for (File subFile : files) {
                    //递归调用,由最底层往上逐层删除
                    removeFile(subFile);
                }
            }
            //如果是文件，直接删除
            file.delete();
        }
    }

    public static void copyFile(File sourceFile,File targetFile) throws FileSystemException{
        if(!sourceFile.canRead()){
            throw new FileSystemException("源文件" + sourceFile.getAbsolutePath() + "不可读，无法复制！");
        }else{
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;

            try{
                fis = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fis);
                fos = new FileOutputStream(targetFile);
                bos = new BufferedOutputStream(fos);
                int len;
                while((len = bis.read()) != -1){
                    bos.write(len);
                }
                bos.flush();

            } catch(IOException e){
                e.printStackTrace();
            } finally{
                try{
                    if(fis != null){
                        fis.close();
                    }
                    if(bis != null){
                        bis.close();
                    }
                    if(fos != null){
                        fos.close();
                    }
                    if(bos != null){
                        bos.close();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyDirectory(String sourcePathString,String targetPathString) throws FileSystemException{
        if(!new File(sourcePathString).canRead()){
            throw new FileSystemException("源文件夹" + sourcePathString + "不可读，无法复制！");
        }else{
            (new File(targetPathString)).mkdirs();
            File[] files = new File(sourcePathString).listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {
                    copyFile(new File(sourcePathString + File.separator + file.getName()), new File(targetPathString + File.separator + file.getName()));
                } else if (file.isDirectory()) {
                    copyDirectory(sourcePathString + File.separator + file.getName(), targetPathString + File.separator + file.getName());
                }
            }
        }
    }



}
