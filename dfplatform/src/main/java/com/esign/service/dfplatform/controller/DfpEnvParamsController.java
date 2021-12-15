package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpEnvParamsBO;
import com.esign.service.dfplatform.BO.DfpQueryEnvParamsBO;
import com.esign.service.dfplatform.VO.DfpEnvParamsVO;
import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.model.DfpEnvEnumModel;
import com.esign.service.dfplatform.model.DfpEnvParamsModel;
import com.esign.service.dfplatform.service.DfpEnvParamsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 环境参数接口
 * @author: lingxi
 * @date: 2020/11/15 22:25
 **/
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpEnvParamsController {

    @Autowired
    DfpEnvParamsService dfpCommonParamsService;

    @OperateLogger(operate = "新增环境参数")
    @ApiOperation(value = "新增环境参数")
    @RequestMapping(value = "/addCommonParams", method = RequestMethod.POST)
    public DfplatformResult<DfpEnvParamsModel> addCommonParams(@RequestBody @Validated DfpEnvParamsBO dfpEnvParamsBO) {

        DfplatformResult<DfpEnvParamsModel> result = dfpCommonParamsService.addOrEeditEnvParams(dfpEnvParamsBO, true);
        return result;
    }

    @OperateLogger(operate = "编辑环境参数")
    @ApiOperation(value = "编辑环境参数")
    @RequestMapping(value = "/editCommonParams", method = RequestMethod.POST)
    public DfplatformResult<DfpEnvParamsModel> editCommonParams(@RequestBody @Validated DfpEnvParamsBO dfpEnvParamsBO) {

        DfplatformResult<DfpEnvParamsModel> result = dfpCommonParamsService.addOrEeditEnvParams(dfpEnvParamsBO, false);
        return result;
    }

    @OperateLogger(operate = "删除环境参数")
    @ApiOperation(value = "删除环境参数")
    @RequestMapping(value = "/deleteEnvParams", method = RequestMethod.POST)
    public DfplatformResult<Integer> deleteEnvParams(@RequestParam int userId, @RequestParam int id) {

        DfplatformResult<Integer> result = dfpCommonParamsService.deleteEnvParams(id);
        return result;
    }

    @ApiOperation(value = "获取环境参数列表")
    @RequestMapping(value = "/getCommonParamsList", method = RequestMethod.POST)
    public DfplatformResult<DfpEnvParamsVO> getCommonParamsList(@RequestBody DfpQueryEnvParamsBO dfpQueryEnvParamsBO) {

        DfplatformResult result = dfpCommonParamsService.getEnvParamsList(dfpQueryEnvParamsBO);
        return result;
    }

    @ApiOperation(value = "获取环境值")
    @RequestMapping(value = "/getAllEnvEnum", method = RequestMethod.GET)
    public DfplatformResult<List<DfpEnvEnumModel>> getAllEnvEnum() {

        DfplatformResult<List<DfpEnvEnumModel>> result = dfpCommonParamsService.getAllEnvEnum();
        return result;
    }
}