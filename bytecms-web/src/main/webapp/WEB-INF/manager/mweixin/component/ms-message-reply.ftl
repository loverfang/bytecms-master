<!-- 回复表单 -->
<script type="text/x-template" id='message-reply'>
    <el-tabs  v-model="activeName" @tab-click="$emit('tab-click',activeName)" class="message-tabs">
        <el-tab-pane :disabled="disabled" label="文字" name="text" >
            <el-form ref="messageForm" :rules='messageFormRules' :model="content">
                <el-form-item class="ms-message-content">
                    <!-- quill-editor富文本 -->
                    <quill-editor ref="quillEditor" :content="content" :options="editorOption" @change="onEditorChange($event)"></quill-editor>
                    <i class="el-icon-delete" v-if="!disabled" @click="$emit('clean-content')"></i>
                    <div class="footer">
                        <i v-if="!disabled" class="el-icon-star-off"></i>
                        <!-- 插入超链接 -->
                        <el-popover v-if="!disabled" placement="top-start" width="350" trigger="click" v-model='popoverShow'>
                            <el-form label-width="100px" :model="hyperlinkForm" ref="hyperlinkForm" :rules='hyperlinkRule'>
                                <el-form-item label="文本内容" prop='text'>
                                    <el-input v-model="hyperlinkForm.text" size='mini'></el-input>
                                </el-form-item>
                                <el-form-item label="链接地址" prop='link'>
                                    <el-input v-model="hyperlinkForm.link" size='mini'></el-input>
                                </el-form-item>
                                <el-form-item style="margin:0">
                                    <el-row type='flex' justify='end'>
                                        <el-col span='6'>
                                            <el-button type="primary" @click="saveLink" size='mini'>保存</el-button>
                                        </el-col>
                                        <el-col span='6'>
                                            <el-button @click="cancelLink" size='mini'>取消</el-button>
                                        </el-col>
                                    </el-row>
                                </el-form-item>
                            </el-form>
                            <a slot="reference">插入超链接</a>
                        </el-popover>
                    </div>
                </el-form-item>
            </el-form>
            </el-form>
        </el-tab-pane>
        <el-tab-pane :disabled="disabled" label="图片" name="image" :class="imgContent == ''?'ms-padding-twenty':''" style="border-bottom: 1px solid #e6e6e6;">
            <template v-if="imgContent == ''">
                <div onclick="fileImgVue.open()" class="chooseLabel">
                    <i class="el-icon-picture-outline"></i>
                    <span>从素材库选择</span>
                </div>
                <div onclick="fileFormVue.open('picture')" class="chooseLabel">
                    <i class="el-icon-plus"></i>
                    <span>新建图片</span>
                </div>
            </template>
            <template v-else>
                <div class="show-check-img border-radius-five">
                    <div class="border-radius-five show-image-model">
                        <img :src ="imgContent"/>
                        <div class="imgFileName">
                            {{imgFileName}}
                        </div>
                    </div>
                </div>
                <i class="el-icon-delete" @click="$emit('img-del')"></i>
            </template>
        </el-tab-pane>
        <el-tab-pane :disabled="disabled" label="图文" name="imageText" :class="(!chooseGraphic.newsArticle)?'ms-padding-twenty':''">
        
            <template v-if="!chooseGraphic.newsArticle">
                <div onclick="newsForm.open()" class="chooseLabel">
                    <i class="el-icon-picture-outline"></i>
                    <span>从素材库选择</span>
                </div>
                <div onclick="newsFormVue.open()" class="chooseLabel">
                    <i class="el-icon-plus"></i>
                    <span>新建图文</span>
                </div>
            </template>
            <template v-else>
            		<el-scrollbar style="height:100%;">
							<div class="show-graphic">
									<span v-text="chooseGraphic.newsArticle && chooseGraphic.newsArticle.articleTitle"></span>
									<img class="border-radius-five" :src="ms.base+chooseGraphic.newsArticle.articleThumbnails" onerror="this.src='${base}/static/mweixin/image/cover.jpg'"/>
									<p v-text="chooseGraphic.newsArticle && chooseGraphic.newsArticle.articleDescription"></p>
							</div>
					</el-scrollbar>
                <i v-if="!disabled" class="el-icon-delete" @click="$emit('out-graphic')"></i>
            </template>
        </el-tab-pane>
        <el-tab-pane :disabled="disabled" label="语音" name="voice" :class="voiceContent == ''?'ms-padding-twenty':''" style="border-bottom: 1px solid #e6e6e6;">
            <template v-if="voiceContent == ''">
                <div onclick="fileVoiceVue.open()" class="chooseLabel">
                    <i class="el-icon-picture-outline"></i>
                    <span>从素材库选择</span>
                </div>
                <div onclick="fileFormVue.open('voice')" class="chooseLabel">
                    <i class="el-icon-plus"></i>
                    <span>新建语音</span>
                </div>
            </template>
            <template v-else>
                <div class="show-check-img border-radius-five">
                    <div class="border-radius-five vedio">
                        <img :src="ms.web+'/mweixin/image/voice.png'">
                        <div class="voiceContent">
                            {{voiceContent}}
                        </div>
                    </div>
                </div>
                <i v-if="!disabled" class="el-icon-delete" @click="$emit('voice-del')"></i>
            </template>
        </el-tab-pane>
        <el-tab-pane :disabled="disabled" label="视频" name="video" :class="videoContent == ''?'ms-padding-twenty':''" style="border-bottom: 1px solid #e6e6e6;">
            <template v-if="videoContent == ''">
                <div onclick="fileVideoVue.open()" class="chooseLabel">
                    <i class="el-icon-picture-outline"></i>
                    <span>从素材库选择</span>
                </div>
                <div onclick="fileFormVue.open('video')" class="chooseLabel">
                    <i class="el-icon-plus"></i>
                    <span>新建视频</span>
                </div>
            </template>
            <template v-else>
                <div class="show-check-img border-radius-five">
                    <div class="border-radius-five vedio">
                        <img :src="ms.web+'/mweixin/image/video.png'">
                        <div class="videoContent">
                            {{videoContent}}
                        </div>
                    </div>
                </div>
                <i v-if="!disabled" class="el-icon-delete" @click="$emit('video-del')"></i>
            </template>
        </el-tab-pane>
    </el-tabs>
