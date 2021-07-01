package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpMenuBO;
import com.esign.service.dfplatform.VO.DfpAllMenuListVO;
import com.esign.service.dfplatform.VO.DfpMenuListVO;
import com.esign.service.dfplatform.VO.DfpMenuModelVO;
import com.esign.service.dfplatform.VO.DfpMenuVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpMenuDAO;
import com.esign.service.dfplatform.daointerface.DfpSceneDAO;
import com.esign.service.dfplatform.model.DfpMenuModel;
import com.esign.service.dfplatform.model.DfpSceneModel;
import com.esign.service.dfplatform.util.DfplaformUtil;
import com.esign.service.dfplatform.util.ObjectConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yunge
 * @Description: 菜单服务
 * @Date: 2021/5/6 9:19
 */
@Service
public class DfpMenuService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpMenuDAO dfpMenuDAO;

    @Autowired
    DfpSceneDAO dfpSceneDAO;

    /**
     * 创建或者编辑菜单
     *
     * @param dfpMenuBO
     * @param isAdd
     * @return
     */
    public DfplatformResult<DfpMenuModel> addOrModifyMenu(DfpMenuBO dfpMenuBO, boolean isAdd) {

        //定义结果
        DfplatformResult<DfpMenuModel> defaultResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            DfpMenuModel dfpMenuModel = new DfpMenuModel();

            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpMenuBO, "请求对象不能为空");
                if (dfpMenuBO.getType() == 1) {

                    DfplaformUtil.state(dfpMenuBO.getParentId() > 0, "父菜单ID不能为空");
                }
                if (!isAdd) {
                    //修改菜单时，原菜单信息不为空
                    DfplaformUtil.state(dfpMenuBO.getId() > 0, "修改菜单Id不能为空");
                    dfpMenuModel = dfpMenuDAO.findById(dfpMenuBO.getId());
                    DfplaformUtil.isNotNull(dfpMenuModel, "没有匹配的菜单信息");
                }
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                //如果是环境设置则不校验名称重复
                if (dfpMenuBO.getType() != 2) {
                    DfpMenuModel checkMenuModel = dfpMenuDAO.findByName(dfpMenuBO.getName());
                    if (isAdd || (checkMenuModel != null && dfpMenuBO.getId() != checkMenuModel.getId())) {

                        DfplaformUtil.isNull(checkMenuModel, "添加的菜单名称已经存在");
                    }
                } else {
                    DfplaformUtil.isNull(dfpMenuDAO.findByParentId(dfpMenuBO.getParentId()), "该项目已经存在环境设置");
                }
                ObjectConverterUtil.convert(dfpMenuBO, dfpMenuModel);
                if (dfpMenuBO.getType() == 0) {
                    //0表示一级菜单
                    dfpMenuModel.setParentId(0);
                } else {
                    //子菜单
                    dfpMenuModel.setParentId(dfpMenuBO.getParentId());
                }
                dfpMenuModel = dfpMenuDAO.save(dfpMenuModel);
                defaultResult.setMessage("创建成功");
                defaultResult.setData(dfpMenuModel);
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 删除目录或菜单
     *
     * @param id
     * @return
     */
    public DfplatformResult<Integer> deleteMenu(int id) {

        //定义返回
        DfplatformResult<Integer> defaultResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.state(id > 0, "id必须大于0");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                DfpMenuModel getMenuinfo = dfpMenuDAO.findById(id);
                DfplaformUtil.isNotNull(getMenuinfo, "要删除的菜单不存在");

                List<DfpMenuModel> dfpMenuModels = dfpMenuDAO.findByParentId(id);
                DfplaformUtil.state(dfpMenuModels.size() == 0, "该类目下面有子类目不能删除");
                List<DfpSceneModel> dfpSceneModels = dfpSceneDAO.findByModuleId(id);

                DfplaformUtil.state(dfpSceneModels.size() == 0, "该类目下关联有场景不能删除");

                dfpMenuDAO.delete(getMenuinfo);

                defaultResult.setMessage("删除成功");
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 获取所有菜单
     *
     * @return
     */
    public DfplatformResult<DfpAllMenuListVO> getAllMenuList() {

        //定义返回值
        DfplatformResult<DfpAllMenuListVO> defenderResult = new DfplatformResult<>();
        DfpAllMenuListVO dfpMenuListVO = new DfpAllMenuListVO();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                Sort sort = new Sort(Sort.Direction.DESC, "id");
                List<DfpMenuModelVO> dfpMenuModelVOS = new ArrayList<>();
                List<DfpMenuModel> getMenuList = dfpMenuDAO.findAll(sort);
                for (DfpMenuModel dfpMenuModel : getMenuList) {
                    DfpMenuModelVO dfpMenuModelVO = new DfpMenuModelVO();
                    ObjectConverterUtil.convert(dfpMenuModel, dfpMenuModelVO);
                    if (dfpMenuModel.getParentId() == 0) {

                        dfpMenuModelVO.setParentName("一级菜单");
                    } else {

                        DfpMenuModel parentMen = dfpMenuDAO.findById(dfpMenuModel.getParentId());
                        dfpMenuModelVO.setParentName(parentMen.getName());
                    }
                    dfpMenuModelVOS.add(dfpMenuModelVO);
                }
                dfpMenuListVO.setMenuList(dfpMenuModelVOS);
                defenderResult.setData(dfpMenuListVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 获取菜单列表
     *
     * @return
     */
    public DfplatformResult<DfpMenuListVO> getMenuList() {

        //定义返回值
        DfplatformResult<DfpMenuListVO> defenderResult = new DfplatformResult<>();
        DfpMenuListVO dfpMenuListVO = new DfpMenuListVO();
        List<DfpMenuVO> menuList = new ArrayList<>();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                //获取一级类目
                List<DfpMenuModel> getMenuList = dfpMenuDAO.findByType(0);
                for (DfpMenuModel dfpMenuModel : getMenuList) {

                    DfpMenuVO dfpMenuVO = new DfpMenuVO();
                    ObjectConverterUtil.convert(dfpMenuModel, dfpMenuVO);
                    if (dfpMenuModel.getParentId() == 0) {

                        dfpMenuVO.setParentName("一级菜单");
                    } else {
                        DfpMenuModel parentModel = dfpMenuDAO.findById(dfpMenuModel.getParentId());
                        if (parentModel != null) {
                            dfpMenuVO.setParentName(parentModel.getName());
                        }
                    }

                    //获取一级类目下的二级类目
                    List<DfpMenuModelVO> dfpMenuModelVOS = new ArrayList<>();
                    List<DfpMenuModel> getMenuLists = dfpMenuDAO.findByParentId(dfpMenuModel.getId());
                    for (DfpMenuModel menuModel : getMenuLists) {

                        DfpMenuModelVO dfpMenuModelVO = new DfpMenuModelVO();
                        ObjectConverterUtil.convert(menuModel, dfpMenuModelVO);
                        dfpMenuModelVO.setParentName(dfpMenuModel.getName());
                        dfpMenuModelVOS.add(dfpMenuModelVO);
                    }
                    dfpMenuVO.setList(dfpMenuModelVOS);
                    menuList.add(dfpMenuVO);
                }

                dfpMenuListVO.setMenuList(menuList);
                defenderResult.setData(dfpMenuListVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 获取菜单信息
     *
     * @param id
     * @return
     */
    public DfplatformResult<DfpMenuModelVO> getMenuDetail(int id) {

        //定义返回
        DfplatformResult<DfpMenuModelVO> defenderResult = new DfplatformResult<>();
        DfpMenuModelVO dfpMenuModelVO = new DfpMenuModelVO();
        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.state(id > 0, "id不能都为空");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                DfpMenuModel getMenuInfo = dfpMenuDAO.findById(id);
                DfplaformUtil.isNotNull(getMenuInfo, "查找的菜单不存在");
                ObjectConverterUtil.convert(getMenuInfo, dfpMenuModelVO);
                if (getMenuInfo.getParentId() == 0) {

                    dfpMenuModelVO.setParentName("一级菜单");
                } else {
                    dfpMenuModelVO.setParentName(dfpMenuDAO.findById(getMenuInfo.getParentId()).getName());
                }
                defenderResult.setData(dfpMenuModelVO);
            }
        }, defenderResult);

        return defenderResult;
    }
}
