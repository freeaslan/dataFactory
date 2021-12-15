package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpEnvParamsBO;
import com.esign.service.dfplatform.BO.DfpQueryEnvParamsBO;
import com.esign.service.dfplatform.VO.DfpEnvParamsModelVO;
import com.esign.service.dfplatform.VO.DfpEnvParamsVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpEnvEnumDAO;
import com.esign.service.dfplatform.daointerface.DfpEnvParamsDAO;
import com.esign.service.dfplatform.daointerface.DfpMenuDAO;
import com.esign.service.dfplatform.model.DfpEnvEnumModel;
import com.esign.service.dfplatform.model.DfpEnvParamsModel;
import com.esign.service.dfplatform.model.DfpMenuModel;
import com.esign.service.dfplatform.util.ConvertUtil;
import com.esign.service.dfplatform.util.DfplaformUtil;
import com.esign.service.dfplatform.util.ObjectConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 环境参数设置
 * @author: lingxi
 * @date: 2020/11/15 23:12
 **/
@Service
public class DfpEnvParamsService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpEnvParamsDAO dfpEnvParamsDAO;

    @Autowired
    DfpEnvEnumDAO dfpEnvEnumDAO;

    @Autowired
    DfpMenuDAO dfpMenuDAO;

    /**
     * 新增或者修改项目环境公共参数
     *
     * @param dfpEnvParamsBO
     * @param isAdd          isAdd=true（新增） isAdd=false(编辑)
     * @return
     */
    public DfplatformResult<DfpEnvParamsModel> addOrEeditEnvParams(DfpEnvParamsBO dfpEnvParamsBO, boolean isAdd) {

        //定义变量
        DfplatformResult<DfpEnvParamsModel> defenderResult = new DfplatformResult<>();

        //开启事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            DfpEnvParamsModel dfpEnvParamsModel = new DfpEnvParamsModel();

            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpEnvParamsBO, "请求对象不能为空");
                DfpMenuModel dfpMenuModel = dfpMenuDAO.findByName(dfpEnvParamsBO.getProjectName());
                DfplaformUtil.isNotNull(dfpMenuModel, "关联的项目不存在");

                DfpEnvEnumModel dfpEnvEnumModel = dfpEnvEnumDAO.findById(dfpEnvParamsBO.getEnvId());
                DfplaformUtil.isNotNull(dfpEnvEnumModel, "关联的环境不存在");

                //检查同一个项目、环境、服务配置是否存在
                if (isAdd) {
                    checkEnvParamsUnique(dfpEnvParamsBO, true);
                } else {

                    //修改配置信息时，原配置信息不为空
                    DfplaformUtil.state(dfpEnvParamsBO.getId() > 0, "修改环境配置id必传");
                    dfpEnvParamsModel = dfpEnvParamsDAO.findById(dfpEnvParamsBO.getId());
                    DfplaformUtil.isNotNull(dfpEnvParamsModel, "没有匹配的环境配置信息");
                    checkEnvParamsUnique(dfpEnvParamsBO, false);
                }
            }

            @Override
            public void doPacker() {

                //对象赋值
                if (!isAdd) {
                    dfpEnvParamsModel.setId(dfpEnvParamsBO.getId());
                } else {
                    dfpEnvParamsModel.setCreatorId(dfpEnvParamsBO.getUserId());
                }

                dfpEnvParamsModel.setModifierId(dfpEnvParamsBO.getUserId());
                dfpEnvParamsModel.setEnvId(dfpEnvParamsBO.getEnvId());
                dfpEnvParamsModel.setProjectName(dfpEnvParamsBO.getProjectName());
                dfpEnvParamsModel.setServiceName(dfpEnvParamsBO.getServiceName());
                dfpEnvParamsModel.setHost(dfpEnvParamsBO.getHost());
                //设置头部信息
                dfpEnvParamsModel.setHeader(ConvertUtil.toJson(dfpEnvParamsBO.getHeader()));
            }

            @Override
            public void doOperate() throws Exception {

                //新增或者更新环境变量信息
                dfpEnvParamsModel = dfpEnvParamsDAO.save(dfpEnvParamsModel);
                defenderResult.setData(dfpEnvParamsModel);
                defenderResult.setMessage(isAdd ? "新增环境参数成功" : "编辑环境参数成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 删除环境参数
     *
     * @param id
     * @return
     */
    public DfplatformResult<Integer> deleteEnvParams(int id) {

        //定义返回结果
        DfplatformResult<Integer> defenderResult = new DfplatformResult<>();
        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.state(id > 0, "场景ID必须大于零");
                DfpEnvParamsModel dfpEnvParamsModel = dfpEnvParamsDAO.findById(id);
                DfplaformUtil.isNotNull(dfpEnvParamsModel, "要删除的环境参数不存在");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                //直接物理删除
                dfpEnvParamsDAO.deleteById(id);
                defenderResult.setData(id);
                defenderResult.setMessage("删除环境参数成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 校验项目中是否已有同名环境信息
     *
     * @param dfpEnvParamsBO
     * @param isAdd
     */
    private void checkEnvParamsUnique(DfpEnvParamsBO dfpEnvParamsBO, boolean isAdd) {

        DfpEnvParamsModel dfpEnvParamsModel = dfpEnvParamsDAO.findByEnvIdAndProjectNameAndServiceName(dfpEnvParamsBO.getEnvId(),
                dfpEnvParamsBO.getProjectName(), dfpEnvParamsBO.getServiceName());

        //新增或者修改时查询的数据与给定的ID不一致时，检查环境是否存在
        if (isAdd || (dfpEnvParamsModel != null && dfpEnvParamsBO.getId() != dfpEnvParamsModel.getId())) {

            DfplaformUtil.isNull(dfpEnvParamsModel, dfpEnvParamsBO.getProjectName() + "已存在相同的环境配置");
        }
    }

    /**
     * 查询项目环境公共参数
     *
     * @param dfpEnvParamsBO
     * @return
     */
    public DfplatformResult<DfpEnvParamsVO> getEnvParamsList(DfpQueryEnvParamsBO dfpEnvParamsBO) {

        //变量定义
        DfplatformResult<DfpEnvParamsVO> defenderResult = new DfplatformResult<>();
        DfpEnvParamsVO dfpEnvParamsVO = new DfpEnvParamsVO();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {

            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpEnvParamsBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

                dfpEnvParamsVO.setPageIndex(dfpEnvParamsBO.getPageIndex());
                dfpEnvParamsVO.setPageSize(dfpEnvParamsBO.getPageSize());
            }

            @Override
            public void doOperate() throws Exception {

                DfpMenuModel dfpMenuModel = dfpMenuDAO.findByName(dfpEnvParamsBO.getParentName());
                List<String> menuNames = dfpMenuDAO.findNameByParentId(dfpMenuModel.getId());
                //获取分页数据
                Page<DfpEnvParamsModel> dfpEnvParamsModelPage = getEnvParamsByPage(dfpEnvParamsBO, menuNames);
                if (dfpEnvParamsModelPage != null) {

                    //设置数据总数
                    dfpEnvParamsVO.setTotalNum(dfpEnvParamsModelPage.getTotalElements());
                    List<DfpEnvParamsModelVO> dfpEnvParamsModelVOS = new ArrayList<>();
                    List<DfpEnvParamsModel> dfpEnvParamsModels = dfpEnvParamsModelPage.getContent();
                    for (DfpEnvParamsModel dfpEnvParamsModel : dfpEnvParamsModels) {

                        DfpEnvParamsModelVO dfpEnvParamsModelVO = new DfpEnvParamsModelVO();
                        ObjectConverterUtil.convert(dfpEnvParamsModel, dfpEnvParamsModelVO);
                        //查询环境名称
                        DfpEnvEnumModel dfpEnvEnumModel = dfpEnvEnumDAO.findById(dfpEnvParamsModel.getEnvId());
                        dfpEnvParamsModelVO.setEnvName(dfpEnvEnumModel.getEnvName());
                        dfpEnvParamsModelVOS.add(dfpEnvParamsModelVO);
                    }
                    dfpEnvParamsVO.setDfpEnvParamsModelList(dfpEnvParamsModelVOS);
                }
                defenderResult.setData(dfpEnvParamsVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 分页查询设置环境信息
     *
     * @param dfpQueryEnvParamsBO
     * @return
     */
    private Page<DfpEnvParamsModel> getEnvParamsByPage(DfpQueryEnvParamsBO dfpQueryEnvParamsBO, List<String> menuNames) {

        //设置排序规则
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(dfpQueryEnvParamsBO.getPageIndex(), dfpQueryEnvParamsBO.getPageSize(), sort);
        Specification<DfpEnvParamsModel> spec = new Specification<DfpEnvParamsModel>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<DfpEnvParamsModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //项目名称
                List<Predicate> list = new ArrayList<Predicate>();
                //项目Id不为空，则作为查询条件
                if (dfpQueryEnvParamsBO.getEnvId() > 0) {

                    list.add(criteriaBuilder.equal(root.get("envId"), dfpQueryEnvParamsBO.getEnvId()));
                }

                //项目名称不为空，则作为查询条件
                if (StringUtils.isNotBlank(dfpQueryEnvParamsBO.getProjectName())) {

                    list.add(criteriaBuilder.equal(root.get("projectName"), dfpQueryEnvParamsBO.getProjectName()));
                } else {
                    if (menuNames.size() > 0) {
                        list.add(root.get("projectName").in(menuNames));
                    }
                }
                //服务名称不为空，则作为查询条件
                if (StringUtils.isNotBlank(dfpQueryEnvParamsBO.getServiceName())) {

                    list.add(criteriaBuilder.equal(root.get("serviceName"), dfpQueryEnvParamsBO.getServiceName()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
        return dfpEnvParamsDAO.findAll(spec, pageable);
    }

    /**
     * 获取环境值
     *
     * @return
     */
    public DfplatformResult<List<DfpEnvEnumModel>> getAllEnvEnum() {

        //定义返回值
        DfplatformResult<List<DfpEnvEnumModel>> defenderResult = new DfplatformResult<>();

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

                List<DfpEnvEnumModel> dfpEnvEnumModels = dfpEnvEnumDAO.findAll();
                defenderResult.setData(dfpEnvEnumModels);
            }
        }, defenderResult);

        return defenderResult;
    }
}
