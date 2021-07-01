package com.esign.service.dfplatform.VO;

import com.esign.service.dfplatform.model.DfpApisModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

/**
 * @Description: 对应数据库表dfp_apis模型
 * @author: lingxi
 * @date: 2020/11/30 18:02
 **/
@Data
public class DfpApisModelVO extends DfpApisModel {

    Map<String, Object> data;
}
