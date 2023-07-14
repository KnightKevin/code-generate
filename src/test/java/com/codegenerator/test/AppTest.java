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

import java.util.List;

@Slf4j
public class AppTest {

    @Test
    public void test1() throws NoSuchMethodException {

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

class Demo {
    List<String> list;
}