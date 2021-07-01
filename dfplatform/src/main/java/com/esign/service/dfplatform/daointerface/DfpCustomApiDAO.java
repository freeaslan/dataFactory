package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpCustomApiModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author huangtai
 * @Description:dfp_custom_api表操作
 */
public interface DfpCustomApiDAO extends JpaRepository<DfpCustomApiModel, Long> {

    List<DfpCustomApiModel> findBySceneId(int sceneId);

    void deleteBySceneId(int sceneId);
}
