package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpUserBO;
import com.esign.service.dfplatform.BO.DfpUserListBO;
import com.esign.service.dfplatform.VO.DfpUserListVO;
import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.model.DfpUserModel;
import com.esign.service.dfplatform.service.DfpUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yunge
 * @Description: 用户接口
 * @Date: 2021/5/6 9:19
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpUserController {

    @Autowired
    DfpUserService dfpUserService;

    @OperateLogger(operate = "用户登录")
    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public DfplatformResult<DfpUserModel> userLogin(@RequestBody @Validated DfpUserBO dfpUserBO) {

        DfplatformResult<DfpUserModel> result = dfpUserService.userLogin(dfpUserBO);
        return result;
    }

    @OperateLogger(operate = "创建新用户")
    @ApiOperation(value = "创建新用户")
    @RequestMapping(value = "/createNewUser", method = RequestMethod.POST)
    public DfplatformResult<DfpUserModel> createNewUser(@RequestBody @Validated DfpUserBO dfpUserBO) {

        DfplatformResult<DfpUserModel> result = dfpUserService.createNewUser(dfpUserBO);
        return result;
    }

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    public DfplatformResult<DfpUserListVO> getUserList(@RequestBody @Validated DfpUserListBO dfpUserListBO) {

        DfplatformResult result = dfpUserService.getUserList(dfpUserListBO);
        return result;
    }
}
