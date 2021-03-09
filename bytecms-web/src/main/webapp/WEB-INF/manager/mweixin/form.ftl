<!DOCTYPE html>
<html>
<head>
    <title>微信设置</title>
    <#include "./include/head-file.ftl">
    <link rel="stylesheet" href="//at.alicdn.com/t/font_1126286_yq3wbz7mmoq.css" />
    <style>
        [v-cloak] {
            display: none;
        }
       .ms-container{
           padding-bottom: 0;
           height: calc(100vh - 74px);
       }
        #appForm .iconfont{
         font-size: 12px;
         margin-right: 5px;
        }
	</style>
</head>
<body>
	<div id="appForm"  v-cloak>
		<el-header class="ms-header ms-tr" height="46px">
		 	<el-button type="primary"  size="mini" @click="save('form')"><i class="iconfont icon-icon-"></i>保存</el-button>
            <el-button  @click="resetForm('form')" size="mini"><i class="iconfont icon-zhongzhi"></i>重置</el-button>
            <el-button size="mini"  plain onclick="javascript:history.go(-1)"><i class="iconfont icon-fanhui"></i>返回</el-button>
		</el-header>
		<el-main class="ms-container">
			<el-form :model="form" :rules="rules" ref="form" label-width="150px" class="demo-ruleForm" size="mini">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="公众号名称:" prop="weixinName">
                            <el-input v-model="form.weixinName"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信号:" prop="weixinNo">
                            <el-input v-model="form.weixinNo"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信原始ID:" prop="weixinOriginId"><el-input v-model="form.weixinOriginId"></el-input></el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信类型:" prop="weixinType">
                            <el-select v-model="form.weixinType" placeholder="请选择微信类型">
                                <el-option label="服务号" key="0" :value="0"></el-option>
                                <el-option label="订阅号" key="1" :value="1"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信appId:" prop="weixinAppId"><el-input v-model="form.weixinAppId"></el-input></el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信appSecret:" prop="weixinAppSecret"><el-input v-model="form.weixinAppSecret"></el-input></el-form-item>
                    </el-col>
<#--                    <el-col :span="12">-->
<#--                        <el-form-item label="微信支付key:" prop="weixinPayKey"><el-input v-model="form.weixinPayKey"></el-input></el-form-item>-->
<#--                    </el-col>-->
<#--                    <el-col :span="12">-->
<#--                        <el-form-item label="微信支付mchid:" prop="weixinPayMchId"><el-input v-model="form.weixinPayMchId"></el-input></el-form-item>-->
<#--                    </el-col>-->
                    <el-col :span="12">
                        <el-form-item label="URL服务器:">
	                <span slot='label'>
						<el-popover placement="top-start" title="提示(只读)"  trigger="hover" content="微信公众号后台接入需要配置的地址  ">
							<i class="el-icon-question" slot="reference"></i>
						</el-popover>URL服务器:
					</span>
                            <el-input :disabled="false" v-model="location.origin+ms.base+'/mweixin/portal?weixinNo='+form.weixinNo"></el-input></el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="微信token:" prop="weixinToken"><el-input v-model="form.weixinToken"></el-input></el-form-item>
                    </el-col>
                </el-row>
            </el-form>
		</el-main>
	</div>
</body>

</html>
<script>
var formVue = new Vue({
	el: '#appForm',
	data: {
		form: {
			weixinId: 0, //微信编号
			weixinNo:"", //微信号
			weixinOriginId:"", //微信原始ID
			weixinName:"", //公众号名称
			weixinType:"", //微信类型 0服务号 1订阅号
			weixinAppId:"", //微信appId
			weixinAppSecret:"", //微信appSecret
			weixinToken:"", //微信token
			// weixinPayKey:"", //微信支付key
			// weixinPayMchId:"", //微信支付mchid
		},
		//表单验证
		rules: {
			weixinName: [
				{ required: true, message: '请输入公众号名称', trigger: 'blur' },
                { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
			],
			weixinNo: [
				{ required: true, message: '请输入微信号', trigger: 'blur' },
                { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
			],
			weixinOriginId: [
				{ required: true, message: '请输入微信原始ID', trigger: 'blur' },
                { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
			],
			weixinType: [
				{ required: true, message: '请选择一个微信类型', trigger: 'change' }
			],
			weixinAppId: [
				{ required: true, message: '请输入微信appId', trigger: 'blur' },
                { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
			],
			weixinAppSecret: [
				{ required: true, message: '请输入微信appSecret', trigger: 'blur' },
                { min: 1, max: 150, message: '长度在 1 到 150 个字符', trigger: 'blur' }
			],
			weixinToken: [
				{ required: true, message: '请输入微信token', trigger: 'blur' },
                { min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'blur' }
			],
			// weixinPayKey: [
			// 	{ required: false, message: '请输入微信支付key', trigger: 'blur' },
            //     { min: 1, max: 225, message: '长度在 1 到 225 个字符', trigger: 'blur' }
			// ],
			// weixinPayMchId: [
			// 	{ required: false, message: '请输入微信支付mchid', trigger: 'blur' },
            //     { min: 1, max: 225, message: '长度在 1 到 225 个字符', trigger: 'blur' }
			// ],
			
		}
	},
	methods: {
		save(formName){
			var that = this;
			var url =ms.manager +"/mweixin/save.do"
			if(that.weixinId > 0){
				url =ms.manager +"/mweixin/update.do";
			}
			this.$refs[formName].validate((valid) => {
				if (valid) {
					ms.http.post(url,this.form,{
						headers: {
	                		'Content-Type': 'application/x-www-form-urlencoded'
	                	}
					}).then(function(data){
						if(data){
							that.$notify({
				       			title: '成功',
				        		message: '保存成功',
				        		type: 'success'
		        			});
		            		location.href =ms.manager +"/mweixin/index.do"; 
						}else{
							that.$notify({
			       				title: '失败',
			        			message: data.msg,
			        			type: 'warning'
		        			});
						}
		            });	
				}else{
                    return false;
				}
			})
			
		},
	    //重置
        resetForm(formName) {
            this.$refs[formName].resetFields();
        },
        get(weixinId){
        	var that = this;
        	ms.http.get(ms.manager +"/mweixin/get.do", {"weixinId":weixinId}).then(function(data){
	            that.form = data.data;
            }).catch(function(err) {
                console.log(err);
            });
        },
	},
	mounted(){
		this.weixinId =  ms.util.getParameter("weixinId");
		if(this.weixinId > 0){
			this.get(this.weixinId);
		}
	}
})
</script>
