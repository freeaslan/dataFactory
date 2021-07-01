package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpSwaggerBO;
import com.esign.service.dfplatform.VO.DfpApisModelVO;
import com.esign.service.dfplatform.VO.DfpServiceVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.service.DfpSwaggerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: huangtai
 * @Description: swagger接口
 * @Date: 2021/6/15 11:45
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpSwaggerController {

    @Autowired
    DfpSwaggerService dfpSwaggerService;

    @ApiOperation(value = "导入swagger")
    @RequestMapping(value = "/importSwaggers", method = RequestMethod.POST)
    public DfplatformResult<Integer> importSwaggers(@RequestBody @Validated DfpSwaggerBO dfpSwaggerBO) {

        DfplatformResult<Integer> result = dfpSwaggerService.importSwaggers(dfpSwaggerBO);
        return result;
    }

    @ApiOperation(value = "根据项目获取服务名称")
    @RequestMapping(value = "/getServices", method = RequestMethod.GET)
    public DfplatformResult<List<DfpServiceVO>> getServices(@RequestParam String projectName) {

        DfplatformResult<List<DfpServiceVO>> result = dfpSwaggerService.getServices(projectName);
        return result;
    }

    @ApiOperation(value = "根据项目获取接口")
    @RequestMapping(value = "/getPaths", method = RequestMethod.GET)
    public DfplatformResult<List<DfpServiceVO>> getPaths(@RequestParam String serviceName) {

        DfplatformResult<List<DfpServiceVO>> result = dfpSwaggerService.getPaths(serviceName);
        return result;
    }

    @ApiOperation(value = "根据id获取接口详细信息")
    @RequestMapping(value = "/getApis", method = RequestMethod.GET)
    public DfplatformResult<DfpApisModelVO> getApis(@RequestParam int id) {

        DfplatformResult<DfpApisModelVO> result = dfpSwaggerService.getApis(id);
        return result;
    }
}
