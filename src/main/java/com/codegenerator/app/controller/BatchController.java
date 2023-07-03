package com.codegenerator.app.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


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

    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    public Step stepA() {
        return stepBuilderFactory.get("stepA").tasklet((contribution, chunkContext)->{
            System.out.println("Processing A");


            try {
                Thread.sleep(10000);
                System.out.println("processing A end");
            } catch (Exception e) {

            }

            return RepeatStatus.FINISHED;
        })

                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory.get("stepB").tasklet((contribution, chunkContext)->{
            System.out.println("Processing B");

            try {
                Thread.sleep(10000);
                System.out.println("processing B end");
            } catch (Exception e) {

            }


            return RepeatStatus.FINISHED;
        })
                .build();
    }

    @Bean
    public Step stepC() {
        return stepBuilderFactory.get("stepC").tasklet((contribution, chunkContext)->{
            System.out.println("Processing C");
            return RepeatStatus.FINISHED;
        })
                .build();
    }

    @Bean
    public Job myJob() {
        return jobBuilderFactory.get("myJob")
                .start(stepA())
                .next(stepB())
                .next(stepC())
                .build();
    }

    @GetMapping("/batch")
    public String batch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {


       JobExecution execution =jobLauncher.run(myJob(), new JobParameters());

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

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }


}
