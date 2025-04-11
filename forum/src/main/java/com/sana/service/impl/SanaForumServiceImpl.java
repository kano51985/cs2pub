package com.sana.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sana.constants.CacheConstants;
import com.sana.domain.VO.LoginUserVO;
import com.sana.domain.entity.*;
import com.sana.mapper.SanaForumMapper;
import com.sana.mapper.SanaRoleForumMapper;
import com.sana.mapper.SanaTopicMapper;
import com.sana.response.PageR;
import com.sana.service.ISanaForumService;
import com.sana.utils.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 论坛业务类
 * @Version: 1.0
 */

@Service
public class SanaForumServiceImpl extends ServiceImpl<SanaForumMapper, SanaForum> implements ISanaForumService {
    @Autowired
    private SanaForumMapper sanaForumMapper;
    @Autowired
    private SanaRoleForumMapper sanaRoleForumMapper;
    @Autowired
    private SanaTopicMapper sanaTopicMapper;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Override
    public void createNewForum(SanaForum sanaForum) {
        sanaForum.setStatus(1);
        sanaForum.setCreateTime(LocalDateTime.now());
        sanaForum.setUpdateTime(LocalDateTime.now());
        sanaForumMapper.insert(sanaForum);
    }

    @Override
    public List<SanaForum> getAllForums() {
        return sanaForumMapper.selectList(null);
    }

    @Override
    public List<SanaForum> getBelongedForum(String token) {
        LoginUserVO userInfo = redisCacheUtil.getCacheObject(CacheConstants.LOGIN_USER_KEY + token);
        // 拿到用户权限，根据拥有的权限去查询用户所拥有的论坛
        List<SanaRole> roleList = userInfo.getUser().getRoleList();
        // 1.1 获取角色id去查询关联表
        List<String> roleIds = roleList.stream().map(SanaRole::getId).toList();
        List<SanaRoleForum> sanaRoleForums = sanaRoleForumMapper.selectList(new LambdaQueryWrapper<SanaRoleForum>()
                .in(SanaRoleForum::getRoleId, roleIds));
        Stream<String> belongedForumIds = sanaRoleForums.stream().map(SanaRoleForum::getForumId);
        // 1.2 根据关联表的论坛id获取用户所有论坛
        List<SanaForum> belongedForums = sanaForumMapper.selectList(new LambdaQueryWrapper<SanaForum>()
                .in(SanaForum::getId, belongedForumIds));
        // TODO 后期需要自行手动配置哪个角色拥有哪个论坛的访问权限
        return belongedForums;
    }

    @Override
    public PageR<List<SanaTopic>> getForumBelongedTopics(String forumId) {
        // 直接铁头查
        // TODO 这个得分页，后面记得补一下
        Page<SanaTopic> page = new Page<>();
    }

}
