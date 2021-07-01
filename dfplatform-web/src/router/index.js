/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import axios from 'axios'
Vue.prototype.$axios = axios

Vue.use(Router)

const _import = require('./import-development')

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } }
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口整体布局' },
  children: [
    // 通过meta对象设置路由展示方式
    // 1. isTab: 是否通过tab展示内容, true: 是, false: 否
    // 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
    // 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
    { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页' } },
    { path: '/sysMenu', component: _import('common/menu'), name: 'sysMenu', meta: { title: '菜单管理' } },
    { path: '/demoCreate', component: _import('modules/ApiCreate'), name: 'demoCreate', meta: { title: '创建场景', isTab: true } },
    { path: '/Environment', component: _import('modules/Environment'), name: 'Environment', meta: { title: '环境配置', isTab: true } },
    { path: '/ParametersList', component: _import('modules/ParametersList'), name: 'ParametersList', meta: { title: '动态参数', isTab: true } },
    { path: '/demoList', component: _import('modules/ApiSceneList'), name: 'demoList', meta: { title: '项目一', isTab: true } }
  ],
  beforeEnter (to, from, next) {
    let user = window.localStorage.getItem('user')
    if (user == null) {
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),
  isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
  routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
  // 添加动态(菜单)路由
  // 1. 已经添加 or 全局路由, 直接访问
  // 2. 获取菜单列表, 添加并保存本地存储
  var url = window.SITE_CONFIG.baseUrl + '/dfplatform/getMenuList'
  if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
    next()
  } else {
    axios.get(url)
      .then(res => {
        var menudata = res.data
        if (menudata && menudata.code === 0) {
          console.log('res.data.data.menuList:' + JSON.stringify(res.data.data.menuList))
          fnAddDynamicMenuRoutes(res.data.data.menuList)
          console.log('res.data.data.menuList:1111111')
          router.options.isAddDynamicMenuRoutes = true
          sessionStorage.setItem('menuList', JSON.stringify(menudata.data.menuList || '[]'))
          next({ ...to, replace: true })
        } else {
          sessionStorage.setItem('menuList', '[]')
          next()
        }
      }).catch((e) => {
        console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
        router.push({ name: 'login' })
      })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
  var temp = []
  for (var i = 0; i < globalRoutes.length; i++) {
    if (route.path === globalRoutes[i].path) {
      return 'global'
    } else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
      temp = temp.concat(globalRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
  // console.log('menuList:' + JSON.stringify(menuList))
  // console.log('routes:' + JSON.stringify(routes))
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    var List = menuList[i].list
    // console.log('List:' + JSON.stringify(List))
    if (List && List.length >= 1) {
      for (var m = 0; m < List.length; m++) {
        temp = temp.concat(List[m])
        var componenturl
        if (List[m].type === 2) {
          componenturl = _import('modules/Environment')
        } else {
          componenturl = _import('modules/ApiSceneList')
        }
        var route = {
          path: '/' + List[m].id,
          component: componenturl,
          name: List[m].id.toString(),
          meta: {
            id: List[m].id,
            component: componenturl,
            name: List[m].id.toString(),
            path: '/' + List[m].id,
            title: List[m].name,
            isDynamic: true,
            isTab: true,
            iframeUrl: ''
          }
        }
        routes.push(route)
      }
    }
  }
  if (temp.length >= 1) {
    fnAddDynamicMenuRoutes(temp, routes)
  } else {
    mainRoutes.name = 'main-dynamic'
    mainRoutes.children = routes
    router.addRoutes([
      mainRoutes,
      { path: '*', redirect: { name: '404' } }
    ])
    sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
    console.log('\n')
    console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
    console.log(mainRoutes.children)
    console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
  }
}

export default router
