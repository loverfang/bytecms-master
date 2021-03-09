<!-- 新建图文 -->
<div id='news-form' class="ms-news-form" v-show="weixinVue.menuActive == '新建图文'" v-cloak>
   <el-container class="ms-admin-picture">
      <!--右侧头部-->
      <el-header class="ms-header" height="50px">
         <el-row>
            <!-- 添加隐藏按钮，主要是为了产生间距 -->
            <el-button size="mini" type="text"></el-button>
            <el-button class="ms-fr" size="mini"  @click="weixinVue.menuActive = '图文'"><i class="iconfont icon-fanhui"></i>返回</el-button>
            <@shiro.hasPermission name="news:saveupdate">
               <el-button class="ms-fr" type="primary" size="mini"  @click="save"><i class="iconfont icon-icon-"></i>保存</el-button>
            </@shiro.hasPermission>
         </el-row>
      </el-header>
      <el-container class="ms-container">
         <el-aside width="280px">
            <!-- 主图文章 -->
            <div class="ms-main-article" @click='openMainArticle'>
               <img :src="ms.base+newsBean.newsArticle.articleThumbnails ||　ms.base+'/WEB-INF/manager/images/article-default.png'" onerror="this.src='${base}/static/mweixin/image/cover.jpg'">
               <div class="ms-article-mask"></div>
               <span v-text='newsBean.newsArticle.articleTitle'></span>
            </div>
            <!-- 子文章列表 -->
            <draggable v-model="newsBean.articleList" :options="{draggable:'.ms-article-item'}">
               <div v-for="(element,index) in newsBean.articleList" :key="index" class="ms-article-item" @click='updateSubArticle(element,index)'>
                  <p>
                     <span v-text='element.articleTitle'></span>
                  </p>
                  <img :src="ms.base+element.articleThumbnails ||　ms.base+'/WEB-INF/manager/images/article-default-thumb.jpg'"
                     onerror="this.src='${base}/static/mweixin/image/thumbnail.jpg'">
                  <div class="ms-article-item-mask"><i class="el-icon-delete" @click='deleteArticle(element,index)'></i></div>
               </div>
            </draggable>
            <div class="ms-article-footer">
               <el-button size="mini" icon='el-icon-plus' @click='addArticle'>添加图文</el-button>
            </div>
         </el-aside>
         <el-main>
            <div class="ms-main-header">
               <el-upload class="ms-pic-upload" :show-file-list="false" :on-success="basicPicSuccess" :before-upload='beforeBasicPicUpload'
                          :data="{uploadPath:'/${weixinId}/weixin/news/image'}"
                           accept=".jpg,.jpeg,.png"
                          :action="ms.web + '/file/upload.do'" >
                  <img :src=" ms.base+articleForm.articleThumbnails" class="article-thumbnail" @mouseover='headMask=true;'
                     @mouseleave='headMask=false;' v-if="articleForm.articleThumbnails" />
                  <template v-else="!articleForm.articleThumbnails">
                     <i class="el-icon-picture"></i>
                     <span>添加封面</span>
                  </template>
               </el-upload>
               <el-form :model="articleForm" :rules="rules" ref="articleForm" label-width='56px'>
                  <el-row>
                     <el-col :span="24">
                        <el-form-item label="标题" prop="articleTitle">
                           <el-input size='small' placeholder="请输入图文标题" v-model='articleForm.articleTitle' class='basic-title-input'
                                     @input.self="resetWordNum('articleTitle',64)">
                              <span slot='suffix' v-text="titleWordNumber+'/64'"></span>
                           </el-input>
                        </el-form-item>
                     </el-col>
                  </el-row>
                  <el-row>
                     <el-col :span="12">
                        <el-form-item label="作者" prop="articleAuthor">
                           <el-input size='small' placeholder="请输入图文作者" v-model='articleForm.articleAuthor' @input.self="resetWordNum('articleAuthor',8)" >
                              <span slot='suffix' v-text="authorWordNumber+'/8'"></span>
                           </el-input>
                        </el-form-item>
                     </el-col>
                  </el-row>
                  <el-row>
                     <el-col :span="24">
                        <!-- @input="resetWordNum(120)" -->
                        <el-form-item label="摘要" prop="articleDescription" >
                           <el-input size='small'  type='textarea' placeholder="选填，如果不写会默认抓取正文前120个字" :autosize="{ minRows: 3, maxRows: 3}"
                                     resize='none' style="margin-top:3px;" v-model='articleForm.articleDescription'>
                              <span slot='suffix' v-text="descWordNumber+'/120'" ></span>
                           </el-input>
                        </el-form-item>
                     </el-col>
                  </el-row>


               </el-form>
            </div>
            <div class="ms-main-body">
               <!-- 百度编辑器 -->
               <script id="ueditorArticle" type="text/plain" name="articleContent"></script>
            </div>
         </el-main>
      </el-container>
   </el-container>
