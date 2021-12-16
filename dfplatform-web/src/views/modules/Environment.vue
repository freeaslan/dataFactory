<template>
  <div class="container">
    <div class="handle-box">
      <el-form ref="form" :model="form" label-width="40px">
        <el-row :gutter="26">
          <el-col :span="6">
            <el-form-item label="项目">
              <el-select
                v-model="searchform.projectName"
                placeholder="选择项目"
                style="width: 80%"
                @change="function2($event)"
              >
                <el-option
                  v-for="item in returnProject"
                  :key="item.id"
                  :value="item.name"
                  :label="item.name"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="服务">
              <el-select
                v-model="searchform.serviceName"
                placeholder="选择服务"
                style="width: 80%"
                @focus="focusServiceName1"
              >
                <el-option
                  v-for="item in returnService"
                  :key="item.id"
                  :value="item.serviceName"
                  :label="item.serviceName"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" icon="search" @click="search">
              搜索
            </el-button>
          </el-col>
          <!-- 新增故障按钮 -->
          <el-col :span="2">
            <el-button type="primary" icon="search" @click="add">
              新增
            </el-button>
          </el-col>
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
        prop="listId"
        label="序号"
        min-width="50"
        align="center"
      ></el-table-column>

      <el-table-column
        prop="listProjectName"
        label="项目"
        width="266"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="listServiceName"
        label="服务"
        min-width="120"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="listEnvName"
        label="环境"
        min-width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="listHost"
        label="请求域名"
        min-width="160"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="listHeader"
        label="请求头部"
        min-width="300"
        align="center"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column prop label="操作" min-width="80" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="small"
            @click="update(scope.$index, scope.row)"
          >修改</el-button>
          <el-button
            type="text"
            size="small"
            @click="deleteHandle(scope.row.id)"
            >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增环境配置 -->
    <el-dialog
      title="新增环境配置"
      :visible.sync="addVisible"
      width="35%"
      size="tiny"
      :lock-scroll="false"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="addform" label-width="80px">
        <el-form-item label="项目">
          <el-select
            v-model="addform.projectName"
            placeholder="选择项目"
            @change="function2($event)"
          >
            <el-option
              v-for="item in returnProject"
              :key="item.id"
              :value="item.name"
              :label="item.name"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="服务">
          <el-select
            v-model="addform.serviceName"
            placeholder="选择服务"
            @focus="focusServiceName"
          >
            <el-option
              v-for="item in returnService"
              :key="item.id"
              :value="item.serviceName"
              :label="item.serviceName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="环境">
          <el-select
            v-model="addform.envId"
            placeholder="请选择所属环境"
            @focus="focusEnvName"
          >
            <el-option
              v-for="item in returnEnv"
              :key="item.id"
              :value="item.id"
              :label="item.envName"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请求域名">
          <el-input width="mini" v-model="addform.host"> </el-input>
        </el-form-item>
        <el-form-item label="请求头部">
          <el-input
            type="textarea"
            resize="none"
            :rows="5"
            v-model="addform.header"
            style="font-size: 16px"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="sub_add"
          v-loading.fullscreen.lock="fullscreenLoading"
          >提交</el-button
        >
      </span>
    </el-dialog>
    <!-- 编辑弹出框 -->
    <el-dialog
      title="编辑"
      :visible.sync="editrVisible"
      width="40%"
      :lock-scroll="false"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="editform" label-width="80px">
        <el-form-item label="项目">
          <el-input width="mini" v-model="editform.projectName" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="服务">
          <el-input width="mini" v-model="editform.serviceName" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="环境">
          <el-input width="mini" v-model="editform.envName" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="请求域名">
          <el-input width="mini" v-model="editform.host"> </el-input>
        </el-form-item>
        <el-form-item label="请求头部">
          <el-input
            type="textarea"
            resize="none"
            :rows="5"
            v-model="editform.header"
            style="font-size: 16px"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editrVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="sub_edit"
          v-loading.fullscreen.lock="fullscreenLoading"
        >
          提交
        </el-button>
      </span>
    </el-dialog>
    <div class="pagination">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        ref="Pagination"
        layout="prev, pager, next"
        :total="total"
        :page-size="20"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: 'basetable',
  data () {
    return {
      returnProject: [],
      returnService: [],
      returnEnv: [],
      dfp_url: window.SITE_CONFIG.baseUrl,
      timeer: '',
      timeer2: '',
      operator: window.localStorage.getItem('user'),
      tableData: [],
      yearData: [],
      addVisible: false,
      editrVisible: false,
      fullscreenLoading: false,
      form: {
        quarter: '',
        year: ''
      },
      addform: {
        projectName: '',
        serviceName: '',
        envId: '',
        host: '',
        header: '{}',
        userId: 0
      },
      searchform: {
        projectName: '',
        serviceName: ''
      },
      editform: {
        projectName: '',
        serviceName: '',
        envId: '',
        envName: '',
        host: '',
        header: '{}',
        userId: 0
      },
      ServiceData: [],
      table: true,
      cur_page: 1,
      total: 0,
      multipleSelection: [],
      select_word: '',
      del_list: [],
      is_search: false,
      editVisible: false,
      delVisible: false,
      count: '',
      provinceVal: '',
      provinceVal2: ''
    }
  },
  created () {
    var parentName = sessionStorage.getItem('projectName')
    var menuurl = this.dfp_url + '/dfplatform/getMenuTree'
    this.$axios.get(menuurl).then(res => {
      var datas = res.data.data
      var tempMenuLists = datas.menuList
      var list = []
      for (var i = 0; i < tempMenuLists.length; i++) {
        if (parentName === tempMenuLists[i].name) {
          var menu = tempMenuLists[i].list
          for (var j = 0; j < menu.length; j++) {
            var map = {}
            var model = menu[j]
            if (model.type === 1) {
              map.id = model.id
              map.name = model.name
              list.push(map)
            }
          }
        }
      }
      this.returnProject = list
    })
    var val = 0
    this.search(val)
  },
  computed: {
    data () {
      return this.tableData.filter(d => {
        let isDel = false
        for (let i = 0; i < this.del_list.length; i++) {
          if (d.name === this.del_list[i].name) {
            isDel = true
            break
          }
        }
        if (!isDel) {
          if (
            d.address.indexOf(this.select_cate) > -1 &&
            (d.name.indexOf(this.select_word) > -1 ||
              d.address.indexOf(this.select_word) > -1)
          ) {
            return d
          }
        }
      })
    }
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
    focusServiceName () {
      // 根据项目名获取服务名
      var url = this.dfp_url + '/dfplatform/getServices'
      this.$axios
        .get(url, { params: { projectName: this.addform.projectName } })
        .then(res => {
          var datas = res.data
          this.returnService = datas.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    focusServiceName1 () {
      // 根据项目名获取服务名
      var url = this.dfp_url + '/dfplatform/getServices'
      var projectName = this.searchform.projectName

      this.$axios
        .get(url, { params: { projectName: projectName } })
        .then(res => {
          var datas = res.data
          this.returnService = datas.data
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    function2 (val) {
      this.returnService = []
      this.addform.serviceName = ''
    },
    function3 (val) {
      this.returnService = []
      this.searchform.serviceName = ''
    },
    // 新窗口打开Sonar
    findInfo (index, row) {
      var url = row.breakdown_link
      window.open(url, '_blank')
    },
    // 转换时间格式
    formateDate (datetime) {
      function addDateZero (num) {
        return num < 10 ? '0' + num : num
      }
      let d = new Date(datetime)
      let formatdatetime =
        d.getFullYear() +
        '-' +
        addDateZero(d.getMonth() + 1) +
        '-' +
        addDateZero(d.getDate())
      return formatdatetime
    },
    // 点击修改按钮
    update (index, row) {
      this.editrVisible = true
      this.editform.id = row.listId
      this.editform.projectName = row.listProjectName
      this.editform.serviceName = row.listServiceName
      this.editform.envId = row.listEnvId
      this.editform.envName = row.listEnvName
      this.editform.host = row.listHost
      this.editform.header = JSON.stringify(JSON.parse(row.listHeader), null, 2)
    },
    // 点击新增按钮
    add () {
      this.addVisible = true
    },
    // 删除
    deleteHandle (id) {
      this.$confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          var url = this.dfp_url + '/dfplatform/deleteEnvParams'
          this.$axios
            .get(url, {
              params: {
                id: id,
                userId: this.operator
              }
            })
            .then(res => {
              var datas = res.data
              if (datas && datas.code === 0) {
                this.$message({
                  message: '环境配置删除成功',
                  type: 'success'
                })
                var val = 0
                this.search(val)
              } else {
                this.$message.error(datas.msg)
              }
            })
        })
        .catch(() => {})
    },
    // 提交环境配置
    sub_add () {
      let _this = this
      var projectName = this.addform.projectName
      var serviceName = this.addform.serviceName
      var envId = this.addform.envId
      var host = this.addform.host
      var header = this.addform.header
      var url = this.dfp_url + '/dfplatform/addCommonParams'
      if (serviceName === '' || host === '' || header === '') {
        _this.$message.error('无法提交，服务名和请求域名和请求头部不能为空！')
      } else {
        this.fullscreenLoading = true
        this.$axios
          .post(
            url,
          {
            projectName: projectName,
            serviceName: serviceName,
            envId: parseInt(envId),
            host: host,
            header: JSON.parse(header),
            userId: window.localStorage.getItem('userId')
          }
          )
          .then(res => {
            this.fullscreenLoading = false
            var datas = res.data
            if (datas.code === 0) {
              this.$message({
                message: '新增成功',
                type: 'success'
              })
            } else {
              this.$message({
                message: datas.message,
                type: 'fail'
              })
            }
            this.addVisible = false
            var val = 0
            this.search(val)
          })
          .catch(err => {
            console.log(err)
          })
      }
    },
    // 提交编辑故障清单信息
    sub_edit () {
      var id = this.editform.id
      var host = this.editform.host
      var projectName = this.editform.projectName
      var serviceName = this.editform.serviceName
      var envId = this.editform.envId
      var header = JSON.parse(
        this.editform.header.replace(/[\r\n]/g, '').replace(/\s+/g, '')
      )
      var url = this.dfp_url + '/dfplatform/editCommonParams'
      this.fullscreenLoading = true
      this.$axios
        .post(
          url,
        {
          id: id,
          host: host,
          header: header,
          projectName: projectName,
          serviceName: serviceName,
          envId: envId,
          userId: window.localStorage.getItem('userId')
        },
        {
          headers: {}
        }
        )
        .then(res => {
          this.fullscreenLoading = false
          var datas = res.data
          if (datas.code === 0) {
            this.$message({
              message: '新增成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: datas.message,
              type: 'fail'
            })
          }
          this.editrVisible = false
          var val = 0
          this.search(val)
        })
        .catch(err => {
          console.log(err)
        })
      // }
    },
    // 分页导航
    handleCurrentChange (val) {
      this.cur_page = val
      this.search(val)
    },
    // 搜索
    search (val) {
      let _this = this
      var pageNum = 0
      if (val === this.cur_page) {
        pageNum = val - 1
      }
      var parentName = sessionStorage.getItem('projectName')
      var projectName = this.searchform.projectName
      var serviceName = this.searchform.serviceName
      var url = this.dfp_url + '/dfplatform/getCommonParamsList'
      this.$axios
        .post(
          url,
        {
          parentName: parentName,
          projectName: projectName,
          serviceName: serviceName,
          pageIndex: pageNum,
          pageSize: 20
        },
        {
          headers: {}
        }
        )
        .then(res => {
          var data = res.data.data
          var count = data.totalNum
          this.total = parseInt(count)
          var list = []
          var model = data.dfpEnvParamsModelList
          for (var i = 0; i < model.length; i++) {
            var map = {}
            var item = model[i]
            map.listId = item.id
            map.listProjectName = item.projectName
            map.listServiceName = item.serviceName
            map.listEnvName = item.envName
            map.listEnvId = item.envId
            map.listHost = item.host
            map.listHeader = item.header
            list.push(map)
          }
          this.tableData = list
          var datas = res.data
          if (datas.code === 0) {
          } else {
            _this.$message.error(datas.message)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    formatter (row, column) {
      return row.address
    },
    filterTag (value, row) {
      return row.tag === value
    },
    handleEdit (index, row) {
      const item = this.tableData[index]
      this.$router.push({
        path: '/AddCases',
        query: {
          case_id: item.case_id
        }
      })
      this.idx = index
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    }
  }
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}
</style>
<style>
.abab .el-collapse-item__wrap {
  border-bottom: 0px;
}
.abab .el-collapse-item__header {
  border-bottom: 0px;
}
</style>
