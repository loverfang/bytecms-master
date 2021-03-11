/**
 * 基础变量定义
 */
(function(window) {
	var ms = {
		base: "http://localhost:8081", //主机地址
		login:"/oauth/login", //登录页面
		debug:true, //测试模式
		log:function(msg) {
			console.log(msg);
		}
	}
	window.ms = ms;
})(window);