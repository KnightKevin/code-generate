package com.codegenerator.app.aop;

import com.codegenerator.app.annotation.LogRecord;
import com.codegenerator.app.module.LogRecordOps;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class LogRecordOperationSource {

    public Collection<LogRecordOps> computeLogRecordOperations(Method method, Class<?> targetClass) {

        // 修饰符为非public的就直接返回空数组
        if (!Modifier.isPublic(method.getModifiers())) {
            return Collections.emptyList();
        }

        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);



        HashSet<LogRecordOps> result = new HashSet<>();

        result.addAll(parseLogRecordAnnotations(ClassUtils.getInterfaceMethodIfPossible(method)));


        return result;
    }

    private Collection<LogRecordOps> parseLogRecordAnnotations(AnnotatedElement ae) {
        Collection<LogRecord> logRecords = AnnotatedElementUtils.findAllMergedAnnotations(ae, LogRecord.class);

        Collection<LogRecordOps> ret = new ArrayList<>();

        if (!logRecords.isEmpty()) {
            for (LogRecord i : logRecords) {
                ret.add(parseLogRecordAnnotation(ae, i));
            }
        }

        return ret;
    }

    private LogRecordOps parseLogRecordAnnotation(AnnotatedElement ae, LogRecord logRecord) {
        LogRecordOps logRecordOps = LogRecordOps.builder()
                .successLogTemplate(logRecord.success())
                .build();

        return logRecordOps;
    }
}
