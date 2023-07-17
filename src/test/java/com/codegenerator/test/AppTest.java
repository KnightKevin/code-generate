package com.codegenerator.test;

import com.codegenerator.app.model.Order;
import com.codegenerator.app.model.Place;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AppTest {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");


    @Test
    public void test1() throws NoSuchMethodException {

        final String template = "更新了订单{ORDER{#order.orderId}},{ORDER{#order.orderId}},m更新内容为...";
        Matcher matcher = pattern.matcher(template);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(2);
            String functionName = matcher.group(1);

            String value = "value";
            // 匹配后记得替换

            matcher.appendReplacement(sb, Matcher.quoteReplacement(value));


            log.info("sfd");
        }

        matcher.appendTail(sb);




        Order order = new Order();
        order.setOrderId("orderIdsdfsdfsdf");
        order.setName("ccc");
        order.setPlace(new Place("palcasdfasf"));

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("order", order);

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("asfdasdf = #{#order.orderId}", new TemplateParserContext());
        Object s = exp.getValue(context);

        log.info("s = {}", s);
    }

    public static String func1() {
        return "func1asdfasdfadfasdfasdf";
    }

}

interface IParseFunction {

}
