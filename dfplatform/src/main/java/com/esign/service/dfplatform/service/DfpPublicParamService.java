package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpPublicParamBO;
import com.esign.service.dfplatform.BO.DfpQueryPublicParamBO;
import com.esign.service.dfplatform.VO.DfpPublicParamListVO;
import com.esign.service.dfplatform.VO.DfpPublicParamVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpPublicParamDAO;
import com.esign.service.dfplatform.daointerface.DfpSceneParamsDAO;
import com.esign.service.dfplatform.daointerface.DfpUserDAO;
import com.esign.service.dfplatform.model.DfpPublicParamModel;
import com.esign.service.dfplatform.model.DfpSceneParamsModel;
import com.esign.service.dfplatform.model.DfpUserModel;
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
 * @Author: huangtai
 * @Description: 动态参数服务
 * @Date: 2021/6/8 9:19
 */
@Service
public class DfpPublicParamService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpPublicParamDAO dfpPublicParamDAO;

    @Autowired
    DfpUserDAO dfpUserDAO;

    @Autowired
    DfpSceneParamsDAO dfpSceneParamsDAO;

    /**
     * 新增和编辑动态参数
     *
     * @param dfpPublicParamBO
     * @param isAdd
     * @return
     */
    public DfplatformResult<DfpPublicParamModel> addOrEditPublicParam(DfpPublicParamBO dfpPublicParamBO, boolean isAdd) {

        //定义返回结果
        DfplatformResult<DfpPublicParamModel> defenderResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {

            DfpPublicParamModel dfpPublicParamModel = new DfpPublicParamModel();

            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpPublicParamBO, "请求对象不能为空");
                DfpUserModel dfpUserModel = dfpUserDAO.findById(dfpPublicParamBO.getUserId());
                DfplaformUtil.isNotNull(dfpUserModel, "用户不存在");
                if (!isAdd) {

                    DfplaformUtil.state(dfpPublicParamBO.getId() > 0, "动态参数ID必须大于0");
                    dfpPublicParamModel = dfpPublicParamDAO.findById(dfpPublicParamBO.getId());
                    DfplaformUtil.isNotNull(dfpPublicParamModel, "编辑的动态参数不存在");
                }
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                //相同key的公共参数只能存在一个
                DfpPublicParamModel checkPublicParamModel = dfpPublicParamDAO.findByParamKey(dfpPublicParamBO.getParamKey());
                if (isAdd || (checkPublicParamModel != null && dfpPublicParamBO.getId() != checkPublicParamModel.getId())) {
                    DfplaformUtil.isNull(checkPublicParamModel, "动态参数已经存在");
                }

                ObjectConverterUtil.convert(dfpPublicParamBO, dfpPublicParamModel);

                if (isAdd) {
                    dfpPublicParamModel.setCreatorId(dfpPublicParamBO.getUserId());
                }
                dfpPublicParamModel.setModifierId(dfpPublicParamBO.getUserId());

                String jarName = StringUtils.substringBeforeLast(dfpPublicParamModel.getJarName(), ".");
                if ("jar".equals(dfpPublicParamModel.getParamClassType())) {
                    jarName = jarName + ".jar";
                } else if ("class".equals(dfpPublicParamModel.getParamClassType())) {
                    jarName = jarName + ".class";
                }
                dfpPublicParamModel.setJarName(jarName);
                dfpPublicParamModel = dfpPublicParamDAO.save(dfpPublicParamModel);
                defenderResult.setData(dfpPublicParamModel);
                defenderResult.setMessage(isAdd ? "新增动态参数成功" : "编辑动态参数成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 获取动态参数信息
     *
     * @param dfpQueryPublicParamBO
     * @return
     */
    public DfplatformResult<DfpPublicParamListVO> queryPublicParamByPage(DfpQueryPublicParamBO dfpQueryPublicParamBO) {

        //定义返回结果
        DfplatformResult<DfpPublicParamListVO> defenderResult = new DfplatformResult<>();
        DfpPublicParamListVO dfpPublicParamListVO = new DfpPublicParamListVO();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpQueryPublicParamBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

                dfpPublicParamListVO.setPageIndex(dfpQueryPublicParamBO.getPageIndex());
                dfpPublicParamListVO.setPageSize(dfpQueryPublicParamBO.getPageSize());
            }

            @Override
            public void doOperate() throws Exception {

                //分页获取数据
                Page<DfpPublicParamModel> dfpPublicParamModelPage = getPublicParamByPage(dfpQueryPublicParamBO);
                if (dfpPublicParamModelPage != null) {

                    //数据总数
                    dfpPublicParamListVO.setTotalNum(dfpPublicParamModelPage.getTotalElements());

                    List<DfpPublicParamVO> dfpPublicParamVOS = new ArrayList<>();
                    List<DfpPublicParamModel> dfpPublicParamModels = dfpPublicParamModelPage.getContent();
                    for (DfpPublicParamModel dfpPublicParamModel : dfpPublicParamModels) {

                        //组装模型
                        DfpPublicParamVO dfpPublicParamVO = new DfpPublicParamVO();
                        dfpPublicParamVO.setDfpPublicParamModel(dfpPublicParamModel);
                        dfpPublicParamVO.setDfpUserModel(dfpUserDAO.findById(dfpPublicParamModel.getCreatorId()));
                        dfpPublicParamVOS.add(dfpPublicParamVO);
                    }
                    dfpPublicParamListVO.setDfpPublicParamVOS(dfpPublicParamVOS);
                }
                defenderResult.setData(dfpPublicParamListVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 删除动态参数
     *
     * @param id
     * @return
     */
    public DfplatformResult<Integer> deleteDynamicParam(int id) {

        //定义返回结果
        DfplatformResult<Integer> defenderResult = new DfplatformResult<>();

        //开启事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.state(id > 0, "动态参数ID必须大于0");
                DfpPublicParamModel dfpPublicParamModel = dfpPublicParamDAO.findById(id);
                DfplaformUtil.isNotNull(dfpPublicParamModel, "要删除的参数不存在");

                List<DfpSceneParamsModel> dfpSceneParamsModels = dfpSceneParamsDAO.findByParamKeyAndParamType(dfpPublicParamModel.getParamKey(),
                        "public");
                DfplaformUtil.isBlank(dfpSceneParamsModels, "有场景在使用该参数不能删除");
            }

            @Override
            public void doPacker() {

            }

            @Override
            public void doOperate() throws Exception {

                dfpPublicParamDAO.deleteById(id);
                defenderResult.setData(id);
                defenderResult.setMessage("删除动态参数成功");
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 分页查询动态参数信息
     *
     * @param dfpQueryPublicParamBO
     * @return
     */
    private Page<DfpPublicParamModel> getPublicParamByPage(DfpQueryPublicParamBO dfpQueryPublicParamBO) {

        //设置排序规则
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(dfpQueryPublicParamBO.getPageIndex(), dfpQueryPublicParamBO.getPageSize(), sort);
        Specification<DfpPublicParamModel> spec = new Specification<DfpPublicParamModel>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<DfpPublicParamModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //查询条件
                List<Predicate> list = new ArrayList<Predicate>();
                //关键词不为空，则作为查询条件
                if (StringUtils.isNotBlank(dfpQueryPublicParamBO.getParamKey())) {

                    list.add(criteriaBuilder.like(root.get("paramKey"), "%" + dfpQueryPublicParamBO.getParamKey() + "%"));
                }
                //关键词不为空，则作为查询条件
                if (StringUtils.isNotBlank(dfpQueryPublicParamBO.getParamClassPath())) {
                    list.add(criteriaBuilder.like(root.get("paramClassPath"), "%" + dfpQueryPublicParamBO.getParamClassPath() + "%"));
                }
                //如果用户Id不为空，则作为查询条件
                if (dfpQueryPublicParamBO.getUserId() > 0) {
                    list.add(criteriaBuilder.equal(root.get("creatorId"), dfpQueryPublicParamBO.getUserId()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
        return dfpPublicParamDAO.findAll(spec, pageable);
    }
}
