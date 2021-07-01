package com.esign.service.dfplatform.BO;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @Author: yuqiang
 * @Description: 场景复制
 * @Date: 2020/3/15 14:58
 */
@Data
public class DfpCpSceneBO {

    @Range(min = 1, message = "场景id应该大于0")
    int sceneId;

    @Range(min = 1, message = "用户id应该大于0")
    int userId;
}
