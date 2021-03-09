package net.bytecms.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: PageDto
 * @Author: LG
 * @Date: 2019/5/15 13:25
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDto<D> implements Serializable {

    private static final long serialVersionUID = 1L;

    private D dto;

    // 总记录数
    private long total;
    // 列表数据
    private List<D> rows;

    // 当前第几页
    private long pageNo=1;

    //总页数
    private long pageCount;

    // 一页多少条
    private long pageSize=10;

    //是否还有下一页
    private boolean hasNext=true;

    public PageDto() {

    }

    public PageDto(long total,  List<D> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageDto(long total, long pageSize, List<D> rows) {
        this.total = total;
        this.rows = rows;
        this.pageCount = (total + pageSize - 1)/pageSize;
    }

    public PageDto(long total, long pageSize, int pageNo,List<D> rows) {
        this.total = total;
        this.rows = rows;
        this.pageNo = pageNo;
        this.pageCount = (total + pageSize - 1)/pageSize;
    }

    public PageDto(long total,long pageCount,long  pageNo, List<D> rows) {
        this.total = total;
        this.pageCount = pageCount;
        this.pageNo = pageNo;
        this.rows = rows;
    }

    public PageDto(long total, List<D> rows, boolean hasNext) {
        this.total = total;
        this.rows = rows;
        this.hasNext=hasNext;
    }
}
