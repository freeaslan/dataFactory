package com.esign.service.dfplatform.daointerface;

import com.esign.service.dfplatform.model.DfpMenuModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yunge
 * @Description: dfp_menu表操作
 * @date: 2020/11/16 11:38
 */
public interface DfpMenuDAO extends JpaRepository<DfpMenuModel, Long>, JpaSpecificationExecutor<DfpMenuModel> {

    List<DfpMenuModel> findByType(int type);

    List<DfpMenuModel> findByParentId(int parentId);

    DfpMenuModel findByName(String name);

    DfpMenuModel findById(int id);

    @Query(value = "SELECT name FROM dfp_menu WHERE parent_id=? and type =1", nativeQuery = true)
    List<String> findNameByParentId(int parentId);
}
