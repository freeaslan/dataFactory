package com.esign.service.dfplatform.VO;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: huangtai
 * @Description: 场景请求视图
 * @Date: 2020/9/14 10:54
 */
@Data
public class DfpRequestSceneVO {

    String taskId;

    String script;

    String reportUrl;

    List<Map<String, Object>> responses;
}
