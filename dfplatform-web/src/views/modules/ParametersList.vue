<template>
  <div class="container">
    <div class="handle-box">
      <el-form ref="form" :model="searchform" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="6" style="padding-left: 0px; padding-right: 10px">
            <el-form-item label="参数名称">
              <el-input
                v-model="searchform.paramKey"
                style="width: 80%"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="类路径">
              <el-input
                v-model="searchform.paramClassPath"
                style="width: 80%"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" icon="search" @click="search"
              >搜索</el-button
            >
          </el-col>
          <el-col :span="2">
            <el-button type="primary" icon="search" @click="add"
              >新增</el-button
            >
          </el-col>
          <el-col :span="3.5">
            <el-tooltip
              class="item"
              effect="dark"
              content="配置文件使用规则请查看使用说明"
              placement="top"
            >
              <el-button
                style="float: right; margin-right: 10px"
                type="primary"
                @click="dialogSettingFileVisible = true"
                >上传jar/class文件</el-button
              >
            </el-tooltip>
            <el-dialog
              title="上传jar/class文件"
              :visible.sync="dialogSettingFileVisible"
              size="small"
              width="30%"
            >
              <el-upload
                class="upload-demo"
                :limit="1"
                drag
                action=""
                accept=".jar,.class"
                :before-upload="settingFileUpload"
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                  将文件拖到此处，或<em>点击上传</em>
                </div>
                <div class="el-upload__tip" slot="tip">
                  只能上传.jar和.class文件
                </div>
              </el-upload>
            </el-dialog>
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
        prop="id"
        label="序号"
        min-width="40"
        align="center"
      ></el-table-column>

      <el-table-column
        prop="paramKey"
        label="参数名称"
        width="266"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="jarName"
        label="jar/calss名称"
        min-width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="paramClassPath"
        label="类路径"
        min-width="80"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="paramClassMethod"
        label="方法名称"
        min-width="80"
        align="center"
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
    <!-- 新增动态参数 -->
    <el-dialog
      title="新增动态参数"
      :visible.sync="addVisible"
      width="45%"
      size="tiny"
      :lock-scroll="false"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="addform" label-width="110px">
        <el-form-item label="参数名称" required>
          <el-input
            v-model="addform.paramKey"
            placeholder="参数名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="类型" required>
          <el-input
            v-model="addform.paramClassType"
            placeholder="类型：仅支持jar/class"
          ></el-input>
        </el-form-item>
        <el-form-item label="jar/calss名称" required>
          <el-input
            v-model="addform.jarName"
            placeholder="jar/calss名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="类路径" required>
          <el-input
            v-model="addform.paramClassPath"
            placeholder="类所在包名+类名：test.test.test"
          ></el-input>
        </el-form-item>
        <el-form-item label="方法名称" required>
          <el-input v-model="addform.paramClassMethod" placeholder="方法名称">
          </el-input>
        </el-form-item>
        <el-form-item label="方法参数">
          <el-tooltip
            class="item"
            effect="dark"
            content="解释：按照参数顺序传参，String=参数类型，20000101=参数默认值，&拼接第二个参数"
            placement="top"
          >
            <el-input
              v-model="addform.paramClassParamsData"
              placeholder="例子：String:20000101&String:20000101"
            >
            </el-input>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="最后执行" prop="afterOtherParam">
          <el-tooltip
            class="item"
            effect="dark"
            content="解释：标记该参数是否在其它参数都替换结束后再替换该参数，比如header中的加签值"
            placement="top"
          >
            <el-radio-group v-model="addform.afterOtherParam">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="body加签" prop="isNeedBody">
          <el-tooltip
            class="item"
            effect="dark"
            content="解释：标记是否需要把整个body体作为第一个参数进行方法调用，比如header中的加签"
            placement="top"
          >
            <el-radio-group v-model="addform.isNeedBody">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-tooltip>
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
      title="编辑动态参数"
      :visible.sync="editVisible"
      width="45%"
      :lock-scroll="false"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="editform" label-width="110px">
        <el-form-item label="参数名称" required>
          <el-input
            v-model="editform.paramKey"
            placeholder="参数名称"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="类型" required>
          <el-input
            v-model="editform.paramClassType"
            placeholder="类型：仅支持jar/class"
          ></el-input>
        </el-form-item>
        <el-form-item label="jar/calss名称" required>
          <el-input
            v-model="editform.jarName"
            placeholder="jar/calss名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="类路径" required>
          <el-input
            v-model="editform.paramClassPath"
            placeholder="类所在包名+类名：test.test.test"
          ></el-input>
        </el-form-item>
        <el-form-item label="方法名称" required>
          <el-input v-model="editform.paramClassMethod" placeholder="方法名称">
          </el-input>
        </el-form-item>
        <el-form-item label="方法参数">
          <el-tooltip
            class="item"
            effect="dark"
            content="解释：按照参数顺序传参，String=参数类型，20000101=参数默认值，&拼接第二个参数"
            placement="top"
          >
            <el-input
              v-model="editform.paramClassParamsData"
              placeholder="例子：String:20000101&String:20000101"
            >
            </el-input>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="最后执行" prop="afterOtherParam">
          <el-tooltip
            class="item"
            effect="dark"
            content="解释：标记该参数是否在其它参数都替换结束后再替换该参数，比如header中的加签值"
            placement="top"
          >
            <el-radio-group v-model="editform.afterOtherParam">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="body加签" prop="isNeedBody">
          <el-tooltip
            class="item"
            effect="dark"
            content="解释：标记是否需要把整个body体作为第一个参数进行方法调用，比如header中的加签"
            placement="top"
          >
            <el-radio-group v-model="editform.isNeedBody">
              <el-radio :label="0">是</el-radio>
              <el-radio :label="1">否</el-radio>
            </el-radio-group>
          </el-tooltip>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel_edid">取 消</el-button>
        <el-button
          type="primary"
          @click="sub_edit"
          v-loading.fullscreen.lock="fullscreenLoading"
          >提交</el-button
        >
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
  data () {
    return {
      dfp_url: window.SITE_CONFIG.baseUrl,
      userId: window.localStorage.getItem('userId'),
      labelPosition: 'left',
      tableData: [],
      addVisible: false,
      fullscreenLoading: false,
      dialogSettingFileVisible: false,
      addform: {
        paramKey: '',
        paramClassType: '',
        jarName: '',
        paramClassPath: '',
        paramClassMethod: '',
        paramClassParamsData: '',
        isNeedBody: 1,
        afterOtherParam: 1,
        userId: ''
      },
      searchform: {
        paramKey: '',
        paramClassPath: ''
      },
      editform: {
        paramKey: '',
        paramClassType: '',
        jarName: '',
        paramClassPath: '',
        paramClassMethod: '',
        paramClassParamsData: '',
        isNeedBody: 1,
        afterOtherParam: 1,
        userId: ''
      },
      table: true,
      cur_page: 1,
      multipleSelection: [],
      select_word: '',
      del_list: [],
      editVisible: false
    }
  },
  created () {
    var val = 0
    this.getData(val)
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
    // 点击修改按钮
    update (index, row) {
      this.editVisible = true
      this.editform.id = row.id
      this.editform.afterOtherParam = row.afterOtherParam
      this.editform.isNeedBody = row.isNeedBody
      this.editform.jarName = row.jarName
      this.editform.paramClassMethod = row.paramClassMethod
      this.editform.paramClassPath = row.paramClassPath
      this.editform.paramClassType = row.paramClassType
      this.editform.paramKey = row.paramKey
      this.editform.userId = this.userId
      this.editform.paramClassParamsData = row.paramClassParamsData
    },
    // 点击新增按钮
    add () {
      this.addVisible = true
    },
    // 提交环境配置
    sub_add () {
      var url = this.dfp_url + '/dfplatform/addPublicParam'
      this.$axios
        .post(url, {
          afterOtherParam: this.addform.afterOtherParam,
          isNeedBody: this.addform.isNeedBody,
          jarName: this.addform.jarName,
          paramClassMethod: this.addform.paramClassMethod,
          paramClassPath: this.addform.paramClassPath,
          paramClassType: this.addform.paramClassType,
          paramKey: this.addform.paramKey,
          userId: this.userId,
          paramClassParamsData: this.addform.paramClassParamsData
        })
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
          this.addform = { isNeedBody: 1, afterOtherParam: 1 }
          var val = 0
          this.search(val)
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    // 提交编辑故障清单信息
    sub_edit () {
      var url = this.dfp_url + '/dfplatform/editPublicParam'
      this.$axios
        .post(url, {
          afterOtherParam: this.editform.afterOtherParam,
          isNeedBody: this.editform.isNeedBody,
          jarName: this.editform.jarName,
          paramClassMethod: this.editform.paramClassMethod,
          paramClassPath: this.editform.paramClassPath,
          paramClassType: this.editform.paramClassType,
          paramKey: this.editform.paramKey,
          userId: this.userId,
          paramClassParamsData: this.editform.paramClassParamsData,
          id: this.editform.id
        })
        .then(res => {
          this.fullscreenLoading = false
          var datas = res.data
          if (datas.code === 0) {
            this.$message({
              message: '编辑成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: datas.message,
              type: 'fail'
            })
          }
          this.cancel_edid()
          var val = 0
          this.search(val)
        })
        .catch(err => {
          this.$message.error(err.response.data.message)
        })
    },
    // 取消提交表单
    cancel_edid () {
      this.editVisible = false
      this.editform = {
        paramKey: '',
        paramClassType: '',
        jarName: '',
        paramClassPath: '',
        paramClassMethod: '',
        paramClassParamsData: '',
        isNeedBody: 1,
        afterOtherParam: 1,
        userId: ''
      }
    },
    // 分页导航
    handleCurrentChange (val) {
      this.cur_page = val
      this.search(val)
    },
    // 搜索
    search (val) {
      var pageNum = 0
      if (val === this.cur_page) {
        pageNum = val - 1
      }
      var url = this.dfp_url + '/dfplatform/queryPublicParamByPage'
      this.$axios
        .post(url, {
          paramKey: this.searchform.paramKey,
          paramClassPath: this.searchform.paramClassPath,
          pageIndex: pageNum,
          pageSize: 20
        })
        .then(res => {
          var datas = res.data
          var total = datas.data.totalNum
          this.total = parseInt(total)
          var dfpPublicParamList = datas.data.dfpPublicParamVOS
          var list = []
          for (var i = 0; i < dfpPublicParamList.length; i++) {
            var map = {}
            var item = dfpPublicParamList[i]
            var model = item.dfpPublicParamModel
            map.id = model.id
            map.paramKey = model.paramKey
            map.jarName = model.jarName
            map.paramClassPath = model.paramClassPath
            map.paramClassMethod = model.paramClassMethod
            map.paramClassType = model.paramClassType
            map.paramClassParamsData = model.paramClassParamsData
            map.isNeedBody = model.isNeedBody
            map.afterOtherParam = model.afterOtherParam
            list.push(map)
          }
          this.tableData = list
          this.table = true
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    // 删除
    deleteHandle (id) {
      this.$confirm(`确定对[id=${id}]进行[删除]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          var url = this.dfp_url + '/dfplatform/deleteDynamicParam'
          this.$axios
            .get(url, {
              params: {
                id: id,
                userId: this.userId
              }
            })
            .then(res => {
              var datas = res.data
              if (datas && datas.code === 0) {
                this.$message({
                  message: '动态参数删除成功',
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
    }
    getData (val) {
      var pageNum = 0
      if (val === this.cur_page) {
        pageNum = val - 1
      }
      var url = this.dfp_url + '/dfplatform/queryPublicParamByPage'
      this.$axios
        .post(url, {
          pageIndex: pageNum,
          pageSize: 20
        })
        .then(res => {
          var datas = res.data
          var total = datas.data.totalNum
          this.total = parseInt(total)
          var dfpPublicParamList = datas.data.dfpPublicParamVOS
          var list = []
          for (var i = 0; i < dfpPublicParamList.length; i++) {
            var map = {}
            var item = dfpPublicParamList[i]
            var model = item.dfpPublicParamModel
            map.id = model.id
            map.paramKey = model.paramKey
            map.jarName = model.jarName
            map.paramClassPath = model.paramClassPath
            map.paramClassMethod = model.paramClassMethod
            map.paramClassType = model.paramClassType
            map.paramClassParamsData = model.paramClassParamsData
            map.isNeedBody = model.isNeedBody
            map.afterOtherParam = model.afterOtherParam
            list.push(map)
          }
          this.tableData = list
          this.table = true
        })
        .catch(function (error) {
          console.log(error)
        })
    },
    settingFileUpload (file) {
      const fileExt = file.name
        .split('.')
        .pop()
        .toLocaleLowerCase()
      var url = this.dfp_url + '/dfplatform/filesUpload'
      if (fileExt === 'jar' || fileExt === 'class') {
        var formData = new FormData()
        formData.append('file', file)
        formData.append('userId', window.localStorage.getItem('userId'))
        var options = {
          // 设置axios的参数
          url: url,
          data: formData,
          method: 'post',
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
        this.$axios(options)
          .then(res => {
            this.dialogSettingFileVisible = false
            var datas = res.data
            if (datas.code === 0) {
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
      } else {
        this.$message.error('请选择后缀为jar/class的文件')
      }
      return false
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
