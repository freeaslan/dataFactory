package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpModuleBO;
import com.esign.service.dfplatform.VO.DfpModuleVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpMenuDAO;
import com.esign.service.dfplatform.daointerface.DfpModuleDAO;
import com.esign.service.dfplatform.model.DfpMenuModel;
import com.esign.service.dfplatform.model.DfpModuleModel;
import com.esign.service.dfplatform.util.DfplaformUtil;
import com.esign.service.dfplatform.util.ObjectConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : qiuzhu
 * @description: 模块操作服务
 * @date : 2020-09-16 16:40
 */
@Service
public class DfpModuleService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpModuleDAO dfpModuleDAO;

    @Autowired
    DfpMenuDAO dfpMenuDAO;

    /**
     * 新增模块
     *
     * @param dfpModuleBO
     * @return
     */
    public DfplatformResult<DfpModuleVO> addModule(DfpModuleBO dfpModuleBO) {

        //变量定义
        DfplatformResult<DfpModuleVO> defenderResult = new DfplatformResult<>();
        DfpModuleVO dfpModuleVO = new DfpModuleVO();

        //开启事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            DfpModuleModel dfpModuleModel = new DfpModuleModel();

            @Override
            public void doCheck() {

                checkModuleName(dfpModuleBO);
            }

            @Override
            public void doPacker() {

                ObjectConverterUtil.convert(dfpModuleBO, dfpModuleModel);
                dfpModuleModel.setCreatorId(dfpModuleBO.getUserId());
            }

            @Override
            public void doOperate() throws Exception {

                //新建模块
                dfpModuleModel = dfpModuleDAO.save(dfpModuleModel);
                ObjectConverterUtil.convert(dfpModuleModel, dfpModuleVO);
                defenderResult.setData(dfpModuleVO);
                defenderResult.setMessage("新增模块成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 查询项目下的模块
     *
     * @param projectName
     * @return
     */
    public DfplatformResult<List<DfpModuleVO>> queryModule(String projectName) {

        //定义变量
        DfplatformResult<List<DfpModuleVO>> defenderResult = new DfplatformResult<>();
        List<DfpModuleVO> dfpModuleVOs = new ArrayList<>();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {

            @Override
            public void doCheck() {

                DfplaformUtil.state(StringUtils.isNotBlank(projectName), "项目名必须不为空");
                DfpMenuModel dfpMenuModel = dfpMenuDAO.findByName(projectName);
                DfplaformUtil.isNotNull(dfpMenuModel, "查询的项目不存在");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                //根据项目名称查询
                List<DfpModuleModel> dfpModuleModels = dfpModuleDAO.findByProjectName(projectName);
                ObjectConverterUtil.convertList(dfpModuleModels, dfpModuleVOs, DfpModuleVO.class);
                defenderResult.setData(dfpModuleVOs);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 校验项目中是否已有同名模块
     *
     * @param dfpModuleBO
     */
    private void checkModuleName(DfpModuleBO dfpModuleBO) {

        DfpMenuModel dfpMenuModel = dfpMenuDAO.findByName(dfpModuleBO.getProjectName());
        DfplaformUtil.isNotNull(dfpMenuModel, "模块所属的项目不存在");

        DfpModuleModel dfpModuleModel = dfpModuleDAO.findByModuleNameAndProjectName(dfpModuleBO.getModuleName(),
                dfpModuleBO.getProjectName());
        DfplaformUtil.isNull(dfpModuleModel, dfpModuleBO.getProjectName() + "已存在相同模块");
    }
}
