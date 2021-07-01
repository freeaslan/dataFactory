package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpEnvEnumModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: dfp_env_enum表操作
 * @author: huangtai
 * @date: 2021/06/08 17:45
 **/
public interface DfpEnvEnumDAO extends JpaRepository<DfpEnvEnumModel, Long> {

    DfpEnvEnumModel findById(int id);
}
