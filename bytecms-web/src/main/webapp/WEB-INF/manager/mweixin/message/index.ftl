<!-- 被动回复 && 关注回复 -->
<div id="ms-message" class="ms-weixin-content"
	v-if="weixinVue.menuActive == '被动回复'||weixinVue.menuActive == '关注时回复'" v-cloak>
	<el-container class="ms-admin-message"> 
		<el-header class="ms-header" height="50px"> 
			<el-row>
				<el-button type="text"></el-button>
				<@shiro.hasPermission name="passiveMessage:save"><el-button type="primary" class="ms-fr"size="mini" @click="messageSaveOrUpdate" v-if="weixinVue.menuActive == '被动回复'"><i class="iconfont icon-icon-"></i>保存</el-button></@shiro.hasPermission>
				<@shiro.hasPermission name="followMessage:save"><el-button type="primary" class="ms-fr"size="mini"  @click="messageSaveOrUpdate" v-if="weixinVue.menuActive == '关注时回复'"><i class="iconfont icon-icon-"></i>保存</el-button></@shiro.hasPermission>
			</el-row>
		</el-header> 
		<el-container>
			<em-main class="ms-container">
				<ms-message-reply :content="messageContent"
					:img-Content="imgContent" :choose-Graphic="chooseGraphic"
					:img-File-Name="imgFileName"
					:voice-Content="voiceContent" :video-Content="videoContent"
					:active-Name="activeName" @editor-change="onEditorChange($event)"
					@tab-click="activeName = $event"
					@img-del="deleteContent('image')"
					@voice-del="deleteContent('voice')" @video-del="deleteContent('video')"
					@out-graphic="deleteContent('imageText')" @clean-content="deleteContent('text')">
				</ms-message-reply>
			</em-main>
		</el-container>
	</el-container>
