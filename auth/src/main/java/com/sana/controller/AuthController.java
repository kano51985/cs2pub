package com.sana.controller;


import com.sana.annotation.PermissionCheck;
import com.sana.domain.dto.LoginDTO;
import com.sana.response.R;
import com.sana.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 庞宇
 * @CreateTime: 2025-04-11
 * @Description: 认证相关控制器
 * @Version: 1.0
 */

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("login")
    public R login(@RequestBody LoginDTO loginDTO) {
        return R.success(authService.login(loginDTO));
    }

    @GetMapping("test")
    @PermissionCheck("admin")
    public void test() {
        System.out.println("1111111");
    }
}
