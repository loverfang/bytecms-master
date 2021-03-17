<el-dialog id="form" :title="(addEditForm.type == 1 || addEditForm.type == 0) ? '菜单编辑' : '按钮编辑'" :visible.sync="dialogVisible" width="50%" v-cloak>
    <el-form ref="addEditForm" :model="addEditForm" :rules="rules" label-width="110px" size="mini">

        <el-form-item  label="菜单名称" prop="name">
            <el-input v-model="addEditForm.name" placeholder="请输入标题"></el-input>
        </el-form-item>

        <el-form-item label="菜单类型" prop="type">
            <el-radio-group v-model="addEditForm.type">
                <el-radio :label="0">目录</el-radio>
                <el-radio :label="1">菜单</el-radio>
                <el-radio :label="2">按钮</el-radio>
            </el-radio-group>
        </el-form-item>

        <el-form-item  label="父级菜单" prop="parentId">
            <ms-tree-select ref="treeselect"
                    :props="props"
                    :options="menuList"
                    :value="addEditForm.parentId"
                    :clearable="isClearable"
                    :accordion="isAccordion"
                    @get-value="getValue($event)"></ms-tree-select>
        </el-form-item>

        <el-form-item  label="图标" prop="icon">
            <ms-icon v-model="addEditForm.icon"></ms-icon>
        </el-form-item>

        <el-form-item label="权限标识" prop="perms">
            <template slot='label'>权限标识
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        按钮的权限标识，推荐格式：业务名:update,业务名:del等
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-input v-model="addEditForm.perms" placeholder="请输入权限标识"></el-input>
        </el-form-item>

        <el-form-item prop="API地址" prop="url">
            <template slot='label'>链接地址
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        导航地址，如“model/index.do”
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-input v-model="addEditForm.url" placeholder="请输入链接地址"></el-input>
        </el-form-item>

        <el-form-item prop="orderNum">
            <template slot='label'>排序
                <el-popover placement="top-start" title="提示" trigger="hover" width="400">
                    <template slot-scope="slot">
                        菜单列表会根据倒序排列，左侧导航菜单也会根据倒序排序
                    </template>
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-input v-model.number="addEditForm.orderNum" maxlength="11" placeholder="请输入排序"></el-input>
        </el-form-item>

    </el-form>
    <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保存</el-button>
    </div>
</el-dialog>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                isClearable: false,
                // 可清空（可选）
                isAccordion: true,
                // 可收起（可选）
                name: '',
                props: {
                    // 配置项（必选）
                    value: 'id',
                    label: 'name',
                    children: 'children' // disabled:true
                },
                menuList: [],
                //菜单数据
                menuData: [],
                saveDisabled: false,
                dialogVisible: false,
                //表单数据
                addEditForm: {
                    id: 0,
                    type: 0,      // 类型   0：目录   1：菜单   2：按钮 //请输入菜单类型！
                    parentId: '',   // 父菜单ID
                    name: '',       // 请输入至少三个字符的菜单名称！
                    perms: '',      // 请输入至少四个字符的规则描述！
                    url: '',        // API接口, 请输入至少四个字符的规则描述！
                    icon:'',
                    orderNum:'',    // 菜单顺序
                },
                rules: {
                    name: [{ required: true, message: '请输入标题', trigger: 'blur' }, {min: 1, max: 20, message: '长度不能超过20个字符', trigger: 'change'}],
                    type: [{ required: true, message: '菜单类型不能为空', trigger: 'blur' }],
                    parentId:[{ required: true, message: '请选择父菜单', trigger: 'change'}],
                    orderNum: [{ required: true, message: '排序数字不能为空', trigger: 'blur'}, { type: 'number', message: '排序必须为数字值' }],
                }
            };
        },
        watch: {
            dialogVisible: function (n, o) {
                if (!n) {
                    this.$refs.addEditForm.resetFields();
                }
            },
            menuData: function (n, o) {
                if (n) {
                    this.menuList.push({
                        name: '顶级菜单',
                        id: 0,
                        children: this.menuData
                    });
                }
            }
        },
        methods: {
            open: function (id) {
                this.addEditForm.id = 0;
                this.addEditForm.parentId = '';
                // 点击非新增时的任意菜单
                if (id > 0) {
                    // 加载菜单详情进行展示
                    this.get(id);
                }
                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },

            save: function () {
                var that = this;
                var url = ms.manager + "/menu/save";

                if (that.addEditForm.id > 0) {
                    url = ms.manager + "/menu/update";
                }else{
                    // ID=0说明是新增,不需要设定id值
                    delete this.addEditForm.id;
                }

                if (that.addEditForm.type == 2) {
                    that.addEditForm.icon = '';
                }

                that.$refs.addEditForm.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        if (!that.addEditForm.parentId) {
                            delete that.addEditForm.id;
                        }

                        debugger;
                        var data = JSON.parse(JSON.stringify(that.addEditForm));
                        // 删除某些属性
                        delete data.modelChildList;

                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({ title: '成功', message: '保存成功', type: 'success' });
                                that.saveDisabled = false;
                                that.dialogVisible = false;
                                window.location.href = ms.manager + "page/menu";
                            } else {
                                that.$notify({ title: '失败', message: data.msg, type: 'warning' });
                                that.saveDisabled = false;
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },

            getValue: function (data) {

                // 不能把自己的子菜单作为父菜单
                if (data.node.parentId != null) {
                    // 获得选中节点的祖先节点,包含自身节点
                    console.log(ms.util.treeFindPath(this.menuList, data.node.id))
                    let menuParentIds = ms.util.treeFindPath(this.menuList, data.node.id);
                    var parentIndex = menuParentIds.join(",").split(",").indexOf(this.addEditForm.id);
                    if (parentIndex > -1) {
                        this.$notify({title: '提示', message: '不能把自身的子菜单作为父级菜单', type: 'info' });
                        return false;
                    }
                }

                // 自己不能是自己的父菜单
                if (this.addEditForm.id != 0 && data.node.id == this.addEditForm.id) {
                    this.$notify({ title: '提示', message: '不能把自身作为父级菜单', type: 'info'});
                    return false;
                }

                //按钮不能作为父菜单即按钮没有层级关系
                if (data.node.type == 2) {
                    this.$notify({title: '提示', message: '不能将功能按钮添加为菜单', type: 'info' });
                } else {
                    this.addEditForm.parentId = data.node.id;
                    this.$refs.treeselect.valueId = data.node[this.props.value];
                    this.$refs.treeselect.valueTitle = data.node[this.props.label];
                    data.dom.blur();
                }
            },

            //获取当前菜单
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/menu/info", {id: id}).then(function (data) {
                    if (data.res) {
                        that.addEditForm = data.res;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },

        created: function () {}
    });
</script>
<style>
    #form .el-select{
        width: 100%;
    }
</style>
