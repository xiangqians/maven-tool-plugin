package org.xiangqian.maven.plugin.defoliation;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xiangqian
 * @date 20:53:07 2022/04/21
 */
@Mojo(name = "generate-date", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class GenerateDateMojo extends DefoliationMojo {

    // date属性名
    @Parameter(property = "name", defaultValue = "defoliation-date")
    private String name;

    @Parameter(property = "pattern", defaultValue = "yyyy/MM/dd HH:mm:ss.S")
    private String pattern;

    @Override
    protected void run() throws Exception {
        String format = DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
        setProperty(name, format);
    }

}