</script>
<script>
    Vue.component('msMessageReply', {
        template: '#message-reply',
        props: {
            content: {
                require: false,
                default: '',
            },
            imgContent: {
                require: false,
                default: '',
            },
            imgFileName: {
                require: false,
                default: '',
            },
            voiceContent: {
                require: false,
                default: '',
            },
            videoContent: {
                require: false,
                default: '',
            },
            chooseGraphic: {
                require: false,
                default:{},
            },
            activeName:{
                require: false,
                default:"text",
            },
            disabled:{
                require: false,
                default:false,
            },
        },
        data() {
            return {
                popoverShow:false,
                //富文本设置
                editorOption: {
                    theme: 'bubble',
                    placeholder: "",
                    modules: {
                        toolbar: [
                            ['bold', 'italic', 'underline', 'strike'],
                            [{
                                'list': 'ordered'
                            }, {
                                'list': 'bullet'
                            }],
                            [{
                                'header': ['text', 'image', 'imageText', 'voice', 'video', false]
                            }],
                            [{
                                'color': []
                            }, {
                                'background': []
                            }],
                            [{
                                'font': []
                            }],
                            [{
                                'align': []
                            }],
                            ['link', 'image'],
                            ['clean']
                        ]
                    }
                },
                hyperlinkForm: {
                    text: "",
                    link: "",
                },
                messageFormRules: {
                    name: [{
                        required: true,
                        message: '请输入菜单名称',
                        trigger: ['blur', 'change']
                    },
                        {
                            min: 1,
                            max: 5,
                            message: '长度在 1 到 5 个字符',
                            trigger: ['blur', 'change']
                        }
                    ],
                },
                hyperlinkRule: {
                    text: [{
                        required: true,
                        message: '请输入超链接显示的文本内容',
                        trigger: 'blur'
                    },
                        {
                            min: 1,
                            max: 50,
                            message: '长度在 1 到 50 个字符',
                            trigger: 'blur'
                        }
                    ],
                    link: [{
                        required: true,
                        message: '请输入超链接地址',
                        trigger: 'change'
                    }, {
                        validator: function (rule, value, callback) {
                            /^(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?$/.test(value) ?
                                callback() : callback('链接不合法')
                        }
                    }],
                }
            }
        },
        methods: {
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
            cancelLink: function () {
                this.$refs.hyperlinkForm.resetFields();
                this.popoverShow = false
            },
            onEditorChange: function(quill){
                this.$emit('editor-change',quill)
            }
        },
        mounted: function () {//初始赋值

        }
    })
</script>
<style>
/* 回复框组件样式 */
.ms-padding-twenty{
	padding:20px !important;
}

.message-tabs{
	flex: 1;
    border-radius: 4px 4px 0 0 !important;
    border: none !important;
	height:100%;
	}
.message-tabs .el-tabs__header{
	margin:0px;
}

.message-tabs .el-tabs__header .el-tabs__nav-scroll {
    padding: 0 20px;
    border: 1px solid #e6e6e6;
}
/* 回复框内容部分 */
.message-tabs .el-tabs__content {
    border: 1px solid #e6e6e6;
    height: calc(100% - 42px);
    position:relative;
	padding:10px 20px
}
.message-tabs .el-tabs__content .el-tab-pane {
  border:0px !important;
  width: 100%;
  height:100%;
  display: flex;
  justify-content: space-between;
}
.message-tabs .el-tabs__content .el-tab-pane > .el-form {
    width: 100%;
}
.message-tabs .el-tabs__content .el-tab-pane > .el-form .ms-message-content .el-form-item__content .ql-container{
	height: 145px;
    border-bottom: 1px solid #e6e6e6;
}
.message-tabs .el-tabs__content .el-tab-pane >div:first-child{
	    margin-right: 20px;
}
.message-tabs .el-tabs__content .el-tab-pane > span  .show-graphic >.show-image{
	height: 130px;
    display: flex;
    align-items: center;
}  

.message-tabs .el-tabs__content .el-tab-pane .chooseLabel{
  flex: 1;
  border: 1px dashed #e6e6e6;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
    cursor: pointer;
}
.message-tabs .el-tabs__content .el-tab-pane .chooseLabel i{
	font-weight: bolder;
    font-size: 20px;
    color: #09f;
}
.message-tabs .el-tabs__content .el-tab-pane .chooseLabel span{
	margin-top: 8px;
    line-height: 1;
}

.ql-tooltip{
    display: none
}

/* quill富文本内容样式 */
.ql-editor > p{
	display:inline;
}
/* quill富文本内容样式 END */

/* 回复框内样式 */
.el-tabs__content .el-tab-pane  .el-icon-delete{
     position:absolute;
     right:15px !important;
     bottom:12px !important;
    cursor: pointer;
 }
.el-tabs__content .el-tab-pane  .el-icon-delete:hover {
    color: #09f;
    background: #fff;
    border-color: #09f;
}

.show-check-img{
	height: 160px;
    width: 160px;
    padding: 10px;
    display: flex;
    flex-flow: row nowrap;
    align-items: center;
    justify-content: center;
    border: 1px solid lightsteelblue;
    position:relative;
    margin-top: 10px;
}
.show-check-img .show-image-model{
	width: 100%;
    height: 100%;
    background: rgba(203, 203, 203,1.4);
    display: flex;
    flex-flow: column nowrap;
}
.show-check-img .vedio{
    width: 100%;
    height: 100%;
    display: flex;
    flex-flow: column nowrap;
}
.voiceContent ,.videoContent,.imgFileName{
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    text-align: center;
    line-height: 30px;
}
.show-check-img .show-image-model img,
.show-check-img .vedio img{
	width:100%;
	height:85%;
	object-fit: cover;
    object-position: center center;
    border-radius:6px;
}

.show-graphic{
    padding: 0 10px;
    display: flex;
    border-bottom: 1px solid #e6e6e6;
    flex-direction: column;
    line-height: 2em;
    box-shadow: 0px 0px 10px #888;
    border: 1px solid rgb(239, 239, 240);
    max-width: 300px;
}
.show-graphic span{
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: block;
    cursor: pointer;
}
.show-graphic img {
    width: 100%;
    height: 146px;
    margin: 5px auto;
    border-radius: 4px;
    object-fit: contain;
}

.show-graphic p {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    font-weight: initial;
    font-size: 12px;
    color: #999;
}
/* 回复框内样式End */
/* 微信背景log */
.wechat-log{
    display: flex;
    align-items: center;
    justify-content: flex-end;
    position: absolute;
    top: 10px;
    left: 10px;
    width: calc(100% - 20px);
    color: white;
    height: 25% !important;
    background-color: rgba(143, 143, 143,0.6);
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
}
.wechat-log > i{
	margin-right:10px;
}
/* 微信背景log End*/
</style>