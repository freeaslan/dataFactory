## dfplaform-web
- dfplaform-web基于vue、element-ui构建开发，实现后台管理前端功能，提供一系列的功能


## 功能
- 账号注册，登录/退出
- 系统管理：通过菜单管理可以添加目录、菜单和环境设置
- 动态参数：平台通用的参数配置。可以动态加载jar和class
- 环境管理：配置各环境参数。比如：域名、header信息
- 项目：swagger导入、创建模块、创建场景、请求场景等

## 克隆项目
git clone https://github.com/freeaslan/dataFactory.git

## 安装依赖
npm install

## 启动服务
npm run dev

## 打包，打包生成的文件在/dist下
npm run build


## 说明文档
1.对接后后端api接口
修改/static/config/index.js目录文件中 window.SITE_CONFIG['baseUrl'] = '后端api接口请求地址';
修改/static/config/index-pro.js目录文件中 window.SITE_CONFIG['baseUrl'] = '后端api接口请求地址';

2.打包之后，发布需要的文件
/dist目录下：版本号（静态资源，年月日时分）、config（配置文件）、index.html



