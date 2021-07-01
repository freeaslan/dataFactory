package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpPublicParamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author huangtai
 * @Description: dfp_public_param表操作
 * @date: 2021/06/08 11:38
 */
public interface DfpPublicParamDAO extends JpaRepository<DfpPublicParamModel, Long>, JpaSpecificationExecutor<DfpPublicParamModel> {

    DfpPublicParamModel findById(int id);

    DfpPublicParamModel findByParamKey(String keyName);
}
