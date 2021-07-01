package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpSceneRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * dfp_scene_request表操作
 *
 * @author huangtai
 */
public interface DfpSceneRequestDAO extends JpaRepository<DfpSceneRequestModel, Long> {

    List<DfpSceneRequestModel> findBySceneIdOrderByApiOrderAsc(int sceneId);

    void deleteBySceneId(int sceneId);
}
