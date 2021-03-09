<template>
  <a-modal
    :title="modalTitle"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
            <a-form-item label="业务ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
            <a-input v-if="modalOpera==1" v-decorator="['id', {rules: [{required: true}]}]" />
            </a-form-item>
        <#list table.fields as field>
         <#if (field.name != 'create_time' && field.name != 'update_time' && field.name != 'creator' && field.name != 'modifier' && field.name != 'id')>
           <a-form-item label="${field.comment}" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['${field.propertyName}', {rules: [{required: true, min: 3, message: '请输入至少三个字符的规则！'}]}]" />
            </a-form-item>
         </#if>
        </#list>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import moment from 'moment'
import {save,getById,update,selectRoles} from '@/api/${cfg.customModelName?uncap_first}'
import pick from 'lodash.pick'
export default {
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 13 }
      },
     modalTitle:'',
     modalOpera:0,//0：新增 1：编辑
     visible: false,
     confirmLoading: false,
     form: this.$form.createForm(this)
    }
  },
	created () {
	},
  methods: {
        add () {
           this.modal('新建xx',0);
        },
		edit (record) {
		   this.modal('编辑xx',1);
			  getById({id:record.id}).then(response=>{
				this.form.setFieldsValue(pick(response.data[0],<#list table.fields as field><#if (field.name != 'create_time' && field.name != 'update_time' && field.name != 'creator' && field.name != 'modifier')>'${field.propertyName}'<#if field_has_next>,</#if></#if></#list>
			  ));
			})
		},
		modal(title,opera){
			this.modalTitle=title;
			this.modalOpera=opera;
			this.visible = true;
			this.form.resetFields();
		},
        handleSubmit () {
          const { form: { validateFields } } = this;
          this.confirmLoading = true
          validateFields((errors, values) => {
            if (!errors) {
                  if(this.modalOpera===0){
                         save(values).then(response =>{
                             this.$message.info(response.message)
                             this.$emit('ok');
                             this.handleCancel();
                         })
                    }else if(this.modalOpera===1){
                         update(values).then(response =>{
                         this.$message.info(response.message)
                         this.$emit('ok');
                         this.handleCancel();
                        })
                    }
            }
            this.confirmLoading = false
          })
        },
        handleCancel () {
            this.visible = false
            this.confirmLoading = false
        }
  }
}
</script>
