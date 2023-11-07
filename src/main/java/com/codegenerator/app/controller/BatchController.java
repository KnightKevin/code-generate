package com.codegenerator.app.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.codegenerator.app.model.InputParams;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;


@RestController
public class BatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobExplorer jobExplorer;

    @Resource(name = "myJob")
    private Job myJob;

    @GetMapping("/batch")
    public String batch(@RequestParam String id) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("cloudId", id)
                .toJobParameters();

       JobExecution execution =jobLauncher.run(myJob, jobParameters);

        return execution.getId()+"";
    }

    @PostMapping("/batch2")
    public String batch2(@RequestBody Object params) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        jobParametersBuilder.addString("params", JSON.toJSONString(params));

        // 将params入库
        JobExecution execution =jobLauncher.run(myJob, jobParametersBuilder.toJobParameters());

        return execution.getId()+"";
    }

    @GetMapping("/start")
    public String start(String jobName) throws Exception {

        jobOperator.start(jobName, "");

        return "";
    }

    @GetMapping("/startNextInstance")
    public String startNextInstance(String jobName) throws Exception {

        jobOperator.startNextInstance(jobName);

        return "";
    }

    @GetMapping("/restart")
    public String restart(long executionId) throws Exception {
        JobExecution execution = jobExplorer.getJobExecution(executionId);
        jobOperator.restart(executionId);
        return "ok";
    }

    @GetMapping("/stop")
    public String stop(long executionId) throws Exception {

        jobOperator.stop(executionId);

        return "";
    }




}
