set names utf8;

INSERT INTO `dfp_apis` VALUES ('1', 'example', 'demo', '/dfplatform/addMenu', 'you server ip:8180', 'post', 'dfp-menu-controller', '创建目录和菜单', null, null,0,0);
INSERT INTO `dfp_apis` VALUES ('2', 'example', 'demo', '/dfplatform/addModule', 'you server ip:8180', 'post', 'dfp-module-controller', '新增模块', null, null,0,0);

INSERT INTO `dfp_menu` VALUES ('1', 'demo', '0', 'shoucang', '0',0,0);
INSERT INTO `dfp_menu` VALUES ('2', 'example', '1', 'zhedie', '1',0,0);
INSERT INTO `dfp_menu` VALUES ('3', '环境配置', '1', 'shezhi', '2',0,0);

INSERT INTO `dfp_module` VALUES ('1', 'example', 'demo',0);

INSERT INTO `dfp_public_param` VALUES ('1', 'jarDemo', 'jar', 'IDString', 'com.tsign.RandomUtil', 'String:123', 'jarDemo.jar', '1', '1', '1', '1');
INSERT INTO `dfp_public_param` VALUES ('2', 'classDemo', 'class', 'ChineseString', 'com.esign.service.tmsdefender.util.RandomUtil', 'int:4', 'RandomUtil.class', '1', '1', '1', '1');

INSERT INTO `dfp_scene` VALUES ('1', 'example', '1', 'jardemo', '1', '1');
INSERT INTO `dfp_scene` VALUES ('2', 'example', '1', 'classdemo', '1', '1');

INSERT INTO `dfp_scene_params` VALUES ('1', '1', '1', 'jarDemo', 'public', '菜单名称', 'example2');
INSERT INTO `dfp_scene_params` VALUES ('2', '1', '1', 'projectName', 'response', '从response获取值','$.data.name');
INSERT INTO `dfp_scene_params` VALUES ('3', '1', '2', 'moduleName', 'normal', '模块名称', 'moduleDemo');
INSERT INTO `dfp_scene_params` VALUES ('7', '2', '1', 'classDemo', 'public', '菜单名称', '3');
INSERT INTO `dfp_scene_params` VALUES ('8', '2', '1', 'projectName', 'response', '从response获取值', '$.data.name');
INSERT INTO `dfp_scene_params` VALUES ('9', '2', '2', 'moduleName', 'normal', '模块名称', 'moduleDemo');

INSERT INTO `dfp_scene_request` VALUES ('1', '1', '1', '0', '新增example2', '/dfplatform/addMenu', '{\"type\":\"post\",\"host\":\"you server ip:8180\",\"url\":\"/dfplatform/addMenu\",\"headers\":{\"Content-Type\":\"application/json;charset=UTF-8\"},\"query\":{},\"path\":null,\"requestBody\":{\"icon\":\"zhedie\",\"id\":0,\"name\":\"{{jarDemo}}\",\"parentId\":1,\"type\":1}}');
INSERT INTO `dfp_scene_request` VALUES ('2', '1', '2', '1', 'example2新增模块demo', '/dfplatform/addModule', '{\"type\":\"post\",\"host\":\"you server ip:8180\",\"url\":\"/dfplatform/addModule\",\"headers\":{\"Content-Type\":\"application/json;charset=UTF-8\"},\"query\":{},\"path\":null,\"requestBody\":{\"moduleName\":\"{{moduleName}}\",\"projectName\":\"{{projectName}}\"}}');
INSERT INTO `dfp_scene_request` VALUES ('5', '2', '1', '0', '新增xample2', '/dfplatform/addMenu', '{\"type\":\"post\",\"host\":\"you server ip:8180\",\"url\":\"/dfplatform/addMenu\",\"headers\":{\"Content-Type\":\"application/json;charset=UTF-8\"},\"query\":{},\"path\":null,\"requestBody\":{\"icon\":\"zhedie\",\"id\":0,\"name\":\"{{classDemo}}\",\"parentId\":1,\"type\":1}}');
INSERT INTO `dfp_scene_request` VALUES ('6', '2', '2', '1', 'example2新增模块demo', '/dfplatform/addModule', '{\"type\":\"post\",\"host\":\"you server ip:8180\",\"url\":\"/dfplatform/addModule\",\"headers\":{\"Content-Type\":\"application/json;charset=UTF-8\"},\"query\":{},\"path\":null,\"requestBody\":{\"moduleName\":\"{{moduleName}}\",\"projectName\":\"{{projectName}}\"}}');
#you server ip:8180  需要修改为后端服务地址
INSERT INTO `dfp_env_params` VALUES ('1', '2', 'example', 'demo', '{}', 'you server ip:8180');