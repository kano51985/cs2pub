package com.sana.service.impl;


import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.sana.constants.RoleConstants;
import com.sana.domain.entity.*;
import com.sana.mapper.SanaReplyMapper;
import com.sana.mapper.SanaTopicMapper;
import com.sana.request.PageParam;
import com.sana.response.PageR;
import com.sana.service.ISanaTopicService;
import com.sana.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴业务类
 * @Version: 1.0
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class SanaTopicServiceImpl extends ServiceImpl<SanaTopicMapper, SanaTopic> implements ISanaTopicService {
    @Autowired
    private SanaTopicMapper sanaTopicMapper;
    @Autowired
    private SanaReplyMapper replyMapper;
    @Override
    public boolean createNewTopic(SanaTopic topicInstance) {
        topicInstance.setStatus(1);
        int insert = sanaTopicMapper.insert(topicInstance);
        // 按理来说，发帖设置完标题后还需要设置content内容，同时发帖者是1楼
        if (insert == 1){
            // 向回复表插入关联内容
            SanaReply reply = new SanaReply();
            reply.setContent(topicInstance.getContent());
            reply.setFloor(1);
            reply.setTopicId(topicInstance.getId());
            reply.setStatus(1);
            int replyIns = replyMapper.insert(reply);
            return replyIns == 1;
        }
        return false;

    }

    @Override
    public boolean updateTopic(SanaTopic topicInstance) {
        /*
          判断当前用户是否是帖子的创建者，或者是否是管理员
         */
        boolean perCheck = checkTopicPermission(topicInstance);
        if (!perCheck){
            return false;
        }
        int update = sanaTopicMapper.updateById(topicInstance);
        return update == 1;
    }

    @Override
    public boolean deleteTopic(String topicId) {
        // 获取帖子信息
        SanaTopic matchedTopic = sanaTopicMapper.selectById(topicId);
        // 检查删除权限
        boolean perCheck = checkTopicPermission(matchedTopic);
        if (!perCheck){
            return false;
        }
        // 执行删除
        int i = sanaTopicMapper.deleteById(topicId);
        return i == 1;
    }

    @Override
    public MyPage<SanaReply> getTopicBelongedReplies(String topicId, int page, int size) {
        // 创建Page对象
        //PageMethod.startPage(page, size);
        Page<SanaReply> replies = new Page<>(page, size);
        // 构建查询条件
        LambdaQueryWrapper<SanaReply> wp = new LambdaQueryWrapper<SanaReply>()
                .eq(SanaReply::getTopicId, topicId);
        // 执行分页查询
        Page<SanaReply> sanaReplies = replyMapper.selectPage(replies,null);
        MyPage<SanaReply> myPage = new MyPage<>();
        myPage.setPageNum((int)sanaReplies.getCurrent());
        myPage.setPageSize((int)sanaReplies.getSize());
        myPage.setTotal(sanaReplies.getTotal());
        myPage.setPages((int)sanaReplies.getPages());
        myPage.setPageData(sanaReplies.getRecords());
        return myPage;
    }


    /**
     * 判断当前用户是否是帖子的创建者，或者是否是管理员
     */
    private boolean checkTopicPermission(SanaTopic topicInstance) {
        SanaUser user = UserContext.getUser().getUser();
        if (topicInstance.getCreator().equals(user.getId())
                || user.getRoleList().stream().map(SanaRole::getRole).toList().contains(RoleConstants.ADMIN)){
            return true;
        }
        return false;
    }
}
