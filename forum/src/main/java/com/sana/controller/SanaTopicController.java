package com.sana.controller;


import com.sana.constants.CacheConstants;
import com.sana.domain.entity.SanaTopic;
import com.sana.mapper.SanaTopicMapper;
import com.sana.response.R;
import com.sana.utils.RedisCacheUtil;
import com.sana.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴控制器
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("topic")
public class SanaTopicController {

    // todo 把所有业务逻辑写到service里面

    @Autowired
    private SanaTopicMapper sanaTopicMapper;

    @Autowired
    private UserUtil userUtil;

    /**
     * 查看指定论坛贴下的回复内容
     */
    @GetMapping("{topicId}")
    public R getTopicReplies(@PathVariable("topicId") String topicId) {
        // todo 根据topicId查询帖子内容
        return null;
    }

    /**
     * 发帖子
     */
    @PostMapping
    public R createNewTopic(@RequestBody SanaTopic topicInstance,@RequestHeader("token") String token) {
        // todo 检查是否拥有论坛权限
        // 拿到uid
        String userId = userUtil.getUserId(token);
        // 新增
        topicInstance.setCreator(userId);
        topicInstance.setCreateTime(LocalDateTime.now());
        topicInstance.setStatus(1);
        topicInstance.setUpdater(userId);
        topicInstance.setUpdateTime(LocalDateTime.now());
        int insert = sanaTopicMapper.insert(topicInstance);
        return insert == 1 ? R.success("发帖成功") : R.error("发帖失败");
    }
    /**
     * 修改帖子
     */
    @PutMapping("{topicId}")
    public R updateTopic(@RequestBody SanaTopic topicInstance,@RequestHeader("token") String token) {
        // 拿到uid
        String userId = userUtil.getUserId(token);
        // 修改
        topicInstance.setUpdater(userId);
        topicInstance.setUpdateTime(LocalDateTime.now());
        int update = sanaTopicMapper.updateById(topicInstance);
        // todo 检查是否是管理员或者是帖子所有者
        return update == 1? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("{topicId}")
    public R deleteTopic(@PathVariable("topicId") String topicId,@RequestHeader("token") String token) {
        // 拿到uid
        String userId = userUtil.getUserId(token);
        // 删除
        int delete = sanaTopicMapper.deleteById(topicId);
        // todo 检查是否是管理员或者是帖子所有者
        return delete == 1? R.success("删除成功") : R.error("删除失败");
    }

}
