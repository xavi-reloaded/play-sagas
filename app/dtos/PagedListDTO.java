package dtos;

import io.ebean.PagedList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedListDTO <T> {
    List<T> data;
    Integer pageIndex;
    Integer pageSize;
    Boolean hasPrev;
    Boolean hasNext;
    Integer rowCount;
    Integer totalPageCount;

    public PagedListDTO(){ }

    public PagedListDTO(List<T> data){
        this.data = data;
    }

    public PagedListDTO(PagedList<T> pagedList) {
        this.setData(pagedList.getList());
        this.setPageIndex(pagedList.getPageIndex());
        this.setPageSize(pagedList.getPageSize());
        this.setRowCount(pagedList.getTotalCount());
        this.setTotalPageCount(pagedList.getTotalPageCount());
        this.setHasNext(pagedList.hasNext());
        this.setHasPrev(pagedList.hasPrev());
    }
}
