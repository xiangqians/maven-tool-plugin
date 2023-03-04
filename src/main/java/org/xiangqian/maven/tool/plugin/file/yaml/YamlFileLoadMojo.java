package org.xiangqian.maven.tool.plugin.file.yaml;

import lombok.NoArgsConstructor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.xiangqian.maven.tool.plugin.file.FileLoadMojo;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.Expression;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.ExpressionParser;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.SimpleExpressionParser;

import java.io.File;
import java.util.function.BiConsumer;

/**
 * YAML文件加载
 *
 * @author xiangqian
 * @date 12:12 2023/03/04
 */
@NoArgsConstructor
@Mojo(name = "yaml-file-load", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class YamlFileLoadMojo extends FileLoadMojo {

    public YamlFileLoadMojo(FileLoadMojo fileLoadMojo) {
        super(fileLoadMojo);
    }

    @Override
    protected void run() throws Exception {
        if (!check()) {
            return;
        }

        for (File file : getIncludes()) {
            // 校验文件
            if (!check(file)) {
                continue;
            }

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
