package net.bytecms.service.dto.resource;


import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @since 2019-11-11
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysResourceDto extends BaseModel<SysResourceDto> {
    private static final long serialVersionUID = 1L;

        private String id;


        /**
        * 文件唯一标识
        */
        private String fileUid;


        /**
        * 文件名称
        */
        private String fileName;


        /**
        * 标题
        */
        private String fileMd5;


        /**
        * 文件大小kb
        */
        private Long fileSize;


        /**
        * 文件类型
        */
        private String fileType;


        /**
        * 相对地址
        */
        private String filePath;



    /**
        * 全地址
        */
        private String fileFullPath;


        /**
        * 组
        */
        private String groupName;


        /**
        * 业务类型所属哪个模块的
        */
        private String businessType;


        /**
        * 原始名称
        */
        private String orgName;


        /**
        * 更新人名称
        */
        private String modifiedName;


        /**
        * 创建人名称
        */
        private String createName;
}
