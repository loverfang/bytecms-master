<div id="group-message-log" v-if="weixinVue.menuActive == '群发记录'" class="ms-weixin-content" v-cloak>
    <el-main class="ms-container" >
        <el-table class="ms-table-pagination" ref="multipleTable"  border="true" :data="dataList" tooltip-effect="dark" style="width: 100%">
            <el-table-column prop="pmNewType" label="消息类型"  width="150" align="center" :formatter='formatter'></el-table-column>
            <el-table-column label="发送对象" prop="pmTag"></el-table-column>
            <el-table-column label="发送时间"  align="center"  prop="createDate"></el-table-column>
            <el-table-column label="操作" align="center" width="180">
                <template slot-scope="scope">
                    <el-button  type="text" @click="view(scope.row)" size="mini">查看</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                background :page-sizes="[5, 10, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                :current-page="currentPage"
                class="ms-pagination"
                :page-size="pagesize"
                :total="total" class="ms-pagination"
                @current-change='currentChange'
                @size-change="sizeChange">
        </el-pagination>
    </el-main>
</div>
<script>
    var groupMessageLogVue = new Vue({
        el: '#group-message-log',
        data: {
            dataList: [], //微信被动消息回复列表
            total: 0, //总记录数量
            pagesize: 10, //页面数量
            currentPage: 1, //初始页
            mananger: ms.manager,
            //搜索表单
            searchForm: {
                categoryTitle: ''
            },
        },
        methods: {
            //查询列表
            list: function () {
                var that = this;
                ms.http.get(ms.manager + "/mweixin/message/list.do", {
                    pageNo: that.currentPage,
                    pageSize: that.pagesize,
                    pmType: 'all',
                }).then(
                    function (data) {
                        that.total = data.data.total;
                        that.dataList = data.data.rows;
                    }).catch(function (err) {
                    console.log(err);
                });
            },
            view:function(row){
                weixinVue.menuActive = '群发记录页面'
                groupReply.groupMessageForm = row;
            },
            formatter(row, column, cellValue, index){
                var type={
                    '1': '文本',
                    '2': '图片',
                    '3': '图文',
                    '4': '语音',
                    '5': '视频',
                }
                return type[cellValue] || ''

            },
            //pageSize改变时会触发
            sizeChange: function (pagesize) {
                this.pagesize = pagesize;
                this.list();
            },
            //currentPage改变时会触发
            currentChange: function (currentPage) {
                this.currentPage = currentPage;
                this.list();
            },
        },
        created() {
            this.list();
        },
    })
</script>
<style>
    #group-message-log .ms-table-pagination {
        height: calc(100vh - 120px);
    }
    #group-message-log .ms-container{
        padding-bottom: 0;
        height: calc(100% - 24px);
        padding: 15px;
    }
    #group-message-log .ms-weixin-content{
        height: 100%;
        margin-bottom: 0;
        padding-bottom: 0;
    }
    #group-message-log .ms-pagination {
        padding: 14px 0 0 0;
        text-align: right;
    }
</style>