package org.xiangqian.maven.plugin.tool.file.yaml.expression.simple.rules;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiangqian
 * @date 11:23:42 2022/04/23
 */
@Data
@AllArgsConstructor
public class IntName implements Name<Integer> {

    private int value;

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public Integer get() {
        return value;
    }

}
