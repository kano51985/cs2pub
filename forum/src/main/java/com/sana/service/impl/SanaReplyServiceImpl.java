package com.sana.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.domain.entity.SanaReply;
import com.sana.mapper.SanaReplyMapper;
import com.sana.service.ISanaReplyService;
import org.springframework.stereotype.Service;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴下的回复业务类
 * @Version: 1.0
 */

@Service
public class SanaReplyServiceImpl extends ServiceImpl<SanaReplyMapper, SanaReply> implements ISanaReplyService {
}
