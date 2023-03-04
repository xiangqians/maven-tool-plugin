package org.xiangqian.maven.tool.plugin.file.yaml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.xiangqian.maven.tool.plugin.file.FileLoadMojo;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.Expression;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.ExpressionParser;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.SimpleEvaluationContext;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.SimpleExpressionParser;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
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

            load(file);
        }
    }

    private void load(File file) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(new FileInputStream(file));
        Map<String, Object> kvMap = new HashMap<>();
        while (MapUtils.isNotEmpty(map)) {
            Kv kv = getKv(map, null);
            if (Kv.isValid(kv)) {
                kvMap.put(kv.getKey(), kv.getValue());
            }
        }

        setProperty(new PropertyFunc() {
            @Override
            public Object get(String name) {
                return kvMap.get(name);
            }

            @Override
            public void forEach(BiConsumer<String, Object> consumer) {
                kvMap.forEach(consumer);
            }
        });
    }

    public static Kv getKv(Object obj, List<String> keys) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            if (MapUtils.isEmpty(map)) {
                return new Kv();
            }

            // key
            String key = map.keySet().iterator().next();
            if (Objects.isNull(keys)) {
                keys = new ArrayList<>();
            }
            keys.add(StringUtils.trim(key));

            // value
            Object value = map.get(key);

            // getKv
            Kv kv = getKv(value, keys);
            if (BooleanUtils.isTrue(kv.getRemove())) {
                kv.setRemove(null);
                map.remove(key);
            }
            return kv;
        }

        return new Kv(Optional.ofNullable(keys).filter(CollectionUtils::isNotEmpty).map(list -> StringUtils.join(list, ".")).orElse(null), obj);
    }

    @Data
    @AllArgsConstructor
    public static class Kv {
        private String key;
        private Object value;
        private Boolean remove;

        public Kv() {
            this.remove = true;
        }

        public Kv(String key, Object value) {
            this(key, value, true);
        }

        public static boolean isValid(Kv kv) {
            return Objects.nonNull(kv) && Objects.nonNull(kv.getKey());
        }
    }

    private void load0(File file) throws FileNotFoundException {
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
