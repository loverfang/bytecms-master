<!-- 微信用户列表 -->
<div id="weixin-people" class="ms-weixin-content" v-if="weixinVue.menuActive == '微信用户'" v-cloak>
   <el-container>
      <!--右侧头部-->
      <el-header class="ms-header" height="50px">
         <el-row>
            <@shiro.hasPermission name="people:sync"><el-button :loading="loading" class="ms-fr" size="mini" icon="el-icon-upload" @click="synchronousPeople">一键同步</el-button></@shiro.hasPermission>
         </el-row>
      </el-header>
      <el-container>
         <!--内容头部-->
         <el-header class="ms-tr ms-header">
         <el-row type='flex' align='middle' gutter='20'  justify='end'  style="height: 38px;">
            <el-col :span='7'>
            	<el-input  placeholder="请输入用户昵称" v-model='weixinPeopleForm.puNickname' size='mini' clearable>
            	</el-input>
            </el-col>
            <el-col :span='3'>
               <el-button class="ms-fr" type="primary" icon="el-icon-search" size="mini" @click="currentChange(1)">查询</el-button>
            </el-col>

         </el-row>
            
         </el-header>
         <!--素材列表-->
         <el-main class="ms-container">
            <template>
               <el-table class="ms-table-pagination" :data="tableData"  border="true"  class="tableRowClass" row-class-name='people-tab-row' :max-height="tableHeight">
                  <el-table-column prop="weixinPeopleHeadimgUrl" label="用户头像" width="90">
                     <template  slot-scope="scope">
                        <img :src="scope.row.weixinPeopleHeadimgUrl" style="border-radius: 50px" min-width="35" height="35" />
                     </template>
                  </el-table-column>
                  <el-table-column prop="puNickname" label="用户昵称" align='left' >
                  </el-table-column>
                  <el-table-column prop="weixinPeopleProvince" label="用户所在地" align='left' width="200" >
                  </el-table-column>
                  <el-table-column prop="weixinPeopleOpenId" label="openId" align='left' width="320" >
                  </el-table-column>
                  <el-table-column
                          align="center"
                          label="操作"
                          width="100">
                     <template slot-scope="scope">
                        <el-button @click="handleClick(scope.row)" type="text" size="medium">发送消息</el-button>
                     </template>
                  </el-table-column>
               </el-table>
               <el-pagination background :page-sizes="[5, 10, 20]" layout="total, sizes, prev, pager, next, jumper" :current-page="currentPage"  :page-size="pageSize"  :total="total" class="ms-pagination" @current-change='currentChange' @size-change="sizeChange">
               </el-pagination>
            </template>
         </el-main>

      </el-container>
   </el-container>
</div>

<script>
   var weixinPeopleVue = new Vue({
      el: '#weixin-people',
      data: {
         tableData: [],
         weixinPeopleForm: {
				puNickname: '', //微信用户名称
				pageNo:0,
				pageSize:10,
				pmId:"",
		 },
		 total: 0, //总记录数量
         pageSize: 10, //页面数量
         currentPage:1, //初始页
         loading:false,//按钮状态
      },
      computed:{
         //表格最大高度 用来自适应
         tableHeight:function () {
            return document.documentElement.clientHeight - 210;
         }
      },
      methods: {
         // 获取关键词列表
         list: function() {
			var that = this;
				ms.http.get(ms.manager + "/mweixin/weixinPeople/list.do",that.weixinPeopleForm)
				.then((data)=>{
					if(data.data.rows.length > 0){
						that.tableData = data.data.rows;
						that.total = data.data.total;
					} else {
						that.tableData = [];
					}
				},(err) => {
					that.$notify.error(err);
				})
         },
         // 同步微信用户
         synchronousPeople: function() {
         	var that = this;
            that.loading = true;
            that.$notify.warning("正在同步，请不要刷新页面，预计需要1-5分钟");
         	ms.http.get(ms.manager + "/mweixin/weixinPeople/importPeople.do")
         	.then((data)=>{
         		if(data.result == true){
         			that.$notify.success("同步成功");
				} else {
					that.$notify.error("同步失败");
				}
            that.loading = false;
         	},(err) => {
				that.$notify.error(err);
               that.loading = false;
			})
         },
         handleClick: function(row) {
         	//这里暂时使用一键群发页面来发送
            weixinVue.menuActive = '单独发送'
            groupReply.openId = row.weixinPeopleOpenId;

         },
        //pageSize改变时会触发
        sizeChange:function(pageSize) {
            this.pageSize = pageSize;
            this.weixinPeopleForm.pageSize = pageSize;
            this.list();
        },
        //currentPage改变时会触发
        currentChange:function(currentPage) {
            this.currentPage = currentPage;
            this.weixinPeopleForm.pageNo = currentPage;
            this.list();
        },
      },
      mounted: function() {
      	this.list();
      }
   })
</script>
<style>
    #weixin-people .people-tab-row th,
    #weixin-people .people-tab-row td {
      padding: 8px 0;
      min-width: 0;
    }
    #weixin-people .ms-container{
       margin: 12px;
       padding: 0 10px 0 10px;
    }
    #weixin-people .ms-table-pagination {
       height: calc(100vh - 160px);
    }
</style>