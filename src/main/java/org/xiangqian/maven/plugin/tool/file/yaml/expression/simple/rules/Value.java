package org.xiangqian.maven.plugin.tool.file.yaml.expression.simple.rules;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xiangqian
 * @date `10:47:23 2022/04/23`
 */
@Data
@AllArgsConstructor
public class Value {

    private Name name;
    private Object value;

}
