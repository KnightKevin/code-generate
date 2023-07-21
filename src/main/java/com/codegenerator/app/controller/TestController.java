package com.codegenerator.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.codegenerator.app.annotation.OperateLog;
import com.codegenerator.app.model.Metric;
import com.codegenerator.app.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;

@Slf4j
@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/a")
    public void a(int n) {

        String prefix = "http_";

        StringBuilder metricData = new StringBuilder();


        for (int i = 0; i < n; i++) {
            metricData.append(buildMetric(new Metric(prefix + i, randomDouble())));
        }

        push(metricData.toString());

        log.info("send metricData is: {}", metricData);
    }

    private String buildMetric(Metric metric) {
        return String.format("my_metric{instanceId=\"%s\"} %f\n", metric.getInstanceId(), metric.getValue());
    }


    private void push(String metricData) {
        String pushgatewayUrl = "http://host231:9091/metrics/job/my_job";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> requestEntity = new HttpEntity<>(metricData, headers);


        restTemplate.postForObject(pushgatewayUrl, requestEntity, Void.class);
    }


    private double randomDouble() {
        // 创建 Random 对象
        Random random = new Random();

        // 生成 0 到 1 之间的 4 位小数
        double randomDecimal = random.nextDouble();

        return randomDecimal;
    }

    @PostMapping("/b")
    public String b(@RequestBody JSONObject object) {
        log.info("alertManager:   {}", object.toJSONString());
        return "b";
    }
}
