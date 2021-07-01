<template>
  <aside class="site-sidebar" :class="'site-sidebar--' + sidebarLayoutSkin">
    <div class="site-sidebar__inner">
      <el-menu
        :default-active="menuActiveName || 'home'"
        :collapse="sidebarFold"
        :collapseTransition="false"
        class="site-sidebar__menu"
      >
        <el-menu-item index="home" @click="$router.push({ name: 'home' })">
          <icon-svg name="shouye" class="site-sidebar__menu-icon"></icon-svg>
          <span slot="title">项目介绍</span>
        </el-menu-item>
        <sub-menu
          v-for="menu in menuList"
          :key="menu.id"
          :menu="menu"
          :dynamicMenuRoutes="dynamicMenuRoutes"
        >
        </sub-menu>
        <el-menu-item
          index="params"
          @click="$router.push({ name: 'ParametersList' })"
        >
          <icon-svg name="menu" class="site-sidebar__menu-icon"></icon-svg>
          <span slot="title">动态参数</span>
        </el-menu-item>
        <el-submenu index="system">
          <template slot="title">
            <icon-svg name="config" class="site-sidebar__menu-icon"></icon-svg>
            <span>系统管理</span>
          </template>
          <el-menu-item
            index="sysMenu"
            @click="$router.push({ name: 'sysMenu' })"
          >
            <icon-svg name="menu" class="site-sidebar__menu-icon"></icon-svg>
            <span slot="title">菜单管理</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
  </aside>
</template>

<script>
import SubMenu from './mainSidebarSubMenu'
export default {
  data () {
    return {
      dynamicMenuRoutes: []
    }
  },
  components: {
    SubMenu
  },
  computed: {
    sidebarLayoutSkin: {
      get () {
        return this.$store.state.common.sidebarLayoutSkin
      }
    },
    sidebarFold: {
      get () {
        return this.$store.state.common.sidebarFold
      }
    },
    menuList: {
      get () {
        return this.$store.state.common.menuList
      },
      set (val) {
        this.$store.commit('common/updateMenuList', val)
      }
    },
    menuActiveName: {
      get () {
        return this.$store.state.common.menuActiveName
      },
      set (val) {
        this.$store.commit('common/updateMenuActiveName', val)
      }
    },
    mainTabs: {
      get () {
        return this.$store.state.common.mainTabs
      },
      set (val) {
        this.$store.commit('common/updateMainTabs', val)
      }
    },
    mainTabsActiveName: {
      get () {
        return this.$store.state.common.mainTabsActiveName
      },
      set (val) {
        this.$store.commit('common/updateMainTabsActiveName', val)
      }
    }
  },
  watch: {
    $route: 'routeHandle'
  },
  created () {
    this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
    this.dynamicMenuRoutes = JSON.parse(
      sessionStorage.getItem('dynamicMenuRoutes') || '[]'
    )
    this.routeHandle(this.$route)
  },
  methods: {
    // 路由操作
    routeHandle (route) {
      if (route.meta.isTab) {
        // tab选中, 不存在先添加
        // console.log('Yitem.name:' + item.name)
        var tab = this.mainTabs.filter(item => item.name === route.name)[0]
        console.log('tab:' + tab)
        if (!tab) {
          if (route.meta.isDynamic) {
            console.log('route.meta.isDynamic:' + route.meta.isDynamic)
            route = this.dynamicMenuRoutes.filter(
              item => item.name === route.name
            )[0]
            if (!route) {
              return console.error('未能找到可用标签页!')
            }
          }
          tab = {
            id: route.meta.id || route.name,
            name: route.name,
            title: route.meta.title,
            type: 'module',
            iframeUrl: route.meta.iframeUrl || '',
            params: route.params,
            query: route.query
          }
          this.mainTabs = this.mainTabs.concat(tab)
        }
        this.menuActiveName = tab.id + ''
        this.mainTabsActiveName = tab.name
      }
    },
    demoListClick () {
      window.sessionStorage.setItem('projectName', 'null')
      this.$router.push({ name: 'demoList' })
    },
    EnvironmentClick () {
      window.sessionStorage.setItem('projectName', 'null')
      this.$router.push({ name: 'Environment' })
    }
  }
}
</script>
