package com.codegenerator.app.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("prototype") // 多例
public class BusinessThread implements Runnable{

    private String acceptStr;

    public BusinessThread(String acceptStr) {
        this.acceptStr = acceptStr;
    }

    @Override
    public void run() {
        log.info("多线程以处理此任务开始，id:{}", acceptStr);

        try {
            Thread.sleep(4000);
            log.info("多线程以处理此任务完成，id:{}", acceptStr);

        } catch (Exception e) {
            log.info("error", e);
        }

    }

    public String getAcceptStr() {
        return acceptStr;
    }
}
