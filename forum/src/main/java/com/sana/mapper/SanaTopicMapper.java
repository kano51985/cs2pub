package com.sana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sana.domain.entity.SanaTopic;
import com.sana.mapper.Re.ReBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SanaTopicMapper extends ReBaseMapper<SanaTopic> {
}
