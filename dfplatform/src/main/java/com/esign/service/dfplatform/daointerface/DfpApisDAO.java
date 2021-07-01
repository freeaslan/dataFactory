package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpApisModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description: api接口DAO
 * @author: lingxi
 * @date: 2020/11/30 17:45
 **/
public interface DfpApisDAO extends JpaRepository<DfpApisModel, Long> {

    DfpApisModel findById(int id);

    DfpApisModel findByProjectNameAndServiceNameAndUrlAndMethod(String projectName, String serviceName, String url, String method);

    @Query(value = "SELECT DISTINCT service_name FROM  dfp_apis where project_name=?1", nativeQuery = true)
    List<String> findDistinctByProjectName(String projectName);

    List<DfpApisModel> findByServiceName(String serviceName);
}
