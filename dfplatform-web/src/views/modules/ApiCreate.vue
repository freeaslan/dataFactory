<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div style="width: 800px">
          <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="所属项目">
              <el-input
                v-model="form.projectName"
                disabled
                style="width: 47%"
              ></el-input>
            </el-form-item>
            <el-form-item label="所属模块">
              <el-select
                v-model="belongModule"
                filterable
                placeholder="请选择"
                clearable
                style="width: 47%"
              >
                <el-option
                  v-for="item in modules"
                  :key="item.id"
                  :label="item.moduleName"
                  :value="item.moduleName"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="场景名称">
              <el-input v-model="form.name" style="width: 47%"></el-input>
            </el-form-item>
            <el-form-item label="步骤描述">
              <el-input v-model="form.describe" style="width: 47%"></el-input>
            </el-form-item>
            <el-row>
              <el-col :span="7">
                <el-form-item label="服务">
                  <el-select
                    v-model="belongService"
                    filterable
                    placeholder="请选择"
                    clearable
                    style="width: 245%"
                  >
                    <el-option
                      v-for="item in services"
                      :key="item.id"
                      :label="item.serviceName"
                      :value="item.serviceName"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="15">
              <el-col :span="13">
                <el-form-item label="接口">
                  <el-select
                    v-model="onePath"
                    filterable
                    placeholder="请选择"
                    clearable
                    style="width: 100%"
                  >
                    <el-option
                      v-for="item in paths"
                      :key="item.id"
                      :label="item.pathName"
                      :value="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-button type="primary" icon="search" @click="addRow"
                  >增加步骤</el-button
                >
              </el-col>
              <el-col :span="3">
                <el-button
                  type="primary"
                  icon="search"
                  @click="addRowDefinition"
                  >自定义</el-button
                >
              </el-col>
              <!--<el-col :span="5">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="脚本导入规则请查看使用说明"
                  placement="top"
                >
                  <el-button
                    style="float: right; margin-right: 10px"
                    type="primary"
                    @click="dialogPostmanVisible = true"
                    >导入postman脚本</el-button
                  >
                </el-tooltip>
                <el-dialog
                  title="导入postman脚本"
                  :visible.sync="dialogPostmanVisible"
                  size="small"
                  width="30%"
                >
                  <el-upload
                    class="upload-demo"
                    :limit="1"
                    drag
                    action=""
                    accept=".json"
                    :before-upload="handleBeforeUpload"
                  >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">
                      将文件拖到此处，或<em>点击上传</em>
                    </div>
                    <div class="el-upload__tip" slot="tip">
                      只能上传.json文件
                    </div>
                  </el-upload>
                </el-dialog>
              </el-col-->
            </el-row>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="grid-content bg-purple-light abc">
          <el-input
            type="textarea"
            resize="none"
            :rows="10"
            v-model="reponseOutput"
            readonly
            style="font-size: 16px"
          ></el-input></div
      ></el-col>
    </el-row>
    <div>
      <el-table :data="newsList">
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-tabs type="border-card">
                <el-tab-pane label="Host">
                  <el-input
                    v-model="scope.row.host"
                    placeholder=""
                    style="width: 46%"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="Url">
                  <el-input
                    v-model="scope.row.url"
                    placeholder=""
                    style="width: 50%"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="Header">
                  <el-input
                    type="textarea"
                    v-model="scope.row.header"
                    placeholder=""
                    style="width: 50%"
                    :rows="8"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="Method">
                  <el-input
                    v-model="scope.row.method"
                    placeholder=""
                    style="width: 50%"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="query">
                  <el-input
                    type="textarea"
                    v-model="scope.row.query"
                    placeholder=""
                    style="width: 50%"
                    :rows="16"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="RequestBody">
                  <el-input
                    type="textarea"
                    v-model="scope.row.data"
                    placeholder=""
                    style="width: 50%"
                    :rows="16"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="Variable">
                  <el-input
                    type="textarea"
                    v-model="scope.row.variables"
                    placeholder=""
                    style="width: 50%"
                    :rows="16"
                  ></el-input>
                </el-tab-pane>
                <el-tab-pane label="Parameters">
                  <el-input
                    type="textarea"
                    v-model="scope.row.parameters"
                    placeholder=""
                    style="width: 50%"
                    :rows="16"
                  ></el-input>
                </el-tab-pane>
              </el-tabs>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="onePath" label="接口名" min-width="220">
        </el-table-column>
        <el-table-column label="步骤描述" min-width="280">
          <template slot-scope="scope">
            <el-input
              placeholder="请输入内容"
              v-show="show"
              v-model="scope.row.describe"
            ></el-input>
            <span v-show="!show">{{ scope.row.describe }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click.stop="sortUp(scope.$index, scope.row)"
              >向上↑
            </el-button>
            <el-button
              size="mini"
              type="text"
              @click.stop="sortDown(scope.$index, scope.row)"
              >向下↓
            </el-button>
            <el-button
              size="mini"
              type="text"
              @click.stop="remove(scope.$index)"
            >
              删除
            </el-button>
            <el-button size="mini" type="text" @click="show = true">
              编辑
            </el-button>
            <!-- <el-button size='mini'
              type='text' @click='show =false'>完成</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="margin-top: 20px; float: right">
      <el-button
        type="primary"
        icon="search"
        @click="debug"
        style="margin-right: 20px"
      >
        调试
      </el-button>
      <el-button type="primary" @click="log">查看报告</el-button>
      <el-button
        type="primary"
        icon="search"
        @click="save"
        v-if="sceneId == ''"
      >
        创建
      </el-button>
      <el-button type="primary" icon="search" @click="edit" v-if="sceneId != ''"
        >修改
      </el-button>
    </div>
  </div>
</template>

<script>
import MiddleUtil from './MiddleUtil'
export default {
  name: 'demoCreate',
  props: {},
  data () {
    return {
      form: {
        projectName: '',
        name: '',
        describe: ''
      },
      ApiListUrl: { name: 'demoList' },
      sceneId: '',
      isSuccess: false,
      dfp_url: window.SITE_CONFIG.baseUrl,
      log_url: '',
      belongModule: '',
      belongService: '',
      onePath: '',
      modules: [],
      services: [],
      paths: [],
      newsList: [],
      dialogPostmanVisible: false,
      dialogSettingFileVisible: false,
      reponseOutput: '',
      show: false
    }
  },
  created () {
    this.form.projectName = sessionStorage.getItem('projectName')
    this.get_modules()
    this.get_service()
    var sceneId = this.$route.query.sceneId

    // eslint-disable-next-line eqeqeq
    if (sceneId != undefined) {
      this.get_scene(sceneId)
      this.sceneId = sceneId
    }
  },
  watch: {
    belongService (val) {
      var serviceName = val
      this.get_path(serviceName)
    }
  },
  methods: {
    get_modules () {
      let _this = this
      var url = this.dfp_url + '/dfplatform/queryModule'
      this.$axios
        .get(url, {
          params: {
            projectName: this.form.projectName
          }
        })
        .then(res => {
          var datas = res.data
          var code = datas.code
          // eslint-disable-next-line eqeqeq
          if (code == 0) {
            var list = []
            for (var i = 0; i < datas.data.length; i++) {
              var map = {}
              var item = datas.data[i]
              map.id = item.id
              map.moduleName = item.moduleName
              list.push(map)
            }
            this.modules = list
          } else {
            _this.$message.error(datas.message)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    get_scene (sceneId) {
      let _this = this
      var url = this.dfp_url + '/dfplatform/queryScene'
      this.$axios
        .get(url, {
          params: {
            sceneId: sceneId
          }
        })
        .then(res => {
          var datas = res.data
          var code = datas.code
          var data = datas.data
          // eslint-disable-next-line eqeqeq
          if (code == 0) {
            var moduleId = data.moduleId
            _this.form.name = data.sceneName
            _this.form.creatorName = data.creatorName

            for (var j = 0; j < _this.modules.length; j++) {
              // eslint-disable-next-line eqeqeq
              if (moduleId == _this.modules[j].id) {
                _this.belongModule = _this.modules[j].moduleName
              }
            }
            var dfpHttpRequestBOS = data.dfpHttpRequestBOS
            // eslint-disable-next-line no-redeclare
            for (var j = 0; j < dfpHttpRequestBOS.length; j++) {
              var map = {}
              var item = dfpHttpRequestBOS[j]
              map.id = item.apiId
              map.onePath = item.requestOtherName
              map.describe = item.requestDescript
              map.host = item.httpClientRequest.host
              map.url = item.httpClientRequest.url
              map.header = JSON.stringify(
                item.httpClientRequest.headers,
                null,
                2
              )
              map.method = item.httpClientRequest.type
              map.data = JSON.stringify(
                item.httpClientRequest.requestBody,
                null,
                2
              )
              map.query = JSON.stringify(item.httpClientRequest.query)
              map.variables = JSON.stringify(item.dfpParamsBOS, null, 2)
              map.parameters = JSON.stringify(
                item.dfpGetFromResponseBOS,
                null,
                2
              )
              _this.newsList.push(map)
            }
          } else {
            _this.$message.error(datas.message)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    get_service () {
      let _this = this
      var url = this.dfp_url + '/dfplatform/getServices'
      this.$axios
        .get(url, {
          params: {
            projectName: this.form.projectName
          }
        })
        .then(res => {
          var datas = res.data
          var code = datas.code
          // eslint-disable-next-line eqeqeq
          if (code == 0) {
            var list = []
            for (var i = 0; i < datas.data.length; i++) {
              var map = {}
              var item = datas.data[i]
              map.id = item.id
              map.serviceName = item.serviceName
              list.push(map)
            }
            this.services = list
          } else {
            _this.$message.error(datas.message)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    get_path (serviceName) {
      let _this = this
      var url = this.dfp_url + '/dfplatform/getPaths'
      this.$axios
        .get(url, {
          params: {
            serviceName: serviceName
          }
        })
        .then(res => {
          var datas = res.data
          var code = datas.code
          // eslint-disable-next-line eqeqeq
          if (code == 0) {
            var list = []
            for (var i = 0; i < datas.data.length; i++) {
              var map = {}
              var item = datas.data[i]
              map.id = item.id
              map.pathName = item.pathName
              list.push(map)
            }
            this.paths = list
          } else {
            _this.$message.error(datas.message)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    sortUp (index, row) {
      if (index === 0) {
        this.$message({
          message: '已经是列表中第一个接口！',
          type: 'warning'
        })
      } else {
        let temp = this.newsList[index - 1]
        this.$set(this.newsList, index - 1, this.newsList[index])
        this.$set(this.newsList, index, temp)
      }
    },
    sortDown (index, row) {
      if (index === this.newsList.length - 1) {
        this.$message({
          message: '已经是列表中最后一个接口！',
          type: 'warning'
        })
      } else {
        let i = this.newsList[index + 1]
        this.$set(this.newsList, index + 1, this.newsList[index])
        this.$set(this.newsList, index, i)
      }
    },
    remove (index) {
      this.newsList.splice(index, 1)
    },
    async addRow () {
      let _this = this
      var describe = this.form.describe
      var belongService = this.belongService
      var onePath = this.onePath
      // eslint-disable-next-line eqeqeq
      if (describe == '' || belongService == '' || onePath == '') {
        _this.$message.error('步骤描述或服务或接口不能为空！')
      } else {
        var url = this.dfp_url + '/dfplatform/getApis'
        this.$axios
          .get(url, {
            params: {
              id: onePath,
              projectName: _this.form.projectName
            }
          })
          .then(res => {
            var datas = res.data
            var code = datas.code
            // eslint-disable-next-line eqeqeq
            if (code === 0) {
              var map = {}
              var item = datas.data
              map.id = item.id
              map.onePath = item.url
              map.describe = describe
              var gethost = item.host
              var getheader = item.header
              // eslint-disable-next-line camelcase,eqeqeq
              if (gethost === '') {
                map.host = item.host
              } else {
                // eslint-disable-next-line camelcase
                map.host = gethost
              }
              map.url = item.url
              // eslint-disable-next-line camelcase,eqeqeq
              if (getheader === '') {
                map.header = '{}'
              } else {
                map.header = JSON.stringify(JSON.parse(getheader), null, 2)
              }
              map.method = item.method
              map.variables = '[]'
              map.parameters = '[]'
              // eslint-disable-next-line eqeqeq
              if (item.method === 'get' || item.method === 'delete') {
                map.query = JSON.stringify(item.data, null, 2)
                map.data = '{}'
              } else {
                map.data = JSON.stringify(item.data, null, 2)
                map.query = '{}'
              }
              this.newsList.push(map)
              _this.form.describe = ''
              // }
            } else {
              _this.$message.error(datas.message)
            }
          })
          .catch(err => {
            this.$message.error(err.response.data.message)
          })
      }
    },
    addRowDefinition () {
      var map = {}
      // map.id = item.id;
      map.onePath = '自定义接口'
      map.describe = '自定义描述'
      map.host = ''
      map.url = ''
      map.header = '{}'
      map.method = 'get/post/put/delete'
      map.query = '{}'
      map.data = '{}'
      map.variables = '[]'
      map.parameters = '[]'

      this.newsList.push(map)
    },
    debug () {
      let _this = this
      var belongModule = this.belongModule
      var modules = this.modules
      for (var i = 0; i < modules.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (modules[i].moduleName == belongModule) {
          var moduleId = modules[i].id
        }
      }
      var projectName = this.form.projectName
      var sceneName = this.form.name
      var newsList = this.newsList
      var dfpHttpRequestBOS = []
      for (var j = 0; j < newsList.length; j++) {
        var map = {}
        var map1 = {}
        map.apiId = newsList[j].id
        map.apiOrder = j
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}'
        ) {
          map.dfpGetFromResponseBOS = JSON.parse(
            newsList[j].parameters.replace(/[\r\n]/g, '')
          )
        } else {
          map.dfpGetFromResponseBOS = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != 'null'
        ) {
          map.dfpParamsBOS = JSON.parse(
            newsList[j].variables.replace(/[\r\n]/g, '')
          )
        } else {
          map.dfpParamsBOS = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].header != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].header != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].header != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].header != 'null'
        ) {
          map1.headers = JSON.parse(newsList[j].header.replace(/[\r\n]/g, ''))
        } else {
          map1.headers = {}
        }
        map1.host = newsList[j].host
        map1.type = newsList[j].method
        map1.url = newsList[j].url
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].data != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].data != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].data != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].data != 'null'
        ) {
          map1.requestBody = JSON.parse(
            newsList[j].data.replace(/[\r\n]/g, '').replace(/\s+/g, '')
          )
        } else {
          map1.requestBody = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].query != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].query != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].query != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].query != 'null'
        ) {
          map1.query = JSON.parse(
            newsList[j].query.replace(/[\r\n]/g, '').replace(/\s+/g, '')
          )
        } else {
          map1.query = null
        }
        map.httpClientRequest = map1
        map.requestDescript = newsList[j].describe
        map.requestOtherName = newsList[j].onePath
        dfpHttpRequestBOS.push(map)
      }
      var requestData = {}
      requestData.dfpHttpRequestBOS = dfpHttpRequestBOS
      requestData.moduleId = moduleId
      requestData.projectName = projectName
      requestData.sceneName = sceneName
      // eslint-disable-next-line eqeqeq
      if (dfpHttpRequestBOS.length == 0) {
        _this.$message.error('无法调试，请增加步骤！')
      } else {
        let _this = this
        var url = this.dfp_url + '/dfplatform/debugSecne'
        this.$axios
          .post(url, requestData, {})
          .then(res => {
            var datas = res.data
            var code = datas.code
            this.reponseOutput = JSON.stringify(datas, null, 2)
            _this.log_url = datas.data.reportUrl
            // eslint-disable-next-line eqeqeq
            if (code == 0) {
              _this.isSuccess = true
            } else {
              _this.isSuccess = true
              _this.$message.error(datas.message)
            }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
    },
    save () {
      let _this = this
      var belongModule = this.belongModule
      var operator = window.localStorage.getItem('userId')
      var modules = this.modules
      for (var i = 0; i < modules.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (modules[i].moduleName == belongModule) {
          var moduleId = modules[i].id
        }
      }
      var projectName = this.form.projectName
      var sceneName = this.form.name
      var newsList = this.newsList
      var dfpHttpRequestBOS = []
      for (var j = 0; j < newsList.length; j++) {
        var map = {}
        var map1 = {}
        map.apiId = newsList[j].id
        map.apiOrder = j
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != 'null'
        ) {
          map.dfpGetFromResponseBOS = JSON.parse(
            newsList[j].parameters.replace(/[\r\n]/g, '')
          )
        } else {
          map.dfpGetFromResponseBOS = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != 'null'
        ) {
          map.dfpParamsBOS = JSON.parse(
            newsList[j].variables.replace(/[\r\n]/g, '')
          )
        } else {
          map.dfpParamsBOS = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != 'null'
        ) {
          map1.headers = JSON.parse(newsList[j].header.replace(/[\r\n]/g, ''))
        } else {
          map1.headers = {}
        }
        map1.host = newsList[j].host
        map1.type = newsList[j].method
        map1.url = newsList[j].url
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != 'null'
        ) {
          map1.requestBody = JSON.parse(
            newsList[j].data.replace(/[\r\n]/g, '').replace(/\s+/g, '')
          )
        } else {
          map1.requestBody = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].variables != 'null'
        ) {
          map1.query = JSON.parse(
            newsList[j].query.replace(/[\r\n]/g, '').replace(/\s+/g, '')
          )
        } else {
          map1.query = null
        }
        map.httpClientRequest = map1
        map.requestDescript = newsList[j].describe
        map.requestOtherName = newsList[j].onePath
        dfpHttpRequestBOS.push(map)
      }
      var requestData = {}
      requestData.dfpHttpRequestBOS = dfpHttpRequestBOS
      requestData.moduleId = moduleId
      requestData.projectName = projectName
      requestData.sceneName = sceneName
      requestData.userId = operator

      if (
        // eslint-disable-next-line eqeqeq
        dfpHttpRequestBOS.length == 0 ||
        // eslint-disable-next-line eqeqeq
        sceneName == '' ||
        // eslint-disable-next-line eqeqeq
        belongModule == ''
      ) {
        _this.$message.error(
          '无法保存，请增加步骤或选择所属模块或填写场景名称！'
        )
        // eslint-disable-next-line eqeqeq
      } else {
        let _this = this
        var url = this.dfp_url + '/dfplatform/addScene'
        this.$axios
          .post(url, requestData, {})
          .then(res => {
            var datas = res.data
            var code = datas.code
            // eslint-disable-next-line eqeqeq
            if (code == 0) {
              _this.$alert('成功', '保存结果', {
                confirmButtonText: '确定',
                callback: action => {
                  MiddleUtil.$emit('demo', 'demoCreate')
                  MiddleUtil.$emit('listReload', 0)
                  // _this.$parent.queryTable()
                }
              })
            } else {
              _this.$message.error(datas.message)
            }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
    },
    edit () {
      let _this = this
      var belongModule = this.belongModule
      var modules = this.modules
      // var creatorName = this.form.creatorName
      var operator = window.localStorage.getItem('userId')
      for (var i = 0; i < modules.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (modules[i].moduleName == belongModule) {
          var moduleId = modules[i].id
        }
      }
      var projectName = this.form.projectName
      var sceneName = this.form.name
      var newsList = this.newsList
      var dfpHttpRequestBOS = []
      for (var j = 0; j < newsList.length; j++) {
        var map = {}
        var map1 = {}
        map.apiId = newsList[j].id
        map.apiOrder = j
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != 'null'
        ) {
          map.dfpGetFromResponseBOS = JSON.parse(
            newsList[j].parameters.replace(/[\r\n]/g, '')
          )
        } else {
          map.dfpGetFromResponseBOS = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != 'null'
        ) {
          map.dfpParamsBOS = JSON.parse(
            newsList[j].variables.replace(/[\r\n]/g, '')
          )
        } else {
          map.dfpParamsBOS = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != 'null'
        ) {
          map1.headers = JSON.parse(newsList[j].header.replace(/[\r\n]/g, ''))
        } else {
          map1.headers = {}
        }
        map1.host = newsList[j].host
        map1.type = newsList[j].method
        map1.url = newsList[j].url
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != 'null'
        ) {
          map1.requestBody = JSON.parse(
            newsList[j].data.replace(/[\r\n]/g, '').replace(/\s+/g, '')
          )
        } else {
          map1.requestBody = null
        }
        if (
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != {} &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != '{}' &&
          // eslint-disable-next-line eqeqeq
          newsList[j].parameters != 'null'
        ) {
          map1.query = JSON.parse(
            newsList[j].query.replace(/[\r\n]/g, '').replace(/\s+/g, '')
          )
        } else {
          map1.query = null
        }
        map.httpClientRequest = map1
        map.requestDescript = newsList[j].describe
        map.requestOtherName = newsList[j].onePath
        dfpHttpRequestBOS.push(map)
      }
      var requestData = {}
      requestData.dfpHttpRequestBOS = dfpHttpRequestBOS
      requestData.moduleId = moduleId
      requestData.projectName = projectName
      requestData.userId = operator
      // requestData.creatorName = creatorName
      // eslint-disable-next-line eqeqeq
      if (_this.sceneId != '' && _this.sceneId != undefined) {
        requestData.sceneId = _this.sceneId
      }
      requestData.sceneName = sceneName
      if (
        // eslint-disable-next-line eqeqeq
        dfpHttpRequestBOS.length == 0 ||
        // eslint-disable-next-line eqeqeq
        sceneName == '' ||
        // eslint-disable-next-line eqeqeq
        belongModule == ''
      ) {
        _this.$message.error(
          '无法保存，请增加步骤或选择所属模块或填写场景名称！'
        )
        // eslint-disable-next-line eqeqeq
      } else {
        let _this = this
        var url = this.dfp_url + '/dfplatform/editScene'
        this.$axios
          .put(url, requestData, {})
          .then(res => {
            var datas = res.data
            var code = datas.code
            // eslint-disable-next-line eqeqeq
            if (code == 0) {
              _this.$alert('成功', '保存结果', {
                confirmButtonText: '确定',
                callback: action => {
                  MiddleUtil.$emit('demo', 'demoCreate')
                  MiddleUtil.$emit('listReload', 0)
                }
              })
            } else {
              _this.$message.error(datas.message)
            }
            this.clearEditForm()
          })
          .catch(function (error) {
            console.log(error)
          })
      }
    },
    // 清空表单
    clearEditForm () {
      this.form = {
        name: ''
      }
    },
    handleBeforeUpload (file) {
      const fileExt = file.name
        .split('.')
        .pop()
        .toLocaleLowerCase()
      if (fileExt === 'json') {
        this.file = file
        this.readData(file)
        this.$message.success('文件上传成功')
        this.dialogPostmanVisible = false
      } else {
        this.$message.error('请选择后缀为json的文件')
      }
      return false
    },
    readData (file) {
      const reader = new FileReader()
      reader.readAsText(file)
      reader.onerror = e => {
        this.$message.error('读取文件错误')
      }
      reader.onload = e => {
        var obj = JSON.parse(e.target.result)
        var items = obj.item
        var lengthx = items.length
        for (var i = 0; i < lengthx; i++) {
          var map = {}
          map.id = i + 1
          try {
            map.describe = items[i].name
            map.method = items[i].request.method
            var urls = items[i].request.url
            var urltype = typeof urls

            if (urltype === 'object') {
              // 解析URL
              var myURL = new URL(urls.raw)
              map.host = myURL.host
              map.url = myURL.pathname
              map.onePath = myURL.pathname

              // 解析请求里的query
              var querys = urls.query
              var querytype = typeof querys
              var queryjson = {}
              if (querytype === 'object') {
                var queryslen = querys.length
                for (var r = 0; r < queryslen; r++) {
                  var querydata = querys[r]
                  var key = querydata.key
                  var keyvalue = querydata.value
                  queryjson[key] = keyvalue
                }
                // console.log(JSON.stringify(headerjson));
                map.query = JSON.stringify(queryjson)
              } else {
                map.query = '{}'
              }
            } else if (urltype === 'string') {
              // eslint-disable-next-line no-redeclare
              var myURL = new URL(urls)
              map.host = myURL.host
              map.url = myURL.pathname
              map.onePath = myURL.pathname
              map.query = '{}'
            } else {
              map.host = ''
              map.url = ''
              map.onePath = ''
              map.query = '{}'
            }

            // 解析请求里的header
            var headers = items[i].request.header
            var headerslen = headers.length
            var headerjson = {}
            if (headerslen !== 0) {
              for (var t = 0; t < headerslen; t++) {
                var headerdata = headers[t]
                // eslint-disable-next-line no-redeclare
                var key = headerdata.key
                // eslint-disable-next-line no-redeclare
                var keyvalue = headerdata.value
                headerjson[key] = keyvalue
              }
              // console.log(JSON.stringify(headerjson));
              map.header = JSON.stringify(headerjson)
            } else {
              map.header = '{}'
            }

            // 解析请求里的body
            var bodys = items[i].request.body
            var bodytype = typeof bodys
            if (bodytype === 'object') {
              var bodydata = bodys.raw
              if (bodydata === '') {
                map.data = '{}'
              } else {
                map.data = bodys.raw
              }
            } else {
              map.data = '{}'
            }
            this.$message.success('postman脚本解析成功')
          } catch (e) {
            map.host = ''
            map.url = ''
            map.onePath = ''
            map.header = '{}'
            map.query = '{}'
            map.data = '{}'
            this.$message.success('postman脚本解析异常，请请查看使用说明')
          }
          map.variables = '[]'
          map.parameters = '[]'
          this.newsList.push(map)
          this.form.describe = ''
        }
      }
    },
    // 查看报告
    log () {
      if (this.log_url !== '') {
        window.open(this.log_url, '_blank')
      } else {
        this.$message.error('请调试后再查看日志！')
      }
    }
  }
}
</script>
<style scoped>
.abc {
  position: relative;
  height: 100%;
}
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
