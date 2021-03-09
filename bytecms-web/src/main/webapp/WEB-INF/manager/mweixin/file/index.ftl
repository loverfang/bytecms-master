<!-- 素材库 -->
<div id="file-img"  v-cloak>
    <el-dialog title="上传图片" :visible.sync="isShow" custom-class='ms-weixin-dialog border-radius-five'>
	    <el-container>
	  		<el-aside width="160px">
					<ul class="pictrue-group">
					<li @click="currentPage = 1;categoryId = ''; picList()" label="all" :class="{active:categoryId === ''}">
								<span>全部</span>
							</li>
						<template v-for="(group,index) in picGroup" >
							<li @click="currentPage = 1;categoryId = group.category.categoryId; picList()" :label="group.category.categoryTitle + '('+group.total+')'"  :key="index" :class="{active:categoryId === group.category.categoryId}">
								<span>{{group.category.categoryTitle+'('+group.total+')'}}</span>
							</li>
						</template>
					</ul>
			</el-aside>
	  		<el-main>
	  		<el-scrollbar style="height:100%;">
	  			<el-row class='showImg' height="88%">
		  			  <template v-for="(pic,index) in pictureList">
		  				<el-col :span="5" class='border-radius-five'  v-if="categoryId == ''||categoryId == pic.categoryId">
		  					<el-card @click.native="chooseImg = (index+1)">
			  					<img class="border-radius-five" :src="ms.base+pic.fileUrl" alt=""  />
			  					<div class="img-title">
			  						<span>{{pic.fileName}}</span>
			  					</div>
			  				</el-card>
			  				<div class="clicked-background" v-if="chooseImg == (index+1)">
                    			<i class="iconfont icon-icon"></i>
                    		</div>
		  				</el-col>
		  			  </template>
		  		 <el-col :span="5" class='border-radius-five'>
		  		 	<el-upload
							class="avatar-uploader"
							:action="ms.web + '/file/upload.do'"
							:data="{uploadFloderPath:'/upload/${weixinId}/weixin/picture'}"
							:show-file-list="false"
							accept=".bpm,.png,.jpeg,.gif,.jpg"
							:on-success="handleSuccess"
							:before-upload="beforePicUpload">
		            <i class="el-icon-plus avatar-uploader-icon"></i>
        			</el-upload>
        		</el-col>
	  			</el-row>
	  		 </el-scrollbar>
	  		 <div class="picture-page">
				  <el-pagination
			      :current-pag="currentPage"
			      :page-size="pageSize"
			      layout="prev, pager, next, jumper"
				  @current-change="currentChange"
			      :total="total" background>
			      </el-pagination>
	  			</div>
	  		</el-main>
	</el-container>
	<div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="getChooseData" size='mini'>确 定</el-button>
            <el-button @click="isShow = false" size='mini'>取 消</el-button>
        </div>
    </el-dialog>
