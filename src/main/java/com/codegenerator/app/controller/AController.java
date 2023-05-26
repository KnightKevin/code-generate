package com.codegenerator.app.controller;

import com.codegenerator.app.annotation.LogRecord;
import com.codegenerator.app.module.Body;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AController {


    @LogRecord(success = "修改了订单的配送员：从“{queryOldUser{#body.deliveryOrderNo()}}”, 修改到“{deveryUser{#body.userId}}”",
            bizNo="#body.getDeliveryOrderNo()")
    @PostMapping("/a")
    public String a(@RequestBody Body body) {
        return "a";
    }
}
