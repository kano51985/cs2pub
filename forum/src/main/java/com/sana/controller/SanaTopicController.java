package com.sana.controller;


import com.sana.domain.entity.MyPage;
import com.sana.domain.entity.SanaReply;
import com.sana.domain.entity.SanaTopic;
import com.sana.response.R;
import com.sana.service.ISanaTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴控制器
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/topic")
public class SanaTopicController {

    @Autowired
    private ISanaTopicService sanaTopicService;

    /**
     * 查看指定论坛贴下的回复内容
     */
    @GetMapping("/{topicId}")
    public R getTopicReplies(@PathVariable("topicId") String topicId,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size) {
        MyPage<SanaReply> replies = sanaTopicService.getTopicBelongedReplies(topicId, page, size);
        return R.success(replies);
    }

    /**
     * 发帖子
     */
    @PostMapping
    public R createNewTopic(@RequestBody SanaTopic topicInstance) {
        boolean flag = sanaTopicService.createNewTopic(topicInstance);
        return flag? R.success("发帖成功") : R.error("发帖失败");
    }

    /**
     * 修改帖子
     */
    @PutMapping("{topicId}")
    public R updateTopic(@RequestBody SanaTopic topicInstance,@RequestHeader("token") String token) {
        boolean flag = sanaTopicService.updateTopic(topicInstance);
        return flag ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("{topicId}")
    public R deleteTopic(@PathVariable("topicId") String topicId,@RequestHeader("token") String token) {
        boolean flag = sanaTopicService.deleteTopic(topicId);
        return flag ? R.success("删除成功") : R.error("删除失败");
    }

}
