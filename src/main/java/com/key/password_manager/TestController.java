package com.key.password_manager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/login")
    public String test1() {
        return "success";
    }

    @GetMapping("/api/test")
    public String test2() {
        return "success2";
    }

}
