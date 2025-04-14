package com.sana.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.domain.entity.MyPage;
import com.sana.domain.entity.SanaReply;
import com.sana.domain.entity.SanaTopic;
import com.sana.domain.req.SanaReplyUpdateReq;
import com.sana.mapper.SanaReplyMapper;
import com.sana.service.ISanaReplyService;
import com.sana.utils.UserContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴下的回复业务类
 * @Version: 1.0
 */

@Service
public class SanaReplyServiceImpl extends ServiceImpl<SanaReplyMapper, SanaReply> implements ISanaReplyService {
    @Autowired
    private SanaReplyMapper sanaReplyMapper;
    @Override
    public Page<SanaReply> getTopicBelongedReplies(String topicId, int page, int size) {
        return sanaReplyMapper.selectPage(Page.of(page, size)
                , Wrappers.<SanaReply>lambdaQuery()
                        .eq(SanaReply::getTopicId,topicId));
    }

    @Override
    public boolean replyTopic(SanaReply sanaReply) {
        sanaReply.setStatus(1);
        // 拿到楼层
        Integer maxFloor = sanaReplyMapper.selectList(new LambdaQueryWrapper<SanaReply>()
                        .eq(SanaReply::getTopicId, sanaReply.getTopicId()).last("FOR UPDATE"))
                .stream().map(SanaReply::getFloor).max(Comparator.comparing(x -> x)).orElse(null);
        sanaReply.setFloor(maxFloor + 1);
        int insert = sanaReplyMapper.insert(sanaReply);
        return insert == 1;
    }

    @Override
    public boolean updateByReplyId(SanaReplyUpdateReq req) {
        SanaReply sanaReply = new SanaReply();
        //拷贝实体类，将请求中的属性拷贝到需要修改的实体类中
        BeanUtils.copyProperties(req, sanaReply);
        return this.updateById(sanaReply);
    }
}
