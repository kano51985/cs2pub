package com.sana.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sana.domain.entity.SanaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<SanaUser> {
    SanaUser selectByUsername(@Param("account") String account,@Param("accountType") int accountType);

}
