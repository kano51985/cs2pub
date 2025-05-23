package com.sana.controller;


import cn.hutool.core.util.ObjectUtil;
import com.sana.domain.entity.SanaUser;
import com.sana.response.R;
import com.sana.service.IUserService;
import com.sana.utils.UserContext;
import com.sana.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 用户控制器
 * @Version: 1.0
 */

@RestController
@Slf4j
@RequestMapping("user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserUtil userUtil;

    /**
     * 获取指定用户信息
     */
    @GetMapping("{userId}")
    public R getUserInfo(@PathVariable("userId") String uid){
        SanaUser userInfo = userService.getById(uid);
        return R.success(userInfo);
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    public R updateUserInfo(@RequestBody SanaUser user){
        String contextUid = UserContext.getUser().getUser().getId();
        if(ObjectUtil.isNotNull(contextUid)){
            if (contextUid.equals(user.getId())){
                userService.updateById(user);
                return R.success("修改成功");
            }
        }
        return R.error();

    }
}
