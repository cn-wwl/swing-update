package com.wwl.core.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author wwl
 * @date 2022/7/27 13:49
 * @desc 文件压缩工具类
 */
public class ZipUtils {
    private static final int BUFFER_SIZE = 2 * 1024;
    /**
     * 是否保留原来的目录结构
     * true:  保留目录结构;
     * false: 所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    private static final boolean KeepDirStructure = true;
    private static final Logger log = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * 压缩成ZIP
     *
     * @param srcDir       压缩 文件/文件夹 路径
     * @param outPathFile  压缩 文件/文件夹 输出路径+文件名 D:/xx.zip
     * @param isDelSrcFile 是否删除原文件: 压缩前文件
     */
    public static void toZip(String srcDir, String outPathFile, boolean isDelSrcFile) throws Exception {
        long start = System.currentTimeMillis();
        try (FileOutputStream out = new FileOutputStream(new File(outPathFile));
             ZipOutputStream zos = new ZipOutputStream(out)
        ) {
            File sourceFile = new File(srcDir);
            if (!sourceFile.exists()) {
                throw new Exception("需压缩文件或者文件夹不存在");
            }
            compress(sourceFile, zos, sourceFile.getName());
            if (isDelSrcFile) {
                delDir(srcDir);
            }
            log.info("原文件:{}. 压缩到:{}完成. 是否删除原文件:{}. 耗时:{}ms. ", srcDir, outPathFile, isDelSrcFile, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error("zip error from ZipUtils: {}. ", e.getMessage());
            throw new Exception("zip error from ZipUtils");
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name)
            throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName());
                    } else {
                        compress(file, zos, file.getName());
                    }
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "resource"})
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            throw new IOException("需解压文件不存在.");
        }
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        try (ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"))) {
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                if (entry.isDirectory()) {
                    String dirPath = FilenameUtils.normalizeNoEndSeparator(descDir + File.separator + zipEntryName, true);
                    File dir = new File(dirPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                } else {
                    FileUtils.copyInputStreamToFile(zip.getInputStream(entry), new File((descDir + File.separator + zipEntryName).replaceAll("\\*", "/")));
                }
            }
        }
    }

    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到的目录
     * @throws Exception
     */
    public static void unzip(String zipFilePath, String desDirectory) throws Exception {

        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }
        // 读入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));

        // 遍历每一个文件
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                // 文件夹
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                // 直接创建
                mkdir(new File(unzipFilePath));
            } else {
                // 文件
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                File file = new File(unzipFilePath);
                // 创建父目录
                mkdir(file.getParentFile());
                // 写出文件流
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(new FileOutputStream(unzipFilePath));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }


    /**
     * 解压
     *
     * @param zipFilePath  带解压文件
     * @param desDirectory 解压到的目录
     * @throws Exception
     */
    public static void extractUnzip(String zipFilePath, String desDirectory) throws Exception {

        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("创建解压目标文件夹失败");
            }
        }
        // 读入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));

        // 遍历每一个文件
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        String discardDir =null;

        while (zipEntry != null) {
            if (zipEntry.isDirectory()) {
                // // 文件夹
                // String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
                // // 直接创建
                // mkdir(new File(unzipFilePath));
                discardDir = zipEntry.getName();

            } else {
                // 文件
                String unzipFilePath = desDirectory + File.separator + zipEntry.getName().replace(discardDir,"");
                File file = new File(unzipFilePath);
                // 创建父目录
                mkdir(file.getParentFile());
                // 写出文件流
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(new FileOutputStream(unzipFilePath));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }


    /**
     * 如果父目录不存在则创建
     *
     * @param file
     */
    private static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();
    }

    /**
     * 删除文件或文件夹以及文件夹下所有文件
     *
     * @param dirPath
     * @throws IOException
     */
    public static void delDir(String dirPath) throws IOException {
        log.info("删除文件开始:{}.", dirPath);
        long start = System.currentTimeMillis();
        try {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                return;
            }
            if (dirFile.isFile()) {
                dirFile.delete();
                return;
            }
            File[] files = dirFile.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                delDir(files[i].toString());
            }
            dirFile.delete();
            log.info("删除文件:{}. 耗时:{}ms. ", dirPath, System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.info("删除文件:{}. 异常:{}. 耗时:{}ms. ", dirPath, e, System.currentTimeMillis() - start);
            throw new IOException("删除文件异常.");
        }
    }

}
