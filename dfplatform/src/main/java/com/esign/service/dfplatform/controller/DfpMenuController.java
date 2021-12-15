package com.esign.service.dfplatform.controller;

import com.esign.service.dfplatform.BO.DfpMenuBO;
import com.esign.service.dfplatform.BO.DfpMenuListBO;
import com.esign.service.dfplatform.VO.DfpAllMenuListVO;
import com.esign.service.dfplatform.VO.DfpMenuListVO;
import com.esign.service.dfplatform.VO.DfpMenuModelVO;
import com.esign.service.dfplatform.aop.OperateLogger;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.model.DfpMenuModel;
import com.esign.service.dfplatform.service.DfpMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: yunge
 * @Description: 菜单接口
 * @Date: 2021/5/6 9:19
 */
@RestController
@RequestMapping(value = "/dfplatform", produces = "application/json")
public class DfpMenuController {

    @Autowired
    DfpMenuService dfpMenuService;

    @ApiOperation(value = "获取菜单列表树")
    @RequestMapping(value = "/getMenuList", method = RequestMethod.GET)
    public DfplatformResult<DfpMenuListVO> getMenuList() {

        DfplatformResult<DfpMenuListVO> result = dfpMenuService.getMenuList();
        return result;
    }

    @ApiOperation(value = "根据Id获取菜单信息")
    @RequestMapping(value = "/getMenuById", method = RequestMethod.GET)
    public DfplatformResult<DfpMenuModelVO> getMenuById(@RequestParam int id) {

        DfplatformResult<DfpMenuModelVO> result = dfpMenuService.getMenuDetail(id);
        return result;
    }

    @ApiOperation(value = "获取所有菜单列表")
    @RequestMapping(value = "/getAllMenuList", method = RequestMethod.POST)
    public DfplatformResult<DfpAllMenuListVO> getAllMenuList(@RequestBody DfpMenuListBO dfpMenuListBO) {

        DfplatformResult<DfpAllMenuListVO> result = dfpMenuService.getAllMenuList(dfpMenuListBO);
        return result;
    }

    @ApiOperation(value = "菜单下拉列表")
    @RequestMapping(value = "/getMenuTree", method = RequestMethod.GET)
    public DfplatformResult<DfpMenuListVO> getMenuTree() {

        DfplatformResult<DfpMenuListVO> result = dfpMenuService.getMenuList();
        return result;
    }

    @OperateLogger(operate = "创建目录或者菜单")
    @ApiOperation(value = "创建目录和菜单")
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    public DfplatformResult<DfpMenuModel> addMenu(@RequestBody @Validated DfpMenuBO dfpMenuBO) {

        DfplatformResult<DfpMenuModel> result = dfpMenuService.addOrModifyMenu(dfpMenuBO, true);
        return result;
    }

    @OperateLogger(operate = "编辑目录或者菜单")
    @ApiOperation(value = "更新目录和菜单")
    @RequestMapping(value = "/updateMenu", method = RequestMethod.POST)
    public DfplatformResult<DfpMenuModel> updateMenu(@RequestBody @Validated DfpMenuBO dfpMenuBO) {

        DfplatformResult<DfpMenuModel> result = dfpMenuService.addOrModifyMenu(dfpMenuBO, false);
        return result;
    }

    @OperateLogger(operate = "删除目录或者菜单")
    @ApiOperation(value = "删除目录或菜单")
    @RequestMapping(value = "/deleteMenu", method = RequestMethod.GET)
    public DfplatformResult<Integer> deleteMenu(@RequestParam int userId, @RequestParam int id) {

        DfplatformResult<Integer> result = dfpMenuService.deleteMenu(id);
        return result;
    }
}
