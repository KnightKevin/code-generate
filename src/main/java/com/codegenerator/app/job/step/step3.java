package com.codegenerator.app.job.step;

import com.codegenerator.app.model.JobParams;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class step3 implements Tasklet {

    @Autowired
    private JobParams jobParams;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Processing C");




        try {
            Thread.sleep(1000);

            jobParams.setA("cA");
            jobParams.setB("cB");


            System.out.println("processing A end");
        } catch (Exception e) {

        }

        return RepeatStatus.FINISHED;
    }
}
