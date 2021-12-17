<template>
  <nav class="site-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <a class="site-navbar__brand-lg" href="javascript:;">数据工厂</a>
        <a class="site-navbar__brand-mini" href="javascript:;">数厂</a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu class="site-navbar__menu" mode="horizontal">
        <el-menu-item
          class="site-navbar__switch"
          index="0"
          @click="sidebarFold = !sidebarFold"
        >
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <el-menu
        class="site-navbar__menu site-navbar__menu--right"
        mode="horizontal"
      >
        <!-- <el-submenu index="1">
          <template slot="title">Git源码</template>
          <el-menu-item index="2-1">
            <a
              href="https://github.com/renrenio/renren-fast-vue"
              target="_blank"
            >
              前端
            </a>
          </el-menu-item>
          <el-menu-item index="2-2">
            <a href="https://gitee.com/renrenio/renren-fast" target="_blank">
              后台
            </a>
          </el-menu-item>
        </el-submenu> -->
        <el-menu-item class="site-navbar__avatar" index="3">
          <el-dropdown :show-timeout="0" placement="bottom">
            <span class="el-dropdown-link">
              <img src="~@/assets/img/logo-header.png" :alt="userName" />{{
                userName
              }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-button
                style="float: right; margin-right: 10px;color: #606266;"
                type="text"
                @click="dialogModelVisible = true"
              >
                修改密码
              </el-button>
              <el-dropdown-item @click.native="logoutHandle()">
                退出
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
     <el-dialog append-to-body
       title="更新密码"
       :visible.sync="dialogModelVisible"
       size="tiny"
       width="30%"
     >
       <el-form :model="userform" label-width="110px">
         <el-form-item label="旧密码">
           <el-input
             size="mini"
             v-model="userform.password"
             placeholder="请输入旧密码"
           >
           </el-input>
         </el-form-item>
         <el-form-item label="新密码">
           <el-input
             size="mini"
             v-model="userform.newPassword"
             placeholder="请输入新密码"
           >
           </el-input>
         </el-form-item>
         <el-form-item label="再次输入新密码">
           <el-input
             size="mini"
             v-model="userform.newPasswordAgain"
             placeholder="再次输入新密码"
           >
           </el-input>
         </el-form-item>
       </el-form>
       <div slot="footer" class="dialog-footer">
         <el-button @click="dialogModelVisible = false">取 消</el-button>
         <el-button type="primary" @click="createmodel">确 定</el-button>
       </div>
     </el-dialog>
    </div>
  </nav>
</template>

<script>
export default {
  data () {
    return {
      userform: {
        username: '',
        password: '',
        newPassword: '',
        newPasswordAgain: ''
      },
      updatePassowrdVisible: false,
      dialogModelVisible: false,
      dfp_url: window.SITE_CONFIG.baseUrl
    }
  },
  created () {
    this.userform.username = this.$store.state.user.name
  },
  computed: {
    navbarLayoutType: {
      get () {
        return this.$store.state.common.navbarLayoutType
      }
    },
    sidebarFold: {
      get () {
        return this.$store.state.common.sidebarFold
      },
      set (val) {
        this.$store.commit('common/updateSidebarFold', val)
      }
    },
    userName: {
      get () {
        return this.$store.state.user.name
      }
    }
  },
  methods: {
    createmodel () {
      var url = this.dfp_url + '/dfplatform/updatePassword'
      this.$axios
        .post(url, {
          isupdate: true,
          newPassword: this.$md5(this.userform.newPassword),
          newPasswordAgain: this.$md5(this.userform.newPasswordAgain),
          password: this.$md5(this.userform.password),
          username: this.$store.state.user.name
        })
        .then(res => {
          var datas = res.data
          if (datas.code === 0) {
            this.dialogModelVisible = false
            this.$message({
              message: '密码修改成功',
              type: 'success'
            })
            localStorage.removeItem('user')
            this.$router.push({ name: 'login' })
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
    // 退出
    logoutHandle () {
      this.$confirm(`确定进行[退出]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          localStorage.removeItem('user')
          this.$router.push({ name: 'login' })
        })
        .catch(() => {})
    }
  }
}
</script>
