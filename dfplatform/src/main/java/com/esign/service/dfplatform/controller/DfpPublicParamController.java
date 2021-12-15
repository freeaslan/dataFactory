package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpPublicParamBO;
import com.esign.service.dfplatform.BO.DfpQueryPublicParamBO;
import com.esign.service.dfplatform.VO.DfpPublicParamListVO;
import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.model.DfpPublicParamModel;
import com.esign.service.dfplatform.service.DfpPublicParamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: huangtai
 * @Description: 动态参数
 * @Date: 2021/5/6 9:19
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpPublicParamController {

    @Autowired
    DfpPublicParamService dfpPublicParamService;

    @OperateLogger(operate = "新增动态参数")
    @ApiOperation(value = "新增动态参数")
    @RequestMapping(value = "/addPublicParam", method = RequestMethod.POST)
    public DfplatformResult<DfpPublicParamModel> addPublicParam(@RequestBody @Validated DfpPublicParamBO dfpPublicParamBO) {

        DfplatformResult<DfpPublicParamModel> result = dfpPublicParamService.addOrEditPublicParam(dfpPublicParamBO, true);
        return result;
    }

    @OperateLogger(operate = "编辑动态参数")
    @ApiOperation(value = "编辑动态参数")
    @RequestMapping(value = "/editPublicParam", method = RequestMethod.POST)
    public DfplatformResult<DfpPublicParamModel> editPublicParam(@RequestBody @Validated DfpPublicParamBO dfpPublicParamBO) {

        DfplatformResult<DfpPublicParamModel> result = dfpPublicParamService.addOrEditPublicParam(dfpPublicParamBO, false);
        return result;
    }

    @ApiOperation(value = "分页查询公共参数信息")
    @RequestMapping(value = "/queryPublicParamByPage", method = RequestMethod.POST)
    public DfplatformResult<DfpPublicParamListVO> queryPublicParamByPage(@RequestBody @Validated DfpQueryPublicParamBO dfpQueryPublicParamBO) {

        DfplatformResult<DfpPublicParamListVO> result = dfpPublicParamService.queryPublicParamByPage(dfpQueryPublicParamBO);
        return result;
    }

    @OperateLogger(operate = "删除动态参数")
    @ApiOperation(value = "删除动态参数")
    @RequestMapping(value = "/deleteDynamicParam", method = RequestMethod.POST)
    public DfplatformResult<Integer> deleteDynamicParam(@RequestParam int userId, @RequestParam int id) {

        DfplatformResult<Integer> result = dfpPublicParamService.deleteDynamicParam(id);
        return result;
    }
}
