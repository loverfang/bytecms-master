<!-- 自定义菜单 -->
<div id="ms-menu" class="ms-weixin-content" v-if="weixinVue.menuActive == '自定义菜单'" v-cloak>
	<el-container class="ms-custom-container">
		<el-header class="ms-header" height="50px">
			<el-row>
				<@shiro.hasPermission name="weixin:menu:sync"><el-button type="primary"  class="ms-fr" size="mini" @click='menuCreate'><i style="margin-right: 5px" class="iconfont icon-14fabu"></i>发布菜单</el-button></@shiro.hasPermission>
				<@shiro.hasPermission name="weixin:menu:save"><el-button type="primary" class="ms-fr" size="mini"  @click='menuSave'><i class="iconfont icon-icon-"></i>保存</el-button></@shiro.hasPermission>
			</el-row>
		</el-header>
		<el-container class="ms-container">
			<el-aside>
				<el-container>
					<el-header>公众号</el-header>
					<el-main></el-main>
					<el-footer>
						<el-button icon="iconfont icon-dakaijianpan"></el-button>
						<div class="ms-create-menu">
							<draggable v-model="allMenuList" :options="{draggable:'.ms-create-sub-menu'}" @start="drag=true" @end="dragEnd(allMenuList)">
								<div class="ms-create-sub-menu" v-for="(menu,index) of allMenuList" :key="index" draggable="true">
									<!-- 父菜单 -->
									<!-- 拖拽模块 -->
                                        <el-button class="list-group-item" type="primary" @click="openSubMenu(index,menu)"  v-on:mouseenter.native="visible(menu)" @mouseleave.native="invisible(menu)"
                                                   draggable="true">{{menu.menuTitle }}
                                            <div @click.stop="menuDel(menu)" v-show="menu.seen" class="el-badge__content el-badge__content--undefined ms-menu-del" ><i class='el-icon-close'></i></div>
                                        </el-button>
									<!-- 修改全局影响子菜单列表问题 添加sub-menu-list-nopadding类名-->
									<div class="sub-menu-list sub-menu-list-nopadding" v-show="menu.addSubMenuShow">
										<draggable v-model="menu.subMenuList" :options="{draggable:'.sub-menu-item'}" @start="drag=true" @end="dragEnd(menu.subMenuList)">
											<!-- 子菜单 -->
												<el-button v-for="(sub,index) of menu.subMenuList" :key="index" class="sub-menu-item" v-on:mouseenter.native="visible(sub)" @mouseleave.native="invisible(sub)"
                                                           @click='clickSubMenu(sub)' draggable="true">
													{{sub.menuTitle}}
                                                    <div v-show="sub.seen" @click.stop="menuDel(menu,sub)" id="sub-menu-del" class="el-badge__content el-badge__content--undefined ms-menu-del"><i class='el-icon-close'></i></div>
												</el-button>
										</draggable>
										<!-- 添加子菜单的加号按钮 -->
										<el-button class="son-button" icon="el-icon-plus" class="ms-create-btn" @click="addSubMenu(menu.subMenuList)"></el-button>
									</div>
								</div>
								<!-- 添加父菜单的加号按钮 -->
								<el-button icon="el-icon-plus" @click="addMenu" v-show="allMenuList.length<3" class="add-menu"></el-button>
						</draggable>
                        </div>
					</el-footer>
				</el-container>
			</el-aside>
			<el-main class='ms-relative' v-if="">
				<el-card class="menu-card" shadow="never">
					<div slot="header" class="clearfix">
						<span v-text="menuForm.menuId ? '修改菜单' : '新建菜单'"></span>
					</div>
					<el-form ref="menuForm" :rules="menuFormRules" :model="menuForm" label-width="100px" size="mini">
						<el-form-item label="菜单名称" prop="menuTitle" class="ms-menu-name">
							<el-input v-model="menuForm.menuTitle" :disabled="!menuForm.menuStatus"></el-input>
							<span class="menu-validate-font">菜单名称字数不多于5个汉字或10个字母</span>
						</el-form-item>
						<el-form-item label="菜单内容" class="ms-menu-content">
							<el-radio-group v-model="menuForm.menuType" @change="radioChange"  :disabled="!menuForm.menuStatus">
								<el-radio :label="1">发送消息</el-radio>
