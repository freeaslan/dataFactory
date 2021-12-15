package com.esign.service.dfplatform.service;

import com.esign.service.dfplatform.BO.DfpUserBO;
import com.esign.service.dfplatform.BO.DfpUserListBO;
import com.esign.service.dfplatform.VO.DfpUserListVO;
import com.esign.service.dfplatform.base.DfplatformResult;
import com.esign.service.dfplatform.base.OperateCallBack;
import com.esign.service.dfplatform.base.OperateTemplate;
import com.esign.service.dfplatform.daointerface.DfpUserDAO;
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
 * @Author: yunge
 * @Description: 用户服务
 * @Date: 2021/5/6 9:19
 */
@Service
public class DfpUserService {

    @Autowired
    OperateTemplate operateTemplate;

    @Autowired
    DfpUserDAO dfpUserDAO;

    /**
     * 用户登录
     *
     * @param dfpUserBO
     * @return
     */
    public DfplatformResult<DfpUserModel> userLogin(DfpUserBO dfpUserBO) {

        //定义返回结果
        DfplatformResult<DfpUserModel> defaultResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpUserBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                //根据用户名和密码登录 获取用户信息
                DfpUserModel dfpUserModel = dfpUserDAO.findByUsername(dfpUserBO.getUsername());
                DfplaformUtil.isNotNull(dfpUserModel, "用户不存在");
                DfplaformUtil.state(dfpUserBO.getPassword().equals(dfpUserModel.getPassword()), "密码输入不正确");
                defaultResult.setData(dfpUserModel);
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 注册用户
     *
     * @param dfpUserBO
     * @return
     */
    public DfplatformResult<DfpUserModel> createNewUser(DfpUserBO dfpUserBO) {

        //定义结果
        DfplatformResult<DfpUserModel> defaultResult = new DfplatformResult<>();

        //事务
        operateTemplate.operateWithTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpUserBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {
            }

            @Override
            public void doOperate() throws Exception {

                DfpUserModel checkUserModel = dfpUserDAO.findByUsername(dfpUserBO.getUsername());
                DfplaformUtil.isNull(checkUserModel, "注册用户已经存在");

                DfpUserModel dfpUserModel = new DfpUserModel();
                ObjectConverterUtil.convert(dfpUserBO, dfpUserModel);
                dfpUserModel = dfpUserDAO.save(dfpUserModel);

                //设置结果
                defaultResult.setData(dfpUserModel);
                defaultResult.setMessage("注册成功");
            }
        }, defaultResult);

        return defaultResult;
    }

    /**
     * 获取用户信息
     *
     * @param dfpUserListBO
     * @return
     */
    public DfplatformResult<DfpUserListVO> getUserList(DfpUserListBO dfpUserListBO) {

        DfplatformResult<DfpUserListVO> defenderResult = new DfplatformResult<>();
        DfpUserListVO dfpUserListVO = new DfpUserListVO();

        //事务
        operateTemplate.operateWithOutTransaction(new OperateCallBack() {
            @Override
            public void doCheck() {

                DfplaformUtil.isNotNull(dfpUserListBO, "请求对象不能为空");
            }

            @Override
            public void doPacker() {

                dfpUserListVO.setPageIndex(dfpUserListBO.getPageIndex());
                dfpUserListVO.setPageSize(dfpUserListBO.getPageSize());
            }

            @Override
            public void doOperate() throws Exception {

                //分页获取数据
                Page<DfpUserModel> dfpUserModelPage = getUserByPage(dfpUserListBO);
                if (dfpUserModelPage != null) {

                    dfpUserListVO.setTotalNum(dfpUserModelPage.getTotalElements());
                    dfpUserListVO.setUserList(dfpUserModelPage.getContent());
                }
                defenderResult.setData(dfpUserListVO);
            }
        }, defenderResult);

        return defenderResult;
    }

    /**
     * 分页查询用户信息
     *
     * @param dfpUserListBO
     * @return
     */
    private Page<DfpUserModel> getUserByPage(DfpUserListBO dfpUserListBO) {

        //设置排序规则
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(dfpUserListBO.getPageIndex(), dfpUserListBO.getPageSize(), sort);
        Specification<DfpUserModel> spec = new Specification<DfpUserModel>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<DfpUserModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //查询条件
                List<Predicate> list = new ArrayList<Predicate>();
                //用户名不为空，则作为查询条件
                if (StringUtils.isNotBlank(dfpUserListBO.getUsername())) {

                    list.add(criteriaBuilder.equal(root.get("username"), dfpUserListBO.getUsername()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
        return dfpUserDAO.findAll(spec, pageable);
    }
}
