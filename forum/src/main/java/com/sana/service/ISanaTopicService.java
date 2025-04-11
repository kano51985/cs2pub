package com.sana.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.entity.MyPage;
import com.sana.domain.entity.SanaReply;
import com.sana.domain.entity.SanaTopic;
import com.sana.response.PageR;

public interface ISanaTopicService extends IService<SanaTopic> {
    boolean createNewTopic(SanaTopic topicInstance);

    boolean updateTopic(SanaTopic topicInstance);

    boolean deleteTopic(String topicId);

    MyPage<SanaReply> getTopicBelongedReplies(String topicId, int page, int size);
}
