package org.xiangqian.maven.plugin.defoliation;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author xiangqian
 * @date 20:02:45 2022/04/21
 */
@Mojo(name = "generate-timestamp", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class GenerateTimestampMojo extends DefoliationMojo {

    // timestamp属性名
    @Parameter(property = "name", defaultValue = "defoliation-timestamp")
    private String name;

    @Override
    protected void run() throws Exception {
        setProperty(name, System.currentTimeMillis());
    }

}
