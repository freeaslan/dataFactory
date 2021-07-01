/**
 * 生产环境
 */
;(function () {
  window.SITE_CONFIG = {};
  // api接口请求地址
  window.SITE_CONFIG['baseUrl'] = 'http://you server ip:8180';

  window.SITE_CONFIG['domain']  = './'; // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
