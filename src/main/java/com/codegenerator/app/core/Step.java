package com.codegenerator.app.core;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codegenerator.app.mapper.TaskParamsMapper;
import com.codegenerator.app.model.TaskParams;
import com.codegenerator.app.model.level2.Task2Context;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Step implements Tasklet {

    @Autowired
    private TaskParamsMapper taskParamsMapper;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        QueryWrapper<TaskParams> wrapper = new QueryWrapper<>();
        final String jobId = chunkContext.getStepContext().getJobInstanceId() + "";
        wrapper.eq("job_id", jobId);
        List<TaskParams> list = taskParamsMapper.selectList(wrapper);

        Map<String, Object> map = new HashMap<>();

        list.forEach(i->{
            map.put(i.getKeyName(), i.getVal());
        });


        // 获取上下文
        String contextStr = (String) chunkContext.getStepContext().getJobParameters().get("params");
        Task2Context taskContext = JSON.parseObject(contextStr, Task2Context.class);
        taskContext.setData(map);

        RepeatStatus status = handle(taskContext);

        // 将可能产生的新数据放入数据库中

        Map<String, Object> forNextData = taskContext.getForNextData();

        if (forNextData != null && !forNextData.isEmpty()) {
            forNextData.forEach((k, v)-> {
                TaskParams params = new TaskParams();
                params.setKeyName(k);
                params.setVal(JSON.toJSONString(v));
                params.setJobId(jobId);

                QueryWrapper<TaskParams> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("job_id", jobId);
                queryWrapper.eq("key_name", k);
                if (taskParamsMapper.selectOne(queryWrapper) == null) {
                    taskParamsMapper.insert(params);
                } else {
                    taskParamsMapper.update(params, queryWrapper);
                }



            });
        }

        return status;
    }


    public abstract RepeatStatus handle(Task2Context context);





}
