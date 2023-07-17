package com.codegenerator.app.controller;

import com.codegenerator.app.annotation.OperateLog;
import com.codegenerator.app.model.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @OperateLog("order name is {{queryOrderName(#body.orderId)}}")
    @PostMapping("/a")
    public String a(@RequestBody Order body) {
        return "a";
    }

    @GetMapping("/b")
    public String b() {
        return "b";
    }
}
