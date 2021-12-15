<template>
  <el-dialog
    :title="dataForm.id === 0 ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="80px"
    >
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="dataForm.type">
          <el-radio :label="0">目录</el-radio>
          <el-radio :label="1">菜单</el-radio>
          <el-radio :label="2">环境配置</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-show="dataForm.type !== 2" label="名称" prop="name">
        <el-input
          v-model="dataForm.name"
          :placeholder="dataForm.type === 0 ? '目录名称' : '菜单名称'"
        ></el-input>
      </el-form-item>
      <el-form-item
        v-show="dataForm.type !== 0"
        label="上级菜单"
        prop="parentName"
      >
        <el-popover
          ref="menuListPopover"
          placement="bottom-start"
          trigger="click"
        >
          <el-tree
            :data="menuList"
            :props="menuListTreeProps"
            :key="dataForm.type"
            node-key="id"
            ref="menuListTree"
            @current-change="menuListTreeCurrentChangeHandle"
            :default-expand-all="true"
            :highlight-current="true"
            :expand-on-click-node="false"
          >
          </el-tree>
        </el-popover>
        <el-input
          v-model="dataForm.parentName"
          v-popover:menuListPopover
          :readonly="true"
          placeholder="点击选择上级菜单"
          class="menu-list__input"
        ></el-input>
      </el-form-item>
      <el-form-item label="菜单图标" prop="icon">
        <el-row>
          <el-col>
            <el-popover
              ref="iconListPopover"
              placement="bottom-start"
              trigger="click"
              popper-class="mod-menu__icon-popover"
            >
              <div class="mod-menu__icon-inner">
                <div class="mod-menu__icon-list">
                  <el-button
                    v-for="(item, index) in iconList"
                    :key="index"
                    @click="iconActiveHandle(item)"
                    :class="{ 'is-active': item === dataForm.icon }"
                  >
                    <icon-svg :name="item"></icon-svg>
                  </el-button>
                </div>
              </div>
            </el-popover>
            <el-input
              v-model="dataForm.icon"
              v-popover:iconListPopover
              :readonly="true"
              placeholder="菜单图标名称"
              class="icon-list__input"
            ></el-input>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import Icon from '@/icons'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        id: 0,
        type: 0,
        name: '',
        parentId: 0,
        parentName: '',
        url: '',
        perms: '',
        orderNum: 0,
        icon: '',
        iconList: []
      },
      dataRule: {
        name: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' }
        ],
        parentName: [
          { required: true, message: '上级菜单不能为空', trigger: 'change' }
        ],
        icon: [{ required: true, message: '图标不能为空', trigger: 'change' }]
      },
      menuList: [],
      dfp_url: window.SITE_CONFIG.baseUrl,
      menuListTreeProps: {
        label: 'name',
        children: 'children'
      }
    }
  },
  created () {
    this.iconList = Icon.getNameList()
  },
  methods: {
    init: function (id, type) {
      var menuUrl = this.dfp_url + '/dfplatform/getMenuTree'
      this.$axios.get(menuUrl).then(res => {
        var datas = res.data.data
        this.menuList = datas.menuList
      })
      var url = this.dfp_url + '/dfplatform/getMenuById'
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
      })
      if (type === 'update') {
        this.$axios.get(url, { params: { id: id } }).then(res => {
          var datas = res.data.data
          this.dataForm.id = datas.id
          this.dataForm.type = datas.type
          this.dataForm.name = datas.name
          this.dataForm.icon = datas.icon
          this.dataForm.parentId = datas.parentId
          if (datas.type === 0) {
            this.dataForm.typename = '目录'
          } else {
            this.dataForm.typename = '菜单'
            this.dataForm.parentName = datas.parentName
          }
        })
      } else {
        this.dataForm.id = 0
      }
    },
    // 菜单树选中
    menuListTreeCurrentChangeHandle (data, node) {
      this.dataForm.parentId = data.id
      this.dataForm.parentName = data.name
      this.$refs.menuListPopover.doClose()
    },
    // 图标选中
    iconActiveHandle (iconName) {
      this.dataForm.icon = iconName
      this.$refs.iconListPopover.doClose()
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          var addMenu = this.dfp_url + '/dfplatform/addMenu'
          var updateMenu = this.dfp_url + '/dfplatform/updateMenu'
          var url
          var menuid
          var menuname
          if (this.dataForm.id === 0) {
            url = addMenu
            menuid = null
            if (this.dataForm.type === 2) {
              menuname = '环境配置'
            } else {
              menuname = this.dataForm.name
            }
          } else {
            url = updateMenu
            menuid = this.dataForm.id
            menuname = this.dataForm.name
          }
          var menuparentname = this.dataForm.parentName
          var menuparentId = this.dataForm.parentId
          var menutype = this.dataForm.type
          var menuicon = this.dataForm.icon
          var json = {
            id: menuid,
            type: menutype,
            name: menuname,
            parentId: menuparentId,
            parentName: menuparentname,
            icon: menuicon,
            userId: window.localStorage.getItem('userId')
          }
          this.$axios
            .post(url, json, {})
            .then(res => {
              var datas = res.data
              if (datas && datas.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success'
                })
                this.visible = false
                this.$emit('refreshDataList')
              } else {
                this.$message({
                  message: 'datas.message',
                  type: 'fail'
                })
              }
            })
            .catch(err => {
              this.$message.error(err)
            })
        }
      })
    }
  }
}
</script>

<style lang="scss">
.mod-menu {
  .menu-list__input,
  .icon-list__input {
    > .el-input__inner {
      cursor: pointer;
    }
  }
  &__icon-popover {
    width: 458px;
    overflow: hidden;
  }
  &__icon-inner {
    width: 478px;
    max-height: 258px;
    overflow-x: hidden;
    overflow-y: auto;
  }
  &__icon-list {
    width: 458px;
    padding: 0;
    margin: -8px 0 0 -8px;
    > .el-button {
      padding: 8px;
      margin: 8px 0 0 8px;
      > span {
        display: inline-block;
        vertical-align: middle;
        width: 18px;
        height: 18px;
        font-size: 18px;
      }
    }
  }
  .icon-list__tips {
    font-size: 18px;
    text-align: center;
    color: #e6a23c;
    cursor: pointer;
  }
}
</style>
