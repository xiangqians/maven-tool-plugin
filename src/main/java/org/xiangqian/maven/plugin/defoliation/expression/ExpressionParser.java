package org.xiangqian.maven.plugin.defoliation.expression;

/**
 * ${} 表达式解析器
 *
 * @author xiangqian
 * @date 20:33:41 2022/04/20
 */
public interface ExpressionParser {

    Expression parseExpression(String expressionString);

}
