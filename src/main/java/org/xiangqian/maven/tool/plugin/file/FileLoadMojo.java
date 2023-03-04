package org.xiangqian.maven.tool.plugin.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.tool.plugin.AbsMojo;
import org.xiangqian.maven.tool.plugin.file.properties.PropertiesFileLoadMojo;
import org.xiangqian.maven.tool.plugin.file.yaml.YamlFileLoadMojo;

import java.io.File;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * @author xiangqian
 * @date 12:13 2023/03/04
 */
@NoArgsConstructor
@Mojo(name = "file-load", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class FileLoadMojo extends AbsMojo {

    // 需要包含的配置文件
    @Getter
    @Setter
    @Parameter(property = "includes")
    private File[] includes;

    // 配置将要获取资源文件的属性名以及该属性值的引用名
    @Parameter(property = "properties")
    private Properties properties;

    public FileLoadMojo(FileLoadMojo fileLoadMojo) {
        super(fileLoadMojo);
        this.includes = fileLoadMojo.includes;
        this.properties = fileLoadMojo.properties;
    }

    @Override
    protected void run() throws Exception {
        if (!check()) {
            return;
        }

        // properties
        PropertiesFileLoadMojo propertiesFileLoadMojo = new PropertiesFileLoadMojo(this);
        // yaml
        YamlFileLoadMojo yamlFileLoadMojo = new YamlFileLoadMojo(this);

        for (File file : includes) {
            // 校验文件
            if (!check(file)) {
                continue;
            }

            // 文件扩展名
            String extension = StringUtils.trim(FilenameUtils.getExtension(file.getName()));
            // properties
            if ("properties".equals(extension)) {
                propertiesFileLoadMojo.setIncludes(new File[]{file});
                propertiesFileLoadMojo.execute();
            }
            // yaml
            else if ("yml".equals(extension) || "yaml".equals(extension)) {
                yamlFileLoadMojo.setIncludes(new File[]{file});
                yamlFileLoadMojo.execute();
            }
            // unknown
            else {
                warn(String.format("file type is not supported: %s", file.getAbsolutePath()));
            }
        }
    }

    /**
     * 校验文件
     *
     * @param file
     * @return
     */
    protected boolean check(File file) {
        if (Objects.isNull(file)) {
            warn("file is null");
            return false;
        }

        if (!file.exists()) {
            warn(String.format("file not found: %s", file.getAbsolutePath()));
            return false;
        }

        return true;
    }

    protected boolean check() {
        if (ArrayUtils.isEmpty(includes)) {
            warn("includes is empty");
            return false;
        }

        if (MapUtils.isEmpty(properties)) {
            warn("No system properties found");
            return false;
        }

        return true;
    }

    /**
     * @param func
     */
    protected void setProperty(PropertyFunc func) {
        if (Objects.isNull(func)) {
            return;
        }

        boolean all = false;
        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = Optional.ofNullable(names.nextElement()).map(Object::toString).map(StringUtils::trim).orElse(null);
            if (StringUtils.isEmpty(name)) {
                continue;
            }

            String value = Optional.ofNullable(properties.getProperty(name)).map(String::toString).map(StringUtils::trim).orElse(null);
            if ("all".equals(name) && "*".equals(value)) {
                all = true;
                break;
            }
        }

        // all
        if (all) {
            func.forEach((key, value) -> setProperty(key, value));
        }
        // some
        else {
            names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String name = Optional.ofNullable(names.nextElement()).map(Object::toString).map(StringUtils::trimToNull).orElse(null);
                if (StringUtils.isEmpty(name)) {
                    continue;
                }

                String value = StringUtils.trimToNull(properties.getProperty(name));
                String propertyName = Optional.ofNullable(value).orElse(name);
                Object propertyValue = func.get(name);
                setProperty(propertyName, propertyValue);
            }
        }
    }

    /**
     * 属性函数
     */
    protected static interface PropertyFunc {

        /**
         * 根据属性名获取属性值
         *
         * @param name
         * @return
         */
        Object get(String name);

        /**
         * 遍历所有属性名和属性值
         *
         * @param consumer
         */
        void forEach(BiConsumer<String, Object> consumer);

    }

}
