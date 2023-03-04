package org.xiangqian.maven.plugin.tool.expression;

/**
 * ${} 表达式解析器
 *
 * @author xiangqian
 * @date 20:33:41 2022/04/20
 */
public interface ExpressionParser {

    /**
     * @param expressionString ${name}
     * @return
     */
    Expression parseExpression(String expressionString);

}
