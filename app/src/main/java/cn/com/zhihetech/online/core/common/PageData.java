package cn.com.zhihetech.online.core.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ShenYunjie on 2015/11/16.
 */
public class PageData<T> implements Serializable, Cloneable {
    private long total; //数据总数
    private int page;   //当前第几页
    private int pageSize;   //每页显示数据条数
    private int totalPage;  //数据总共页数
    private List<T> rows;   //数据

    public PageData() {
        super();
    }

    public PageData(long total, Pager pager) {
        this.page = pager.getPage();
        this.pageSize = pager.getRows();
        this.total = total;
        this.totalPage = (int) (total / this.pageSize);
        if (total % this.pageSize != 0) {
            this.totalPage++;
        }
    }

    /**
     * 是否还有更多数据（下一页）
     *
     * @return
     */
    public boolean hasNextPage() {
        return page * pageSize < total;
    }

    /**
     * 获取下一页分页数据
     *
     * @return
     */
    public Pager getNextPage() {
        return new Pager((page + 1), pageSize);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
