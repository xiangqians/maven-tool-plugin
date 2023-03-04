package org.xiangqian.maven.plugin.tool;

import org.xiangqian.maven.plugin.tool.expression.EvaluationContext;
import org.xiangqian.maven.plugin.tool.expression.Expression;
import org.xiangqian.maven.plugin.tool.expression.ExpressionParser;
import org.xiangqian.maven.plugin.tool.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.plugin.tool.expression.simple.SimpleExpressionParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author xiangqian
 * @date 10:21:54 2022/04/23
 */
public class ExpressionParserTest {

    public static void main(String[] args) {

        EvaluationContext evaluationContext = new SimpleEvaluationContext(ExpressionParserTest.class.getClassLoader().getResourceAsStream("test.yml"));
        ExpressionParser expressionParser = new SimpleExpressionParser(evaluationContext);

        // server.port
        Expression expression = expressionParser.parseExpression("${server.port}");
        Integer port = expression.getValue(Integer.class);
        System.out.format("port=%d\n", port);

        // spring.application.name
        expression = expressionParser.parseExpression("${spring.application.name}");
        String name = expression.getValue(String.class);
        System.out.format("name=%s\n", name);

        // spring.profiles.active
        expression = expressionParser.parseExpression("${spring.profiles.active}");
        String active = expression.getValue(String.class);
        System.out.format("active=%s\n", active);

        // defoliation.resources.locations
        expression = expressionParser.parseExpression("${defoliation.resources.locations}");
        List locations = expression.getValue(List.class);
        System.out.format("locations=%s\n", locations);

        // defoliation.resources.locations
        expression = expressionParser.parseExpression("${defoliation.resources[locations]}");
        locations = expression.getValue(List.class);
        System.out.format("locations=%s\n", locations);

        // defoliation.resources.locations
        expression = expressionParser.parseExpression("${defoliation[resources].locations}");
        locations = expression.getValue(List.class);
        System.out.format("locations=%s\n", locations);

        // defoliation.resources.locations
        expression = expressionParser.parseExpression("${defoliation.resources.locations[0]}");
        String locations0 = expression.getValue(String.class);
        System.out.format("locations0=%s\n", locations0);

        // defoliation.resources.locations
        expression = expressionParser.parseExpression("${defoliation[resources].locations[0]}");
        locations0 = expression.getValue(String.class);
        System.out.format("locations0=%s\n", locations0);
    }

}
