package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpModuleBO;
import com.esign.service.dfplatform.VO.DfpModuleVO;
import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.service.DfpModuleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: 模块操作接口
 * @Date: 2020/9/14 11:54
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpModuleController {

    @Autowired
    DfpModuleService dfpModuleService;

    @OperateLogger(operate = "新增模块")
    @ApiOperation(value = "新增模块")
    @RequestMapping(value = "/addModule", method = RequestMethod.POST)
    public DfplatformResult<DfpModuleVO> addModule(
            @RequestBody @Validated DfpModuleBO dfpModuleBO) {

        DfplatformResult result = dfpModuleService.addModule(dfpModuleBO);
        return result;
    }

    @ApiOperation(value = "获取模块列表")
    @RequestMapping(value = "/queryModule", method = RequestMethod.GET)
    public DfplatformResult<List<DfpModuleVO>> queryModule(
            @RequestParam String projectName) {

        DfplatformResult result = dfpModuleService.queryModule(projectName);
        return result;
    }
}
