package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpPublicParamBO;
import com.esign.service.dfplatform.BO.DfpQueryPublicParamBO;
import com.esign.service.dfplatform.VO.DfpPublicParamListVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.model.DfpPublicParamModel;
import com.esign.service.dfplatform.service.DfpPublicParamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huangtai
 * @Description: 公共参数
 * @Date: 2021/5/6 9:19
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpPublicParamController {

    @Autowired
    DfpPublicParamService dfpPublicParamService;

    @ApiOperation(value = "新增公共参数")
    @RequestMapping(value = "/addPublicParam", method = RequestMethod.POST)
    public DfplatformResult<DfpPublicParamModel> addPublicParam(@RequestBody @Validated DfpPublicParamBO dfpPublicParamBO) {

        DfplatformResult<DfpPublicParamModel> result = dfpPublicParamService.addOrEditPublicParam(dfpPublicParamBO, true);
        return result;
    }

    @ApiOperation(value = "编辑公共参数")
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
}
