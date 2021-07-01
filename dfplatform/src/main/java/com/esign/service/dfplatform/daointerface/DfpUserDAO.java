package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author yunge
 * @Description: dfp_user表操作
 * @date: 2020/11/16 11:38
 */
public interface DfpUserDAO extends JpaRepository<DfpUserModel, Long>, JpaSpecificationExecutor<DfpUserModel> {

    DfpUserModel findByUsername(String username);

    DfpUserModel findById(int id);
}
