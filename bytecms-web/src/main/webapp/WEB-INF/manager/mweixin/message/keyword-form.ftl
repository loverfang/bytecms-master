<!-- 关键词回复 -->
<div id="keyword-form" class="ms-weixin-content" v-if="weixinVue.menuActive == '关键词表单'" v-cloak>
   <el-container>
      <el-header class="ms-header" height="50px">
         <el-row>
            <el-button class="ms-fr" size="mini"  @click="weixinVue.menuActive = '关键词回复'"><i class="iconfont icon-fanhui"></i>返回</el-button>
           <el-button class="ms-fr" type="primary" size="mini"  @click="messageSave()"><i class="iconfont icon-icon-"></i>保存</el-button>
         </el-row>
      </el-header>
      <el-main class="ms-container" width="100%">
         <el-form :model="passiveMessageForm" status-icon :rules="passiveMessageFormRules" ref="passiveMessageForm" label-width="100px">
            <el-form-item label="关键词"  ref="form" prop="pmKey" class="ms-keyword-input">
               <el-row type='flex' justify='space-between' align='center'>
                  <el-col :span='12'>
                     <el-input placeholder="请输入关键词" v-model="passiveMessageForm.pmKey" class="input-with-select" size='mini' maxlength='30' @input='resetWord'>
                        <span class="ms-input__suffix" slot='suffix' v-text="wordNumber+'/30'"></span>
                     </el-input>
                  </el-col>
                  <el-col>
                  </el-col>
               </el-row>
            </el-form-item>
            <el-form-item class="ms-keyword-form-content" label="回复内容">
               <ms-message-reply :content="messageContent"
                   :img-Content="imgContent" :choose-Graphic="chooseGraphic"
                                 :img-File-Name="imgFileName"
                    :voice-Content="voiceContent" :video-Content="videoContent"
                    :active-Name="activeName"
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
   var passiveMessageFormVue = new Vue({
      el: '#keyword-form',
      data: {
         messageContent:'',//文本消息
         imgContent:'',//图片消息
         newsContent:'',//图文消息
         voiceContent:'',//音频
         videoContent:'',//视频
         passiveMessageForm: {
            pmKey: "", //关键词
            select: '',
            pmContent: "",
            pmWeixinId:"",
            pmType:"",
            pmId:"",
            pmImgId:"",
         },
         passiveMessageFormRules: {
            pmKey: [
               { required: true, message: '请输入关键词', trigger: 'blur' }
            ],
         },
         activeName: 'text',
         wordNumber: 30, //剩余字数
         pmId:0,//消息ID
         chooseGraphic:{},		//选中的图文
         chooseImg:'',  //选择的图片
         imgFileName:'',  //选择的图片
         chooseVoice:'',//选择的音频
         chooseVideo:'',//选择的视频
      },
      watch:{
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
         },
         passiveMessageForm:function (ne) {
            this.messageContent = '';
            this.imgContent = '';
            this.newsContent = '';
            this.voiceContent = '';
            this.videoContent = '';
            this.chooseGraphic = {};
            var that = this
            that.passiveMessageForm.pmId = ne.pmId
            if (ne.pmNewType){
               that.activeName = ne.pmNewType;
            }
            switch (ne.pmNewType) {
               case "text"://文本类型
                  that.passiveMessageForm = ne;
                  //给富文本编辑器的内容加上p标签以免出错
                  that.messageContent = '<p>'+that.passiveMessageForm.pmContent+'</p>';
                  break;
               case "image"://图片类型
                  that.passiveMessageForm = ne;
                  ms.http.get(ms.manager + "/mweixin/file/get.do",{
                          fileId:that.passiveMessageForm.pmContent
                  })
                          .then(function(data)  {
                                     that.imgContent =ms.base+data.data.fileUrl;
                             that.imgFileName =  data.data.fileName;
                                  }, function(err)
                                  {
                                     that.$notify.error(err);
                                  }
                          )
                  break;
               case "imageText"://图文类型
                  that.passiveMessageForm = ne;
                  that.newsContent = that.passiveMessageForm.pmContent;
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
                     fileId:that.passiveMessageForm.pmContent,
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
                     fileId:that.passiveMessageForm.pmContent,
                  }).then(function (data) {
                             that.videoContent = data.data.fileName;
                          }, function (err) {
                             that.$notify.error(err);
                          }
                  )
                  break;

            }
         }
      },
      methods: {
         //清除编辑数据
         rest:function(){
            this.passiveMessageForm={
               pmKey: "", //关键词
                       select: '',
                       pmContent: "",
                       pmWeixinId:"",
                       pmType:"",
                       pmId:"",
                       pmImgId:"",
            }
            this.activeName = 'text'
         },
         // 添加关键词
         addKeyWord: function() {

         },
         onEditorChange: function(quill){
            this.messageContent = quill.html;		//获取富文本内的html内容
         },
         messageSave: function(){
            this.$refs.passiveMessageForm.validate((valid) => {
               if (valid) {
                  if (this.activeName){
                     this.passiveMessageForm.pmNewType=this.activeName;
                  }
                  switch (this.activeName) {
                     case "imageText"://图文
                        this.passiveMessageForm.pmContent = this.newsContent;
                        break;
                     case "image"://图片
                        this.passiveMessageForm.pmContent = this.chooseImg.fileId;
                        break;
                     case "text"://文本
                        //保存之前需要去掉p标签
                        var p = document.createElement("p");
                        p.innerHTML =  this.messageContent;
                        this.passiveMessageForm.pmContent = p.querySelector('p').innerHTML
                        break;
                     case "voice"://语音
                        this.passiveMessageForm.pmContent = this.chooseVoice.fileId;
                        break;
                     case "video"://视频
                        this.passiveMessageForm.pmContent = this.chooseVideo.fileId;
                        break;
                  }
                  this.messageSaveOrUpdate(this.passiveMessageForm)
                  weixinVue.menuActive = '关键词回复';
               }else{
                  return false;
               }
            })
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
         // 保存关键字消息回复
         messageSaveOrUpdate: function(data) {
            var that = this;
            if(data.pmId){
               ms.http.post(ms.manager + "/mweixin/message/update.do",data)
                       .then((data)=>{
                  if(data.result){
                     that.isShow = false;
                     keywordVue.list();
                     that.$notify.success("修改成功");
                     that.rest();
                  }else{
                     that.$notify.error(data.msg);
                  }
            }, (err) => {
                  that.$notify.error(err);
               })
            } else {
               data.pmType = 'keyword';
               data.pmWeixinId =  ${weixinId};
               ms.http.post(ms.manager + "/mweixin/message/save.do",data)
                       .then((data)=>{
                  if(data.result){
                     that.isShow = false;
                     keywordVue.list();
                     that.$notify.success("保存成功");
                     that.rest();
                  }else{
                     that.$notify.error(data.msg);
                  }
            }, (err) => {
                  that.$notify.error(err);
               })
            }
         },
         // 计算剩余字数
         resetWord: function(value) {
            if(!value) return 30;
            if(value.length >= 30) {
               this.$notify.error('任务名称不得超过30个字');
               // 这里涉及到获取数据更新之后的DOM，需要用$nextTick
               this.$nextTick(function() {
                  this.passiveMessageForm.pmKey = event.target.value = value.slice(0, 30);
               })
               this.wordNumber = 0
            } else {
               this.wordNumber = 30 - value.length
            }
         },

      }
   })
