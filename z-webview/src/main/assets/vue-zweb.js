/*!
 * vueZWeb.js v1.0.0
 * (c) 2017 Zyao89
 * Released under the MIT License.
 */
(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() : typeof define === 'function' && define.amd ? define(factory) : (global.VueZWeb = factory());
}(this, (function () {
  'use strict';

  var name = 'ZWeb';
  var timeout = 30 * 1000;

  var readyPool = [];
  var globalRoot = window;
  var VueZWeb = {};

  var isDef = function (v) {
    return v !== undefined;
  };

  var addReadyPool = function addReadyPool(obj) {
    if (obj) {
      var index = readyPool.indexOf(obj);
      if (index < 0) {
        readyPool.push(obj);
      }
    }
  }

  var removeReadyPool = function removeReadyPool(obj) {
    if (obj) {
      var index = readyPool.indexOf(obj);
      if (index >= 0) {
        readyPool.splice(index, 1);
      }
    }
  }

  var install = function install(Vue, options) {
    if (install.installed) {
      return
    }
    install.installed = true;

    if (options && options.name) {
      name = options.name;
    }
    if (options && options.timeout) {
      timeout = options.timeout;
    }
    if (window.Vue && window.Vue.isVue) {
      globalRoot = window.Vue;
    }
    globalRoot['__' + name + '_Dispatch_Ready__'] = function () {
      readyPool.forEach(function (vm) {
        try {
          vm._zWebReady && vm._zWebReady();
        } catch (error) {
          console.error(error);
        }
        clearTimeout(vm._zWebTimer);
      });
      readyPool = [];
    };

    Vue.mixin({
      beforeCreate: function beforeCreate() {
        if (isDef(this.$options.zWebReady)) {
          this._zWebReady = this.$options.zWebReady;
          addReadyPool(this);
        }
      },
      mounted: function mounted() {
        var vm = this;
        clearTimeout(this._zWebTimer);
        this._zWebTimer = setTimeout(function () {
          vm._zWebReady && vm._zWebReady();
        }, timeout);
      },
      beforeDestroy: function beforeDestroy() {
        clearTimeout(this._zWebTimer);
        removeReadyPool(this);
      }
    });
  }

  VueZWeb.install = install;
  VueZWeb.version = '1.0.0';

  var inBrowser = typeof window !== 'undefined';
  if (inBrowser && window.Vue) {
    window.Vue.use(VueZWeb);
  }

  return VueZWeb;

})));