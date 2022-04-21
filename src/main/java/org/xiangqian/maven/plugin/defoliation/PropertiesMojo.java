package org.xiangqian.maven.plugin.defoliation;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.plugin.defoliation.expression.Expression;
import org.xiangqian.maven.plugin.defoliation.expression.ExpressionParser;
import org.xiangqian.maven.plugin.defoliation.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.plugin.defoliation.expression.simple.SimpleExpressionParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * properties
 *
 * @author xiangqian
 * @date 21:47:09 2022/04/19
 */
@Mojo(name = "properties", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class PropertiesMojo extends DefoliationMojo {

    // 需要包含的配置文件
    @Parameter(property = "includes")
    private File[] includes;

    /**
     * 配置将要获取资源文件的属性名以及该属性值的引用名
     */
    @Parameter(property = "properties")
    private Properties properties;

    @Override
    protected void run() throws Exception {
        if (ArrayUtils.isEmpty(includes)) {
            info("includes is empty");
            return;
        }

        if (MapUtils.isEmpty(properties)) {
            info("No system properties found");
            return;
        }

        List<ExpressionParser> expressionParsers = new ArrayList<>();
        for (File include : includes) {
            if (include == null || !include.exists()) {
                continue;
            }
            ExpressionParser expressionParser = new SimpleExpressionParser(new SimpleEvaluationContext(include));
            expressionParsers.add(expressionParser);
        }

        if (CollectionUtils.isEmpty(expressionParsers)) {
            info("expressionParsers is empty");
            return;
        }

        // parse & set
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String propertyName = propertyNames.nextElement().toString();
            String propertyValue = properties.getProperty(propertyName);
            Object value = null;
            for (ExpressionParser expressionParser : expressionParsers) {
                Expression expression = expressionParser.parseExpression(String.format("${%s}", propertyName));
                value = expression.getValue(Object.class);
                if (value != null) {
                    break;
                }
            }
            setProperty(propertyValue, value);
        }
    }

}
