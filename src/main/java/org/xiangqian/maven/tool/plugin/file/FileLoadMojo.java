package org.xiangqian.maven.tool.plugin.file;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.tool.plugin.AbsMojo;

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
@Mojo(name = "file-load", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class FileLoadMojo extends AbsMojo {

    // 需要包含的配置文件
    @Parameter(property = "includes")
    protected File[] includes;

    // 配置将要获取资源文件的属性名以及该属性值的引用名
    @Parameter(property = "properties")
    protected Properties properties;

    @Override
    protected void run() throws Exception {
        if (!check()) {
            return;
        }

        for (File file : includes) {
            // 文件扩展名
            String extension = null;
            if (file == null || !file.exists() || (extension = FilenameUtils.getExtension(file.getName())) == null) {
                return;
            }
            // properties
            if (extension.equals("properties")) {
            }
            // yaml
            else if (extension.equals("yml") || extension.equals("yaml")) {
            }
        }
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
