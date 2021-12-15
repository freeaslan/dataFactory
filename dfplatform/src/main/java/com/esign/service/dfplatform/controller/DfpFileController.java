package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.util.FileUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yunge
 * @Description: 上传文件接口
 * @Date: 2021/3/1 16:19
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpFileController {

    @OperateLogger(operate = "动态参数文件上传")
    @ApiOperation(value = "动态参数文件上传")
    @RequestMapping(value = "/filesUpload", method = RequestMethod.POST)
    public DfplatformResult<String> filesUpload(@RequestParam int userId, @RequestParam("file") MultipartFile file) {

        DfplatformResult<String> result = FileUtil.filesUpload(file);
        return result;
    }

    @OperateLogger(operate = "下载操作文档")
    @ApiOperation(value = "下载使用文章")
    @RequestMapping(value = "/documentDownload", method = RequestMethod.GET)
    public DfplatformResult<Long> documentDownload(@RequestParam int userId, HttpServletResponse response) {

        DfplatformResult<Long> defenderResult = FileUtil.documentDownload(response);
        return defenderResult;
    }
}
