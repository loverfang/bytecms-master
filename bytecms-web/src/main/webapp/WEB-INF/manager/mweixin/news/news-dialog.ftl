<!-- 选择图文 -->
<div id="ms-news-dialog"  v-cloak>
    <el-dialog title="选择图文" :visible.sync="isShow" custom-class='ms-weixin-dialog border-radius-five'>
        <el-main  v-loading="loading" class="ms-weixin-news-list" style="position:relative">
            <ms-empty :size="'large'" msg='图文' :click="openNew" v-if='!showNewsList.length && !loading'></ms-empty>
                <el-scrollbar style="height: calc(100vh - 150px);width:100%" ref='graphicsScroll'>
            	    <waterfall ref="s" :col='4' :width="itemWidth"  :gutter-Width="gutterWidth" :data="showNewsList">
                    <template v-for="(article,index) of showNewsList">
                    <div class="graphic-model" @click="clickedGraphic(article,index)">
                        <div class="ms-weixin-news-item"  :key='index'>
                            <div class="head hidden-long-words">
                                <span v-text="'更新于'+formmateTime(article.newsDateTime)"></span>
                                <i class="iconfont icon-weixin" v-if='article.newsIsSyn'></i>
                            </div>
                            <div class="body hidden-long-words">
                                <span v-text="article.newsArticle && article.newsArticle.articleTitle"></span>
                                <img :src="ms.base+article.newsArticle.articleThumbnails" onerror="this.src='${base}/static/mweixin/image/cover.jpg'"/>
                                <p v-text="article.newsArticle && article.newsArticle.articleDescription"></p>
                            </div>
                            <div v-for="(element,index) in article.articleList" :key="index" class="ms-article-item">
                                <p class="ms-two-line" v-text='element.articleTitle'></p>
                                <img :src="imgUrl(element.articleThumbnails)"
                                     onerror="this.src='${base}/static/mweixin/image/thumbnail.jpg'" class="subimg">
                            </div>
                        </div>
                        <div class="clicked-background" v-if="chooseGraphic == (index+1)">
                            <i class="iconfont icon-icon"></i>
                        </div>
                      </div>
                    </template>
                </waterfall>
                </el-scrollbar>
        </el-main>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="choosed() " size='mini'>确 定</el-button>
            <el-button @click="isShow = false" size='mini'>取 消</el-button>
        </div>  
    </el-dialog>
</div>

<script>
    var newsForm = new Vue({
        el: '#ms-news-dialog',
        computed:{
			itemWidth(){
				return (138*0.5*(document.documentElement.clientWidth/375))
			},
			gutterWidth(){
				return (9*0.5*(document.documentElement.clientWidth/375))
			},
            //判断是否是外链接生成图片地址,是则直接返回链接不然使用项目名拼接
            imgUrl(url){
                return function (url) {
                    reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;   //正则表达式判断http：//    https：//  为合法
                    objExp = new RegExp(reg);
                    if (reg.test(url)) {
                        return url;
                    }else {
                        return ms.base + url
                    }
                }
            },
        },
        data: {
            isShow: false,
            showNewsList:[],
            loading:true,//加载状态
			chooseGraphic:0,		//选中的图文
            isLoadMore: true,	    //是否懒加载
            page:0,
        },
        watch:{
            isShow:function (n,o) {
                if(!n){//取消选中
                    this.chooseGraphic = 0
                }
            }
        },
        methods: {
            // 表单打开
            open: function () {
                this.isShow = true;
                this.newsList();
            },
            openNew:function (){
                this.isShow = false;
                newsFormVue.open()
            },
            // 获取微信素材
            newsList: function () {
                var that = this;
                //刷新列表
                this.showNewsList = [];
                this.isLoadMore = true; //懒加载功能
                this.page = 1;
                this.getNewsData();//初始化
                this.$nextTick(function () {
                    that.addScrollEvent();
                    that.$waterfall.resize();		//瀑布流刷新
                });
            },
            clickedGraphic: function(article,index){  //图文点击事件处理
            	this.chooseGraphic = (index+1);
            	},
            choosed:function(){  //将选中图文传入显示页面
            	this.isShow = false
            	var graphicObj = this.showNewsList[this.chooseGraphic-1];
                switch (weixinVue.menuActive) {
                    case "关键词表单":
                        if (graphicObj.articleList.length){
                            this.$notify.error("只能选择单图文");
                            break;
                        }
                        passiveMessageFormVue.chooseGraphic = graphicObj;
                        break;
                    case "被动回复"://不写break会到下一个
                    case "关注时回复":
                        if (graphicObj.articleList.length){
                            this.$notify.error("只能选择单图文");
                            break;
                        }
                        messageVue.chooseGraphic = graphicObj;
                        break;
                    case "自定义菜单":
                        if (graphicObj.articleList.length){
                            this.$notify.error("只能选择单图文");
                            break;
                        }
                        menuVue.chooseGraphic = graphicObj;
                        break;
                    case "单独发送":
                        if (graphicObj.articleList.length){
                            this.$notify.error("只能选择单图文");
                            break;
                        }
                    case "一键群发":
                        groupReply.chooseGraphic = graphicObj;
                        break;
                }
            },

            getNewsData:function(){
                var that = this
                this.loading = true
                ms.http.get(ms.manager + "/mweixin/news/list.do", {
                    articleTitle: that.articleTitle,
                    pageSize:10,
                    pageNo:this.page
                }).then(function (res) {
                    that.loading = false
                    if (res.data.rows.length > 0) {
                        //追加数据
                        that.showNewsList = that.showNewsList.concat(res.data.rows);
                        that.$nextTick(function () {
                            that.$waterfall.resize();		//瀑布流刷新
                        });
                    } else {
                        //没数据加载禁用懒加载
                        that.isLoadMore = false;
                        that.$notify.success('所有图文已经加载完成，没有更多图文加载了!');
                    }

                }, function (err) {
                    that.loading = false
                    that.$notify.error(err)
                });
            },
            addScrollEvent:function(){		//添加滚动条监听事件
                this.$refs.graphicsScroll.wrap.addEventListener('scroll', this.loadmore);
            },
            loadmore:function(){
                var scrollElt = this.$refs.graphicsScroll.wrap;	//滚动条节点
                <!-- 滚动条所能滚动的高度 = (滚动条滑动的总高度 - 滚动条的高度) -->
                var scrollTop =  scrollElt.scrollTop;		//滚动条的位置
                var scrollHeight = scrollElt.scrollHeight;   	//滚动条滑动的总高度
                var scrollsetHeight = scrollElt.offsetHeight;			//滚动条高度
                if(scrollTop === (scrollHeight-scrollsetHeight)){
                    if(this.isLoadMore){
                        this.page += 1;
                        //加载数据
                        this.getNewsData();
                    }
                }
            },
            formmateTime: function (time) {
                var updateTime = /^[0-9]{4}-[0-9]{2}-[0-9]{2}/.exec(time)
                if (updateTime != null) {
                    return updateTime[0]
                }
            }
        },

        mounted: function() {

        }
    })
