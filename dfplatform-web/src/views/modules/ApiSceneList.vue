<template>
  <div class="container">
    <div class="handle-box">
      <el-form ref="form" :model="form" label-width="70px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="场景名称">
              <el-input v-model="form.scenario" style="width: 80%"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="所属模块" label-width="70px">
              <el-select
                v-model="form.modle"
                placeholder="选择模块"
                style="width: 80%"
                class="handle-select mr10"
                @focus="function1"
                @change="function2($event)"
              >
                <el-option
                  v-for="item in returnData"
                  :key="item.id"
                  :value="item.id"
                  :label="item.moduleName"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" icon="search" @click="search">
              搜索
            </el-button>
          </el-col>
          <el-button
            style="float: right; margin-right: 10px"
            type="primary"
            @click="
              () => {
                dialogSwaggerVisible = true
                Swaggerform.servicename = ''
                Swaggerform.ip = ''
              }
            "
          >
            导入swagger
          </el-button>
          <el-dialog
            title="导入swagger"
            :visible.sync="dialogSwaggerVisible"
            size="tiny"
            width="30%"
          >
            <el-form :model="Swaggerform" label-width="100px">
              <el-form-item label="所属项目" v-model="Swaggerform.project">
                <el-input
                  size="mini"
                  v-model="Swaggerform.project"
                  :disabled="true"
                >
                </el-input>
              </el-form-item>
              <el-form-item label="服务名称">
                <el-input
                  size="mini"
                  v-model="Swaggerform.servicename"
                  placeholder="请输入服务名称"
                >
                </el-input>
              </el-form-item>
              <el-form-item label="Swagger地址">
                <el-input
                  size="mini"
                  v-model="Swaggerform.ip"
                  placeholder="请输入Swagger地址"
                >
                </el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogSwaggerVisible = false">取 消</el-button>
              <el-button type="primary" @click="swaggerimport">导入</el-button>
            </div>
          </el-dialog>
          <el-button
            style="float: right; margin-right: 10px"
            label-width="100px"
            type="primary"
            @click="createScence"
            >创建场景</el-button
          >
          <el-dialog
            title="请求场景"
            :visible.sync="dialogRequestVisible"
            size="tiny"
            width="40%"
          >
            <el-form :model="requestform" label-width="100px">
              <el-form-item label="场景名称">
                <el-input size="mini" v-model="requestform.sName" disabled>
                </el-input>
              </el-form-item>
              <el-form-item label="所属模块">
                <el-input size="mini" v-model="requestform.sModel" disabled>
                </el-input>
              </el-form-item>
              <el-form-item label="环境">
                <el-select
                  v-model="selectsurroundings"
                  placeholder="请选择所属环境"
                  @focus="focusEnvName"
                  filterable
                  style="width: 70%"
                >
                  <el-option
                    v-for="item in returnEnv"
                    :key="item.id"
                    :value="item.id"
                    :label="item.envName"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                v-for="(item, index) in paramList"
                :label="item.label"
                :key="index"
              >
                <el-radio
                  v-model="item.val"
                  v-if="item.type == 'boolean'"
                  label="true"
                  >是</el-radio
                >
                <el-radio
                  v-model="item.val"
                  v-if="item.type == 'boolean'"
                  label="false"
                  >否</el-radio
                >
                <el-input
                  v-if="item.type == 'string' && item.enum == undefined"
                  v-model="item.val"
                ></el-input>
                <el-select
                  v-if="item.type == 'string' && item.enum !== undefined"
                  v-model="item.val"
                  filterable
                  placeholder="请选择"
                  clearable
                  style="width: 60%"
                >
                  <el-option
                    v-for="item in item.enum"
                    :key="item.value"
                    :label="item.value"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-select
                  v-if="item.type == 'array' && item.enum !== undefined"
                  v-model="item.val"
                  multiple
                  filterable
                  placeholder="请选择"
                  clearable
                  style="width: 60%"
                >
                  <el-option
                    v-for="item in item.enum"
                    :key="item.value"
                    :label="item.value"
                    :value="item.value"
                  ></el-option>
                </el-select>
                <el-input
                  v-if="item.type == 'object'"
                  v-model="item.val"
                  type="textarea"
                  resize="none"
                  :rows="4"
                ></el-input>
                <el-input-number
                  v-model="item.val"
                  v-if="item.type == 'integer'"
                  @change="handleChange"
                  label
                ></el-input-number>
                <el-date-picker
                  v-model="timeer"
                  type="datetime"
                  placeholder="选择日期时间"
                  v-if="item.type == 'number'"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="返回结果">
                <div class="grid-content bg-purple-light abc">
                  <el-input
                    type="textarea"
                    resize="none"
                    :rows="10"
                    v-model="reponseOutput"
                    readonly
                    style="font-size: 16px"
                  ></el-input>
                </div>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="dialogRequestVisible = false">
                取 消
              </el-button>
              <el-button type="primary" @click="oneRequestScene">
                提 交
              </el-button>
              <el-button type="primary" @click="log">查看报告</el-button>
            </div>
          </el-dialog>
          <el-button
            style="float: right; margin-right: 10px"
            type="primary"
            @click=";(dialogModelVisible = true), (modelform.modelname = '')"
          >
            创建模块
          </el-button>
          <!-- <el-button
            style="float: right; margin-right: 10px"
            type="primary"
            @click="gotoLink"
            >使用说明</el-button
          > -->
          <el-dialog
            title="创建模块"
            :visible.sync="dialogModelVisible"
            size="tiny"
            width="30%"
          >
            <el-form :model="modelform" label-width="100px">
              <el-form-item label="所属项目" v-model="modelform.project">
                <el-input size="mini" v-model="modelform.project" disabled>
                </el-input>
              </el-form-item>
              <el-form-item label="模块名称">
                <el-input
                  size="mini"
                  v-model="modelform.modelname"
                  placeholder="请输入模块名称"
                >
                </el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogModelVisible = false">取 消</el-button>
              <el-button type="primary" @click="createmodel">确 定</el-button>
            </div>
          </el-dialog>
        </el-row>
      </el-form>
    </div>
    <el-table
      :data="tableData"
      v-show="table"
      border
      class="table"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        prop="id"
        label="id"
        min-width="50"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="scenario"
        label="场景名称"
        min-width="220"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="modle"
        label="所属模块"
        min-width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="creatorName"
        label="创建人"
        min-width="60"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="modifierName"
        label="修改人"
        min-width="60"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="apiaction"
        label="操作"
        min-width="100"
        align="center"
      >
        <template slot-scope="scope">
          <span
            style="margin-right: 10px"
            type="primary"
            class="el-link"
            @click="editScene(scope.$index, scope.row)"
          >
            编辑
          </span>
          <span
            style="margin-right: 10px"
            type="primary"
            class="el-link"
            @click="copyScene(scope.$index, scope.row)"
          >
            复制
          </span>
          <span
            style="margin-right: 10px"
            type="primary"
            class="el-link"
            @click="requestScene(scope.$index, scope.row)"
          >
            请求场景
          </span>
          <span
            style="margin-right: 10px"
            type="primary"
            class="el-link"
            @click="handleDelete(scope.$index, scope.row)"
          >
            删除
          </span>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        ref="Pagination"
        layout="prev, pager, next"
        :total="total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import MiddleUtil from './MiddleUtil'

