<!DOCTYPE html>
<html>
<head>
    <title>用户</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" v-cloak class="ms-index">
    <el-header class="ms-header" height="50px">
        <el-col :span="12">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="save()">新增</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"
                       :disabled="!selectionList.length">删除
            </el-button>
        </el-col>
    </el-header>

    <div class="ms-search" style="padding: 20px 10px 20px 10px;">
        <el-row>
            <el-form :model="form" ref="searchForm" label-width="80px" size="mini">
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="账号" prop="username">
                            <el-input v-model="form.dto.username" :disabled="false" :style="{width:  '100%'}" :clearable="true" placeholder="请输入用户账号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="真实姓名" prop="name">
                            <el-input v-model="form.dto.name"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入真实姓名">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="手机号码" prop="mobile">
                            <el-input v-model="form.dto.mobile"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入手机号码">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="form.dto.email"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入邮箱">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="注册时间" prop="peopleDateTimes">
                            <el-date-picker
                                    v-model="form.dto.peopleDateTimes"
                                    value-format="yyyy-MM-dd"
                                    type="daterange"
                                    :style="{width:  '100%'}"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24"  style="text-align:right;">
                        <el-button type="primary" icon="el-icon-search" size="mini" @click="loading=true;currentPage=1;list()">查询
                        </el-button>
                        <el-button @click="rest" icon="el-icon-refresh" size="mini">重置</el-button>
                    </el-col>
                </el-row>
            </el-form>
        </el-row>
    </div>
    <el-main class="ms-container">
        <el-table v-loading="loading" ref="multipleTable" height="calc(100vh-68px)" class="ms-table-pagination" border :data="treeList"
                  tooltip-effect="dark" @selection-change="handleSelectionChange">
            <template slot="empty">
                {{emptyText}}
            </template>
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column label="账号" align="left" prop="username">
            </el-table-column>
            <el-table-column label="真实姓名" align="left" prop="name">
            </el-table-column>
            <el-table-column label="邮箱"  width="200" align="center" prop="email">
            </el-table-column>
            <el-table-column label="手机号码" width="120" align="center" prop="mobile">
            </el-table-column>
            <el-table-column label="用户性别" align="left" prop="sex">
                <template slot-scope="scope">
                    {{scope.row.sex==0?'男':'女'}}
                </template>
            </el-table-column>
            <el-table-column label="用户状态" align="center" prop="status">
                <template slot-scope="scope">
                    {{scope.row.status==0?'正常':'禁用'}}
                </template>
            </el-table-column>
            <el-table-column label="创建时间" width="200" align="center" prop="gmtCreate" :formatter="dateFormat">
            </el-table-column>
            <el-table-column label="操作" width="180px" align="center">
                <template slot-scope="scope">
                    <el-link :underline="false" type="primary" @click="save(scope.row.id)">编辑</el-link>
                    <el-link :underline="false" type="primary" @click="del([scope.row])">删除</el-link>
                    <el-link :underline="false" type="primary" @click="updateUserState(scope.row)">充值密码</el-link>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
                background
                :page-sizes="[5, 10, 20]"
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
        data: {
            treeList: [],
            //用户列表
            selectionList: [],
            //用户列表选中
            loading: true,
            emptyText: '',
            total: 0,
            //总记录数量
            pageSize: 10,
            //页面数量
            currentPage: 1,
            //初始页
            mananger: ms.manager,
            //搜索表单
            form: {
                dto: {}
            },
            peopleDateTimes: null,
            puSexOptions: [{
                "value": 1,
                "label": "男"
            }, {
                "value": 2,
                "label": "女"
            }],
            peopleStateOptions: [{
                "value": 0,
                "label": "未审"
            }, {
                "value": 1,
                "label": "已审"
            }],
        },
        methods: {
            //查询列表
            list: function () {
                var that = this;
                var page = {
                    pageNo: that.currentPage,
                    pageSize: that.pageSize
                };
                var form = JSON.parse(JSON.stringify(that.form));

                for (key in form) {
                    if (!form[key]) {
                        delete form[key];
                    }
                }

                history.replaceState({
                    form: form,
                    page: page,
                    total: that.total
                }, "");

                if (that.peopleDateTimes) {
                    that.form.peopleDateTimes = that.peopleDateTimes[0] + '至' + that.peopleDateTimes[1];
                } else {
                    that.form.peopleDateTimes = null;
                }

                setTimeout(function () {
                    ms.http.post(ms.manager + "/user/page",Object.assign({}, that.form, page))
                        .then(function (data) {
                            debugger;
                            if(data.code === 0){
                                if (data.res.total <= 0) {
                                    that.loading = false;
                                    that.emptyText = '暂无数据';
                                    that.treeList = [];
                                } else {
                                    that.emptyText = '';
                                    that.loading = false;
                                    that.total = data.res.total;
                                    that.treeList = data.res.rows;
                                    // 设置头像的处理,此版本用户无头像不处理
                                    // that.treeList.forEach(function (item) {
                                    //     if (item.puIcon!=null && item.puIcon!="" &&
                                    //         JSON.parse(item.puIcon).length > 0) {
                                    //         item.puIcon = JSON.parse(item.puIcon)[0].path;
                                    //     } else {
                                    //         item.puIcon = null;
                                    //     }
                                    // });
                                }
                            }else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'error'
                                });
                            }
                    }).catch(function (err) {
                        console.log(err);
                    });
                }, 500);
            },
            //用户列表选中
            handleSelectionChange: function (val) {
                this.selectionList = val;
            },
            dateFormat: function (row, column, cellValue, index) {
                debugger
                if (cellValue) {
                    return ms.util.date.fmt(cellValue, 'yyyy-MM-dd');
                } else {
                    return '';
                }
            },
            //删除
            del: function (row) {
                var that = this;
                that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ms.http.post(ms.manager + "/people/peopleUser/delete.do", row.length ? row : [row], {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(function (data) {
                        if (data.result) {
                            that.$notify({
                                type: 'success',
                                message: '删除成功!'
                            }); //删除成功，刷新列表

                            that.list();
                        } else {
                            that.$notify({
                                title: '失败',
                                message: data.msg,
                                type: 'warning'
                            });
                        }
                    });
                }).catch(function () {
                    that.$notify({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            //更新用户审核状态
            updateUserState: function (row) {
                var that = this;
                that.$confirm('此操作将审核用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ms.http.post(ms.manager + "/people/updateState.do", row.length ? row : [row], {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }).then(function (data) {
                        if (data.result) {
                            that.$notify({
                                type: 'success',
                                message: '审核成功!'
                            }); //审核成功，刷新列表

                            that.list();
                        }
                    });
                }).catch(function () {
                    that.$notify({
                        type: 'info',
                        message: '已取消审核'
                    });
                });
            },
            //新增
            save: function (id) {
                if (id) {
                    location.href = this.mananger + "/people/peopleUser/form.do?peopleId=" + id;
                } else {
                    location.href = this.mananger + "/people/peopleUser/form.do";
                }
            },
            //pageSize改变时会触发
            sizeChange: function (pagesize) {
                this.loading = true;
                this.pageSize = pagesize;
                this.list();
            },
            //currentPage改变时会触发
            currentChange: function (currentPage) {
                this.loading = true;
                this.currentPage = currentPage;
                this.list();
            },
            //重置表单
            rest: function () {
                this.loading = true;
                this.$refs.searchForm.resetFields();
                this.peopleDateTimes = null;
                this.list();
            }
        },
        mounted: function () {
            if (history.hasOwnProperty("state") && history.state) {
                this.form = history.state.form;
                this.total = history.state.total;
                this.currentPage = history.state.page.pageNo;
                this.pageSize = history.state.page.pageSize;
            }

            this.list();
        }
    });
</script>
<style>
    #index .ms-search {
        padding: 20px 0;
    }
</style>
