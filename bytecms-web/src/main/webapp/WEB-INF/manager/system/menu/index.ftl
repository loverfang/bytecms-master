<!DOCTYPE html>
<html>
<head>
	<title>菜单</title>
	<meta charset="utf-8">
	<#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" v-cloak class="ms-index">
	<el-header class="ms-header" height="50px">
		<el-col :span="24">
			<el-button type="primary" icon="el-icon-plus" size="mini" @click="editMenu(0)">新增</el-button>
<#--			<el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"  :disabled="!selectionList.length">删除</el-button>-->
			<el-button icon="iconfont icon-daoru" size="mini" @click="dialogImportVisible=true" style="float: right">导入</el-button>
		</el-col>
	</el-header>

	<el-dialog title="导入菜单" :visible.sync="dialogImportVisible" width="600px" append-to-body v-cloak>
		<el-popover style="position: absolute;left: 16%;top: 5.6%;" placement="top-start" title="提示" trigger="hover" content="可通过代码生成器编辑菜单中复制菜单获取">
			<i class="el-icon-question" slot="reference"></i>
		</el-popover>
		<el-form>
			<el-form-item>
				<el-input :rows="10" type="textarea" v-model="menuJson"></el-input>
			</el-form-item>
		</el-form>
		<div slot="footer">
			<el-button size="mini" @click="dialogImportVisible = false">取 消</el-button>
			<el-button size="mini" :loading="saveDisabled" :disabled="menuJson==''" type="primary" @click="imputJson()">确 定</el-button>
		</div>
	</el-dialog>

	<el-main class="ms-container">
		<el-table ref="multipleTable" v-loading="loading"
				  height="calc(100vh - 68px)"
				  class="ms-table-pagination"
				  border :data="dataList"
				  tooltip-effect="dark"
<#--				  @selection-change="handleSelectionChange"-->
				  row-key="id"
				  :tree-props="{children: 'children'}">

			<template style="width:100%" slot="empty">
				{{emptyText}}
			</template>
<#--			<el-table-column type="selection"></el-table-column>-->

			<el-table-column label="菜单名称" align="left" prop="name" show-overflow-tooltip>
			</el-table-column>
			<el-table-column label="菜单图标" align="center" prop="icon">
				<template slot-scope="scope">
					<i style="font-size: 24px !important;" class="iconfont" :class="scope.row.icon"></i>
				</template>
			</el-table-column>
			<el-table-column label="菜单标识" align="left" prop="attributes.perms" show-overflow-tooltip>
			</el-table-column>
			<el-table-column label="API接口" align="left" prop="attributes.url" show-overflow-tooltip>
			</el-table-column>
			<el-table-column label="菜单类型" align="left" prop="attributes.type" show-overflow-tooltip>
				<template slot-scope="scope">
					{{scope.row.attributes.type=='0'?'目录':scope.row.attributes.type=='1'?'菜单':'按钮'}}
				</template>
			</el-table-column>
			<el-table-column label="菜单排序" align="right" prop="attributes.orderNum">
			</el-table-column>
			<el-table-column label="操作" align="center" width="150">
				<template slot-scope="scope">
					<el-button size="medium" type="text" @click="editMenu(scope.row.id)">编辑</el-button>
					<el-button size="medium" type="text" @click="del([scope.row])">删除</el-button>
					<el-button size="medium" type="text" @click="editMenu(scope.row.id)">增加</el-button>
				</template>
			</el-table-column>

		</el-table>
	</el-main>
</div>
<#include "/component/ms-icon.ftl">
<#include "/component/ms-tree-select.ftl">
<#include "/system/menu/form.ftl">
</body>

</html>
<script>
	var indexVue = new Vue({
		el: '#index',
		data: {
			dataList: [],
			//列表
			selectionList: [],
			//列表选中
			mananger: ms.manager,
			dialogImportVisible: false,
			menuJson: '',
			saveDisabled: false,
			loading: true,
			emptyText: ''
		},
		watch: {
			'dialogImportVisible': function (n, o) {
				if (!n) {
					this.menuJson = '';
				}
			}
		},
		methods: {
			//查询列表
			list: function () {
				var that = this;
				ms.http.get(ms.manager + "/menu/selectTreeList", {}).then(function (data) {
					if (data.res.length <= 0) {
						that.loading = false;
						that.emptyText = '暂无数据';
						that.dataList = [];
					} else {
						that.emptyText = '';
						that.loading = false;
                        // 表格赋值,弹出框赋值
						that.dataList = data.res.children;
						form.menuData = that.dataList;
					}
				}).catch(function (err) {
					console.log(err);
				});
			},
			//列表选中
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},

			//删除
			del: function (row) {
				var that = this;
				that.$confirm('删除选中菜单，如果有子菜单也会一并删除', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					debugger
					var ids = "";
					var markList = JSON.parse(localStorage.getItem("markList"));

					for (var i = 0; i < row.length; i++) {
						if (ids == "") {
							ids = row[i].id;
							var index = markList.findIndex(function (x) {
								return x.title == row[i].name
							});
							markList.splice(index, 1);
						} else {
							ids = ids + "," + row[i].id;
						}
					}

					localStorage.setItem("markList", JSON.stringify(markList))
					ms.http.del(ms.manager + "/menu/delete", {"id": ids}).then(function (data) {
						if (data.result) {
							that.$notify({ type: 'success', message: '删除成功!' });
							that.list();
						} else {
							that.$notify({ title: '失败', message: data.msg, type: 'warning' });
						}
					});
				}).catch(function () {
					that.$notify({ type: 'info', message: '已取消删除' });
				});
			},

			//新增或编辑
			editMenu: function (id) {
				form.open(id);
			},

			imputJson: function () {
				var that = this;
				this.saveDisabled = true
				ms.http.post(ms.manager + "/model/import.do", {
					menuStr: that.menuJson
				}).then(function (data) {
					if (data.result) {
						that.list();
						var model = JSON.parse(that.menuJson)[0];
						var markList = JSON.parse(localStorage.getItem("markList"));
						var menu = {
							title: model.modelTitle,
							icon: model.modelIcon,
						}
						markList.push(menu);
						localStorage.setItem("markList", JSON.stringify(markList))
						that.saveDisabled = false
						that.dialogImportVisible = false;
						that.$notify({
							title: '导入成功',
							message: "请刷新当前页面，查看菜单",
							type: 'success'
						});
					} else {
						that.$notify({
							title: '失败',
							message: data.msg,
							type: 'warning'
						});
						that.saveDisabled = false
					}
				}).catch(function (err) {
					that.saveDisabled = false
					console.log(err);
				});
			}
		},
		mounted: function () {
			this.list();
		}
	});
</script>
<style>
	#index .ms-search{
		background: #fff;
	}
	#index .iconfont{
		font-size: 12px !important;
		margin-right: 4px;
	}
	#index .ms-search .ms-search-footer{
		line-height: 60px;
		text-align: center;
	}
</style>