package com.sana.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.constants.RoleConstants;
import com.sana.domain.entity.*;
import com.sana.mapper.SanaReplyMapper;
import com.sana.mapper.SanaTopicMapper;
import com.sana.service.ISanaTopicService;
import com.sana.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        String userId = UserContext.getUser().getUser().getId();
        topicInstance.setCreator(userId);
        topicInstance.setCreateTime(LocalDateTime.now());
        topicInstance.setStatus(1);
        topicInstance.setUpdater(userId);
        topicInstance.setUpdateTime(LocalDateTime.now());
        int insert = sanaTopicMapper.insert(topicInstance);
        return insert == 1;
    }

    @Override
    public boolean updateTopic(SanaTopic topicInstance) {
        String userId = UserContext.getUser().getUser().getId();
        // 修改
        topicInstance.setUpdater(userId);
        topicInstance.setUpdateTime(LocalDateTime.now());
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
