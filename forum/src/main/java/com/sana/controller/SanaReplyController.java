package com.sana.controller;


import com.sana.domain.entity.MyPage;
import com.sana.domain.entity.SanaReply;
import com.sana.domain.req.SanaReplyUpdateReq;
import com.sana.response.R;
import com.sana.service.ISanaReplyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛贴回复控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("reply")
@Slf4j
public class SanaReplyController {

    @Autowired
    private ISanaReplyService sanaReplyService;

    /**
     * 查询贴下所有回复
     */
    @GetMapping("/{topicId}")
    public R getTopicReplies(@PathVariable("topicId") String topicId,
                             @RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size) {
        return R.success(sanaReplyService.getTopicBelongedReplies(topicId, page, size));
    }

    /**
     * 回复帖子
     */
    @PostMapping
    public R replyTopic(@RequestBody SanaReply sanaReply) {
        boolean flag =  sanaReplyService.replyTopic(sanaReply);
        return flag ? R.success("回复成功") : R.error("回复失败");
    }

    /**
     * 删除回复
     */
    @DeleteMapping("delete/{id}")
    public R deleteTopic(@PathVariable("id") String id) {
        return R.success(sanaReplyService.getOptById(id));
    }

    /**
     * 修改回复
     */
    @PutMapping("update/{id}")
    public R updateTopic(@RequestBody @Valid SanaReplyUpdateReq req) {
        boolean flag = sanaReplyService.updateByReplyId(req);
        return flag ? R.success("修改成功") : R.error("修改失败");
    }

}
