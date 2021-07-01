<template>
  <div class="site-wrapper site-page--login">
    <div class="site-content__wrapper">
      <div class="site-content">
        <div class="login-main" style="width: 25%; ">
          <div class="ms-title">数 据 工 厂</div>
          <el-tabs type="border-card">
            <el-tab-pane label="登录">
              <el-form
                :model="dataForm"
                :rules="dataRule"
                ref="dataForm"
                @keyup.enter.native="dataFormSubmit()"
                status-icon
              >
                <el-form-item prop="userName" required>
                  <el-input v-model="dataForm.userName" placeholder="帐号">
                  </el-input>
                </el-form-item>
                <el-form-item prop="password" required>
                  <el-input
                    v-model="dataForm.password"
                    type="password"
                    placeholder="密码"
                  >
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    class="login-btn-submit"
                    type="primary"
                    @click="dataFormSubmit()"
                  >
                    登录
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="注册">
              <el-form
                :model="registerForm"
                :rules="dataRule"
                ref="dataForm"
                @keyup.enter.native="register()"
                status-icon
              >
                <el-form-item prop="userName" required>
                  <el-input
                    v-model="registerForm.userName"
                    placeholder="帐号"
                  ></el-input>
                </el-form-item>
                <el-form-item prop="password" required>
                  <el-input
                    v-model="registerForm.password"
                    type="password"
                    placeholder="密码"
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                    class="login-btn-submit"
                    type="primary"
                    @click="register()"
                  >
                    注册
                  </el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      myWidth: window.innerWidth - 500 + 'px',
      dataForm: {
        userName: '',
        password: ''
      },
      registerForm: {
        userName: '',
        password: ''
      },
      dataRule: {
        userName: [
          { required: true, message: '帐号不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      },
      dfp_url: window.SITE_CONFIG.baseUrl
    }
  },
  methods: {
    // 提交表单
    dataFormSubmit () {
      let _this = this
      var url = this.dfp_url + '/dfplatform/userLogin'
      var user = this.dataForm.userName
      var password = this.$md5(this.dataForm.password)
      var json = { username: user, password: password }
      if (user === '' || this.dataForm.password === '') {
        _this.$message.error('账户或密码不能为空')
      } else {
        this.$axios
          .post(url, json, {})
          .then(res => {
            var datas = res.data
            var code = datas.code
            if (code === 0) {
              window.localStorage.clear()
              window.localStorage.setItem('user', user)
              window.localStorage.setItem('userId', datas.data.id)
              window.sessionStorage.setItem('projectName', 'null')
              this.$router.push({ name: 'home' })
            } else {
              _this.$message.error(datas.message)
            }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
      this.$router.push({ name: 'login' })
    },
    register () {
      let _this = this
      var url = this.dfp_url + '/dfplatform/createNewUser'
      var user = this.registerForm.userName
      var password = this.$md5(this.registerForm.password)
      var json = { username: user, password: password }
      if (user === '' || this.registerForm.password === '') {
        _this.$message.error('账户或密码不能为空')
      } else {
        this.$axios
          .post(url, json, {})
          .then(res => {
            var datas = res.data
            var code = datas.code
            if (code === 0) {
              _this.$message.success(datas.message)
            } else {
              _this.$message.error(datas.message)
            }
          })
          .catch(function (error) {
            console.log(error)
          })
      }
      this.$router.push({ name: 'login' })
    }
  }
}
</script>

<style lang="scss">
.site-wrapper.site-page--login {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.1);
  overflow: hidden;
  &:before {
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
    height: 100%;
    content: '';
    background-image: url(~@/assets/img/login_bg.jpg);
    background-size: cover;
  }
  .site-content__wrapper {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    padding: 0;
    margin: 0;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: transparent;
  }
  .site-content {
    min-height: 100%;
    padding: 30px 500px 30px 30px;
  }
  .brand-info {
    margin: 220px 100px 0 90px;
    color: #fff;
  }
  .ms-title {
    width: 100%;
    line-height: 100px;
    text-align: center;
    font-size: 65px;
    color: #fff;
  }
  .brand-info__text {
    margin: 0 0 22px 0;
    font-size: 48px;
    font-weight: 400;
    text-transform: uppercase;
  }
  .brand-info__intro {
    margin: 10px 0;
    font-size: 16px;
    line-height: 1.58;
    opacity: 0.6;
  }
  .login-main {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    /*background: rgba(255, 255, 255, 0.3);*/
    /*background-color:transparent;*/
    overflow: hidden;
  }
  .login-title {
    font-size: 20px;
  }
  .login-btn-submit {
    width: 100%;
    margin-top: 10px;
    background-color: #b10808;
  }
  .el-tabs__nav {
    border: none;
    display: flex;
    width: 100%;
    background-color: rgba(255, 255, 255, 0.1);
    .el-tabs__item {
      border: none;
      flex: 1;
      text-align: center;
    }
  }
  .el-tabs--border-card {
    margin-top: 25px;
    border: none;
    background-color: rgba(0, 0, 0, 0.1);
    .el-tabs__header {
      border: none;
      background-color: rgba(0, 0, 0, 0.1);
      .el-tabs__item {
        border: none;
        background-color: rgba(0, 0, 0, 0.1);
        .is-active {
          border: none;
          background-color: rgba(0, 0, 0, 0.1);
        }
      }
    }
  }
  .el-input__inner {
    margin-top: 10px;
    background-color: rgba(0, 0, 0, 0.1);
  }
}
</style>
