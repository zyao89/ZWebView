(function() {
	document.getElementById("btn1").onclick = function() {

		var prom = ZWebSDK.message(
			"CMD",
			ZWebSDK
		);
		prom.then(
			function(data) {
				console.log("success..." + data);
				document.getElementById("result").innerHTML = data;
				ZWebSDK.tip("success...");
			},
			function(error) {
				console.log("error...");
				document.getElementById("result").innerHTML = error;
				ZWebSDK.tip("error...");
			}
		);
	};

	document.getElementById("btn2").onclick = function() {

		var prom = ZWebSDK.require(
			"https://zyao89.cn",
			"get",
			"HAHA",
			"Json"
		);
		prom.then(
			function(data) {
				console.log("success..." + data);
				document.getElementById("result").innerHTML = data;
				ZWebSDK.tip("success...");
			},
			function(error) {
				console.log("error...");
				document.getElementById("result").innerHTML = error;
				ZWebSDK.tip("error...");
			}
		);

//        ZWebSDK.saveData("**key**", "**value**");
	};

	document.getElementById("btn3").onclick = function() {
        // szMethodName, oData
        var szMethodName = document.getElementById("data").value;
        var oData = {"Msg":"这只是测试数据而已..."};
        document.getElementById("result").innerHTML = "Call: callOS ==>> MethodName: " + szMethodName + " ==>> Data: " + JSON.stringify(oData);
		ZWebSDK.callOS(szMethodName, oData);
	};

	document.getElementById("btn4").onclick = function() {
        var errorMsg = document.getElementById("data").value;
        document.getElementById("result").innerHTML = "Call: exceptionOS ==>> errCode: " + 1007 + " ==>> msg: " + errorMsg;
		ZWebSDK.exceptionOS(1007, errorMsg);
	};

	document.getElementById("btn5").onclick = function() {
        var key = document.getElementById("data").value;
        var value = "这是我存入的密码： 12345678";
        document.getElementById("result").innerHTML = "Call: saveData ==>> Key: " + key + " ==>> Value: " + value;
		ZWebSDK.saveData(key, value);
	};

	window.btnClick = function(method, flag){
	    var value = undefined;
	    if ('undefined' !== typeof flag) {
            value = (flag === true) ? document.getElementById("data").value : flag;
            document.getElementById("result").innerHTML = "Call: " + method + " ==>> Value: " + value;
	    } else {
            document.getElementById("result").innerHTML = "Call: " + method;
	    }
	    ZWebSDK[method](value);
	};

	// window.__ZWeb__.initFramework(0, {
	//   OS: "WEB",
	//   Version: "",
	//   InterName: "",
	//   ExposedName: ""
	// })

	__ZWeb__.init = function(szFrameworkUUID, oData) {
		console.log(JSON.stringify(oData));
		console.log("init： " + window.ZWebSDK);
	};
})();
