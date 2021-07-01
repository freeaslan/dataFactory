package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * dfp_module表操作
 *
 * @author huangtai
 */
public interface DfpModuleDAO extends JpaRepository<DfpModuleModel, Long> {

    DfpModuleModel findById(int id);

    List<DfpModuleModel> findByProjectName(String projectName);

    DfpModuleModel findByModuleNameAndProjectName(String moduleName, String projectName);
}
