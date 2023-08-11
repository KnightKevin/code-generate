package com.codegenerator.app.aop;

import com.codegenerator.app.annotation.OperateLog;
import com.codegenerator.app.model.Order;
import com.codegenerator.app.model.Place;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Component
public class LogAspect {

    final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");


    @Pointcut("@annotation(com.codegenerator.app.annotation.OperateLog)")
    public void pointcut(){
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        OperateLog operateLog = method.getDeclaredAnnotation(OperateLog.class);

//        String template = operateLog.value();

        final String template = "更新了订单{ORDER{#body.orderId}},{ORDER{#body.orderId}},m更新内容为...";

        Matcher matcher = pattern.matcher(template);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(2);
            String functionName = matcher.group(1);


            EvaluationContext context = new MethodBasedEvaluationContext(null, method, joinPoint.getArgs(), new DefaultParameterNameDiscoverer());

            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(expression);
            Object s = exp.getValue(context);


            // 解析方法，并执行方法，得到返回值

            String value = "value";
            // 匹配后记得替换

            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));


            log.info("sfd");
        }

        matcher.appendTail(sb);


        // 获取到实践fasdfasdfasdfasdfasdf

        return joinPoint.proceed();
    }
}
