<el-dialog id="form" v-cloak title="小程序" :visible.sync="dialogVisible" width="50%">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right" size="small">
            <el-form-item  label="引用id" prop="appId"
>
                <el-input-number
                        v-model="form.appId"


                        :disabled="false"
                        controls-position=""
                        :style="{width:'100%'}">
                </el-input-number>
            </el-form-item>
            <el-form-item  label="名称" prop="wmName"
>
                <el-input v-model="form.wmName"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入名称">
                </el-input>
            </el-form-item>
            <el-form-item  label="AppId" prop="wmAppId"
>
                <el-input v-model="form.wmAppId"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入AppId">
                </el-input>
            </el-form-item>
            <el-form-item  label="AppSecret" prop="wmAppSecret"
>
              <template slot='label'>AppSecret
                    <el-popover placement="top-start" title="提示" trigger="hover">
                        小程序证书，前往小程序开发设置查看
                        <i class="el-icon-question" slot="reference"></i>
                    </el-popover>
                </template>
                <el-input v-model="form.wmAppSecret"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入AppSecret">
                </el-input>
            </el-form-item>
            <el-form-item  label="商户Id" prop="wmMchid"
>
                <el-input v-model="form.wmMchid"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入商户Id">
                </el-input>
            </el-form-item>
            <el-form-item  label="支付key" prop="wmPayKey"
>
                <el-input v-model="form.wmPayKey"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入支付key">
                </el-input>
            </el-form-item>
            <el-form-item  label="附件" prop="wmPayFile"
>
            <el-upload
                    :file-list="form.wmPayFile"
                    :action="ms.base+'/file/upload.do'"
                    :on-remove="wmPayFilehandleRemove"
                    :style="{width:''}"
                    :limit="5"
                    accept=".p12"
                    :on-exceed="wmPayFilehandleExceed"
                    :disabled="false"
                    :on-success="wmPayFileSuccess"
                    :data="{uploadPath:'/miniapp/miniApp','isRename':true}">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">最多上传5张文件</div>
            </el-upload>
            </el-form-item>
            </el-form>    <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保存</el-button>
    </div>
</el-dialog>
<script>
        var form = new Vue({
        el: '#form',
        data() {
            return {
                saveDisabled: false,
                dialogVisible:false,
                //表单数据
                form: {
                    // 引用id
                    appId:0,
                    // 名称
                    wmName:'',
                    // AppId
                    wmAppId:'',
                    // AppSecret
                    wmAppSecret:'',
                    // 商户Id
                    wmMchid:'',
                    // 支付key
                    wmPayKey:'',
                    // 附件

                    wmPayFile: [],
                },
                rules:{
                },

            }
        },
        watch:{
            dialogVisible:function (v) {
                if(!v){
                    this.$refs.form.resetFields();
                }
            }
        },
        computed:{
        },
        methods: {
            open(id){
                if (id) {
                    this.get(id);
                }
                this.$nextTick(function () {
                    this.dialogVisible = true;
                })
            },
            save() {
                var that = this;
                var url = ms.manager + "/miniapp/miniApp/save.do"
                if (that.form.id > 0) {
                    url = ms.manager + "/miniapp/miniApp/update.do";
                }
                this.$refs.form.validate((valid) => {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        if(data.wmPayFile){
                             data.wmPayFile = JSON.stringify(data.wmPayFile);
                         }
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.dialogVisible = false;
                                indexVue.list();
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                            }
                            that.saveDisabled = false;
                        });
                    } else {
                        return false;
                    }
                })
            },

            //获取当前小程序
            get(id) {
                var that = this;
                ms.http.get(ms.manager + "/miniapp/miniApp/get.do", {"id":id}).then(function (res) {
                    if(res.result&&res.data){
                    if(res.data.wmPayFile){
                        res.data.wmPayFile = JSON.parse(res.data.wmPayFile);
                        res.data.wmPayFile.forEach(function(value){
                        value.url= ms.base + value.path
                        })
                    }else{
                        res.data.wmPayFile=[]
                    }
                        that.form = res.data;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            },
            //wmPayFile文件上传完成回调
            wmPayFileSuccess:function(response, file, fileList) {
                this.form.wmPayFile.push({url:file.url,name:file.name,path:response,uid:file.uid});
            },
            //上传超过限制
            wmPayFilehandleExceed:function(files, fileList) {
                this.$notify({ title: '当前最多上传5个文件', type: 'warning' });},
            wmPayFilehandleRemove:function(file, files) {
                var index = -1;
                index = this.form.wmPayFile.findIndex(text => text == file);
                if (index != -1) {
                    this.form.wmPayFile.splice(index, 1);
                }
            },
        },
        created() {
        }
    });
</script>
