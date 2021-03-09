<!DOCTYPE html>
<html>
<head>
	<title>小程序用户</title>
		<#include "../../include/head-file.ftl">
		<#include "../../include/increase-search.ftl">
</head>
<body>
	<div id="index" class="ms-index" v-cloak>
        <div class="ms-search">
            <el-row>
                <el-form :model="form"  ref="searchForm"  label-width="120px" size="mini">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item  label="昵称" prop="puNickname"
                            >
                                <el-input v-model="weixinAppPeopleForm.puNickname"
                                          :disabled="false"
                                          :style="{width:  '100%'}"
                                          :clearable="true"
                                          placeholder="请输入用户昵称">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="16" style="text-align: right;">
                            <el-button type="primary" icon="el-icon-search" size="mini" @click="form.sqlWhere=null;currentPage=1;list()">查询</el-button>
                            <el-button @click="rest"  icon="el-icon-refresh" size="mini">重置</el-button>
                        </el-col>
                    </el-row>
                </el-form>
            </el-row>
        </div>
		<el-main class="ms-container">
			<el-table height="calc(100vh - 68px)" v-loading="loading"  :data="dataList" border="true" >
				<template slot="empty">
					{{emptyText}}
				</template>
                <el-table-column prop="mpAvatarUrl" label="用户头像" width="80">
                    <template  slot-scope="scope">
                        <img :src="scope.row.mpAvatarUrl" style="border-radius: 50px" min-width="35" height="35" />
                    </template>
                </el-table-column>
                </el-table-column>
                <el-table-column label="用户昵称" width="100" align="right" prop="puNickname">
                </el-table-column>
                <el-table-column label="openId" align="left" prop="mpOpenId">
                </el-table-column>
                <el-table-column label="用户省份" align="left" prop="mpProvince">
                </el-table-column>
                <el-table-column label="用户城市" align="left" prop="mpCity">
                </el-table-column>

            </el-table>
            <el-pagination
					background
					:page-sizes="[10,20,30,40,50,100]"
					layout="total, sizes, prev, pager, next, jumper"
					:current-page="currentPage"
					:page-size="pageSize"
					:total="total"
					class="ms-pagination"
					@current-change='currentChange'
					@size-change="sizeChange">
            </el-pagination>
         </el-main>
	</div>
</body>

</html>
<script>
var indexVue = new Vue({
	el: '#index',
	data:{
        weixinAppPeopleForm: {
            puNickname: '', //微信小程序用户名称
            pageNo:0,
            pageSize:10,
            pmId:"",
        },
		dataList: [], //小程序用户列表
		selectionList:[],//小程序用户列表选中
		total: 0, //总记录数量
        pageSize: 10, //页面数量
        currentPage:1, //初始页
        manager: ms.manager,
		loadState:false,
		loading: true,//加载状态
		emptyText:'',//提示文字
		//搜索表单
		form:{
			sqlWhere:null,
		},
	},
	methods:{
	    //查询列表
	    list: function() {
	    	var that = this;
			that.loading = true;
			that.loadState = false;
			var page={
				pageNo: that.currentPage,
				pageSize : that.pageSize
			}

			ms.http.post(ms.manager+"/miniapp/miniPeople/list.do",that.weixinAppPeopleForm)
                .then((data)=>{
                    if(that.loadState){
                        that.loading = false;
                    }else {
                        that.loadState = true
                    }
                    if(data.data.rows.length > 0){
                        that.dataList = data.data.rows;
                        that.total = data.data.total;
                        that.loading = false;
                    } else {
                        that.dataList = [];
                        that.emptyText = '暂无数据'
                        that.total = 0;
                    }
                },(err) => {
                    that.$notify.error(err);
			});
			setTimeout(()=>{
				if(that.loadState){
					that.loading = false;
				}else {
					that.loadState = true
				}
			}, 500);
			},
		//小程序用户列表选中
		handleSelectionChange:function(val){
			this.selectionList = val;
		},
		//删除
        del: function(row){
        	var that = this;
        	that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					    	confirmButtonText: '确定',
					    	cancelButtonText: '取消',
					    	type: 'warning'
					    }).then(() => {
					    	ms.http.post(ms.manager+"/miniapp/miniPeople/delete.do", row.length?row:[row],{
            					headers: {
                					'Content-Type': 'application/json'
                				}
            				}).then(
	            				function(res){
		            				if (res.result) {
										that.$notify({
						     				type: 'success',
						        			message: '删除成功!'
						    			});
					    				//删除成功，刷新列表
					      				that.list();
					      			}else {
										that.$notify({
											title: '失败',
											message: data.msg,
											type: 'warning'
										});
									}
	            				});
					    })
        		},
		//新增
        save:function(id){
			if(id){
				location.href=this.manager+"/miniapp/miniPeople/form.do?id="+id;
			}else {
				location.href=this.manager+"/miniapp/miniPeople/form.do";
			}
        },
        //表格数据转换
        //pageSize改变时会触发
        sizeChange:function(pagesize) {
			this.loading = true;
            this.weixinAppPeopleForm.pageSize = pagesize;
            this.list();
        },
        //currentPage改变时会触发
        currentChange:function(currentPage) {
			this.loading = true;
			this.weixinAppPeopleForm.currentPage = currentPage;
            this.list();
        },
		search(data){
        	this.form.sqlWhere = JSON.stringify(data);
        	this.list();
		},
		//重置表单
		rest(){
	        this.weixinAppPeopleForm.puNickname='';
			this.weixinAppPeopleForm.currentPage = 1;
			this.form.sqlWhere = null;
			this.$refs.searchForm.resetFields();
			this.list();
		},
	},
created(){
	if(history.state){
		this.form = history.state.form;
		this.currentPage = history.state.page.pageNo;
		this.pageSize = history.state.page.pageSize;
	}
		this.list();
	},
})
</script>
<style>
	#index .ms-container {
		height: calc(100vh - 78px);
	}
</style>