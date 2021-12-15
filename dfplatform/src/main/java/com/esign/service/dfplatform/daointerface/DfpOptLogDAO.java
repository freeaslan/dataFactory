package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpOptLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author huangtai
 * @Description: dfp_opt_log表操作
 * @date: 2021/8/3 9:38
 */
public interface DfpOptLogDAO extends JpaRepository<DfpOptLogModel, Long>, JpaSpecificationExecutor<DfpOptLogModel> {

}
