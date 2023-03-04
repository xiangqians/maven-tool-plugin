package org.xiangqian.maven.plugin.tool.expression.simple.rules;

/**
 * rule
 *
 * @author xiangqian
 * @date 10:46:05 2022/04/23
 */
public interface Rule {

    Value match(Object value);

    Value popValue();

}
