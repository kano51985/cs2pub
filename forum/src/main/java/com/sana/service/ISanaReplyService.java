package com.sana.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.entity.MyPage;
import com.sana.domain.entity.SanaReply;
import com.sana.domain.req.SanaReplyUpdateReq;
import jakarta.validation.Valid;

public interface ISanaReplyService extends IService<SanaReply> {
    Page<SanaReply> getTopicBelongedReplies(String topicId, int page, int size);

    boolean replyTopic(SanaReply sanaReply);

    /**
     * 修改回复
     * @param req 修改回去请求类
     * @return 返回是否修改成功
     */
    boolean updateByReplyId(SanaReplyUpdateReq req);
}