<#--								<el-radio :label="2">扫码</el-radio>-->
<#--								<el-radio :label="3">扫码（等待消息）</el-radio>-->
								<el-radio :label="0">跳转网页</el-radio>
<#--								<el-radio :label="5">地理位置</el-radio>-->
<#--								<el-radio :label="6">拍照发图</el-radio>-->
<#--								<el-radio :label="7">拍照相册</el-radio>-->
<#--								<el-radio :label="8">相册发图</el-radio>-->
<#--								<el-radio :label="9">关联小程序</el-radio>-->
							</el-radio-group>
						</el-form-item>
						<el-form-item v-show="menuForm.menuType == 0" label="链接" class="ms-menu-content" prop='menuUrl'>
							<el-input v-model="menuForm.menuUrl" type="textarea" placeholder="请输入菜单地址" rows="6" :disabled="!menuForm.menuStatus"></el-input>
						</el-form-item>
						<el-form-item  v-show="menuForm.menuType == 1" label="发送消息" class="ms-menu-content" >
							<ms-message-reply
									:content="messageContent"
									:img-File-Name="imgFileName"
									:img-Content="imgContent" :choose-Graphic="chooseGraphic"
									:voice-Content="voiceContent" :video-Content="videoContent"
									:active-Name="activeName" @editor-change="onEditorChange($event)"
									@tab-click="activeName = $event"
									@img-del="deleteContent('image')"
									@voice-del="deleteContent('voice')" @video-del="deleteContent('video')"
									@out-graphic="deleteContent('imageText')"
									@clean-content="deleteContent('text')">
							</ms-message-reply>
						</el-form-item>

					</el-form>
				</el-card>
			</el-main>
		</el-container>
	</el-container>
