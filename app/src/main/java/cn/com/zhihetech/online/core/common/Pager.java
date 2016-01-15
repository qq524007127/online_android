package cn.com.zhihetech.online.core.common;

import java.io.Serializable;

/**
 * 分页实体类
 * Created by ShenYunjie on 2015/11/16.
 *
 * @version 1.0
 */
public class Pager implements Serializable, Cloneable {

    private Integer page = Constant.DEFAULT_PAGE;
    private Integer rows = Constant.DEFAULT_ROWS;

    public Pager() {
        super();
    }

    public Pager(Integer rows) {
        this.rows = rows <= 0 ? Constant.DEFAULT_ROWS : rows;
    }

    /**
     * @param page 页数
     * @param rows 每页数据条数
     */
    public Pager(Integer page, Integer rows) {
        this();
        this.page = page < 1 ? 1 : page;
        this.rows = rows < 0 ? Constant.DEFAULT_ROWS : rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
