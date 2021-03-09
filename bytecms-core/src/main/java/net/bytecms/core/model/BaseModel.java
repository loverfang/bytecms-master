package net.bytecms.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import net.bytecms.core.annotation.DirectMark;
import net.bytecms.core.annotation.SolrMark;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by JueYue on 2017/9/14.
 */
@Data
@Accessors(chain = true)
public class BaseModel<M> extends Model {

    public BaseModel(){

    }

    @SolrMark
    @DirectMark
    @TableId(value="id",type = IdType.ID_WORKER)
    private String id;

	/**
     * 创建人
     */
    @TableField(value = "create_id")
    private String createId;


    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改人
     */
    @TableField(value = "modified_id")
    private String modifiedId;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_modified")
    private Date gmtModified;


    @TableField(exist = false)
    private ConditionModel condition;

    public ConditionModel condition(){
        return ConditionModel.build(this);
    }
}