export default {
  props: {},
  data: function () {
    return {
      modelform: {
        project: '',
        modelname: ''
      },
      Swaggerform: {
        project: '',
        servicename: '',
        ip: ''
      },
      returnEnv: [],
      ApiCreateUrl: { path: '/demoCreate' },
      log_url: '',
      moduleId: '',
      reponseOutput: '',
      paramList: [],
      dfp_url: window.SITE_CONFIG.baseUrl,
      url: '',
      returnData: [],
      tableData: [],
      table: true,
      cur_page: 1,
      multipleSelection: [],
      select_model: '',
      select_word: '',
      del_list: [],
      is_search: false,
      dialogModelVisible: false,
      dialogSwaggerVisible: false,
      dialogScenarioVisible: false,
      dialogRequestVisible: false,
      total: 0,
      reSceneId: '',
      requestform: {
        sName: '',
        sModel: ''
      },
      form: {
        scenario: '',
        modle: ''
      },
      idx: -1,
      selectsurroundings: ''
    }
  },
  created () {
    this.modelform.project = sessionStorage.getItem('projectName')
    this.Swaggerform.project = sessionStorage.getItem('projectName')
    var val = 0
    this.getData(val)
  },
  mounted () {
    // 查询，提供给其他页面调用
    var that = this
    MiddleUtil.$on('listReload', function (val) {
      that.getData(val)
    })
  },
  methods: {
    focusEnvName () {
      // 根据项目名获取服务名
      var url = this.dfp_url + '/dfplatform/getAllEnvEnum'
      this.$axios
        .get(url)
        .then(res => {
          var datas = res.data
          this.returnEnv = datas.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    log () {
      if (this.log_url !== '') {
        window.open(this.log_url, '_blank')
      } else {
        this.$message.error('请提交后再查看日志！')
      }
    },
    createScence () {
      this.$router.push(this.ApiCreateUrl)
    },
    requestScene: function (index, row) {
      this.dialogRequestVisible = true
      this.requestform.sName = row.scenario
      this.requestform.sModel = row.modle
      this.reSceneId = row.id
      this.reponseOutput = ''
      var url = this.dfp_url + '/dfplatform/getSceneParams'
      this.$axios
        .get(url, {
          params: {
            sceneId: this.reSceneId
          }
        })
        .then(res => {
          var datas = res.data
          var code = datas.code
          var data = JSON.parse(datas.data)
          if (code === 0) {
            var definitions = data.definitions[row.scenario]
            var properties = definitions.properties
            var required = definitions.required
            var list1 = []
            for (var key in properties) {
              var list2 = {}
              list2['name'] = key
              list2['label'] = properties[key].description
              list2['type'] = properties[key].paramType
              list2['val'] = ''
              var a = required.indexOf(key)
              if (a > -1) {
                list2['required'] = true
              } else {
                list2['required'] = false
              }
              list1.push(list2)
            }
            this.paramList = list1
          } else {
            this.$message({
              message: datas.message,
              type: 'fail'
            })
          }
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    // 提交请求场景事件
    oneRequestScene () {
      let _this = this
      var url = this.dfp_url + '/dfplatform/requestScene'
      var timestamp = Date.parse(new Date())
      var paramList = this.paramList
      var sendbody = {}
      var list1 = []
      for (var i = 0; i < paramList.length; i++) {
        var list2 = {}
        var item = paramList[i]
        list2['paramKey'] = item.name
        list2['paramValue'] = item.val
        list1.push(list2)
      }
      sendbody['dfpRequestParamsBOs'] = list1
      sendbody['sceneId'] = this.reSceneId
      sendbody['envId'] = this.selectsurroundings
      sendbody['projectName'] = this.modelform.project
      sendbody['userId'] = window.localStorage.getItem('userId')
      this.$axios
        .post(url, sendbody, {
          headers: {
            traceid: timestamp
          }
        })
        .then(res => {
          var datas = res.data
          this.reponseOutput = JSON.stringify(datas, null, 2)
          _this.log_url = datas.data.reportUrl
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    // 编辑场景
    editScene (index, row) {
      window.close()
      this.$router.push({ name: 'demoCreate', query: { sceneId: row.id } })
    },
    // 复制场景
    copyScene (index, row) {
      var url = this.dfp_url + '/dfplatform/copyScene'
      var operator = window.localStorage.getItem('userId')
      this.$axios
        .post(url, {
          sceneId: row.id,
          userId: operator
        })
        .then(res => {
          var datas = res.data
          if (datas.code === 0) {
            this.$message({
              type: 'success',
              message: datas.message
            })
          } else {
            this.$message({
              type: 'fail',
              message: datas.message
            })
          }
          this.search(this.cur_page)
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    // 分页导航
    handleCurrentChange (val) {
      this.cur_page = val
      this.search(val)
    },
    getData (val) {
      if (val === this.cur_page) {
        var pageNum = val - 1
      } else {
        var pageNum = 0
      }
      var url = this.dfp_url + '/dfplatform/getSceneList'
      this.$axios
        .post(url, {
          sceneName: '',
          moduleId: '',
          pageIndex: pageNum,
          pageSize: 20,
          projectName: this.Swaggerform.project
        })
        .then(res => {
          var datas = res.data
          var total = datas.data.totalNum
          this.total = parseInt(total)
          var unitTests = datas.data.dfpSceneVOS
          var list = []
          for (var i = 0; i < unitTests.length; i++) {
            var map = {}
            var item = unitTests[i]
            var dfpModuleModel = item.dfpModuleModel
            map.id = item.id
            map.scenario = item.sceneName
            map.modle = dfpModuleModel.moduleName
            map.creatorName = item.creatorName
            map.modifierName = item.modifierName
            list.push(map)
            this.tableData = list
            this.table = true
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    search (val) {
      let _this = this
      if (val === this.cur_page) {
        var pageNum = val - 1
      } else {
        var pageNum = 0
        this.$refs.Pagination.internalCurrentPage = 1
      }
      var url = this.dfp_url + '/dfplatform/getSceneList'
      if (_this.moduleId === -1) {
        var serachModuleId = ''
      } else {
        var serachModuleId = _this.moduleId
      }
      this.$axios
        .post(url, {
          sceneName: _this.form.scenario,
          moduleId: serachModuleId,
          pageIndex: pageNum,
          pageSize: 20,
          projectName: this.Swaggerform.project
        })
        .then(res => {
          var datas = res.data
          var total = datas.data.totalNum
          this.total = parseInt(total)
          var unitTests = datas.data.dfpSceneVOS
          var list = []
          for (var i = 0; i < unitTests.length; i++) {
            var map = {}
            var item = unitTests[i]
            var dfpModuleModel = item.dfpModuleModel
            map.id = item.id
            map.scenario = item.sceneName
            map.modle = dfpModuleModel.moduleName
            map.creatorName = item.creatorName
            map.modifierName = item.modifierName
            list.push(map)
          }
          this.tableData = list
          this.table = true
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    createmodel () {
      var url = this.dfp_url + '/dfplatform/addModule'
      this.$axios
        .post(url, {
          moduleName: this.modelform.modelname,
          projectName: this.modelform.project,
          userId: window.localStorage.getItem('userId')
        })
        .then(res => {
          var datas = res.data
          if (datas.code === 0) {
            this.dialogModelVisible = false
            this.$message({
              message: '创建成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: datas.message,
              type: 'fail'
            })
          }
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    swaggerimport () {
      var url = this.dfp_url + '/dfplatform/importSwaggers/'
      this.$axios
        .post(url, {
          projectName: this.Swaggerform.project,
          serviceName: this.Swaggerform.servicename,
          swaggerLink: this.Swaggerform.ip,
          userId: window.localStorage.getItem('userId')
        })
        .then(res => {
          var datas = res.data
          if (datas.code === 0) {
            this.dialogSwaggerVisible = false
            this.$message({
              message: datas.message,
              type: 'success'
            })
          } else {
            this.$message({
              message: datas.message,
              type: 'fail'
            })
          }
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    function1 () {
      // 发请求获取模块下拉框的值
      var url = this.dfp_url + '/dfplatform/queryModule'
      this.$axios
        .get(url, { params: { projectName: this.Swaggerform.project } })
        .then(res => {
          var dict = {}
          dict.id = -1
          dict.moduleName = '全部'
          var modulesList = res.data.data
          modulesList.push(dict)
          this.returnData = modulesList.reverse()
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    function2 (val) {
      this.moduleId = val
    },
    formatter (row, column) {
      return row.address
    },
    filterTag (value, row) {
      return row.tag === value
    },
    handleDelete (index, row) {
      let _this = this
      // eslint-disable-next-line no-unused-vars
      let formData = Object.assign({}, row)
      this.$confirm('确认删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        cancelButtonClass: 'cancel'
      })
        .then(() => {
          var url = this.dfp_url + '/dfplatform/deleteScene'
          this.$axios.delete(url, { params: { sceneId: row.id, userId: window.localStorage.getItem('userId') } }).then(res => {
            var datas = res.data
            var code = datas.code
            if (code === 0) {
              _this.$message.success('删除成功')
              this.search(this.cur_page)
            } else {
              _this.$message.error(datas.message)
              this.search(this.cur_page)
            }
          })
        })
        .catch(() => {
          _this.$message.success('取消删除')
        })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    }
  }
}
</script>

<style scoped>
.abc {
  position: relative;
  height: 100%;
}
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}
.el-link {
  color: #b10808;
}
</style>
