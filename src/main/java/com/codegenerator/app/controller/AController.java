package com.codegenerator.app.controller;

import com.codegenerator.app.task.TestThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AController {

    @Autowired
    TestThreadPoolManager testThreadPoolManager;

    @GetMapping("/addOrder")
    public String addOrder(String orderId) {
        testThreadPoolManager.addOrders(orderId);
        return "a";
    }
}
