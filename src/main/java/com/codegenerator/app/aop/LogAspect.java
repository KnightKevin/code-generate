package com.codegenerator.app.aop;

import com.codegenerator.app.annotation.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.codegenerator.app.annotation.OperateLog)")
    public void pointcut(){
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        OperateLog operateLog = method.getDeclaredAnnotation(OperateLog.class);

        String template = operateLog.value();

        // 获取到实践fasdfasdfasdfasdfasdf

        return joinPoint.proceed();
    }
}
