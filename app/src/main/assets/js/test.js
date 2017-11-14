(function() {
	document.getElementById("print").onclick = function() {
		console.log("click" + window.ZWebSDK);

//		var prom = window.ZWebSDK.require(
//			"https://zyao89.cn",
//			"get",
//			"HAHA",
//			"Json"
//		);
//		prom.then(
//			function(data) {
//				console.log("success..." + data);
//				window.ZWebSDK.print('print', data);
//				ZWebSDK.tip("success...");
//			},
//			function(error) {
//				console.log("error...");
//				ZWebSDK.tip("error...");
//			}
//		);

		var prom = window.ZWebSDK.message(
			"CMD",
			"get"
		);
		prom.then(
			function(data) {
				console.log("success..." + data);
				window.ZWebSDK.print('print', data);
				ZWebSDK.tip("success...");
			},
			function(error) {
				console.log("error...");
				ZWebSDK.tip("error...");
			}
		);
	};

	// window.__ZWeb__.initFramework(0, {
	//   OS: "WEB",
	//   Version: "",
	//   InterName: "",
	//   ExposedName: ""
	// })

	__ZWeb__.init = function(szFrameworkUUID, oData) {
		console.log(JSON.stringify(oData));
		ZWebSDK.exceptionOS(1007, "密码错误");
		ZWebSDK.message("未知消息提示。。。");
		ZWebSDK.message(oData);
	};
})();
