package com.esign.service.dfplatform.util;

import com.esign.service.dfplatform.base.DfplatformResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Author: huangtai
 * @Description: 文件操作工具类
 * @Date: 2020/9/24 22:05
 */
@Slf4j
public class FileUtil {

    /**
     * 复制文件
     *
     * @param sourceFile
     * @param targetFile
     */
    public static void copyFile(String sourceFile, String targetFile) {

        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            input = new FileInputStream(new File(sourceFile));
            inBuff = new BufferedInputStream(input);

            // 新建文件输出流并对它进行缓冲
            output = new FileOutputStream(new File(targetFile));
            outBuff = new BufferedOutputStream(output);

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {

                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                if (inBuff != null) {

                    inBuff.close();
                }
                if (outBuff != null) {

                    outBuff.close();
                }
                if (output != null) {

                    output.close();
                }

                if (input != null) {

                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String fileName) {

        File file = new File(fileName);
        if (file.isFile() && file.exists()) {

            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static DfplatformResult<String> filesUpload(MultipartFile file) {

        DfplatformResult<String> defaultResult = new DfplatformResult<>();
        try {
            DfplaformUtil.isNotNull(file, "请求对象不能为空");

            String fileName = file.getOriginalFilename();
            DfplaformUtil.state(StringUtils.endsWith(fileName, ".jar") ||
                    StringUtils.endsWith(fileName, ".class"), "只能上传.class或者.jar文件");
            DfplaformUtil.state(!StringUtils.contains(fileName, "..//"), "文件名不能包含../符号");
            File directory = new File("");
            String path = directory.getCanonicalPath() + "/src/main/uploadfile/";
            String filePath = path + file.getOriginalFilename();
            File uploadFile = new File(filePath);

            //判断当前文件父级文件是否存在，如果不存在，就创建文件夹
            if (!uploadFile.getParentFile().exists()) {
                uploadFile.mkdirs();
            }
            //如果文件存在则删除，重新上传
            if (uploadFile.exists()) {
                uploadFile.delete();
            }

            //上传文件
            file.transferTo(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultResult.setMessage("配置文件上传成功");
        return defaultResult;
    }

    /**
     * 下载文档
     *
     * @param response
     * @return
     */
    public static DfplatformResult<Long> documentDownload(HttpServletResponse response) {

        DfplatformResult<Long> defenderResult = new DfplatformResult<>();
        OutputStream out = null;
        BufferedInputStream bis = null;
        try {
            File directory = new File("");
            String path = directory.getCanonicalPath();
            path = StringUtils.removeEnd(path, "/target") + "/文档/操作文档.docx";
            log.info("下载地址：" + path);
            File file = new File(path);
            String fileName = file.getName();
            InputStream is = new FileInputStream(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment;fileName="
                    + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            response.setHeader("Content-Length", "" + file.length());
            response.setContentType("application/octet-stream");
            try {

                bis = new BufferedInputStream(is);
                byte[] buffer = new byte[bis.available()];
                out = response.getOutputStream();
                while (bis.read(buffer) > 0) {
                    out.write(buffer);
                }
                defenderResult.setCode(0);
            } catch (Exception e) {
                defenderResult.setCode(-1);
                defenderResult.setMessage("文件下载失败");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        defenderResult.setCode(-1);
                        defenderResult.setMessage("关闭输入输出流失败");
                    }
                }
            }
        } catch (IOException e) {
            defenderResult.setCode(-1);
            defenderResult.setMessage("IO出现异常");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    defenderResult.setCode(-1);
                    defenderResult.setMessage("文档输出异常");
                }
            }
        }
        return null;
    }
}