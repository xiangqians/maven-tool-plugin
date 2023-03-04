package org.xiangqian.maven.plugin.tool.mojo.date;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.plugin.tool.mojo.AbsMojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * date生成
 *
 * @author xiangqian
 * @date 20:53 2022/04/21
 */
@Mojo(name = "date-gen", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class DateGenMojo extends AbsMojo {

    // date属性名
    @Parameter(property = "name", defaultValue = "date")
    private String name;

    @Parameter(property = "pattern", defaultValue = "yyyy/MM/dd HH:mm:ss.S")
    private String pattern;

    @Override
    protected void run() throws Exception {
        String format = DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
        setProperty(name, format);
    }

}
