package net.bytecms.core.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
/**
 *
 * FileInfo 文件信息封装类
 *
 */
@Data
@Accessors(chain = true)
public class FileInfo {

    private String fileName;
    private boolean directory;
    private Date lastModifiedTime;
    private Date lastAccessTime;
    private Date creationTime;
    private String relativePath;
    private String parentRelativePath;
    private long size;

    public FileInfo(){

    }

    /**
     * @param fileName
     * @param directory
     * @param attrs
     */
    public FileInfo(String fileName, boolean directory, BasicFileAttributes attrs) {
        this.fileName = fileName;
        this.directory = directory;
        this.lastModifiedTime = new Date(attrs.lastModifiedTime().toMillis());
        this.lastAccessTime = new Date(attrs.lastAccessTime().toMillis());
        this.creationTime = new Date(attrs.creationTime().toMillis());
        this.size = attrs.size();
    }
}
