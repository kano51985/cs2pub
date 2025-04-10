package com.sana.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    // 当前页
    private Long pageNum = 1L;
    // 每页的数据量
    private Long pageSize = 10L;
}
