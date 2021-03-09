package net.bytecms.service.entity.fragment;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.bytecms.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 * <p>
 * 
 * </p>
 *
 * @author LG
 * @since 2019-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("thinkcms_fragment_attribute")
public class FragmentAttribute extends BaseModel {

private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

        @TableId("id")
        private String id;


    /**
     * 页面片段 数据id
     */

        @TableField("fragment_id")
        private String fragmentId;


    /**
     * 数据
     */

        @TableField("data")
        private String data;


    /**
     * 创建用户id
     */



    /**
     * 修改人id
     */



    /**
     * 创建时间
     */



    /**
     * 修改时间
     */






}
