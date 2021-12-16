<template>
  <div
    class="site-wrapper"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="拼命加载中"
  >
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div
        class="site-content__wrapper"
        :style="{ 'min-height': documentClientHeight + 'px' }"
      >
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
import MainNavbar from './mainNavbar'
import MainSidebar from './mainSidebar'
import MainContent from './mainContent'
export default {
  provide () {
    return {
      // 刷新
      refresh () {
        this.$store.commit('common/updateContentIsNeedRefresh', true)
        this.$nextTick(() => {
          this.$store.commit('common/updateContentIsNeedRefresh', false)
        })
      }
    }
  },
  data () {
    return {
      loading: true,
      dfp_url: window.SITE_CONFIG.baseUrl
    }
  },
  components: {
    MainNavbar,
    MainSidebar,
    MainContent
  },
  computed: {
    documentClientHeight: {
      get () {
        return this.$store.state.common.documentClientHeight
      },
      set (val) {
        this.$store.commit('common/updateDocumentClientHeight', val)
      }
    },
    sidebarFold: {
      get () {
        return this.$store.state.common.sidebarFold
      }
    },
    userId: {
      get () {
        return this.$store.state.user.id
      },
      set (val) {
        this.$store.commit('user/updateId', val)
      }
    },
    userName: {
      get () {
        return this.$store.state.user.name
      },
      set (val) {
        this.$store.commit('user/updateName', val)
      }
    }
  },
  created () {
    this.getUserInfo()
  },
  mounted () {
    this.resetDocumentClientHeight()
  },
  methods: {
    // 重置窗口可视高度
    resetDocumentClientHeight () {
      this.documentClientHeight = document.documentElement['clientHeight']
      window.onresize = () => {
        this.documentClientHeight = document.documentElement['clientHeight']
      }
    },
    // 获取当前用户信息
    getUserInfo () {
      var url = this.dfp_url + '/dfplatform/getUser'
      this.userName = window.localStorage.getItem('user')
      this.$axios.get(url, {params: {username: this.userName}}).then(res => {
        var datas = res.data
        if (datas && datas.code === 0) {
          this.loading = false
          window.localStorage.setItem('userId', datas.data.id)
        }
      })
    }
  }
}
</script>
