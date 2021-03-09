package net.bytecms.core.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain =true)
public class TreeFileInfo {

    private String key;

    private String code;

    private String title;

    private String relativePath;

    private Boolean isLeaf ;

    private FileInfo fileInfo;

    private List<TreeFileInfo> children;
}
