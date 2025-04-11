package com.sana.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 分页类
 * @Version: 1.0
 */
@Data
public class MyPage<T> {
    //当前页码
    @TableField(exist = false)
    private Integer pageNum;
    //每页显示条数
    @TableField(exist = false)
    private Integer pageSize;
    //总条数
    @TableField(exist = false)
    private Long total;
    //总页数
    @TableField(exist = false)
    private Integer pages;
    //数据
    @TableField(exist = false)
    private List<T> pageData;
}
