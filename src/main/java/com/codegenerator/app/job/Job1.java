package com.codegenerator.app.job;

import com.codegenerator.app.job.listen.MyJobListener;
import com.codegenerator.app.job.step.step1;
import com.codegenerator.app.job.step.step2;
import com.codegenerator.app.job.step.step3;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Job1 {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    public Step stepA(step1 step) {
        return stepBuilderFactory.get("stepA")
                .tasklet(step)
                .build();
    }

    @Bean
    public Step stepB(step2 step) {
        return stepBuilderFactory.get("stepB")
                .tasklet(step)
                .build();
    }

    @Bean
    public Step stepC(step3 step) {
        return stepBuilderFactory.get("stepC")
                .tasklet(step)
                .build();
    }

    @Bean
    public Job myJob(@Qualifier("stepA") Step stepA, @Qualifier("stepB")Step stepB,@Qualifier("stepC") Step stepC) {
        return jobBuilderFactory.get("myJob")
                .listener(new MyJobListener())
                .incrementer(new RunIdIncrementer())
                .start(stepA)
                .next(stepB)
                .next(stepC)
                .build();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
