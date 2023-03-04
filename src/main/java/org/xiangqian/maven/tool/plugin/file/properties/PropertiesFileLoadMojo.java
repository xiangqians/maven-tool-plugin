package org.xiangqian.maven.tool.plugin.file.properties;

import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.xiangqian.maven.tool.plugin.file.FileLoadMojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * properties文件加载
 *
 * @author xiangqian
 * @date 21:47 2022/04/19
 */
@NoArgsConstructor
@Mojo(name = "properties-file-load", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class PropertiesFileLoadMojo extends FileLoadMojo {

    public PropertiesFileLoadMojo(FileLoadMojo fileLoadMojo) {
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

            InputStream input = null;
            try {
                input = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(input);
                setProperty(new PropertyFunc() {
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
                });
            } finally {
                IOUtils.closeQuietly(input);
            }
        }
    }

}
