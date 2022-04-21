package org.xiangqian.maven.plugin.defoliation;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.UUID;

/**
 * @author xiangqian
 * @date 20:43:36 2022/04/21
 */
@Mojo(name = "generate-uuid", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class GenerateUUIDMojo extends DefoliationMojo {

    // uuid属性名
    @Parameter(property = "name", defaultValue = "defoliation-uuid")
    private String name;

    // 是否包含 "-"
    @Parameter(property = "hyphen", defaultValue = "true")
    private boolean hyphen;

    @Override
    protected void run() throws Exception {
        String uuid = UUID.randomUUID().toString();
        setProperty(name, hyphen ? uuid : uuid.replace("-", ""));
    }

}