</script>
<style>



    .keyword-form {
        padding-bottom: 20px;
    }

    .keyword-form .el-select .el-input {
        width: 90px;
    }

    .keyword-form .el-select .el-input>input {
        padding: 0 10px !important;
    }

    .keyword-form .ms-keyword-input {
        margin-bottom: 15px !important;
    }

    .keyword-form .ms-keyword-input .el-input__suffix {
        line-height: 28px;
    }

    #keyword-form .ms-keyword-input .el-icon-plus {
        margin-left: 20px;
        font-weight: 700;
        font-size: 14px;
        color: #09f;
    }

    #keyword-form .ms-keyword-input .el-icon-plus:hover {
        cursor: pointer;
    }

    #keyword-form .ms-keyword-form-content {
        margin: 0;
    }

    #keyword-form .ms-keyword-form-content .keyword-form-tabs {
        flex: 1;
        border: 1px solid #e6e6e6;
        border-radius: 4px 4px 0 0 !important;
        min-height: 226px;
    }

    #keyword-form .ms-keyword-form-content .keyword-form-tabs .el-tabs__header
    {
        margin: 0 !important;
    }

    #keyword-form .ms-keyword-form-content .keyword-form-tabs .el-tabs__header .el-tabs__nav-scroll
    {
        padding: 0 20px;
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content {
        position: relative;
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content .el-icon-delete
    {
        position: absolute;
        right: 5px;
        bottom: 66px;
        font-weight: 400;
        font-size: 14px;
        color: rgb(51,51,51);
       cursor: pointer;
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content .el-icon-delete:hover
    {
       color: #09f;
       background: #fff;
       border-color: #09f;
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content textarea {
        height: 127px !important;
        border: none !important;
        border-bottom: 1px solid #e6e6e6 !important
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content .footer {
        padding: 0 14px;
        display: flex;
        justify-content: flex-start;
        align-items: center
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content .footer i
    {
        margin-right: 12px;
        font-size: 16px
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content .footer a
    {
        font-weight: initial;
        font-size: 14px;
        color: #09f
    }

    #keyword-form .ms-keyword-form-content .el-form-item__content .footer a:hover,
    #keyword-form .ms-keyword-form-content .el-form-item__content .footer i:hover
    {
        cursor: pointer;
    }
    #keyword-form > section > main > form > div.el-form-item.ms-keyword-form-content.el-form-item--feedback > div > div > div.el-tabs__content > .el-tab-pane{
       padding: 20px;
       height: 195px;
    }
    #keyword-form >.ms-input__suffix{
       top:5px;
    }
	#keyword-form .iconfont{
		font-size: 12px;
		margin-right: 5px;
	}
    #keyword-form .ms-container {
       height: calc(100vh - 74px);
    }
</style>