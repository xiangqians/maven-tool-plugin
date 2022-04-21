package org.xiangqian.maven.plugin.defoliation.expression;

/**
 * @author xiangqian
 * @date 20:34:26 2022/04/20
 */
public interface Expression {

    String getExpressionString();

    //    private final String expression;
    <T> T getValue(Class<T> type) throws ClassCastException;

    void setValue(Object value);

}
