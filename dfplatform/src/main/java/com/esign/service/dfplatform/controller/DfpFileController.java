package com.esign.service.dfplatform.controller;

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

    @ApiOperation(value = "配置文件上传")
    @RequestMapping(value = "/filesUpload", method = RequestMethod.POST)
    public DfplatformResult<String> filesUpload(@RequestParam("file") MultipartFile file) {

        DfplatformResult<String> result = FileUtil.filesUpload(file);
        return result;
    }

    @ApiOperation(value = "文档下载")
    @RequestMapping(value = "/documentDownload", method = RequestMethod.GET)
    public DfplatformResult<Long> documentDownload(HttpServletResponse response) {

        DfplatformResult<Long> defenderResult = FileUtil.documentDownload(response);
        return defenderResult;
    }
}
