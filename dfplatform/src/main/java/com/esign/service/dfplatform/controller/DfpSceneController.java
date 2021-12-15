package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpCpSceneBO;
import com.esign.service.dfplatform.BO.DfpQuestSceneBO;
import com.esign.service.dfplatform.BO.DfpSceneBO;
import com.esign.service.dfplatform.BO.DfpSceneListBO;
import com.esign.service.dfplatform.VO.*;
import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.service.DfpSceneService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: huangtai
 * @Description: 场景操作接口
 * @Date: 2020/9/14 9:19
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpSceneController {

    @Autowired
    DfpSceneService dfpSceneService;

    @ApiOperation(value = "获取场景列表")
    @RequestMapping(value = "/getSceneList", method = RequestMethod.POST)
    public DfplatformResult<DfpSceneListVO> getSceneList(
            @RequestBody @Validated DfpSceneListBO dfpSceneListBO) {

        DfplatformResult result = dfpSceneService.getSceneList(dfpSceneListBO);
        return result;
    }

    @ApiOperation(value = "获取场景参数")
    @RequestMapping(value = "/getSceneParams", method = RequestMethod.GET)
    public DfplatformResult<String> getSceneParams(@RequestParam int sceneId) {

        DfplatformResult result = dfpSceneService.getSceneParams(sceneId);
        return result;
    }

    @OperateLogger(operate = "发起场景请求")
    @ApiOperation(value = "发起请求")
    @RequestMapping(value = "/requestScene", method = RequestMethod.POST)
    public DfplatformResult<DfpRequestSceneVO> requestScene(
            @RequestBody @Validated DfpQuestSceneBO dfpQuestSceneBO) {

        DfplatformResult result = dfpSceneService.requestScene(dfpQuestSceneBO);
        return result;
    }

    @OperateLogger(operate = "删除场景")
    @ApiOperation(value = "删除场景")
    @RequestMapping(value = "/deleteScene", method = RequestMethod.DELETE)
    public DfplatformResult<Integer> deleteScene(@RequestParam int userId, @RequestParam int sceneId) {

        DfplatformResult result = dfpSceneService.deleteScene(sceneId);
        return result;
    }

    @OperateLogger(operate = "新增场景")
    @ApiOperation(value = "新增场景")
    @RequestMapping(value = "/addScene", method = RequestMethod.POST)
    public DfplatformResult<Integer> addScene(
            @RequestBody @Validated DfpSceneBO dfpSceneBO) {

        DfplatformResult result = dfpSceneService.addOrEditSecne(dfpSceneBO, false);
        return result;
    }

    @OperateLogger(operate = "编辑场景")
    @ApiOperation(value = "编辑场景")
    @RequestMapping(value = "/editScene", method = RequestMethod.PUT)
    public DfplatformResult<Integer> editScene(
            @RequestBody @Validated DfpSceneBO dfpSceneBO) {

        DfplatformResult result = dfpSceneService.addOrEditSecne(dfpSceneBO, true);
        return result;
    }

    @ApiOperation(value = "获取单个场景详情")
    @RequestMapping(value = "/queryScene", method = RequestMethod.GET)
    public DfplatformResult<DfpSceneRequestVO> queryScene(@RequestParam int sceneId) {

        DfplatformResult<DfpSceneRequestVO> defenderResult = dfpSceneService.queryScene(sceneId);
        return defenderResult;
    }

    @OperateLogger(operate = "调试场景")
    @ApiOperation(value = "调试场景")
    @RequestMapping(value = "/debugSecne", method = RequestMethod.POST)
    public DfplatformResult<DfpRequestSceneVO> debugSecne(
            @RequestBody @Validated DfpSceneBO dfpSceneBO) {

        DfplatformResult result = dfpSceneService.debugSecne(dfpSceneBO);
        return result;
    }

    @OperateLogger(operate = "复制场景")
    @ApiOperation(value = "复制场景")
    @RequestMapping(value = "/copyScene", method = RequestMethod.POST)
    public DfplatformResult<Integer> copyScene(
            @RequestBody @Validated DfpCpSceneBO dfpCpSceneBO) {

        DfplatformResult result = dfpSceneService.copyScene(dfpCpSceneBO);
        return result;
    }
}
