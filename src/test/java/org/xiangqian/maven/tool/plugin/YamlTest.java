package org.xiangqian.maven.tool.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.xiangqian.maven.tool.plugin.file.yaml.YamlFileLoadMojo;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

/**
 * @author xiangqian
 * @date 14:04 2023/03/04
 */
public class YamlTest {

    public static void main(String[] args) throws Exception {
        Yaml yaml = new Yaml();
        InputStream input = YamlTest.class.getClassLoader().getResourceAsStream("test.yml");
        Map<String, Object> map = yaml.load(input);
        System.out.println(map);

        Map<String, Object> kvMap = new TreeMap<>();
        while (MapUtils.isNotEmpty(map)) {
            YamlFileLoadMojo.Kv kv = YamlFileLoadMojo.getKv(map, null);
            if (YamlFileLoadMojo.Kv.isValid(kv)) {
                kvMap.put(kv.getKey(), kv.getValue());
            }
        }

        kvMap.forEach((key, value) -> System.out.printf("%s: %s", key, value).println());
    }


}
