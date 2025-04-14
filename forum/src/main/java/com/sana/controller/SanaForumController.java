package com.sana.controller;


import com.sana.annotation.PermissionCheck;
import com.sana.domain.entity.MyPage;
import com.sana.domain.entity.SanaForum;
import com.sana.domain.entity.SanaTopic;
import com.sana.response.PageR;
import com.sana.response.R;
import com.sana.service.ISanaForumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛控制器
 * @Version: 1.0
 */

@RestController
@Slf4j
@RequestMapping("forum")
public class SanaForumController {
    @Autowired
    private ISanaForumService sanaForumService;

    /**
     * 新建论坛（需要论坛名和论坛描述）
     * @param sanaForum
     */
    @PostMapping
    @PermissionCheck("admin")
    public void createNewForum(@RequestBody SanaForum sanaForum) {
        sanaForumService.createNewForum(sanaForum);
    }

//    /**
//     * 查看所有论坛
//     * @return
//     */
//    @GetMapping("forum")
//    @PermissionCheck(value = {"admin","stuff","supporter","premium","member"}
//        ,logical = PermissionCheck.Logical.OR)
//    public R getAllForums(){
//        List<SanaForum> allForums = sanaForumService.getAllForums();
//        return R.success(allForums);
//    }

    /**
     * 获取用户所拥有权限的论坛
     */
    @GetMapping("{forumId}")
    public R getUsersForum(){
        List<SanaForum> sanaForums = sanaForumService.getBelongedForum();
        return R.success(sanaForums);
    }

    /**
     * 访问指定论坛，获取论坛下的所有可用帖子
     */
    @GetMapping("{forumId}/topic")
    public R getForumBelongedTopics(@PathVariable("forumId") String forumId,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size){
        MyPage<SanaTopic> belongedTopics = sanaForumService.getForumBelongedTopics(forumId,page,size);
        return R.success(belongedTopics);
    }

    /**
     * 删除论坛
     */
    @DeleteMapping("{forumId}")
    @PermissionCheck("admin")
    public R deleteForum(@PathVariable("forumId") String forumId){
        boolean b = sanaForumService.removeById(forumId);
        return R.success(b);
    }

    /**
     * 修改论坛
     */
    @PutMapping("{forumId}")
    @PermissionCheck("admin")
    public R updateForum(@PathVariable("forumId") String forumId,@RequestBody SanaForum sanaForum){
        sanaForum.setId(forumId);
        boolean b = sanaForumService.updateById(sanaForum);
        return R.success(b);
    }
}
