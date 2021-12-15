package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpOptLogBO;
import com.esign.service.dfplatform.BO.DfpOptLogListBO;
import com.esign.service.dfplatform.VO.DfpOptLogListVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpOptLogDAO;
import com.esign.service.dfplatform.daointerface.DfpUserDAO;
import com.esign.service.dfplatform.model.DfpOptLogModel;
import com.esign.service.dfplatform.model.DfpUserModel;
import com.esign.service.dfplatform.util.DfplaformUtil;
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
import java.util.Date;
import java.util.List;

/**
 * @Author: huangtai
 * @Description: 操作日志服务
 * @Date: 2021/8/3 9:19
 */
@Service
public class DfpOptLogService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpOptLogDAO dfpOptLogDAO;

    @Autowired
    DfpUserDAO dfpUserDAO;

    /**
     * 保存操作日志
     *
     * @param dfpOptLogBO
     * @return
     */
    public DfplatformResult<DfpOptLogModel> addOptLog(DfpOptLogBO dfpOptLogBO) {

        //定义返回结果
        DfplatformResult<DfpOptLogModel> defenderResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpOptLogBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                //保存操作日志信息
                DfpOptLogModel dfpOptLogModel = new DfpOptLogModel();
                dfpOptLogModel.setOperateLog(dfpOptLogBO.getOperateLog());
                dfpOptLogModel.setCreateTime(new Date());
                dfpOptLogModel.setStatus(dfpOptLogBO.getStatus());
                DfpUserModel dfpUserModel = dfpUserDAO.findById(dfpOptLogBO.getUserId());
                if (dfpUserModel != null) {

                    dfpOptLogModel.setUserName(dfpUserModel.getUsername());
                }
                defenderResult.setData(dfpOptLogDAO.save(dfpOptLogModel));
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 获取操作日志信息
     *
     * @param dfpOptLogListBO
     * @return
     */
    public DfplatformResult<DfpOptLogListVO> getOptLogList(DfpOptLogListBO dfpOptLogListBO) {

        DfplatformResult<DfpOptLogListVO> defenderResult = new DfplatformResult<>();
        DfpOptLogListVO dfpOptLogListVO = new DfpOptLogListVO();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpOptLogListBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

                dfpOptLogListVO.setPageIndex(dfpOptLogListBO.getPageIndex());
                dfpOptLogListVO.setPageSize(dfpOptLogListBO.getPageSize());
            }

            @Override
            public void doOperate() throws Exception {

                //分页获取数据
                Page<DfpOptLogModel> dfpOptLogModelPage = getOptLogByPage(dfpOptLogListBO);
                if (dfpOptLogModelPage != null) {

                    dfpOptLogListVO.setTotalNum(dfpOptLogModelPage.getTotalElements());
                    dfpOptLogListVO.setDfpOptLogModels(dfpOptLogModelPage.getContent());
                }
                defenderResult.setData(dfpOptLogListVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 分页查询操作日志
     *
     * @param dfpOptLogListBO
     * @return
     */
    private Page<DfpOptLogModel> getOptLogByPage(DfpOptLogListBO dfpOptLogListBO) {

        //设置排序规则
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(dfpOptLogListBO.getPageIndex(), dfpOptLogListBO.getPageSize(), sort);
        Specification<DfpOptLogModel> spec = new Specification<DfpOptLogModel>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<DfpOptLogModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //查询条件
                List<Predicate> list = new ArrayList<Predicate>();
                //用户名不为空，则作为查询条件
                if (StringUtils.isNotBlank(dfpOptLogListBO.getUsername())) {

                    list.add(criteriaBuilder.like(root.get("userName"), "%" + dfpOptLogListBO.getUsername() + "%"));
                }
                //场景不为空，模糊查询
                if (StringUtils.isNotBlank(dfpOptLogListBO.getOperateLog())) {

                    list.add(criteriaBuilder.like(root.get("operateLog"), "%" + dfpOptLogListBO.getOperateLog() + "%"));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
        return dfpOptLogDAO.findAll(spec, pageable);
    }
}
