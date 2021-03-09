<!-- 消息发送页面 -->
<div id="group-reply" class="group-reply ms-weixin-content" v-if="weixinVue.menuActive == '一键群发'||weixinVue.menuActive == '单独发送'||weixinVue.menuActive == '群发记录页面'" v-cloak>
    <el-container>
        <el-header class="ms-header" height="50px">
            <el-row>
                <@shiro.hasPermission name="reply:save"><el-button v-if="weixinVue.menuActive != '群发记录页面'" class="ms-fr" type="primary" size="mini"  @click="groupMessageSend"> <i style="margin-right: 5px;font-size: 12px; " class="iconfont icon-14fabu"></i>发送</el-button></@shiro.hasPermission>
                <el-button v-if="weixinVue.menuActive == '群发记录页面'" class="ms-fr"  size="mini"  @click="weixinVue.menuActive = '群发记录'"> <i class="iconfont icon-fanhui"></i>返回</el-button>
            </el-row>
        </el-header>
        <el-main class="ms-container" width="100%">
            <el-form :model="groupMessageForm" status-icon :rules="groupMessageFormRules" ref="groupMessageForm" label-width="100px">
                <el-form-item class="group-reply-form-content" label="回复内容">
                    <ms-message-reply
                            :content="messageContent"
                            :img-File-Name="imgFileName"
                            :disabled="weixinVue.menuActive == '群发记录页面'"
                            :img-Content="imgContent"
                            :choose-Graphic="chooseGraphic"
                            :active-Name="activeName"
                            :voice-Content="voiceContent" :video-Content="videoContent"
                            @editor-change="onEditorChange($event)"
                            @tab-click="activeName = $event"
                            @img-del="deleteContent('image')"
                            @voice-del="deleteContent('voice')" @video-del="deleteContent('video')"
                            @out-graphic="deleteContent('imageText')" @clean-content="deleteContent('text')">
                    </ms-message-reply>
                </el-form-item>
            </el-form>
        </el-main>
    </el-container>
