<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
    <#list table.fields as field>
        <#if (field.name != 'create_time' && field.name != 'update_time' && field.name != 'creator' && field.name != 'modifier' && field.name != 'id')>
            <#if  field.propertyType=='String' >
               <#assign max = field_index+1>
               <#if max<=3 >
               <a-col :md="6" :sm="24">
                      <a-form-item label="${field.comment}">
                        <a-input v-model="queryParam.dto.${field.propertyName}" placeholder=""/>
                      </a-form-item>
               </a-col>
               </#if>
            </#if>
        </#if>
     </#list>
          <a-col :md="6" :sm="24">
            <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
              <a-button type="primary" @click="$refs.table.refresh(true)">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSearchForm()">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="table-operator">
      <a-button type="primary" icon="plus" v-has="{perm:'role:save'}" @click="$refs.formHandle.add()">新建xx</a-button>
    </div>
     <s-table
      ref="table"
      size="default"
      rowKey="id"
      :columns="columns"
      :data="loadData"
    >
    <span slot="action" slot-scope="text, record">
        <template>
        <a @click="handleEdit(record)" v-has="{perm:'role:update'}">编辑</a>
        <a-divider type="vertical" v-has="{perm:'role:update'}"/>
        <a @click="handleDelete(record)" v-has="{perm:'role:delete'}">删除</a>
        <a-divider type="vertical" v-has="{perm:'role:assign'}"/>
        <a @click="handleMenu(record)" v-has="{perm:'role:assign'}">其他操作</a>
        </template>
    </span>
	</s-table>
    <form-handle ref="formHandle" @ok="handleOk" />
  </a-card>
</template>

<script>
import moment from 'moment'
import formHandle from './handle'
import {page,delByPk,batch} from '@/api/${cfg.customModelName?uncap_first}'
import { STable } from '@/components'
export default {
  name: 'TableList',
  components: {
    STable,
    formHandle
  },
  props: {
	  statusFilter: {
	    type: Function,
	    default: undefined
	  },
	},
  data () {
    return {
      mdl: {},
      advanced: false,
      queryParam: {
            dto:{
            }
	  },
      columns: [
      <#list table.fields as field>
      <#if (field.name != 'create_time' && field.name != 'update_time' && field.name != 'creator' && field.name != 'modifier' && field.name != 'id')>
       <#if field.name ?index_of("id")==-1 && field.name ?index_of("Id")==-1>
        {
         title: '${field.comment}',
         dataIndex: '${field.propertyName}'
        },
       </#if>
      </#if>
      </#list>
        {
          title: '操作',
          dataIndex: 'action',
          width: '250px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      loadData: parameter => {
          return page(Object.assign(parameter, this.queryParam)).then(response => {
            return response
          })
      }
    }
  },
  created () {
    this.loadData(this.queryParam)
  },
  methods: {
    handleDelete (record) {
        let that=this;
        this.$confirm({
        title: '操作提醒',
        content: '确定要删除吗?',
        onOk() {
             delByPk({id:record.id}).then(response=>{
                  that.$message.info(response.message);
                  that.$refs.table.refresh();
             }).catch((response) => {that.$message.warn(response.message);});
        },
        onCancel() {},
       });
    },
    handleEdit (record) {
      this.$refs.formHandle.edit(record);
    },
    handleOk () {
      this.$refs.table.refresh();
    },
    resetSearchForm () {
      this.queryParam = {
        dto:{ }
      }
      this.handleOk();
    }
}
}
</script>
