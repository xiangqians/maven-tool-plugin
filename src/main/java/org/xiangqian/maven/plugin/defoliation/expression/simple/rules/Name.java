package org.xiangqian.maven.plugin.defoliation.expression.simple.rules;

/**
 * @author xiangqian
 * @date 11:10:56 2022/04/23
 */
public interface Name<T> {

    boolean isInt();

    boolean isText();

    T get();

}
