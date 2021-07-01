package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpSceneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * dfp_scene表操作
 *
 * @author huangtai
 */
public interface DfpSceneDAO extends JpaRepository<DfpSceneModel, Long>, JpaSpecificationExecutor<DfpSceneModel> {

    DfpSceneModel findById(int id);

    void deleteById(int id);

    List<DfpSceneModel> findByModuleId(int moduleId);
}
