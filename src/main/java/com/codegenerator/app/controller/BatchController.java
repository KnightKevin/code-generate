package com.codegenerator.app.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


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

                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory.get("stepB").tasklet((contribution, chunkContext)->{
            System.out.println("Processing B");
            return RepeatStatus.FINISHED;
        })
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step stepC() {
        return stepBuilderFactory.get("stepC").tasklet((contribution, chunkContext)->{
            System.out.println("Processing C");
            return RepeatStatus.FINISHED;
        })
                .allowStartIfComplete(true)
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

    @GetMapping("/restart")
    public String restart(long executionId) throws Exception {

        JobInstance lastJobInstance = jobExplorer.getLastJobInstance("myJob");
        if (lastJobInstance != null) {
            JobExecution lastExecution = jobExplorer.getLastJobExecution(lastJobInstance);
            if (lastExecution.getStatus().isUnsuccessful() || lastExecution.getStatus().isRunning()) {
                jobOperator.restart(lastExecution.getId());
                return "Job restarted from step: " + lastExecution.getStepExecutions().size();
            } else {
                return "Job has already completed successfully.";
            }
        } else {
            return "No previous job execution found.";
        }
    }
}
