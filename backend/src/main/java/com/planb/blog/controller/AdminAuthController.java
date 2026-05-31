package com.planb.blog.controller;

import com.planb.blog.common.Result;
import com.planb.blog.dto.LoginRequest;
import com.planb.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            return Result.success("登录成功", data);
        }
        return Result.error("用户名或密码错误");
    }
}
