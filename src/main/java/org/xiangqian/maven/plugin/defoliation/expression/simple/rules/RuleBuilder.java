package org.xiangqian.maven.plugin.defoliation.expression.simple.rules;

import java.util.Stack;

/**
 * @author xiangqian
 * @date 11:28:10 2022/04/23
 */
public interface RuleBuilder {

    Stack<Rule> build(String[] names);

}