</div>
<script>
	var menuVue = new Vue({
		el: "#ms-menu",
		data: {
			menuForm: {},//表单
			//所有菜单list
			allMenuList:[],
			activeName:'text',//tab激活菜单
			messageContent:'',//文本内容
			imgContent:'',//图片内容
			imgFileName:'',//图片名称
			voiceContent:'',//音频
			videoContent:'',//视频
			chooseGraphic:{},//图文内容
			chooseImg:{},//选择的图片
			chooseVoice:'',//选择的音频
			chooseVideo:'',//选择的视频
			//用来初始化菜单
			defaultMenu:{
				menuTitle: "新建菜单",
				menuUrl:"",
				subMenuList:[],
				menuStatus:1,
				menuStyle:'link',//类型：text文本 image图片 imageText图文 link外链接
				menuType:0,//菜单属性 0:链接 1:回复
				menuWeixinId: ${weixinId},
                seen:false, //删除按钮可见
			},
			// subArticleList:[],
			menuFormRules: {
				menuTitle: [{
						required: true,
						message: "请输入菜单名称",
						trigger: ["blur", "change"]
					},
					{
						validator: function (rule, value, callback) {
							//获取文本长度
							var length = 0;
							for (var i=0; i<value.length; i++) {
								if (value.charCodeAt(i)>127 || value.charCodeAt(i)==94) {
									length += 2;
								} else {
									length ++;
								}
							}
							if(length>10){
								callback('菜单名称最多5个汉字或者10个字母');
							} else {
								callback();
							}
						}
					}
				],
				menuUrl: [{
					required: true,
					message: '请输入菜单地址',
					trigger: 'change'
				}, {
					validator: function (rule, value, callback) {
						/^(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?$/.test(value) ?
							callback() : callback('链接不合法')
					}
				}],
				menuContent: [{
					required: true,
					message: '内容不能为空',
					trigger: 'change'
				}]
			},
		},
		watch:{
			chooseGraphic:function (news,ol) {
				if(!news.newsId)return;
				this.menuForm.menuContent = news.newsId;
				this.menuForm.menuStyle = 'imageText'
			},
			chooseImg:function (img,ol) {
				if(!img.id){
					this.imgContent = "";
					return;
				}
				this.imgFileName = img.fileName;
				this.imgContent = ms.base+img.fileUrl;
				this.menuForm.menuContent = img.id;
				this.menuForm.menuStyle = 'image'
			},
			chooseVoice:function (voice,ol) {
				if(!voice.fileName){
					this.voiceContent = "";
					return;
				}
				this.menuForm.menuStyle = 'voice'
				this.voiceContent = voice.fileName;
				this.menuForm.menuContent = voice.id;
			},
			chooseVideo:function (video,ol) {
				if(!video.fileName){
					this.videoContent = "";
					return;
				}
				this.menuForm.menuStyle = 'video'
				this.videoContent = video.fileName;
				this.menuForm.menuContent = video.id;
			}
		},
		methods: {
			menuList: function () {
				var that = this;
				ms.http.get(ms.manager + "/mweixin/menu/list.do")
					.then((res) => {
						that.allMenuList = []
						if(res.data.rows){
							res.data.rows.forEach(function (item, index) {
								//父菜单没有menuMenuId
								if(!item.menuMenuId){
									item.subMenuList=[];
                                    item.seen = false;
                                    that.allMenuList.push(item);
									//添加子菜单
									res.data.rows.forEach(function (val) {
										if(val.menuMenuId && val.menuMenuId ==item.menuId){
                                            val.seen = false;
                                            //判断是否是子菜单
                                            val.isSubMenu = true;
											item.subMenuList.push(val)
										}
									})
								}
							})
							// 初始化显示第一个菜单
							if (that.allMenuList.length) {
								that.menuForm = that.allMenuList[0]
								that.getData();
							}
						}
						//父菜单排序
						this.allMenuList.sort(function (a, b) {
							return a.menuSort-b.menuSort;
						})

					}, (err) => {
					})
			},
			onEditorChange: function(quill){
				//获取富文本内容并去除p标签
				this.messageContent = quill.html;
				//文本类型
				//如果富文本获取了焦点代表在编辑则可以改变类型
				if(quill.quill.hasFocus()) {
					this.menuForm.menuStyle = 'text'
					var p = document.createElement("p");
					p.innerHTML = this.messageContent;
					this.menuForm.menuContent = p.querySelector('p').innerHTML
				}
			},
			// 菜单排序
			menuSort: function () {
				ms.http.post(ms.manager + "/mweixin/menu/menuSort.do", this.allMenuList,
						{
							headers: {
								'Content-Type': 'application/json'
							}
						})
			},

			// 添加菜单
			addMenu: function () {
				var that = this;
				this.messageContent=""
				that.$refs.menuForm.validate(function (isPass, object,c) {
					//!menuForm.menuStatus表示输入框被禁用也就是没有菜单的时候是可以直接添加的
							if(isPass ||!that.menuForm.menuStatus||!(that.menuForm.menuType == 0)){
								//默认菜单模板拷贝
								var menu = JSON.parse(JSON.stringify(that.defaultMenu));
								that.allMenuList.push(menu);
								that.menuForm = menu;
								that.$nextTick(function () {
									Array.prototype.forEach.call(
											document.querySelectorAll(".ms-create-sub-menu"),
											function (item, index) {
												item.style.width = '80px';
											}
									);
									document.querySelector(".add-menu").style.width = '80px';
								});
							}
				})

			},
			//菜单类型改变
			radioChange:function(val){
				switch (val) {
					case 0:
						this.menuForm.menuStyle = 'link';
						break;
					case 1:
						if(this.menuForm.menuType == 1&&this.menuForm.menuStyle=='link'){
								this.menuForm.menuStyle = 'text';
								this.activeName = this.menuForm.menuStyle;
							}
						break;
				}
			},
			//拖拽事件结束
			dragEnd:function(menu){
				//拖动位置即保存一次
				menu.forEach(function (value, index, array) {
					value.menuSort = index + 1;
				})
				this.menuSort();
			},
			// 添加子菜单
			addSubMenu: function (subMenuList) {
				var that = this;
				this.messageContent = this.messageContent.replace(/<[^>]+>/g,"").trim();
				this.$refs.menuForm.validate(function (isPass, object) {
					if(isPass||!(that.menuForm.menuType == 0)){
						if ((that.messageContent == '' && Object.keys(that.chooseGraphic).length ==0  && Object.keys(that.chooseImg).length ==0  &&
								Object.keys(that.chooseVoice).length ==0  && Object.keys(that.chooseVideo).length ==0 ) && that.menuForm.menuType == 1){
							return that.$notify.error("发送消息不能为空");
						}
                        that.chooseGraphic = {};
						that.chooseImg = {};
						that.chooseVoice = {};
						that.chooseVideo = {};
		                that.messageContent = '';
						if (subMenuList.length > 4) {
							return that.$notify.error("子菜单最多5项");
						}
						//默认菜单模板拷贝
						var menu = JSON.parse(JSON.stringify(that.defaultMenu));
						menu.menuTitle = "新建子菜单";
						menu.isSubMenu = true;
						subMenuList.push(menu);
						that.menuForm = menu;
					}
				})
			},
			//点击子菜单
			clickSubMenu:function(menu){
				this.menuForm = menu;
				this.$refs.menuForm.clearValidate();
				this.getData();
			},
			getData(){
				var that = this;
				that.chooseGraphic = {};
				that.chooseImg = {};
				that.chooseVoice = {};
				that.chooseVideo = {};
                that.messageContent = '';
				//根据类型tab切换
				if (that.menuForm.menuType == 1 && that.menuForm.menuContent) {
					if (that.menuForm.menuStyle){
						that.activeName = that.menuForm.menuStyle;
					}
					switch (that.menuForm.menuStyle) {
						case 'text':
							that.messageContent = that.menuForm.menuContent;
							break;
						case 'image':
							//获取素材实体
							ms.http.get(ms.manager + "/mweixin/file/get.do", {fileId: that.menuForm.menuContent})
									.then(function (data) {
												that.imgContent = ms.base + data.data.fileUrl;
												that.imgFileName =  data.data.fileName;
											}, function (err) {
												that.$notify.error(err);
											}
									)
							break;
						case 'imageText':
							ms.http.get(ms.manager + "/mweixin/news/" + that.menuForm.menuContent + "/get.do")
									.then(function (data) {
												that.chooseGraphic = data.data
											}, function (err) {
												that.$notify.error(err);
											}
									)
							break
						case 'voice'://音频类型
							//获取素材实体
							ms.http.get(ms.manager + "/mweixin/file/get.do",{
								fileId:that.menuForm.menuContent,
							}).then(function (data) {
										that.chooseVoice=data.data
										that.voiceContent = data.fileName;
									}, function (err) {
										that.$notify.error(err);
									}
							)
							break;
						case 'video'://视频类型
							//获取素材实体
							ms.http.get(ms.manager + "/mweixin/file/get.do",{
								fileId:that.menuForm.menuContent,
							}).then(function (data) {
										that.chooseVideo=data.data
										that.videoContent = data.fileName;
									}, function (err) {
										that.$notify.error(err);
									}
							)
							break;
					}
				}
			},
			//删除回复内容提示
			deleteContent: function (type) {
				var that = this;
				this.$confirm('您正在执行清除内容的操作，是否继续？', '提示!', {
					confirmButtomText: '确定',
					cancelButtomText: '删除',
					type: 'warning'
				}).then(function () {
					switch (type) {
						case "imageText"://图文
							that.newsContent = "";
							that.chooseGraphic = {};
							break;
						case "image"://图片
							that.imgContent = "";
							break;
						case "text"://文本
							that.messageContent = "";
							break;
						case "voice"://音频
							that.voiceContent = "";
							break;
						case "video"://视频
							that.videoContent = "";
							break;
					}
					that.menuForm.menuContent='';
					that.$notify.success("已经成功清除了该内容");
				})
			},
			openSubMenu: function (index, menu) {
				this.messageContent=menu.menuContent
				var that = this;
				this.$refs.menuForm.clearValidate();
				this.$refs.menuForm.validate(function (isPass, object) {
					if(isPass || that.menuForm == menu||!(that.menuForm.menuType == 0)){
						//判断是否是子菜单
						that.menuForm = menu
						that.getData();
						if(!menu.isSubMenu){
							that.closeAllSubMenu(index);
							//排序
							menu.subMenuList.sort(function (a, b) {
								return a.menuSort-b.menuSort;
							})
							menu.addSubMenuShow = !menu.addSubMenuShow;
						}
					}
				})
			},
			// 关闭所有的子菜单弹出层
			closeAllSubMenu: function (num) {
				// 确保当前的菜单不被重置成false
				this.allMenuList.forEach(function (item, index) {
					num != index && (item.addSubMenuShow = false)
				})
			},
			// 保存菜单
			menuSave: function () {

				// 表单校验
				var that = this;
				//设置排序
				that.allMenuList.forEach(function (value, index, array) {
					value.menuSort = index + 1;
					value.subMenuList.forEach(function (value, index, array) {
						value.menuSort = index + 1;
					})
				})

				for(var i in that.allMenuList){
					if (!that.allMenuList[i].subMenuList.length) {
						if (that.allMenuList[i].menuType==1) {
							if (!that.allMenuList[i].menuContent){
								that.$notify.error('菜单内容不能为空')
								return;
							}
						}else{
							if (!that.allMenuList[i].menuUrl){
								that.$notify.error('菜单内容不能为空')
								return;
							}
						}
					}else {
						for (var p in that.allMenuList[i].subMenuList){
							if (that.allMenuList[i].subMenuList[p].menuType) {
								if (!that.allMenuList[i].subMenuList[p].menuContent){
									that.$notify.error('子菜单内容不能为空')
									return;
								}
							}else{
								if (!that.allMenuList[i].subMenuList[p].menuUrl){
									that.$notify.error('子菜单内容不能为空')
									return;
								}
							}
						}
					}
				}
				ms.http.post(ms.manager + "/mweixin/menu/saveOrUpdate.do", that.allMenuList,
						{
							headers: {
								'Content-Type': 'application/json'
							}
						}).then(function (res) {
					if (res.result) {
						that.$notify.success('菜单修改成功')
						that.menuList();
					} else {
						that.$notify.error(res.msg)
					}
				}, function (err) {
				})

			},
			// 删除菜单
			menuDel: function (mainMenu,subMenu) {
				var that = this;
				// 当存在子菜单的时候，不能删除菜单
				if (!subMenu&& mainMenu.subMenuList.length) {
					return that.$notify.error('当前菜单存在子菜单，不能删除')
				}
				//取要删除菜单的id
				var  menuId = subMenu?subMenu.menuId:mainMenu.menuId;

				this.$confirm('此操作将永久删除该菜单, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					if (menuId) {
						ms.http.post(ms.manager + "/mweixin/menu/delete.do", {
							ids: menuId
						}).then(function (res) {
							that.$notify({
								type: 'success',
								message: '删除成功!'
							});
							// 清空表单值
							that.resetForm();
							// 刷新菜单列表
							that.menuList();
						}, function (err) {
						})
					}
					else {
						//如果是父菜单
						if(!subMenu){
							for (let i = 0; i < that.allMenuList.length; i++) {
								if(that.allMenuList[i]==mainMenu){
									that.allMenuList.splice(i,1);
									if(that.allMenuList.length){
										//删除之后到其他菜单
										that.menuForm = that.allMenuList[0]
									}else {
										that.menuForm ={};
										that.$nextTick(function () {
											that.$refs.menuForm.resetFields();
										})
									}
								}
							}
						}else {
							// 如果还没保存到数据库直接删除数组中的对象
							for (let i = 0; i < mainMenu.subMenuList.length; i++) {
								if(mainMenu.subMenuList[i]==subMenu){
									mainMenu.subMenuList.splice(i,1);
									if(mainMenu.subMenuList.length){
										//删除之后跳转
										that.menuForm = mainMenu.subMenuList[0]
									}else {
										//如果没有子菜单了跳转到父菜单
										that.menuForm = mainMenu
									}
								}
							}
						}
					}
				})

			},
			// 清空表单值
			resetForm:function(){
				this.$refs.menuForm.resetFields();
			},
			// 发布菜单
			menuCreate: function () {
				var that = this;
				this.$confirm('此操作将发布公众号菜单, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					ms.http.get(ms.manager + "/mweixin/menu/create.do")
						.then(function (res) {
							if (res.result) {
								that.$notify({
									type: 'success',
									message: '发布成功,重新关注即可刷新!'
								});
							} else {
								that.$notify({
									type: 'error',
									message: '发布失败！'
								});
							}
						}, function (err) {
						})
				}).catch(() => {
					this.$notify({
						type: 'info',
						message: '已取消删除'
					});
				})
			},
            visible:function(menu){
                menu.seen = true;
            },
            invisible:function(menu){
                menu.seen = false;
            },
		},
		mounted: function () {
			this.menuList();
		},

	});
