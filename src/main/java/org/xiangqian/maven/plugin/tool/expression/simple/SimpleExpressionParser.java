package org.xiangqian.maven.plugin.tool.expression.simple;

import org.xiangqian.maven.plugin.tool.expression.EvaluationContext;
import org.xiangqian.maven.plugin.tool.expression.Expression;
import org.xiangqian.maven.plugin.tool.expression.ExpressionParser;

/**
 * @author xiangqian
 * @date 21:14:51 2022/04/20
 */
public class SimpleExpressionParser implements ExpressionParser {

    private EvaluationContext evaluationContext;

    public SimpleExpressionParser(EvaluationContext evaluationContext) {
        this.evaluationContext = evaluationContext;
    }

    @Override
    public Expression parseExpression(String expressionString) {
        return new SimpleExpression(evaluationContext, expressionString);
    }

}
