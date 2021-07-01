<template>
  <main
    class="site-content"
    :class="{ 'site-content--tabs': $route.meta.isTab }"
  >
    <!-- 主入口标签页 s -->
    <el-tabs
      v-if="$route.meta.isTab"
      v-model="mainTabsActiveName"
      :closable="true"
      @tab-click="selectedTabHandle"
      @tab-remove="removeTabHandle"
    >
      <el-tab-pane
        v-for="item in mainTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name"
      >
        <el-card :body-style="siteContentViewHeight">
          <iframe
            v-if="item.type === 'iframe'"
            :src="item.iframeUrl"
            width="100%"
            height="100%"
            frameborder="0"
            scrolling="yes"
          >
          </iframe>
          <keep-alive v-else>
            <router-view v-if="item.name === mainTabsActiveName" />
          </keep-alive>
        </el-card>
      </el-tab-pane>
    </el-tabs>
    <!-- 主入口标签页 e -->
    <el-card v-else :body-style="siteContentViewHeight">
      <keep-alive>
        <router-view />
      </keep-alive>
    </el-card>
  </main>
</template>

<script>
import { isURL } from '@/utils/validate'
import MiddleUtil from '../views/modules/MiddleUtil'
export default {
  data () {
    return {}
  },
  computed: {
    documentClientHeight: {
      get () {
        return this.$store.state.common.documentClientHeight
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
    },
    siteContentViewHeight () {
      var height = this.documentClientHeight - 50 - 30 - 2
      if (this.$route.meta.isTab) {
        height -= 40
        return isURL(this.$route.meta.iframeUrl)
          ? { height: height + 'px' }
          : { minHeight: height + 'px' }
      }
      return { minHeight: height + 'px' }
    }
  },
  mounted () {
    // tabs, 删除tab，提供给其他页面调用
    var that = this
    MiddleUtil.$on('demo', function (tabName) {
      that.removeTabHandle(tabName)
    })
  },
  methods: {
    // tabs, 选中tab
    selectedTabHandle (tab) {
      tab = this.mainTabs.filter(item => item.name === tab.name)
      if (tab.length >= 1) {
        this.$router.push({
          name: tab[0].name,
          query: tab[0].query,
          params: tab[0].params
        })
      }
    },
    // tabs, 删除tab
    removeTabHandle: function (tabName) {
      // console.log('mainTabs:' + JSON.stringify(this.mainTabs))
      // console.log('tabName:' + tabName)
      this.mainTabs = this.mainTabs.filter(item => item.name !== tabName)
      // console.log('mainTabs:' + JSON.stringify(this.mainTabs))
      // console.log('mainTabsActiveName:' + JSON.stringify(this.mainTabsActiveName))
      if (this.mainTabs.length >= 1) {
        // 当前选中tab被删除
        if (tabName === this.mainTabsActiveName) {
          var tab = this.mainTabs[this.mainTabs.length - 1]
          // console.log('tab:' + JSON.stringify(tab))
          this.$router.push(
            { name: tab.name, query: tab.query, params: tab.params },
            () => {
              this.mainTabsActiveName = this.$route.name
            }
          )
        }
      } else {
        this.menuActiveName = ''
        this.$router.push({ name: 'home' })
      }
    }
  }
}
</script>
