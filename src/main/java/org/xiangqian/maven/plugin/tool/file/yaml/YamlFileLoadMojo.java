package org.xiangqian.maven.plugin.tool.file.yaml;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.xiangqian.maven.plugin.tool.file.yaml.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.plugin.tool.file.yaml.expression.simple.SimpleExpressionParser;
import org.xiangqian.maven.plugin.tool.file.yaml.expression.Expression;
import org.xiangqian.maven.plugin.tool.file.yaml.expression.ExpressionParser;
import org.xiangqian.maven.plugin.tool.file.FileLoadMojo;

import java.io.File;
import java.util.function.BiConsumer;

/**
 * YAML文件加载
 *
 * @author xiangqian
 * @date 12:12 2023/03/04
 */
@Mojo(name = "yaml-file-load", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class YamlFileLoadMojo extends FileLoadMojo {

    @Override
    protected void run() throws Exception {
        if (!check()) {
            return;
        }

        for (File file : includes) {
            ExpressionParser expressionParser = new SimpleExpressionParser(new SimpleEvaluationContext(file));
            setProperty(new PropertyFunc() {
                @Override
                public Object get(String name) {
                    Expression expression = expressionParser.parseExpression(String.format("${%s}", name));
                    Object value = expression.getValue(Object.class);
                    return value;
                }

                @Override
                public void forEach(BiConsumer<String, Object> consumer) {
                }
            });
        }
    }

}
