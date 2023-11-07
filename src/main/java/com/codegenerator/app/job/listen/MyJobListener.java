package com.codegenerator.app.job.listen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class MyJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {

        // 在这初始化上下文公用的数据


        log.info("my job beforeJob!");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("my job afterJob!");

    }
}
