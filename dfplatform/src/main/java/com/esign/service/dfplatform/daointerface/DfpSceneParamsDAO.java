package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpSceneParamsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * dfp_scene_params表操作
 *
 * @author huangtai
 */
public interface DfpSceneParamsDAO extends JpaRepository<DfpSceneParamsModel, Long> {

    void deleteBySceneId(int sceneId);

    List<DfpSceneParamsModel> findBySceneId(int scendId);

    List<DfpSceneParamsModel> findBySceneIdAndApiIdAndParamTypeNot(int scendId, int apiId, String paramType);

    List<DfpSceneParamsModel> findBySceneIdAndApiIdAndParamType(int scendId, int apiId, String paramType);

    List<DfpSceneParamsModel> findBySceneIdAndParamTypeNot(int scendId, String paramType);

    List<DfpSceneParamsModel> findByParamKeyAndParamType(String paramKey, String paramType);
}
