package com.codegenerator.app.job.step;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codegenerator.app.core.Step;
import com.codegenerator.app.mapper.TaskParamsMapper;
import com.codegenerator.app.model.TaskContext;
import com.codegenerator.app.model.TaskParams;
import com.codegenerator.app.model.level2.Task2Context;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class step2 extends Step {

    @Autowired
    private TaskContext taskContext;

    @Autowired
    private TaskParamsMapper taskParamsMapper;

    @Override
    public RepeatStatus handle(Task2Context context) {
        System.out.println("Processing B");

        context.putData("a", "v2");
        context.putData("b1", "v1");
        context.putData("c1", "v1");


        try {
            Thread.sleep(1000);
            System.out.println("processing A end");
        } catch (Exception e) {

        }


        return RepeatStatus.FINISHED;

    }

}