</div>
<script>
    var fileImgVue = new Vue({
        el: '#file-img',
        data: {
        	checked:false,
            isShow: false,
            materialGroup: [],
            flieName:"",//放大图片的文件名
            total: 0, //总记录数量
         	pageSize: 10, //页面数量
         	currentPage:1, //初始页
			picGroup:[],
         	categoryId:'', //当前选中分组id
			pictureList:[],   //分组中的图片
			chooseImg:0,   //选中的图片
			fileForm: {
				fileName:"",
				fileUrl:"",
				categoryId:"",
				fileSize:"",
				fileType:"",
			}
        },
        watch:{
			isShow:function (n,o) {
				if(!n){
					//取消选中
					this.chooseImg = 0
				}
			}
        },
        methods: {
            // 表单打开
            open: function () {
                this.isShow = true;
				this.categoryList();
				this.picList();
            },
			// 图片刚加载完
			beforePicUpload(file) {
				this.fileForm.fileName = file.name;
				this.fileForm.fileSize = file.size;
				this.fileForm.fileType="image";
			},
			handleSuccess(url) {
				this.fileForm.fileUrl = url
				var that = this;
				this.fileForm.categoryId = this.categoryId;
				ms.http.post(ms.manager + "/mweixin/file/save.do",that.fileForm).then(function (res) {
					//that.isShow = false
					that.picList();
					that.categoryList();
					that.$notify({
						type:'success',
						message:'上传成功'
					});
				}, function (err) {
					that.$notify.error(err);
				})


            },
            getChooseData:function(){		//获取选择的图片并渲染
            	this.isShow = false;
            	var chooseImg = this.pictureList[this.chooseImg-1];
				switch (weixinVue.menuActive) {
					case "关键词表单":
						passiveMessageFormVue.chooseImg = chooseImg;
						break;
					case "被动回复"://不写break会到下一个
					case "关注时回复":
						messageVue.chooseImg = chooseImg;
						break;
					case "自定义菜单":
						menuVue.chooseImg = chooseImg;
						break;
					case "单独发送":
					case "一键群发":
						groupReply.chooseImg = chooseImg;
						break;

				}
            },
			categoryList: function () {
				var that = this;
				ms.http.get(ms.manager + "/mweixin/file/categoryFile.do").then(function (res) {
					that.picGroup = res.data;
				}, function (err) {
					that.$notify.error(err);
				})
			},
	        // 图片列表
			picList: function () {
				var that = this;
				ms.http.get(ms.manager + "/mweixin/file/list.do", {pageNo:this.currentPage,
					pageSize:this.pageSize,
					categoryId : this.categoryId,
					fileType:"image",
				}).then(function (res) {
					that.pictureList = res.data.rows;
					that.total = res.data.total;
				}, function (err) {
					that.$notify.error(err);
				})
			},
	         //pageSize改变时会触发
        	sizeChange:function(pageSize) {
            	this.pageSize = pageSize;
            	this.picList();
        	},
        	//currentPage改变时会触发
        	currentChange:function(currentPage) {
            	this.currentPage = currentPage;
            	this.picList();
        	},
        },
        mounted: function() {
          var that = this;

       }
    })
</script>
<style>

    /* 素材库选择图片 */
    #file-img .active{	/* 菜单栏选中的样式 */
        background:#ecf5ff;
    }

    #file-img .border-radius-five{	/*5px圆角*/
        border-radius:5px;
    }

    #file-img .ms-weixin-dialog.border-radius-five{
        width:845px;
        height:570px;
    }
    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__header{
        padding:15px 10px 5px 10px;
    }
    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__body{
        height:457px;
        padding:0px 0px;
    }
    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__body .el-container{
        height:100%;
    }

    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__body .el-aside{
        height:100%;
        border-left:1px solid #e6e6e6;
        padding:10px 5px;
    }

    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__body .el-aside .pictrue-group li{
        justify-content: center;
    }

    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__body .el-main{
        height:88%;
        padding:0px;
    }
    #file-img .ms-weixin-dialog.border-radius-five .el-dialog__body .el-main .picture-page{
        position: absolute;
        bottom: 67px;
        right: 10px;
    }
    #file-img .showImg.el-row .el-col{
        margin:10px;
        position:relative;
    }

    #file-img .showImg.el-row .el-card .el-card__body{
        height:168px;
        padding:10px;
        display: flex;
        flex-flow: column nowrap;
        justify-content: center;
        align-items: center;
    }
    #file-img .showImg.el-row .el-card .el-card__body img{
        height:120px;
        width:120px;
    }
    #file-img .showImg.el-row .el-card .el-card__body .img-title{
		width: 150px;
        height:28px;
        padding: 8px;
        overflow: hidden;
        text-overflow: ellipsis;
		white-space: nowrap;
    }
    /* 素材库选择图片 End*/
    /* 上传图片框 */
    #file-img .avatar-uploader{
        display:inline;
    }
    #file-img .avatar-uploader .el-upload {
        border: 1px solid #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;


    }
    #file-img .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    #file-img .avatar-uploader-icon {
        font-size: 60px;
        color: rgb(203, 203, 203);
        width: 148px;
        height: 168px;
        line-height: 178px !important;
        text-align: center;
    }
	#file-img .clicked-background{
		background:rgba(127, 127, 127,0.7);
		position: absolute;
		top: 0px;
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 9px;
	}
	#file-img  .clicked-background i{
		font-size: 52px;
		color: white;
		position: absolute;
		bottom: 20%;
	}
</style>