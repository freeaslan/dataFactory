package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpEnvParamsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description: 环境参数DAO
 * @author: lingxi
 * @date: 2020/11/16 11:38
 **/
public interface DfpEnvParamsDAO extends JpaRepository<DfpEnvParamsModel, Long>, JpaSpecificationExecutor<DfpEnvParamsModel> {

    DfpEnvParamsModel findByEnvIdAndProjectNameAndServiceName(int envId, String projectName, String serviceName);

    DfpEnvParamsModel findById(int id);

    void deleteById(int id);
}