</script>
<style>
    #ms-news-dialog .hidden-long-words{/* 让过长的文字隐藏  */
        overflow: hidden;
        text-overflow:ellipsis;
        white-space: nowrap;
    }
    #ms-news-dialog .hidden-long-words p{
        margin: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        font-weight: initial;
        font-size: 12px;
        color: #999;
    }

    #ms-news-dialog .ms-weixin-dialog.border-radius-five{
        width:845px;
        height:570px;
    }
    #ms-news-dialog .ms-weixin-dialog.border-radius-five .el-dialog__header{
        padding:15px 10px 5px 10px;
    }
    #ms-news-dialog .ms-weixin-dialog.border-radius-five .el-dialog__body{
        height:450px;
        padding:0px 0px;
    }
    #ms-news-dialog .ms-weixin-dialog.border-radius-five .el-dialog__body .el-container{
        height:100%;
    }

    #ms-news-dialog .ms-weixin-dialog.border-radius-five .el-dialog__body .el-aside{
        height:100%;
        border-left:1px solid #e6e6e6;
        padding:10px 5px;
    }

    #ms-news-dialog .ms-weixin-dialog.border-radius-five .el-dialog__body .el-aside .pictrue-group li{
        justify-content: center;
    }

    #ms-news-dialog .ms-weixin-dialog.border-radius-five .el-dialog__body .el-main{
        height:100%;
        padding:0px;
    }

    #ms-news-dialog  > .border-radius-five .ms-weixin-dialog  .el-main.ms-weixin-news-list {
        background: #fff;
        margin: 12px;
        padding: 14px;
        display: flex;
        flex-flow: row wrap;
        justify-content: space-between;
        padding: 10px 0px;
        font-size: 10px;
    }
    #ms-news-dialog .ms-weixin-news-item .ms-article-item {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        border-top: 1px solid #e6e6e6;
        position: relative;
    }

    #ms-news-dialog  .ms-two-line{
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        word-break: break-all;
    }

    #ms-news-dialog .el-main.ms-weixin-news-list  .graphic-model{
        border-top-left-radius: 9px;
        border-top-right-radius: 9px;
        margin: 10px;
        border: 1px solid rgb(237,237,237);
        position: relative;
    }
    #ms-news-dialog .el-main.ms-weixin-news-list  .graphic-model .ms-weixin-news-item{
        padding: 0px 10px;
    }
    #ms-news-dialog .clicked-background{
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

    #ms-news-dialog .clicked-background i{
        font-size: 52px;
        color: white;
        position: absolute;
        bottom: 20%;
    }

    #ms-news-dialog .el-main.ms-weixin-news-list  .graphic-model .head{
        border-bottom: 2px solid rgb(237, 237, 237);
        height: 47px;
        display: flex;
        flex-flow: row nowrap;
        justify-content: space-between;
        align-items: flex-end;
        padding: 6px 0px;
    }
    #ms-news-dialog .el-main.ms-weixin-news-list  .graphic-model .body{
        display: flex;
        flex-flow: column nowrap;
        margin-bottom: 5px;
    }

    #ms-news-dialog .el-main.ms-weixin-news-list  .graphic-model .body span:first-child{
        padding: 10px 0px;
        font-weight: bolder;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        display: block;
        cursor: pointer;
    }
    #ms-news-dialog .el-main.ms-weixin-news-list  .graphic-model .body img{
        max-width:200px;
        max-height: 220px;
        border-radius:5px;
        object-fit: contain;
    }

    #ms-news-dialog .subimg{
        width: 50px;
        height: 50px;
        margin-right: 10px;
    }
</style>