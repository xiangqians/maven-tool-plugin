package org.xiangqian.maven.plugin.tool.file.yaml.expression;

/**
 * @author xiangqian
 * @date 20:53:06 2022/04/20
 */
public interface EvaluationContext {

    /**
     * 查找变量
     *
     * @param name
     * @return
     */
    Object lookupVariable(String name);

    /**
     * 设置变量
     *
     * @param name
     * @param value
     */
    void setVariable(String name, Object value);

}