</div>
<script>
   var newsFormVue = new Vue({
      el: '#news-form',
      data: {
         titleWordNumber: 64, //图文标题剩余字数
         authorWordNumber: 8, //图文作者剩余字数
         descWordNumber: 120, //摘要
         editor: null, //富文本实例
         editorCurrentContent: null, //当前百度编辑器输入的内容
         articleForm: {
            articleTitle: '', //标题
            articleAuthor: '', //作者
            articleDescription: '', //摘要
            articleContent: '', //正文
            articleThumbnails: '', //上传封面图片的url
         },
         thumbnailShow: false, //显示缩略图
         headMask: false, //缩略图删除
         //  文章素材，包括主图文，子图文
         deleteArticleList:[],//存放删除的集合
         //素材实体
         newsBean: {
            articleList: [],
            newsArticle: {},
            newsCategoryId: "0",
            newsType: "text"
         },
         //表单验证
         rules: {
            articleTitle: [{
                  required: true,
                  message: '请输入标题',
                  trigger: 'blur'
               },
               {
                  min: 1,
                  max: 64,
                  message: '长度在 1 到 64 个字符',
                  trigger: 'blur'
               }
            ],
            articleAuthor: [{
                  required: true,
                  message: '请输入作者',
                  trigger: 'blur'
               },
               {
                  min: 1,
                  max: 8,
                  message: '长度在 1 到 8 个字符',
                  trigger: 'blur'
               }
            ],
            articleDescription: [{
                  required: true,
                  message: '请输入摘要',
                  trigger: 'blur'
               },
               {
                  min: 1,
                  max: 54,
                  message: '长度在 1 到 54 个字符',
                  trigger: 'blur'
               }
            ],
         }
      },
      watch: {
         editorCurrentContent: {
            handler: function (n, o) {
               this.articleForm.articleContent = n
            },
            deep: true,
         }
      },
      methods: {
         open: function (newsBean) {
            weixinVue.menuActive = '新建图文';
           if(newsBean){
              this.newsBean = newsBean;
              //如果图文素材没有子文章重新初始化
              if(!this.newsBean.articleList){
                 this.newsBean.articleList =[]
              }
           }else {
              this.newsBean = {
                 articleList: [],
                 newsArticle: {},
                 newsCategoryId: "0",
                 newsType: "text"
              };
           }
            this.resetForm()
         },
         //   图片上传之前进行数据校验
         beforeBasicPicUpload: function (file) {
            var fileType = null;
            ['image/jpeg', 'image/png', 'image/jpg'].indexOf(file.type) > -1 ? fileType = true : fileType =
               false
            var isLt2M = file.size / 1024 / 1024 < 2;
            !fileType && this.$notify.error('文章配图只能是 JPG、JPEG、PNG 格式!');
            !isLt2M && this.$notify.error('文章配图大小不能超过 2MB!');
            return fileType && isLt2M;
         },
         //   图片上传成功函数
         basicPicSuccess: function (url) {
            this.articleForm.articleThumbnails = url
            this.$forceUpdate()
         },
         deleteArticle:function(data,index){
            var that = this;
            this.$confirm('此操作将永久删除该图文, 是否继续?', '提示', {
               confirmButtonText: '确定',
               cancelButtonText: '取消',
               type: 'warning'
            }).then(() => {
               //将要删除的文章存入集合
           if(data.id){
              ms.http.post(ms.manager + "/mweixin/article/delete.do",[data],{
                 headers: {
                    'Content-Type': 'application/json'
                 }
              })
           }
               that.newsBean.articleList.splice(index,1);
            })
         },
         // 添加文章
         addArticle: function () {
            if (this.newsBean.articleList.length > 6) {
               return this.$notify({
                  title: '添加失败',
                  message: '最大图文数量为7',
                  type: 'warning'
               });
            }
            this.editor.setContent('')
            var form={
               articleTitle: '',
               articleAuthor: '',
               articleDescription: '',
               articleContent: '',
               articleThumbnails: '',
            }
            // 将左侧表单的内容存放到数组中
            this.newsBean.articleList.push(form)
            // 清空表单
            this.articleForm = form
            this.thumbnailShow = false //显示上传图标
         },
         // 打开修改子文章
         updateSubArticle: function (element, index) {
            this.articleForm = element

            // 设置百度编辑器
            this.editor.setContent(this.newsBean.articleList[index].articleContent)
         },
         // 打开 主文章
         openMainArticle: function () {
            this.articleForm = this.newsBean.newsArticle
            // thumbnailShow=true 显示图片，否则显示图标
            this.articleForm.articleThumbnails ? this.thumbnailShow = true : this.thumbnailShow = false
            // 设置百度编辑器
            this.editor.setContent(this.newsBean.newsArticle.articleContent)
         },
         // 计算剩余字数
         resetWordNum: function (type, limit) {
            var target = event.target
            type.indexOf('Title') > -1 ? this.titleWordNumber = limit - event.target.value.length : this.authorWordNumber =
               limit - event.target.value.length
            if (event.target.value.length >= limit) {
               this.$notify.error('超出字数限制，请输入不超过' + limit + '字符');
               this.$nextTick(function () {
                  //  这里的event的type是message
                  this.articleForm[type] = target.value.slice(0, limit - 1);
               })
            }
         },
         //  保存微信文章
        save: function () {
            //   表单校验
            var that = this;
            this.$refs.articleForm.validate(function (pass,object){
               if (pass) {
                  if (Object.keys(that.newsBean.newsArticle).length <=1) {
                     return that.$notify.error("主文章不能为空");
                  }


                  //保存或者更新
                  ms.http.post(ms.manager + "/mweixin/news/saveOrUpdate.do", that.newsBean,{
                     headers: {
                        'Content-Type': 'application/json'
                     }
                  }).then(function (res) {
                     if (res.result) {
                        weixinVue.menuActive = '图文'
                        that.$notify({
                           title: '成功',
                           message: '保存成功',
                           type: 'success'
                        });
                     } else {
                        that.$notify({
                           title: '失败',
                           message: res.msg,
                           type: 'warning'
                        });
                     }
                  })
               }

            });
         },
         // 表单重置 新建和修改场景
         resetForm: function () {
               this.thumbnailShow = this.newsBean.newsId ? true : false
            //默认表单打开第一个文章
            this.articleForm = this.newsBean.newsArticle
            //判断是否存是编辑
            if(this.newsBean.newsId){
               var result = this.articleForm.articleContent ? this.articleForm.articleContent : ''
               this.editor.setContent(result)
            }else {
               this.editor.setContent('')
            }


         },
      },
      mounted: function () {
         let that = this;
         //富文本加载
         if (this.editor == null) {
            this.editor = UE.getEditor('ueditorArticle', {
               toolbars: [
                  ['fullscreen', 'undo', 'redo', '|', 'bold', 'italic', 'underline',
                     'strikethrough',
                     'removeformat', 'blockquote',
                     '|', 'forecolor', 'backcolor', 'insertorderedlist',
                     'insertunorderedlist', '|', 'attachment', 'simpleupload', 'link'
                  ]
               ],
               imageScaleEnabled: true,
               autoHeightEnabled: true,
               autoFloatEnabled: true,
               scaleEnabled: false,
               compressSide: 0,
               maxImageSideLength: 2000,
               maximumWords: 80000,
               zIndex: 1000,
               elementPathEnabled: false,
               wordCount: false,
               initialFrameWidth: '100%',
               initialFrameHeight: 500,
               serverUrl: ms.base + "/static/plugins/ueditor/1.4.3.1/jsp/editor.do?jsonConfig=%7BvideoUrlPrefix:\'" + ms.base + "\',fileUrlPrefix:\'" + ms.base + "\',imageUrlPrefix:\'" + ms.base + "\',imagePathFormat:\'/upload/weixin/newsImg/editor/%7Btime%7D\',filePathFormat:\'/upload/weixin/newsFile/editor/%7Btime%7D\',videoPathFormat:\'/upload/weixin/newsVideo/editor/%7Btime%7D\'%7D",
               UEDITOR_HOME_URL: ms.base + '/static/plugins/ueditor/1.4.3.1/'
            });
            this.editor.ready(function () {
               $(that.editor.iframe.contentDocument.activeElement).addClass("ms-webkit-scrollbar").before(
                  "<style>.ms-webkit-scrollbar::-webkit-scrollbar,::-webkit-scrollbar{width:10px;/*滚动条宽度*/height:1.5%;/*滚动条高度*/}/*定义滚动条轨道内阴影+圆角*/.ms-webkit-scrollbar::-webkit-scrollbar-track,::-webkit-scrollbar-track{border-radius:10px;/*滚动条的背景区域的圆角*/background-color:#eeeeee;/*滚动条的背景颜色*/}.ms-task-content::-webkit-scrollbar-track{border-radius:10px;background-color:#FFFFFF;}/*定义滑块内阴影+圆角*/.ms-webkit-scrollbar::-webkit-scrollbar-thumb,::-webkit-scrollbar-thumb{border-radius:10px;/*滚动条的圆角*/background-color:#dddddd;/*滚动条的背景颜色*/}</style>"
               );
            });
            this.editor.addListener('contentChange', function () {
               that.editorCurrentContent = that.editor.getContent();
               //  that.$notify.success(that.editor.getContent());
            });
         }
      }
   })
