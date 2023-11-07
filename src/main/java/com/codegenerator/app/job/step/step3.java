package com.codegenerator.app.job.step;

import com.codegenerator.app.core.Step;
import com.codegenerator.app.mapper.TaskParamsMapper;
import com.codegenerator.app.model.TaskContext;
import com.codegenerator.app.model.level2.Task2Context;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class step3 extends Step {

    @Autowired
    private TaskContext taskContext;

    @Autowired
    private TaskParamsMapper taskParamsMapper;

    @Override
    public RepeatStatus handle(Task2Context context) {
        System.out.println("Processing C");

        context.putData("a", "v3");
        context.putData("bcas", "v1");
        context.putData("casdfa", "v1");


        try {
            Thread.sleep(1000);
            System.out.println("processing A end");
        } catch (Exception e) {

        }


        return RepeatStatus.FINISHED;

    }

}
