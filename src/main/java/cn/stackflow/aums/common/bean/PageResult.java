package cn.stackflow.aums.common.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-06 17:47
 */
@Getter
@Setter
public class PageResult<T> {

    private int page = 1;
    private int size = 20;
    private long total;
    private List<T> rows;


    public static PageResult create(int page,int size){
        PageResult pageResult = new PageResult();
        pageResult.setPage(page);
        pageResult.setSize(size);
        return pageResult;
    }

}
