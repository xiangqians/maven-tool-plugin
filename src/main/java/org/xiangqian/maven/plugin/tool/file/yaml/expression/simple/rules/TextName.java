package org.xiangqian.maven.plugin.tool.file.yaml.expression.simple.rules;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiangqian
 * @date 11:24:36 2022/04/23
 */
@Data
@AllArgsConstructor
public class TextName implements Name<String> {

    private String value;

    @Override
    public boolean isInt() {
        return false;
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    public String get() {
        return value;
    }

}