</script>
<style>
    #news-form .ms-container .el-main .ms-main-header .el-form-item .el-form-item__content .el-form-item__error {
        margin-top: -11px;
    }
    #news-form .ms-container .el-main .ms-main-header .el-form-item .el-form-item__content .el-textarea + .el-form-item__error {
        margin-top: 0px !important;
    }
    #news-form .ms-container .el-main .ms-main-header .el-form-item .el-form-item__content input {
        margin-bottom: 9px;
        height: 25px;
        line-height: 25px;
    }
    #news-form .ms-container .el-main .ms-main-header .el-form-item .el-form-item__content .el-input__suffix .el-textarea__inner {
        margin-top: 5px;
    }


    #news-form {
        display: flex;
        justify-content: space-between;
        width: 100%;
        background: 0 0 !important;
        padding: 0 !important;
    }

    #news-form .ms-main-article:hover {
        cursor: pointer;
    }

    #news-form .ms-main-article .ms-article-mask {
        background: #000;
        opacity: .2;
        width: 100%;
        height: 146px;
        position: absolute;
        top: 0;
        left: 0;
    }

    #news-form .ms-main-article .ms-article-mask:hover {
        cursor: pointer;
    }

    #news-form .el-container .ms-container {
        padding: 0;
        align-items: flex-start;
        display: flex;
        flex-flow:row nowrap;
       background: 0;
    }

    #news-form .el-container .el-aside {
        padding: 14px;
        background: #fff;
        border: 1px solid #ddd;
    }

    #news-form .el-container .el-aside .ms-main-article {
        position: relative;
    }

    #news-form .el-container .el-aside .ms-main-article img {
        width: 100%;
        height: 146px;
    }

    #news-form .el-container .el-aside .ms-main-article span {
        position: absolute;
        bottom: 0;
        margin: 15px;
        color: #fff;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        word-break: break-all;
    }

    #news-form .el-container .el-aside .ms-article-item {
        width: 100%;
        height: 70px;
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-bottom: 1px solid #e6e6e6;
        position: relative;
    }

    #news-form .el-container .el-aside .ms-article-item p {
        margin: 0 10px;
        display: flex;
        justify-content: space-between;
        align-items: center
    }

    #news-form .el-container .el-aside .ms-article-item p span {
        width: 100%;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
         word-break: break-all;
    }

    #news-form .el-container .el-aside .ms-article-item img {
        width: 50px;
        height: 50px;
        margin-right: 10px;
    }

    #news-form .el-container .el-aside .ms-article-item .ms-article-item-mask{
        opacity: .8;
        width: 100%;
        height: 50%;
        position: absolute;
        bottom: 0;
        justify-content: flex-end;
        align-items: center;
       background-color: #ccc;
       display: none;
    }

    #news-form .el-container .el-aside .ms-article-item .ms-article-item-mask>i {
        font-weight: initial;
        font-size: 14px;
        color: #672424;
        margin: 0 10px 0 auto;
       cursor: pointer;
    }

    #news-form .el-container .el-aside .ms-article-item:hover .ms-article-item-mask{
        display: flex;
     }

    #news-form .el-container .el-aside .ms-article-footer {
        background: #fff;
        padding-top: 20px
    }

    #news-form .el-container .el-aside .ms-article-footer .el-button {
        width: 100%;
        background: #f2f2f6
    }

    #news-form .el-container .el-aside .ms-article-footer .el-button i,
    #news-form .el-container .el-aside .ms-article-footer .el-button span
    {
        font-weight: initial;
        font-size: 12px;
        color: #999
    }

    #news-form .el-container .el-main {
        flex: 1;
        margin-left: 14px;
        padding: 0 !important;
    }

    #news-form .el-container .el-main .ms-main-header {
        position: relative;
        background: #fff;
        display: flex;
        justify-content: space-between;
        height: 200px;
        padding: 20px 20px 24px;
        box-sizing: border-box;
        border-bottom: 1px solid #e6e6e6;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload {
        width: 140px;
        height: 100%;
        border-radius: 4px;
        border: 1px dashed #e6e6e6;
        display: flex;
        justify-content: center;
        flex-direction: column;
        margin-right: 20px;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload .el-upload
    {
        display: flex;
        justify-content: space-between;
        flex-direction: column;
        align-items: center;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload .el-upload span
    {
        font-weight: initial;
        font-size: 12px;
        color: #999;
        margin-top: 4px;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload .el-upload i
    {
        font-weight: initial;
        font-size: 18px;
        color: #999;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload .el-upload .article-thumbnail
    {
        width: 140px;
        height: 140px;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload .el-upload>.ms-article-mask
    {
        width: 140px;
        height: 140px;
        display: flex;
        justify-content: center;
        align-items: center;
        left: 21px;
        top: 20px;
        z-index: 99;
        opacity: .6;
    }

    #news-form .el-container .el-main .ms-main-header .ms-pic-upload .el-upload>.ms-article-mask>i
    {
        font-weight: initial;
        font-size: 19px;
        color: #fff;
    }

    #news-form .el-container .el-main .ms-main-header .el-form {
        flex: 1;
        margin: 0 !important;
        display: flex;
        justify-content: space-between;
        flex-direction: column;
    }

    #news-form .el-container .el-main .ms-main-header .el-form .el-form-item
    {
        margin-bottom: 0 !important;
    }


    #news-form .el-container .el-main .ms-main-header .el-form .el-form-item .el-input__suffix>input
    {
        padding-right: 50px !important
    }

    #news-form .el-container .el-main .ms-main-header .el-form .el-form-item .basic-title-input>.el-input__inner
    {
        padding-right: 54px !important
    }

    #news-form .el-container .el-main .ms-main-header .el-form .el-form-item:last-child
    {
        padding-top: 4px
    }

    #news-form .el-container .el-main .ms-main-body {
        height: calc(100vh - 275px);
        background: #fff
    }

    #news-form .el-container .el-main .ms-main-body .edui-editor-toolbarbox
    {
        border: none !important;
        box-shadow: none !important
    }

    #news-form .el-container .el-main .ms-main-body .edui-default .edui-editor
    {
        border: none !important
    }

    #news-form .el-container .el-main .ms-main-body .edui-default .edui-editor .edui-editor-toolbarboxouter
    {
        background-color: none !important;
        background-image: none !important;
        box-shadow: none !important;
        border-bottom: none !important
    }

    #news-form .el-container .el-main .ms-main-body .edui-default .edui-editor .edui-editor
    {
        border: none !important
    }

    #news-form .el-container .el-main .ms-main-body .edui-default .edui-editor .edui-editor .edui-editor-toolbarbox
    {
        box-shadow: none !important
    }

    #news-form .el-container .el-main .ms-main-body .edui-default .edui-editor .edui-editor .edui-editor-toolbarbox .edui-editor-toolbarboxouter
    {
        background-color: transparent !important;
        background-image: none !important;
        border: none !important;
        border-radius: 0 !important;
        box-shadow: none !important
    }

    #news-form .el-container .el-main .ms-main-body .edui-editor-toolbarboxinner
    {
        background: #fff;
    }
	#news-form .iconfont{
		font-size: 12px;
		margin-right: 5px;
	}
</style>