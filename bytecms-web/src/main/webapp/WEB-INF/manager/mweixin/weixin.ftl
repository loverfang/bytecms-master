<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <#include "/include/head-file.ftl"/>
    <#include "/mweixin/include/head-file.ftl"/>
    <style>
        [v-cloak] {
            display: none;
        }
        #ms-weixin .ms-weixin-menu {
            min-height: 100vh;
            min-width: 140px;
        }

        #ms-weixin .ms-weixin-menu .ms-header {
            border-right: solid 1px #e6e6e6;
        }

        #ms-weixin .ms-weixin-menu .ms-header div {
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
        #ms-weixin .ms-weixin-menu .ms-header div i {
            display: inline-block;
            text-align: center;
            line-height: 1.4em;
            width: 1.4em;
            height: 1.4em;
            border-radius: 4px;
            color: #fff;
            font-size: 1.4em;
            background: #02CF5D;
        }

        #ms-weixin .ms-weixin-menu .ms-header >div>span.el-tooltip.ms-ellipsis{
            height:17px;
            width:100px;
            margin:0 10px;
        }

        #ms-weixin .ms-weixin-menu .el-main {
            padding: 0;
        }

        #ms-weixin .ms-weixin-menu .el-main .ms-weixin-menu-menu {
            min-height: calc(100vh - 50px);
            background: #fff;
        }

        #ms-weixin .ms-weixin-menu .el-main .ms-weixin-menu-menu .el-menu-item {
            min-width: 140px;
            width: 100%;
        }


        #ms-weixin .ms-weixin-menu .el-main .ms-weixin-material-item {
            min-width: 100% !important
        }
        #ms-weixin .ms-weixin-menu-menu-item , .el-submenu__title {
            height: 40px !important;
            line-height: 46px !important;
        }
        .ms-table-pagination {
            height: calc(100% - 70px);
        }
    </style>
</head>
<body style="overflow: hidden">

    <div id="ms-weixin"  v-cloak>
        <!--左侧-->
        <el-container class="ms-weixin-menu">
            <!--右侧头部-->
            <el-header class="ms-header" height="50px">
                <div onclick="javascript:history.go(-1)">
                    <i class="iconfont icon-weixin" style="cursor:pointer" ></i>
                    <el-tooltip content="${weixinName}" placement="top-end" effect="light">
                    <span class="ms-ellipsis ms-hover">
                       ${weixinName}
                       </span>
                    </el-tooltip> 
                </div>
            </el-header>
            <el-main>
                <el-menu class="ms-weixin-menu-menu" default-active="0-0">
                    <template v-for="(menu,i) in menuList">
                        <!--单个选项-->
                        <el-menu-item :index="i+''" @click="menuActive = menu.title" v-if="!menu.sub" v-text="menu.title"
                            class="ms-weixin-menu-menu-item"></el-menu-item>
                        <!--多个选项-->
                        <el-submenu :index="i+''" v-if="menu.sub" class="ms-weixin-submenu">
                            <template slot="title">
                                <span v-text="menu.title"></span>
                            </template>
                            <el-menu-item class="ms-weixin-menu-menu-item" @click="menuActive ='';menuActive = sub.title" :index="i+'-'+index"
                                v-for="(sub,index) in menu.sub" v-text="sub.title"></el-menu-item>
                        </el-submenu>
                    </template>
                </el-menu>
            </el-main>
        </el-container>
    </div>

    <script>
        var weixinVue = new Vue({
            el: "#ms-weixin",
            data: {
                menuList: [
                    <@shiro.hasAnyPermissions name="news:view, picture:view,voice:view,video:view" >
                    {
                    title: '素材管理',
                    sub: [
                        <@shiro.hasPermission name="news:view">
                        {
                        title: '图文'
                    },
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="picture:view">
                        {
                        title: '图片'
                    },
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="voice:view">
                        {
                        title: '语音'
                    },
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="video:view">
                        {
                        title: '视频'
                    }
                        </@shiro.hasPermission>
                    ],
                }
                ,
                    </@shiro.hasAnyPermissions>
                    <@shiro.hasPermission name="weixin:menu:view">
                    {
                    title: '自定义菜单',
                },
                    </@shiro.hasPermission>
                    <@shiro.hasPermission name="people:view">
                    {
                    title: '微信用户',
                },
                    </@shiro.hasPermission>
                    <@shiro.hasAnyPermissions name="followMessage:view, passiveMessage:view,keywordMessage:view" >
                    {
                    title: '自动回复',
                    sub: [
                        <@shiro.hasPermission name="followMessage:view">
                        {
                        title: '关注时回复'
                    },
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="passiveMessage:view">
                        {
                        title: '被动回复'
                    },
                            </@shiro.hasPermission>
                        <@shiro.hasPermission name="keywordMessage:view">
                        {
                        title: '关键词回复'
                    }
                        </@shiro.hasPermission>
                    ]
                },
                    </@shiro.hasAnyPermissions>
                    <@shiro.hasPermission name="reply:view">
                    {
                    title: '群发',
                        sub: [
                            {
                                title: '一键群发'
                            },
                            {
                                title: '群发记录'
                            },
                        ]
                }
                    </@shiro.hasPermission>

                ], //左侧导航列表
                menuActive: '图文',       //默认选中
            },
            watch: {
                menuActive: function (n,o) {
                    switch(this.menuActive){
                        case '关注时回复':
                        case '被动回复':
                            messageVue.messageList();
                            break;
                        case '关键词回复':
                            keywordVue.list();
                            break;
                        case '图片':
                            pictureVue.picList();
                            break;
                        case '图文':
                            newsVue.newsList();
                            break;
                        case '语音':
                            voiceVue.voiceInitData();
                            break;
                        case '视频':
                            videoVue.videoInitData();
                            break;
                        case '一键群发':
                            groupReply.newsContent = '';
                            groupReply.imgContent = '';
                            groupReply.chooseGraphic = {};
                            groupReply.messageContent = '';
                            groupReply.voiceContent = '';
                            groupReply.videoContent = '';
                            groupReply.openId = '';
                            break;
                        case '群发记录':
                            groupMessageLogVue.list();
                            break;

                        default:
                            break;
                    }
                },
            }
        })
    </script>
    <#include "/mweixin/file/form.ftl">
    <#include "/mweixin/file/index.ftl">
    <#include "/mweixin/menu/index.ftl">
    <#include "/mweixin/component/ms-empty.ftl">
    <#include "/mweixin/component/ms-create-group.ftl">
    <#include "/mweixin/component/ms-message-reply.ftl">
    <#include "/mweixin/news/index.ftl">
    <#include "/mweixin/news/form.ftl">
    <#include "/mweixin/news/news-dialog.ftl">
    <#include "/mweixin/picture/index.ftl">
    <#include "/mweixin/picture/form.ftl">
    <#include "/mweixin/message/index.ftl">
    <#include "/mweixin/message/keyword-form.ftl">
    <#include "/mweixin/message/keyword.ftl">
    <#include "/mweixin/people/index.ftl">
    <#include "/mweixin/reply/index.ftl">
    <#include "/mweixin/reply/form.ftl">
    <#include "/mweixin/voice/index.ftl">
    <#include "/mweixin/video/index.ftl">
    <#include "/mweixin/voice/form.ftl">
    <#include "/mweixin/video/form.ftl">
</body>
</html>