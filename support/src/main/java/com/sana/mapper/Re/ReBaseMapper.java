package com.sana.mapper.Re;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sana.request.PageParam;
import com.sana.response.PageR;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ReBaseMapper<T> extends BaseMapper<T> {
    /**
     * 分页封装
     * 参数1：条件：分页的数据
     * 参数2：wrapper: 查询条件
     */
    default PageR<T> selectPage(PageParam pageParam, @Param("ew") Wrapper<T> wrapper) {
        // 定义分页
        IPage<T> page = new Page<>();
        // 设置页码
        page.setCurrent(pageParam.getPageNum());
        page.setSize(pageParam.getPageSize());
        // 查询逻辑
        selectPage(page,wrapper);
        return new PageR<>(page.getRecords(),page.getTotal());
    }
}
