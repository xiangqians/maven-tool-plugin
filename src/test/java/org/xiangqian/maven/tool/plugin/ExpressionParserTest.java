package org.xiangqian.maven.tool.plugin;

import org.xiangqian.maven.tool.plugin.file.yaml.expression.EvaluationContext;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.Expression;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.ExpressionParser;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.SimpleExpressionParser;

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

        // tool.resources.locations
        expression = expressionParser.parseExpression("${tool.resources.locations}");
        List locations = expression.getValue(List.class);
        System.out.format("locations=%s\n", locations);

        // tool.resources.locations
        expression = expressionParser.parseExpression("${tool.resources[locations]}");
        locations = expression.getValue(List.class);
        System.out.format("locations=%s\n", locations);

        // tool.resources.locations
        expression = expressionParser.parseExpression("${tool[resources].locations}");
        locations = expression.getValue(List.class);
        System.out.format("locations=%s\n", locations);

        // tool.resources.locations
        expression = expressionParser.parseExpression("${tool.resources.locations[0]}");
        String locations0 = expression.getValue(String.class);
        System.out.format("locations0=%s\n", locations0);

        // tool.resources.locations
        expression = expressionParser.parseExpression("${tool[resources].locations[0]}");
        locations0 = expression.getValue(String.class);
        System.out.format("locations0=%s\n", locations0);
    }

}
