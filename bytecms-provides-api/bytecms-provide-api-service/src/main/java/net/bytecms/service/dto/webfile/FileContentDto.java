package net.bytecms.service.dto.webfile;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FileContentDto {

    @NotBlank(message = "filePath 不能为空")
    private String filePath;

    @NotNull(message = "文件内容不能为null")
    private String fileContent;
}
