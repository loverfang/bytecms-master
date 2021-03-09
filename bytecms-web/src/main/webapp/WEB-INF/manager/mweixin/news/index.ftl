<!--图文素材页-->
<div id="weixin-news" v-if="weixinVue.menuActive == '图文'" v-cloak>
	<el-container>
		<!--右侧头部-->
		<el-header class="ms-header" height="50px">
				<el-row>
					<@shiro.hasPermission name="news:saveupdate"><el-button type="primary" size="mini" icon="el-icon-plus" onclick="newsFormVue.open()">新增</el-button></@shiro.hasPermission>
					<@shiro.hasPermission name="news:sync"><el-button :loading="syncLoading" class="ms-fr" size="mini" icon="el-icon-upload" @click='sync'>同步微信图文</el-button></@shiro.hasPermission>
				</el-row>
			</el-header>
			<el-container >
				<!--内容头部-->
				<el-header class="ms-tr ms-header ms-header-select">
					<el-row type="flex" align='middle' gutter='20' :span='12' justify="end" style="height: 36px;">
						<el-col :span="3">
							<el-select v-model="selectOption" placeholder="请选择" @change="newsList" size='mini'>
								<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
								</el-option>
							</el-select>
						</el-col>
						<el-col :span="7">
							<el-input size='mini' placeholder="请输入图文标题" v-model='articleTitle' class='basic-title-input ms-fr' clearable>
							</el-input>
						</el-col>
						<el-col :span="2" class="ms-weixin-news-button">
							<el-button type="primary" class="ms-fr" icon="el-icon-search" size="mini" @click='newsList'>查询</el-button>
						</el-col>
					</el-row>
				</el-header>
				<!--素材列表-->
				<el-main  v-loading="loading" class="ms-weixin-news-list" style="position:relative">
					<ms-empty  msg='图文' :click.self="open" v-if='!showNewsList.length && !loading'></ms-empty>
					<el-scrollbar style="height: calc(100vh - 160px); width:100%" ref='graphicsScroll'>
					<waterfall :col='4' :width="itemWidth"  :gutter-Width="gutterWidth" :data="showNewsList">
						<template >
							<div class="ms-weixin-news-item" v-for="(article,index) of showNewsList" :key='index'>
								<div class="head">
									<span v-text="'更新于'+formmateTime(article.newsDateTime)"></span>
									<i class="iconfont icon-weixin" v-if='article.newsIsSyn'></i>
								</div>
								<div class="body">
									<span v-text="article.newsArticle && article.newsArticle.articleTitle"></span>
									<img v-if="article.newsArticle" :src="imgUrl(article.newsArticle.articleThumbnails)" onerror="this.src='${base}/static/mweixin/image/cover.jpg'"/>
									<p v-text="article.newsArticle && article.newsArticle.basicDescription"></p>
								</div>
									<div v-for="(element,index) in article.articleList" :key="index" class="ms-article-item">
										<p v-text='element.articleTitle'></p>
										<img :src="imgUrl(element.articleThumbnails)"
											 onerror="this.src='${base}/static/mweixin/image/thumbnail.jpg'">
									</div>
								<div class="footer">
									<@shiro.hasPermission name="news:edit"><i class="el-icon-edit" @click="newsFormVue.open(article)"></i></@shiro.hasPermission>
									<em></em>
									<@shiro.hasPermission name="news:del"><i class="el-icon-delete" @click='del(article,index)'></i></@shiro.hasPermission>
								</div>
							</div>
						</template>
					</waterfall>
						</el-scrollbar>
				</el-main>
			</el-container>
	</el-container>