</script>
<style>
    #ms-menu {
        color: #f2f2f6;
    }

	#ms-menu .iconfont {
		font-size: 12px;
	}
    #ms-menu .ms-custom-container {
        display: flex;
        justify-content: space-between;
    }

    #ms-menu .ms-custom-container .el-aside {
        background: #fff;
        width: 320px !important;
        height: 500px !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container {
        overflow: hidden;
		height: 100%;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-header {
        height: 40px !important;
        line-height: 40px !important;
        font-weight: initial;
        font-size: 16px;
        color: #fff;
        text-align: center;
        background: #323232;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-main {
        padding: 0;
        width: 280px !important;
        height: 380px !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer {
        white-space: nowrap;
        padding: 0;
        font-size: 0;
        background-color: #FAFAFA;
        width: 320px !important;
        height: 50px !important;
        display: flex;
        justify-content: flex-start;
        border-top: 1px solid #e6e6e6 !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer > .el-button {
        width: 40px !important;
        height: 50px !important;
        min-width: 40px;
        padding: 0 !important;
        border: none !important;
        border-right: 1px solid #e6e6e6 !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .el-button {
        border-radius: 0 !important;
        height: 50px !important;
        background: transparent !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu {
        width: 280px;
        font-size: 0;
        display: flex;
        justify-content: space-between;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .el-button {
        flex: 1;
        border: none !important;
        background: transparent !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .el-button span {
        color: #333;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu > div {
        width: 100%;
        display: flex;
        justify-content: space-between;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu {
        flex: 1;
        position: relative;
        display: inline-block;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu > .el-button:first-child {
        width: 100%;
        border-right: 1px solid #e6e6e6 !important;
        padding: 10px !important;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        font-size: 10px;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list {
        position: absolute;
        bottom: 60px;
        left: 3%;
        border: 1px solid #e6e6e6 !important;
        width: 94%;
        display: flex;
        justify-content: space-between;
        flex-direction: column;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list > button {
        margin-left: 0 !important;
        border: none !important;
        border-bottom: 1px solid #e6e6e6 !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list .ms-create-btn {
        border-bottom: none !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list .sub-menu-item {
        font-size: 10px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        padding: 10px !important;
        width: 100%;
        border-bottom:1px solid #e6e6e6 !important;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list-nopadding .el-button + .el-button {
        margin-left: 0px;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .el-button--default {
        padding: 0 !important;
        flex: 1;
    }
    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .menu-validate-font + .el-form-item__error {
        margin-top: -2px !important;
        top: 98%!important;
    }
    #ms-menu .ms-custom-container .el-main {
        padding: 0;
        padding-left: 20px;
        flex: 1;
        height: 500px;
    }
    #ms-menu .ms-custom-container .el-main .menu-card {
        height: 100%;
    }
    #ms-menu .ms-custom-container .el-main .ms-menue-change{
        top: 196px;
        left: 124px;
    }
    #ms-menu .ms-custom-container .el-main .ms-menue-change > button:first-child{
        margin-right: 20px;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-name .el-form-item__content, {
        display: flex;
        justify-content: flex-start;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-name .el-form-item__content .el-input,
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-form-item__content .el-input {
        width: 153px !important;
        height: 30px !important;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-name .el-form-item__content span,
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-form-item__content span {
        margin-left: 10px;
        font-weight: initial;
        font-size: 12px;
        color: #999;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-form-item__content .el-input {
        width: 306px !important;
        height: 30px !important;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs {
        border: 1px solid #e6e6e6;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tabs__header {
        background: #f2f2f6;
        border-radius: 4px 4px 0 0 !important;
        margin: 0 !important;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tabs__header .el-tabs__nav-scroll {
        padding: 0 20px;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tabs__header .el-tabs__nav-scroll i {
        margin-right: 8px;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tab-pane {
        width: 100%;
        display: flex;
        justify-content: space-between;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .ms-padding-twenty{
        padding: 20px;
        height: 195px;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tab-pane > div i {
        font-weight: bolder;
        font-size: 20px;
        color: #0099ff;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tab-pane > div span {
        margin-top: 8px;
        line-height: 1;
		color: #333;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tab-pane > div:hover {
        cursor: pointer;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tab-pane > div:last-child {
        margin-left: 20px;
    }

    #ms-menu {
        color: #f2f2f6;
    }

    #ms-menu .ms-custom-container {
        display: flex;
        justify-content: space-between;
    }

    #ms-menu .ms-custom-container>.ms-container {
        padding: 0;
        display: flex;
        flex-flow: row nowrap;
		background: 0;
    }

    #ms-menu .ms-custom-container .el-aside {
        background: #fff;
        width: 280px !important;
        height: 470px !important;
		border: 1px solid #ddd;
    }

    #ms-menu .ms-custom-container .el-aside .el-container {
        overflow: hidden;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-header {
        height: 40px !important;
        line-height: 40px !important;
        font-weight: initial;
        font-size: 16px;
        color: #fff;
        text-align: center;
        background: #323232;
    }

    .ms-pagination, .ms-tr {
        text-align: right;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-main {
        padding: 0;
        width: 280px !important;
        height: 380px !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer {
        white-space: nowrap;
        padding: 0;
        font-size: 0;
        background-color: #FAFAFA;
        width: 280px !important;
        height: 50px !important;
        display: flex;
        justify-content: flex-start;
        border-top: 1px solid #e6e6e6 !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer>.el-button
    {
        width: 40px !important;
        height: 50px !important;
        min-width: 40px;
        padding: 0 !important;
        border: none !important;
        border-right: 1px solid #e6e6e6 !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .el-button
    {
        border-radius: 0 !important;
        height: 50px !important;
        background: 0 0 !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu
    {
        width: 240px;
        font-size: 0;
        display: flex;
        justify-content: space-between;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .el-button
    {
        flex: 1;
        border: none !important;
        background: 0 0 !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .el-button span
    {
        color: #333;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu
    {
        flex: 1;
        position: relative;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu>.el-button:first-child
    {
        width: 100%;
        border-right: 1px solid #e6e6e6 !important;
        padding: 10px !important;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list
    {
        position: absolute;
        bottom: 60px;
        border: 1px solid #e6e6e6 !important;
        width: 85%;
        display: flex;
        justify-content: flex-start;
        flex-direction: column;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list>button
    {
        margin-left: 0 !important;
        border: none !important;
        border-bottom: 1px solid #e6e6e6 !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list .ms-create-btn
    {
        border-bottom: none !important;
    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .ms-create-sub-menu .sub-menu-list .sub-menu-item
    {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        padding: 10px !important;

    }

    #ms-menu .ms-custom-container .el-aside .el-container .el-footer .ms-create-menu .el-button--default
    {
        padding: 0 !important;
        flex: 1
    }

    #ms-menu .ms-custom-container .el-main {
        padding: 0 0 0 20px;
        flex: 1;
        height: 470px;
    }

    #ms-menu .ms-custom-container .el-main .menu-card {
        height: 100%;
    }
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-name .el-form-item__content
    {
        display: flex;
        justify-content: flex-start;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-form-item__content .el-input,
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-name .el-form-item__content .el-input
    {
        width: 153px !important;
        height: 30px !important;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-form-item__content span,
    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-name .el-form-item__content span
    {
        margin-left: 10px;
        font-weight: initial;
        font-size: 12px;
        color: #999;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-form-item__content .el-input
    {
        width: 306px !important;
        height: 30px !important;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs
    {
        border: 1px solid #e6e6e6;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tabs__header
    {
        background: #f2f2f6;
        border-radius: 4px 4px 0 0 !important;
        margin: 0 !important;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tabs__header .el-tabs__nav-scroll
    {
        padding: 0 20px;
    }

    #ms-menu .ms-custom-container .el-main .menu-card .ms-menu-content .el-tabs .el-tabs__header .el-tabs__nav-scroll i
    {
        margin-right: 8px;
    }

    #ms-menu  .ms-menu-del  {
        position: absolute;
        margin-top: -12px;
        right: 14px;
        transform: translateY(-50%) translateX(100%);
    }

    #ms-menu .el-badge__content {
        right: 14px;
        height: 14px;
        padding: 0px 0px;
        width: 14px;
        border-width: 0px;
        line-height: 14px;
    }
    #ms-menu .iconfont{
		font-size: 12px;
		margin-right: 5px;
	}
    .son-button .el-icon-plus{
        height: 50px;
        padding: 18px 0px;
    }
	#ms-menu .message-tabs .el-tabs__content{
		padding: 10px 20px 0 20px;
	}

</style>