</div>
<script>
    var groupReply = new Vue({
        el: '#group-reply',
        data: {
            messageContent:'',//文本消息
            imgContent:'',//图片消息
            imgFileName:'',//图片名称
            newsContent:'',//图文消息
            voiceContent:'',//音频
            videoContent:'',//视频
            openId:'',//发送给用户的id
            groupMessageForm: {
                pmContent: "",
                pmNewType:"",
                pmTag:'全体粉丝',
                pmType:'all',
            },
            groupMessageFormRules:{},
            activeName: 'text',
            chooseGraphic:{},		//选中的图文
            chooseImg:'',  //选择的图片
            chooseVoice:'',//选择的音频
            chooseVideo:'',//选择的视频
        },
        watch:{
            //值改变重置表单
            openId:function(openId){
                this.newsContent = '';
                this.imgContent = '';
                this.chooseGraphic = {};
                this.messageContent = '';
                this.voiceContent = '';
                this.videoContent = '';
                this.groupMessageForm ={
                    pmContent: "",
                    pmNewType:"",
                    pmTag:'全体粉丝',
                    pmType:'all',
                }
            },
            groupMessageForm:function(message){
                var that = this;
                if (message.pmNewType){
                    that.activeName = message.pmNewType;
                }
                switch (message.pmNewType) {
                    case "text"://文本类型
                        that.passiveMessageForm = message;
                        //给富文本编辑器的内容加上p标签以免出错
                        that.messageContent = '<p>'+that.passiveMessageForm.pmContent+'</p>';
                        break;
                    case "image"://图片类型
                        that.passiveMessageForm = message;
                        ms.http.get(ms.manager + "/mweixin/file/get.do",{
                            fileId:that.passiveMessageForm.pmContent
                        })
                            .then(function(data)  {
                                    that.imgContent =ms.base+data.fileUrl;
                                }, function(err)
                                {
                                    that.$notify.error(err);
                                }
                            )
                        break;
                    case "imageText"://图文类型
                        that.passiveMessageForm = message;
                        that.newsContent = that.passiveMessageForm.pmContent;
                        //获取素材实体
                        ms.http.get(ms.manager + "/mweixin/news/" + that.newsContent + "/get.do")
                            .then(function(data)  {
                                    that.chooseGraphic = data
                                }, function(err)
                                {
                                    that.$notify.error(err);
                                }
                            )
                        break;
                    case "voice"://音频类型
                        //获取素材实体
                        ms.http.get(ms.manager + "/mweixin/file/get.do",{
                            fileId:that.passiveMessageForm.pmContent,
                        }).then(function (data) {
                                that.voiceContent = data.fileName;
                            }, function (err) {
                                that.$notify.error(err);
                            }
                        )
                        break;
                    case "video"://视频类型
                        //获取素材实体
                        ms.http.get(ms.manager + "/mweixin/file/get.do",{
                            fileId:that.passiveMessageForm.pmContent,
                        }).then(function (data) {
                                that.videoContent = data.fileName;
                            }, function (err) {
                                that.$notify.error(err);
                            }
                        )
                        break;
                }
            },
            chooseGraphic:function (ne,ol) {
                this.newsContent = ne.newsId
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
            onEditorChange: function(quill){
                this.messageContent = quill.html;		//获取富文本内的html内容
            },
            groupMessageSend: function(){
                if (this.activeName) {
                    this.groupMessageForm.pmNewType = this.activeName;
                }
                switch (this.activeName) {
                    case "imageText"://图文
                        this.groupMessageForm.pmContent = this.newsContent;
                        break;
                    case "image"://图片
                        if (this.chooseImg.fileId){
                            this.groupMessageForm.pmContent = this.chooseImg.fileId;
                        }else {
                            this.groupMessageForm.pmContent = this.chooseImg.id;
                        }
                        break;
                    case "text"://文本
                        //保存之前需要去掉p标签
                        var p = document.createElement("p");
                        p.innerHTML =  this.messageContent;
                        this.groupMessageForm.pmContent = p.querySelector('p').innerHTML
                        break;
                    case "voice"://语音
                        if (this.chooseVoice.fileId) {
                            this.groupMessageForm.pmContent = this.chooseVoice.fileId;
                        }else {
                            this.groupMessageForm.pmContent = this.chooseVoice.id;
                        }
                    case "video"://视频
                        if (this.chooseVideo.fileId) {
                            this.groupMessageForm.pmContent = this.chooseVideo.fileId;
                        }else {
                            this.groupMessageForm.pmContent = this.chooseVideo.id;
                        }
                        break;
                }
                this.messageSend(this.groupMessageForm)

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
            // 发送消息
            messageSend: function(data) {
                var that = this;
                //判断是否是群发
                if(!that.openId){
                    //群发
                    that.$confirm('群发一个月最多发送4次，是否继续发送？', '提示 !', {
                        confirmButtomText: '确定',
                        cacelButtomText: '取消',
                        type: 'warning'
                    }).then(function() {
                        ms.http.post(ms.manager + "/mweixin/message/sendAll.do",data)
                            .then((data)=>{
                                if(data.result > 0){
                                    that.$notify.success("发送成功");
                                }else{
                                    that.$notify.error("发送失败，"+data.msg);
                                }
                            }, (err) => {
                                that.$notify.error(err);
                            })
                    })
                }else {
                    //发送给单独用户
                    data.openId = that.openId
                    ms.http.post(ms.manager + "/mweixin/message/sendToUser.do",data)
                        .then((data)=>{
                            if(data.result > 0){
                                that.$notify.success("发送成功");
                                weixinVue.menuActive = '微信用户'
                            }else{
                                that.$notify.error("发送失败，"+data.msg);
                            }
                        }, (err) => {
                            that.$notify.error(err);
                        })
                }
            },

        }
    })
</script>
<style>
    #group-reply .message-tabs .el-tabs__content {
        position: relative;
        height: 195px;
    }
    #group-reply .iconfont{
    	font-size: 12px;
    	margin-right: 5px;
    }
</style>