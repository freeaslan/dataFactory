<template>
  <div class="mod-menu">
    <el-form :inline="true" :model="dataForm">
      <el-form-item>
        <el-button type="primary" @click="addOrUpdateHandle(null, 'add')"
          >新增</el-button
        >
      </el-form-item>
    </el-form>

    <el-table :data="dataList" row-key="menuId" border style="width: 100%; ">
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        min-width="80"
        label="名称"
      >
      </el-table-column>
      <el-table-column
        prop="parentName"
        header-align="center"
        align="center"
        min-width="80"
        label="上级菜单"
      >
      </el-table-column>
      <el-table-column header-align="center" align="center" label="图标">
        <template slot-scope="scope">
          <icon-svg :name="scope.row.icon || ''"></icon-svg>
        </template>
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="类型"
      >
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 0" size="small">目录</el-tag>
          <el-tag v-else-if="scope.row.type === 1" size="small" type="success"
            >菜单</el-tag
          >
          <el-tag v-else-if="scope.row.type === 2" size="small" type="danger"
            >环境配置</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="addOrUpdateHandle(scope.row.menuId, 'update')"
            >修改</el-button
          >
          <el-button
            type="text"
            size="small"
            @click="deleteHandle(scope.row.menuId)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdate"
      @refreshDataList="getDataList"
    ></add-or-update>
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
import AddOrUpdate from './menuAddUpdate'
export default {
  name: 'sysMenu',
  data () {
    return {
      dataForm: {},
      dataList: [],
      dataListLoading: false,
      addOrUpdateVisible: false,
      dfp_url: window.SITE_CONFIG.baseUrl,
      total: 0,
    }
  },
  components: {
    AddOrUpdate
  },
  activated () {
    this.getDataList()
  },
  methods: {
    // 分页导航
    handleCurrentChange (val) {
      this.cur_page = val
      this.search(val)
    },
    // 获取数据列表
    getDataList (val) {
      if (val === this.cur_page) {
        var pageNum = val - 1
      } else {
        var pageNum = 0
      }
      var url = this.dfp_url + '/dfplatform/getAllMenuList'
      this.dataListLoading = true
      this.$axios
        .post(url, {
          pageIndex: pageNum,
          pageSize: 20
        })
        .then(res => {
          var datas = res.data
          var total = datas.data.totalNum
          this.total = parseInt(total)
          if (datas && datas.code === 0) {
            var menudata = datas.data.menuList
            var list = []
            for (var i = 0; i < menudata.length; i++) {
              var map = {}
              var item = menudata[i]
              map.menuId = item.id
              map.name = item.name
              map.icon = item.icon
              map.parentName = item.parentName
              map.type = item.type
              map.orderNum = item.orderNum
              list.push(map)
              this.dataList = list
            }
          }
          this.dataListLoading = false
        })
    },
    // 新增 / 修改
    addOrUpdateHandle (id, type) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id, type)
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
          var url = this.dfp_url + '/dfplatform/deleteMenu'
          this.$axios
            .get(url, {
              params: {
                id: id,
                userId: window.localStorage.getItem('userId')
              }
            })
            .then(res => {
              var datas = res.data
              if (datas && datas.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success'
                })
                this.getDataList()
              } else {
                this.$message.error(datas.msg)
              }
            })
          })
        .catch(() => {})
    }
  }
}
</script>
