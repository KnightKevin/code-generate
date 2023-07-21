package com.codegenerator.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
public class AppTest {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");


    @Test
    public void test1() throws IOException {


        int n = 3;
        String prefix = "http_";

        for (int i = 0; i < n; i++) {
            push(prefix+i, randomDouble());
        }
        log.info("s");
    }


    private void push(String id, double v) {
        String pushgatewayUrl = "http://host231:9091/metrics/job/my_job";

        String metricData = String.format("my_metric{instanceId=\"%s\"} %f", id, v);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(pushgatewayUrl, metricData, Void.class);
    }


    private double randomDouble() {
        // 创建 Random 对象
        Random random = new Random();

        // 生成 0 到 1 之间的 4 位小数
        double randomDecimal = random.nextDouble();

        return randomDecimal;
    }


}
