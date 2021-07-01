<template>
  <div class="mod-home">
    <h3>项目介绍</h3>
    <ul>
      <li>
        该数据工厂是一款建立在Dfplatform和Dfplatform-web之上的开源的快速构造数据的平台，它提供了swagger导入接口、自定义接口以及导入postman脚本三种接口导入方式；同时自适应多种屏幕分辨率，我们可以快速的根据业务接口调用快速组装成一个场景并产生测试数据
      </li>
      <li>
        该数据工厂以swagger导入接口为主要接口来源，在很大程度上增加了工厂的灵活性和扩展性，而且我们只需要在业务操作时打开抓包工具，然后把请求的接口以及所需信息复制到工厂中就可以完成造数据的场景，很好的降低业务测试人员参与数据工具创建的门槛
      </li>
    </ul>
    <h3>GitHub源码</h3>
    <ul>
      <li>
        Git地址：<a
          href="https://github.com/freeaslan/dataFactory.git"
          target="_blank"
          >https://github.com/freeaslan/dataFactory.git</a
        >
      </li>
    </ul>
    <h3>相关下载</h3>
    <ul>
      <li><a @click="downloadCaseFile">点此下载操作文档</a></li>
    </ul>
    <h3>浏览器兼容性</h3>
    <ul>
      <li>IE 10+</li>
      <li>360</li>
      <li>Chrome</li>
    </ul>
    <h3>技术栈</h3>
    <ul>
      <li>前端：vue+nodejs+axios+element-ui+iconfont</li>
      <li>后端：SpringBoot+Mysql+JPA+Maven</li>
    </ul>
    <h3>平台功能</h3>
    <ul>
      <li>系统管理：通过菜单管理可以添加目录、菜单和环境设置</li>
      <li>动态参数：平台通用的参数配置。可以动态加载jar和class</li>
      <li>环境管理：配置各环境参数。比如：域名、header信息</li>
      <li>项目：swagger导入、创建模块、创建场景、请求场景等</li>
    </ul>
    <h3>业务架构</h3>
    <ul>
      <span class="el-dropdown-link">
        <img src="~@/assets/img/业务架构.jpg" style="max-width:60%" />
      </span>
    </ul>
    <h3>业务流程</h3>
    <ul>
      <span class="el-dropdown-link">
        <img src="~@/assets/img/业务流程.jpg" style="max-width:60%" />
      </span>
    </ul>
  </div>
</template>

<script>
export default {
  methods: {
    // 下载用例模板
    downloadCaseFile () {
      window.open(
        window.SITE_CONFIG['baseUrl'] + `/dfplatform/documentDownload`
      )
    }
  }
}
</script>

<style>
.mod-home {
  line-height: 1.5;
}
</style>
