/*!
 * ZWeb.js v0.0.1
 * (c) 2017 Zyao89
 * Released under the MIT License.
 */
(function(global, factory) {
	if ("undefined" === typeof global) {
		throw "not find window...";
	}
	var LIB_NAME = "ZWeb";
	if ("undefined" === typeof global["__" + LIB_NAME + "__"]) {
		var zWeb = factory(global, LIB_NAME);
		global["__" + LIB_NAME + "__"] = new zWeb();
	} else {
		console.error(LIB_NAME + ' already loaded...')
	}
})(window, function(global, LIB_NAME) {
	'use strict';

	var _author = 'Zyao89';
	var _version = '1.0.0';

	// 内部方法
	var INTER_NAME = {
		onZWebCreated: "onCreated",
		onZWebException: "onException",
		onZWebRequire: "onRequire",
		onZWebMessage: "onMessage",
		onZWebDestroy: "onDestroy",
		onZWebLog: "onLog",
		saveData: "saveData",
		loadData: "loadData",
		showLoading: "showLoading",
		hideLoading: "hideLoading",
		tip: "tip"
	};

	// 外部暴露方法，可扩展， 使用 callOS 调用
	var METHODS_NAME = {};

	// 监听回调类型名称
	var DISPATCH_TYPE = {
		On_Ready: "onReady"
	};

	// 平台类型
	var OS_TYPE = {
		WEB: "WEB",
		IOS: "IOS",
		ANDROID: "ANDROID"
	};

	// 内部方法
	var ExportsMethod = {
		UUID: undefined,
		Promise: undefined
	};

	// UUID
	(function(root) {
		function UUID() {
			this.id = this.createUUID();
		}

		UUID.prototype.valueOf = function() {
			return this.id;
		};
		UUID.prototype.toString = function() {
			return this.id;
		};

		UUID.prototype.createUUID = function() {
			var dg = new Date(1582, 10, 15, 0, 0, 0, 0);
			var dc = new Date();
			var t = dc.getTime() - dg.getTime();
			var h = "-";
			var tl = UUID.getIntegerBits(t, 0, 31);
			var tm = UUID.getIntegerBits(t, 32, 47);
			var thv = UUID.getIntegerBits(t, 48, 59) + "1";
			var csar = UUID.getIntegerBits(UUID.rand(4095), 0, 7);
			var csl = UUID.getIntegerBits(UUID.rand(4095), 0, 7);

			var n = UUID.getIntegerBits(UUID.rand(8191), 0, 7) + UUID.getIntegerBits(UUID.rand(8191), 8, 15) + UUID.getIntegerBits(UUID.rand(8191), 0, 7) + UUID.getIntegerBits(UUID.rand(8191), 8, 15) + UUID.getIntegerBits(UUID.rand(8191), 0, 15);
			return tl + h + tm + h + thv + h + csar + csl + h + n;
		};

		UUID.getIntegerBits = function(val, start, end) {
			var base16 = UUID.returnBase(val, 16);
			var quadArray = new Array();
			var quadString = "";
			var i = 0;
			for (i = 0; i < base16.length; i++) {
				quadArray.push(base16.substring(i, i + 1));
			}
			for (i = Math.floor(start / 4); i <= Math.floor(end / 4); i++) {
				if (!quadArray[i] || quadArray[i] == "") quadString += "0";
				else quadString += quadArray[i];
			}
			return quadString;
		};

		UUID.returnBase = function(number, base) {
			var convert = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
			if (number < base) var output = convert[number];
			else {
				var MSD = "" + Math.floor(number / base);
				var LSD = number - MSD * base;
				if (MSD >= base) var output = this.returnBase(MSD, base) + convert[LSD];
				else var output = convert[MSD] + convert[LSD];
			}
			return output;
		};

		UUID.rand = function(max) {
			return Math.floor(Math.random() * max);
		};

		root.UUID = UUID;
	})(ExportsMethod);

	// Promise
	(function(root) {
		//constructor
		var Promise = function() {
				this.callbacks = [];
			};

		Promise.prototype = {
			construct: Promise,
			resolve: function(result) {
				this.complete("resolve", result);
			},

			reject: function(result) {
				this.complete("reject", result);
			},

			complete: function(type, result) {
				while (this.callbacks[0]) {
					var func = this.callbacks.shift()[type];
					func && func(result);
				}
			},

			then: function(successHandler, failedHandler) {
				this.callbacks.push({
					resolve: successHandler,
					reject: failedHandler
				});

				return this;
			}
		};

		root.Promise = Promise;
	})(ExportsMethod);

	var ZWebSDK = function(szFrameworkUUID, oParams) {
			// 请求队列
			this.mapRequireQueue = {};
			// 消息队列
			this.mapMessageQueue = {};
			// 数据读取队列
			this.mapDatabaseQueue = {};
			// 唯一UUID
			this.szFrameworkUUID = szFrameworkUUID;
			// 平台
			this.OS = oParams.OS;
			// 版本
			this.Version = oParams.Version;
			// 内部接口
			this.InternalName = oParams.InternalName;
			// 暴露接口
			this.ExposedName = oParams.ExposedName;
			// 监听接口
			this.__onFuncCallBackMap__ = {};

			this.print("Info", JSON.stringify(oParams));
			this.author = _author;
			this.version = _version;
		};

	ZWebSDK.prototype = {
		createUUID: function() {
			return new ExportsMethod.UUID().id;
		},

		saveData: function(szKey, szValue) {
			var oRequireParam = {
				Sequence: this.createUUID(),
				Data: {
					Key: szKey,
					Value: szValue
				}
			};
			var promise = new ExportsMethod.Promise();
			this.mapDatabaseQueue[oRequireParam.Sequence] = promise;
			this.callInterOS(INTER_NAME.saveData, oRequireParam);
			return promise;
		},

		loadData: function(szKey) {
			var oRequireParam = {
				Sequence: this.createUUID(),
				Data: szKey
			};
			var promise = new ExportsMethod.Promise();
			this.mapDatabaseQueue[oRequireParam.Sequence] = promise;
			this.callInterOS(INTER_NAME.loadData, oRequireParam);
			return promise;
		},

		/**
		 * 数据操作返回方法
		 * {
		 *  Sequence："",
		 *  Result: "success" or "error",
		 *  Data: {}
		 * }
		 */
		databaseCallback: function(oResultParam) {
			var sequence = oResultParam.Sequence;
			var promise = this.mapDatabaseQueue[sequence];
			if ('undefined' === typeof promise) return;
			delete this.mapDatabaseQueue[sequence];
			// 请求结果回调出去
			var result = oResultParam.Result;
			if (result === "success") {
				// 成功
				promise.resolve(oResultParam.Data);
			} else {
				// 失败
				promise.reject(oResultParam.Data);
			}
		},

		showLoading: function() {
			this.callInterOS(INTER_NAME.showLoading);
		},

		hideLoading: function() {
			this.callInterOS(INTER_NAME.hideLoading);
		},

		tip: function(szMsg) {
			this.callInterOS(INTER_NAME.tip, szMsg);
		},

		/**
		 * @ url 连接地址
		 * @ szMethod 方法类型
		 * @ oData 数据
		 * @ szType 返回类型
		 */
		require: function(url, szMethod, oData, szType) {
			var oRequireParam = {
				Sequence: this.createUUID(),
				Url: url,
				Method: szMethod,
				Data: oData,
				Type: szType || "json"
			};
			var promise = new ExportsMethod.Promise();
			this.mapRequireQueue[oRequireParam.Sequence] = promise;
			this.callInterOS(INTER_NAME.onZWebRequire, oRequireParam);
			return promise;
		},

		/**
		 * 请求返回方法
		 * {
		 *  Sequence："",
		 *  Result: "success" or "error",
		 *  Data: {}
		 * }
		 */
		requireCallback: function(oResultParam) {
			var sequence = oResultParam.Sequence;
			var promise = this.mapRequireQueue[sequence];
			if ('undefined' === typeof promise) return;
			delete this.mapRequireQueue[sequence];
			// 请求结果回调出去
			var result = oResultParam.Result;
			if (result === "success") {
				// 成功
				promise.resolve(oResultParam.Data);
			} else {
				// 失败
				promise.reject(oResultParam.Data);
			}
		},

		/**
		 * @ szCmd 命令
		 * @ oData 数据
		 */
		message: function(szCmd, oData) {
			var oRequireParam = {
				Sequence: this.createUUID(),
				Cmd: szCmd,
				Data: oData
			};
			var promise = new ExportsMethod.Promise();
			this.mapMessageQueue[oRequireParam.Sequence] = promise;
			this.callInterOS(INTER_NAME.onZWebMessage, oRequireParam);
			return promise;
		},

		/**
		 * 消息请求返回方法
		 * {
		 *  Sequence："",
		 *  Result: "success" or "error",
		 *  Data: {}
		 * }
		 */
		messageCallback: function(oResultParam) {
			var sequence = oResultParam.Sequence;
			var promise = this.mapMessageQueue[sequence];
			if ('undefined' === typeof promise) return;
			delete this.mapMessageQueue[sequence];
			// 请求结果回调出去
			var result = oResultParam.Result;
			if (result === "success") {
				// 成功
				promise.resolve(oResultParam.Data);
			} else {
				// 失败
				promise.reject(oResultParam.Data);
			}
		},

		// 销毁或退出
		destroy: function() {
			this.callInterOS(INTER_NAME.onZWebDestroy);
		},

		// 调用 INTER_NAME 定义的方法
		callInterOS: function(szMethodName, oData) {
			this.callOS(szMethodName, oData, this.InternalName);
		},

		// 调用 METHODS_NAME 定义的方法
		callOS: function(szMethodName, oData, exposedName) {
			if (typeof exposedName === "undefined") {
				exposedName = this.ExposedName;
			}
			switch (this.OS) {
			case OS_TYPE.ANDROID:
				if (global[exposedName] && global[exposedName][szMethodName]) {
					if (typeof oData === "undefined") {
						global[exposedName][szMethodName](this.szFrameworkUUID);
					} else if (typeof oData === "object") {
						global[exposedName][szMethodName](
						this.szFrameworkUUID, JSON.stringify(oData));
					} else {
						global[exposedName][szMethodName](this.szFrameworkUUID, oData);
					}
				} else {
					this.print(
					szMethodName + " : " + this.szFrameworkUUID + "： Android 接口调用异常...");
				}
				break;

			case OS_TYPE.IOS:
				if (
				global.webkit && global.webkit.messageHandlers && global.webkit.messageHandlers[exposedName]) {
					var msg = {
						Method: szMethodName,
						FrameworkID: this.szFrameworkUUID,
						Data: JSON.stringify(oData)
					};
					if (typeof oData === "undefined") {
						delete msg.Data;
					}
					global.webkit.messageHandlers[exposedName].postMessage(msg);
				} else {
					this.print(
					szMethodName + " : " + this.szFrameworkUUID + "： IOS 接口调用异常...");
				}
				break;

			case OS_TYPE.WEB:
				this.print(
				szMethodName + " : " + this.szFrameworkUUID + "： Web 接口调用...");
				break;

			default:
				this.print("init callOS error, 回调失败。");
				break;
			}
		},

		// 异常上报
		exceptionOS: function(iErrCode, oMsg) {
			switch (this.OS) {
			case OS_TYPE.ANDROID:
				if (
				global[this.InternalName] && global[this.InternalName][INTER_NAME.onZWebException]) {
					if (typeof oMsg === "undefined") {
						global[this.InternalName][INTER_NAME.onZWebException](
						this.szFrameworkUUID, iErrCode, "");
					} else if (typeof oData === "object") {
						global[this.InternalName][INTER_NAME.onZWebException](
						this.szFrameworkUUID, iErrCode, JSON.stringify(oMsg));
					} else {
						global[this.InternalName][INTER_NAME.onZWebException](
						this.szFrameworkUUID, iErrCode, oMsg);
					}
				} else {
					this.print(
					INTER_NAME.onZWebException + " : " + this.szFrameworkUUID + "： Android 接口调用异常...");
				}
				break;

			case OS_TYPE.IOS:
				if (
				global.webkit && global.webkit.messageHandlers && global.webkit.messageHandlers[this.InternalName]) {
					var msg = {
						Method: INTER_NAME.onZWebException,
						FrameworkID: this.szFrameworkUUID,
						ErrorCode: iErrCode,
						Data: JSON.stringify(oMsg)
					};
					global.webkit.messageHandlers[this.InternalName].postMessage(msg);
				} else {
					this.print(
					INTER_NAME.onZWebException + " : " + this.szFrameworkUUID + "： IOS 接口调用异常...");
				}
				break;

			case OS_TYPE.WEB:
				this.print(
				INTER_NAME.onZWebException + " : " + this.szFrameworkUUID + "： Web 接口调用...");
				break;

			default:
				this.print("init callOS error, 回调失败。");
				break;
			}
		},

		/**
		 * Debug 打印
		 * @param type 日志类型 可为空
		 * @param msg
		 */
		print: function() {
			var type = "Debug";
			var msg = arguments[0];
			if (arguments.length === 2) {
				type = arguments[0];
				msg = arguments[1];
			}
			var oLog = {
				Type: type,
				Msg: msg
			};
			console.info(oLog);
			switch (this.OS) {
			case OS_TYPE.ANDROID:
				if (
				global[this.InternalName] && global[this.InternalName][INTER_NAME.onZWebLog]) {
					global[this.InternalName][INTER_NAME.onZWebLog](
					this.szFrameworkUUID, JSON.stringify(oLog));
				} else {
					console.error(
					INTER_NAME.onZWebLog + " : " + this.szFrameworkUUID + "： Android 接口调用异常...");
				}
				break;

			case OS_TYPE.IOS:
				if (
				global.webkit && global.webkit.messageHandlers && global.webkit.messageHandlers[this.InternalName]) {
					var msg = {
						Method: INTER_NAME.onZWebLog,
						FrameworkID: this.szFrameworkUUID,
						Data: JSON.stringify(oLog)
					};
					global.webkit.messageHandlers[this.InternalName].postMessage(msg);
				} else {
					console.error(
					INTER_NAME.onZWebException + " : " + this.szFrameworkUUID + "： IOS 接口调用异常...");
				}
				break;

			case OS_TYPE.WEB:
				console.error(
				INTER_NAME.onZWebLog + " : " + this.szFrameworkUUID + "： Web 接口调用...");
				break;

			default:
				console.error("Call print error...");
				break;
			}
		},

		// 注册监听
		on: function(type, func) {
			this.__onFuncCallBackMap__[type] = func;
		},

		// 分发回调
		dispatchCall: function(type, oData) {
			var func = this.__onFuncCallBackMap__[type];
			if (func && typeof func === "function") {
				func(oData);
			}
		}
	};

	var ZWeb = function() {
			this.szFrameworkUUID = 0;
			this.OS = OS_TYPE.WEB;
			this.Version = undefined;
			this.InternalName = undefined;
			this.ExposedName = undefined;
			this.ZWebSDK = undefined;
		};

	ZWeb.prototype = {
		// 初始化框架
		initFramework: function(szFrameworkUUID, oParams) {
			this.szFrameworkUUID = szFrameworkUUID;
			// 平台
			this.OS = oParams.OS;
			// 版本
			this.Version = oParams.Version;
			// 内部接口
			this.InternalName = oParams.InternalName;
			// 暴露接口
			this.ExposedName = oParams.ExposedName;
			// SDK
			global[LIB_NAME + "SDK"] = this.ZWebSDK = new ZWebSDK(
			szFrameworkUUID, oParams);

			function findDimensions() {
				//函数：获取尺寸
				var winWidth = 0;
				var winHeight = 0;
				//获取窗口宽度
				if (global.innerWidth) {
					winWidth = global.innerWidth;
				} else if (document.body && document.body.clientWidth) {
					winWidth = document.body.clientWidth;
				}
				//获取窗口高度
				if (global.innerHeight) {
					winHeight = global.innerHeight;
				} else if (document.body && document.body.clientHeight) {
					winHeight = document.body.clientHeight;
				}
				//通过深入Document内部对body进行检测，获取窗口大小
				if (
				document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
					winHeight = document.documentElement.clientHeight;
					winWidth = document.documentElement.clientWidth;
				}
				return {
					Width: winWidth,
					Height: winHeight
				};
			}

			this.ZWebSDK.callInterOS(INTER_NAME.onZWebCreated, findDimensions());

			// VuePlugin 关联操作
			var vuePlugin = undefined;
			if (global.Vue) {
				vuePlugin = global.Vue['__' + LIB_NAME + '_Dispatch_Ready__'];
			}
			if (!vuePlugin || ('function' !== typeof vuePlugin)) {
				vuePlugin = global['__' + LIB_NAME + '_Dispatch_Ready__'];
			}
			vuePlugin && ('function' === typeof vuePlugin) && vuePlugin();
		},

		// 请求回调
		requireCallback: function(szFrameworkUUID, oResultParam) {
			this.ZWebSDK.requireCallback(oResultParam);
		},

		// 消息回调
		messageCallback: function(szFrameworkUUID, oResultParam) {
			this.ZWebSDK.messageCallback(oResultParam);
		},

		//  数据操作回调
		databaseCallback: function(szFrameworkUUID, oResultParam) {
			this.ZWebSDK.databaseCallback(oResultParam);
		},

		callReceiver: function(szFrameworkUUID, szMethod, oData) {
			this.ZWebSDK.dispatchCall(szMethod, oData);
		},

		goBack: function(szFrameworkUUID) {
			this.ZWebSDK.print("goBack 被调用了...");
			global.history.go(-1);
		},

		goForward: function(szFrameworkUUID) {
			this.ZWebSDK.print("goForward 被调用了...");
			global.history.go(1);
		},

		refresh: function(szFrameworkUUID) {
			this.ZWebSDK.print("refresh 被调用了...");
			global.location.reload();
		}
	};

	ZWeb.author = _author;
	ZWeb.version = _version;

	return ZWeb;
});