</div>
<script>
	var newsVue = new Vue({
		el: "#weixin-news",
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
			}
		},
		data: {
			options: [{
				value: '全部图文',
				label: '全部图文'
			}, {
				value: '微信图片',
				label: '微信图文'
			}],
			selectOption: '全部图文',                   //选中的下拉选项v-model
			isLoadMore: true,	    //是否懒加载
			masterArticle: {
				articleTitle: '', //标题
			},
			page:1,			//加在图文的页数
			showNewsList:[],
			loading:true,//加载状态
            articleTitle:"",//搜索标题
			syncLoading:false,
	},
		methods: {
			open(){
				newsFormVue.open();
			},
			//获取素材数据
			getNewsData:function(){
				var that = this
				this.loading = true
				ms.http.get(ms.manager + "/mweixin/news/list.do", {
					basicTitle: that.articleTitle,
					isSync:that.selectOption=='全部图文'?'':true,
					pageSize:10,
					pageNo:this.page
				}
				).then(function (res) {
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
						//最初加载不显示
						if(that.page!=1) {
							that.$notify.success('没有更多图文加载了!');
						}
					}

				}, function (err) {
					that.loading = false
					that.$notify.error(err)
				});
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
					that.$refs.graphicsScroll.wrap.addEventListener('scroll', that.loadMore);
					that.$waterfall.resize();		//瀑布流刷新
                 });
            },
			loadMore:function(){
			    var scrollElt = this.$refs.graphicsScroll.wrap;	//滚动条节点
			    	<!-- 滚动条所能滚动的高度 = (滚动条滑动的总高度 - 滚动条的高度) -->
				var scrollTop =  scrollElt.scrollTop;		//滚动条的位置
				var scrollHeight = scrollElt.scrollHeight;   	//滚动条滑动的总高度
				var scrollsetHeight = scrollElt.offsetHeight;			//滚动条高度
				if ((scrollHeight - scrollsetHeight) - scrollTop <10) {

				 if(this.isLoadMore){
					this.page += 1;
					//加载数据
					this.getNewsData();
					}
				}
			},
			// 删除
			del: function (newMer, index) {
				var that = this;
				that.$confirm('您正在执行删除该图文的操作，是否继续?','提示 !',{
					confirmButtomText:'确定',
					cacelButtomText:'取消',
					type:'warning'
				}).then(function(){
				ms.http.post(ms.manager + "/mweixin/news/" + newMer.newsId + "/delete.do")
					.then(function (res) {
						if (res.result) {
							that.$notify.success('素材删除成功');
                            that.newsList()
						}
					}, function (err) {
						that.$notify.error(err);
					});
				}).catch(function(){
					that.$notify({
						type:'info',
						message:'您已取消删除操作'
					});
				});
			},
			// 同步微信素材
			sync: function () {
				var that = this;
				that.syncLoading=true;
				that.$notify.warning("正在同步，请不要刷新页面,预计需要1-2分钟");
				ms.http.get(ms.manager + "/mweixin/news/sync.do").then(function (res) {
						that.$notify.success("同步成功");
					that.newsList();
					that.syncLoading=false;
				}, function (err) {
					that.$notify.error(err)
					that.syncLoading=false;
				})
			},
			// 格式化时间
			formmateTime: function (time) {
				var updateTime = /^[0-9]{4}-[0-9]{2}-[0-9]{2}/.exec(time)
				if (updateTime != null) {
					return updateTime[0]
				}
			}
		},
		created:function(){
			this.loading = true
			this.newsList()
		},
	});
</script>
<style>

	#weixin-news {
		width: 100%;
		height: 100%;
	}

	#weixin-news > .el-container {
		width: 100%;
		height: 100%;
	}

	#weixin-news > .el-container .ms-header > .el-select--small:first-child {
		width: 100px !important;
	}

	#weixin-news > .el-container .ms-header .ms-weixin-news-button {
		width: auto;
	}

	#weixin-news > .el-container .ms-weixin-news-list {
		margin: 12px;
		padding: 14px;
		display: flex;
		flex-wrap: wrap;
		background: #fff;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item {
		padding: 0 10px;
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		border: 1px solid #e6e6e6;
		border-radius: 4px;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .head {
		border-bottom: 1px solid #e6e6e6;
		padding: 10px 0;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item {
		margin-bottom: 6%;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .head span {
		color: #333;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .head .icon-weixin {
		font-weight: initial;
		font-size: 16px;
		color: #9acd32;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .body {
		display: flex;
		border-bottom: 1px solid #e6e6e6;
		flex-direction: column;
		line-height: 2em;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .ms-article-item {
		width: 100%;
		height: 100%;
		display: flex;
		justify-content: space-between;
		padding: 10px 0;
		border-bottom: 1px solid #e6e6e6;
		position: relative;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item p {
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		word-break: break-all;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item img {
		width: 50px;
		height: 50px;
		margin-right: 10px;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .body span {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		display: block;
		cursor: pointer;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .body span:hover {
		color: #0099ff;
		background: #fff;
		border-color: #0099ff;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .body img {
		width: 100%;
		height: 120px;
		margin: 0 auto;
		border-radius: 4px;
		object-fit: contain;
		object-position: center center;
	}

	#weixin-news > .el-container .ms-weixin-news-list .vue-waterfall .vue-waterfall-column {
		margin-bottom: 2%;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .body p {
		margin: 0;
		color: #333;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
		font-weight: initial;
		font-size: 12px;
		color: #999;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .footer {
		display: flex;
		padding: 14px 0;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .footer i {
		color: #333;
		margin: auto;
		cursor: pointer;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .footer i:hover {
		color: #0099ff;
		background: #fff;
		border-color: #0099ff;
	}

	#weixin-news > .el-container .ms-weixin-news-list .ms-weixin-news-item .footer em {
		width: 1px;
		height: 1em;
		background: #e6e6e6;
	}

	.is-vertical .el-header .el-row .ms-fr {
		margin-left: 10px;
	}

	#weixin-news .ms-weixin-news-list .el-scrollbar__wrap{
		margin-right: -9px !important;
	}
	#news-vue > .el-container > .ms-header > .el-row > button.el-button--mini.el-button, .ms-header button.ms-fr {
		height: 28px;
	}
</style>