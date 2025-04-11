package com.sana.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sana.domain.entity.SanaForum;
import com.sana.domain.entity.SanaTopic;
import com.sana.response.PageR;

import java.util.List;

public interface ISanaForumService extends IService<SanaForum> {
    /**
     * 新建论坛
     * @param sanaForum 只需要论坛名和论坛描述
     */
    void createNewForum(SanaForum sanaForum);

    /**
     * 获取所有论坛
     */
    List<SanaForum> getAllForums();

    List<SanaForum> getBelongedForum(String token);

    PageR<List<SanaTopic>> getForumBelongedTopics(String forumId);
}
