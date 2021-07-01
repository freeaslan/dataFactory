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
              <el-dropdown-item @click.native="logoutHandle()">
                退出
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
  </nav>
</template>

<script>
export default {
  data () {
    return {
      updatePassowrdVisible: false
    }
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
    // mainTabs: {
    //   get () { return this.$store.state.common.mainTabs },
    //   set (val) { this.$store.commit('common/updateMainTabs', val) }
    // },
    userName: {
      get () {
        return this.$store.state.user.name
      }
    }
  },
  methods: {
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