</div>
<script>
	var messageVue = new Vue({
		el: "#ms-message",
		data: {
			messageContent:'',//文本消息
			imgContent:'',//图片消息
			imgFileName:'',//图片名称
			newsContent:'',//图文消息
			voiceContent:'',//音频
			videoContent:'',//视频
			messageForm: {
				pmContent: '', //消息回复内容
				pmType: '', //回复属性:keyword.关键字回复、attention.关注回复、passivity.被动回复、all.群发
				pmId: '', //消息回复编号
				pmWeixinId: '', //微信编号
				pmNewType:"text"
			},
			//tabs当前选中项
			activeName: 'text',
			chooseGraphic:'',		//选中的图文
			chooseImg:'',  //选择的图片
			chooseVoice:'',//选择的音频
			chooseVideo:'',//选择的视频
		},
		watch:{
			chooseGraphic:function (news,ol) {
				this.newsContent = news.newsId
			},
			chooseImg:function (img,ol) {
				this.imgContent = ms.base+img.fileUrl;
				this.imgFileName = img.fileName;
			},
			chooseVoice:function (voice,ol) {
				this.voiceContent = voice.fileName;
			},
			chooseVideo:function (video,ol) {
				this.videoContent = video.fileName;
			}

		},
		methods: {
			// 保存超链接
			saveLink: function () {
				var that = this;
				that.$refs.hyperlinkForm.validate(function (boolean, object) {
					if (boolean) {
						// 校验成功
						var quill = that.$refs.quillEditor.quill
						var index= quill.getSelection(true).index
						that.$refs.quillEditor.quill.insertEmbed(index,'link',{href:that.hyperlinkForm.link,innerText:that.hyperlinkForm.text},'api')
						that.cancelLink()
					}
				})
			},
			// 取消超链接
			cancelLink: function () {
				this.$refs.hyperlinkForm.resetFields();
				this.popoverShow = false
			},
			// 设置消息回复类型
			messageType: function () {
				if (weixinVue.menuActive == '被动回复') {
                    this.messageForm.pmType = 'passivity';
				} else if (weixinVue.menuActive == '关注时回复') {
                    this.messageForm.pmType = 'attention';
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
					that.$notify.success("已经成功清除了该内容");
				})
			},
			// 获取消息回复
			messageList: function () {
				var that = this;
				//初始化
				that.messageForm = {
					pmContent: '',
					pmType: '',
					pmId: '',
					pmWeixinId: '',
					pmNewType: ''
				};
				that.messageType();
				if (that.messageForm.pmType) {
					that.messageForm.pmWeixinId = ${weixinId};
					ms.http.get(ms.manager + "/mweixin/message/list.do", {
						pmWeixinId: that.messageForm.pmWeixinId,
						pmType: that.messageForm.pmType,
					}).then(function (data) {
						that.messageContent = '';
						that.imgContent = '';
						that.newsContent = '';
						that.voiceContent = '';
						that.videoContent = '';
						that.chooseGraphic = {};
						if (data.data.rows.length > 0) {
							that.messageForm = data.data.rows[0];
							if (that.messageForm.pmNewType){
								that.activeName = that.messageForm.pmNewType;
							}
							switch (that.messageForm.pmNewType) {
								case "text"://文本类型
									//给富文本编辑器的内容加上p标签以免出错
									that.messageContent = '<p>'+that.messageForm.pmContent+'</p>';
									break;
								case "image"://图片类型
									//获取素材实体
									ms.http.get(ms.manager + "/mweixin/file/get.do", {
										fileId: data.data.rows[0].pmContent,
									}).then(function (data) {
												that.imgContent = ms.base + data.data.fileUrl;
										that.imgFileName =  data.data.fileName;
											}, function (err) {
												that.$notify.error(err);
											}
									)
									break;
								case "imageText"://图文类型
									that.newsContent = that.messageForm.pmContent;
									//获取素材实体
									ms.http.get(ms.manager + "/mweixin/news/" + that.newsContent + "/get.do")
											.then(function(data)  {
														that.chooseGraphic = data.data
													}, function(err)
													{
														that.$notify.error(err);
													}
											)
									break;
								case "voice"://音频类型
									//获取素材实体
									ms.http.get(ms.manager + "/mweixin/file/get.do",{
										fileId:data.data.rows[0].pmContent,
									}).then(function (data) {
												that.voiceContent = data.data.fileName;
											}, function (err) {
												that.$notify.error(err);
											}
									)
									break;
								case "video"://视频类型
									//获取素材实体
									ms.http.get(ms.manager + "/mweixin/file/get.do",{
										fileId:data.data.rows[0].pmContent,
									}).then(function (data) {
												that.videoContent = data.data.fileName;
											}, function (err) {
												that.$notify.error(err);
											}
									)
									break;
							}
						} else {
							that.messageForm.pmId = '';
						}
					},function(err){
						that.$notify.error(err);
				})
				}
			},
			onEditorChange: function(quill){
				//获取富文本内容并去除p标签
				this.messageContent = quill.html;
			},
			// 保存消息回复
			messageSaveOrUpdate: function () {
				if (this.activeName){
					this.messageForm.pmNewType = this.activeName;
				}
				switch (this.activeName) {
					case "imageText"://图文
						this.messageForm.pmContent = this.newsContent;
						break;
					case "image"://图片
						this.messageForm.pmContent = this.chooseImg.fileId;
						break;
					case "text"://文本
						//保存之前需要去掉p标签
						var p = document.createElement("p");
						p.innerHTML =  this.messageContent;
						this.messageForm.pmContent = p.querySelector('p').innerHTML
						break;
					case "voice"://语音
						this.messageForm.pmContent = this.chooseVoice.fileId;
						break;
					case "video"://视频
						this.messageForm.pmContent = this.chooseVideo.fileId;
						break;
				}
				this.saveData(this.messageForm);
			},
            saveData: function(data){
                var that = this;
                if (data.pmId!='') {
                    ms.http.post(ms.manager + "/mweixin/message/update.do", data)
                        .then((data) => {
                        	if(data.data.pmId){
								that.$notify.success("修改成功");
								that.messageList();
							}
                        }, (err) => {
                            that.$notify.error(err);
                        })
                } else {
                    that.messageType();
                    that.messageForm.pmWeixinId = ${weixinId};
                    ms.http.post(ms.manager + "/mweixin/message/save.do",data )
                        .then((data) => {
							if(data.data.pmId){
								that.$notify.success("保存成功");
								that.messageList();
							}
                        }, (err) => {
                            that.$notify.error(err);
                        })
                }
            }
		},
	})
