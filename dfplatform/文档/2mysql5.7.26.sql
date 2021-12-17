SET FOREIGN_KEY_CHECKS=0;

DROP database IF EXISTS dfplaform;
Create database dfplaform;

USE dfplaform;

DROP TABLE IF EXISTS `dfp_apis`;
CREATE TABLE `dfp_apis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  `url` varchar(2000) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `header` text DEFAULT NULL,
  `request_body` text DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `modifier_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_custom_api`;
CREATE TABLE `dfp_custom_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) DEFAULT NULL,
  `scene_id` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_env_enum`;
CREATE TABLE `dfp_env_enum` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `env_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_env_params`;
CREATE TABLE `dfp_env_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `env_id` int(11) NOT NULL,
  `project_name` varchar(255) NOT NULL,
  `service_name` varchar(255) NOT NULL,
  `header` text,
  `host` text NOT NULL,
  `creator_id` int(11) DEFAULT '0',
  `modifier_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_menu`;
CREATE TABLE `dfp_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `icon` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `creator_id` int(11) DEFAULT '0',
  `modifier_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_module`;
CREATE TABLE `dfp_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) DEFAULT NULL,
  `module_name` varchar(255) DEFAULT NULL,
  `creator_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_opt_log`;
CREATE TABLE `dfp_opt_log` (
  `id` int(11)  NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT '0',
  `operate_log` varchar(255) DEFAULT '',
  `status` int(11) DEFAULT '0' COMMENT '0 成功，1失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_public_param`;
CREATE TABLE `dfp_public_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(255) DEFAULT NULL COMMENT '参数值',
  `param_class_type` varchar(255) DEFAULT NULL COMMENT '读取参数来源 jar/class',
  `param_class_method` varchar(255) DEFAULT NULL COMMENT '读取参数方法',
  `param_class_path` varchar(255) DEFAULT NULL COMMENT '读取参数类包路径',
  `param_class_params_data` varchar(255) DEFAULT NULL COMMENT '方法需要的参数',
  `jar_name` varchar(255) DEFAULT NULL COMMENT 'jar\\class名称',
  `after_other_param` int(11) DEFAULT NULL COMMENT '0表示其它参数替换后再替换，1表示随机替换',
  `is_need_body` int(11) DEFAULT NULL COMMENT '0表示需要body作为参数，1表示不需要',
  `creator_id` int(11) DEFAULT NULL,
  `modifier_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_scene`;
CREATE TABLE `dfp_scene` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) DEFAULT NULL,
  `module_id` int(11) DEFAULT NULL,
  `scene_name` varchar(255) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `modifier_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_scene_params`;
CREATE TABLE `dfp_scene_params` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_id` int(11) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `param_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `param_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `default_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_scene_request`;
CREATE TABLE `dfp_scene_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scene_id` int(11) DEFAULT NULL,
  `api_id` int(11) DEFAULT NULL,
  `api_order` int(11) DEFAULT NULL,
  `request_descript` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `request_other_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `api_request` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `dfp_user`;
CREATE TABLE `dfp_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO `dfp_env_enum` VALUES ('1', '项目环境');
INSERT INTO `dfp_env_enum` VALUES ('2', '测试环境');
INSERT INTO `dfp_env_enum` VALUES ('3', '开发环境');
INSERT INTO `dfp_env_enum` VALUES ('4', '模拟环境');
INSERT INTO `dfp_env_enum` VALUES ('5', '线上环境');
