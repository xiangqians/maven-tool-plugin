package org.xiangqian.maven.plugin.tool.mojo.properties;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.plugin.tool.expression.Expression;
import org.xiangqian.maven.plugin.tool.expression.ExpressionParser;
import org.xiangqian.maven.plugin.tool.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.plugin.tool.expression.simple.SimpleExpressionParser;
import org.xiangqian.maven.plugin.tool.mojo.AbsMojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * properties
 *
 * @author xiangqian
 * @date 21:47:09 2022/04/19
 */
//@Mojo(name = "properties", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class PropertiesMojo extends AbsMojo {

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

        for (File file : includes) {
            handle(file);
        }
    }

    private void handle(File file) throws IOException {
        // 文件扩展名
        String extension = null;
        if (file == null || !file.exists() || (extension = FilenameUtils.getExtension(file.getName())) == null) {
            return;
        }

        Function function = null;

        // properties
        if (extension.equals("properties")) {
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(is);
                function = new Function() {
                    @Override
                    public Object get(String name) {
                        return properties.getProperty(name);
                    }

                    @Override
                    public void forEach(BiConsumer<String, Object> consumer) {
                        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                            String key = String.valueOf(entry.getKey());
                            Object value = entry.getValue();
                            consumer.accept(key, value);
                        }
                    }
                };
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
        // yaml
        else if (extension.equals("yml") || extension.equals("yaml")) {
            ExpressionParser expressionParser = new SimpleExpressionParser(new SimpleEvaluationContext(file));
            function = new Function() {
                @Override
                public Object get(String name) {
                    Expression expression = expressionParser.parseExpression(String.format("${%s}", name));
                    Object value = expression.getValue(Object.class);
                    return value;
                }

                @Override
                public void forEach(BiConsumer<String, Object> consumer) {
                }
            };
        }

        handle(function);
    }

    // parse & set
    private void handle(Function function) {
        if (function == null) {
            return;
        }

        boolean all = false;
        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = StringUtils.trimToNull(names.nextElement().toString());
            if (StringUtils.isEmpty(name)) {
                continue;
            }
            String value = StringUtils.trimToNull(properties.getProperty(name));
            if ("all".equals(name) && "*".equals(value)) {
                all = true;
                continue;
            }

            String propertyName = Optional.ofNullable(value).orElse(name);
            Object propertyValue = function.get(name);
            setProperty(propertyName, propertyValue);
        }

        if (all) {
            function.forEach((key, value) -> setProperty(key, value));
        }
    }

    public static interface Function {
        Object get(String name);

        void forEach(BiConsumer<String, Object> consumer);
    }

}