</script>
<style>
#ms-message {
	display: flex;
	justify-content: flex-start;
	height: auto;
}

.ms-message>div:first-child {
	margin-right: 10px;
	line-height: 40px;
}

#ms-message .message-tabs {
	flex: 1;
	border-radius: 4px 4px 0 0 !important;
	border: none !important;
}
#ms-message .show-graphic {
	width:274px;
}
#ms-message {
    display: flex;
    justify-content: flex-start;
    height: auto;
}

.ms-message>div:first-child {
    margin-right: 10px;
    line-height: 40px;
}

#ms-message .message-tabs {
    flex: 1;
    border-radius: 4px 4px 0 0 !important;
    border: none !important;
}

#ms-message .message-tabs .el-tabs__header {
    margin: 0 !important;
}

#ms-message .message-tabs .el-tabs__header .el-tabs__nav-scroll {
    padding: 0 20px;
    border: 1px solid #e6e6e6;
}

#ms-message .message-tabs .el-tabs__content {
    border: 1px solid #e6e6e6;
    height: calc(100% - 112px);
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane {
    padding: 0;
    width: 100%;
    display: flex;
    justify-content: space-between;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form {
    width: 100%;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content
{
    margin: 0;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content
{
    position: relative;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .el-icon-delete
{
    position: absolute;
    right: 7px;
    bottom: 66px;
    font-weight: initial;
    font-size: 14px;
    color: rgb(51,51,51);
    cursor: pointer;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .el-icon-delete:hover
{
    color: #09f;
    background: #fff;
    border-color: #09f;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content textarea
{
    height: 127px !important;
    border: none !important;
    border-bottom: 1px solid #e6e6e6 !important;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .footer
{
    height: 38px;
    padding: 0 14px;
    display: flex;
    justify-content: flex-start;
    align-items: center;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .footer i
{
    margin-right: 12px;
    font-size: 16px;
    cursor: pointer;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .footer i:hover
{
    color: #09f;
    background: #fff;
    border-color: #09f;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .footer a
{
    font-weight: initial;
    font-size: 14px;
    color: #09f;
}

#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .footer a:hover,
#ms-message .message-tabs .el-tabs__content .el-tab-pane>.el-form .ms-message-content .el-form-item__content .footer i:hover
{
    cursor: pointer;
}

#ms-message .message-tabs .el-tabs__content .message-article,
#ms-message .message-tabs .el-tabs__content .message-picture {
    padding: 20px !important;
    height: 100%;
}

#ms-message .message-tabs .el-tabs__content .message-article>div,
#ms-message .message-tabs .el-tabs__content .message-picture>div {
    flex: 1;
    border: 1px dashed #e6e6e6;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}

#ms-message .message-tabs .el-tabs__content .message-article>div i,
#ms-message .message-tabs .el-tabs__content .message-picture>div i {
    font-weight: bolder;
    font-size: 20px;
    color: #09f;
}

#ms-message .message-tabs .el-tabs__content .message-article>div span,
#ms-message .message-tabs .el-tabs__content .message-picture>div span {
    margin-top: 8px;
    line-height: 1;
}

#ms-message .message-tabs .el-tabs__content .message-article>div:hover,
#ms-message .message-tabs .el-tabs__content .message-picture>div:hover
{
    cursor: pointer;
}

#ms-message .message-tabs .el-tabs__content .message-article>div:last-child,
#ms-message .message-tabs .el-tabs__content .message-picture>div:last-child
{
    margin-left: 20px;
}
#ms-message .ms-admin-message .ms-container {
    display: flex;
    height: 400px;
    width:100%;
}
#ms-message .iconfont{
		font-size: 12px;
		margin-right: 5px;
}
#ms-message .el-form .ms-message-content .el-form-item__content .ql-container{
	height: 200px;
}
</style>