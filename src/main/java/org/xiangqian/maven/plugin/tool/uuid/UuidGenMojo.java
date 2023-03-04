package org.xiangqian.maven.plugin.tool.uuid;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.plugin.tool.AbsMojo;

import java.util.UUID;

/**
 * uuid生成
 *
 * @author xiangqian
 * @date 20:43 2022/04/21
 */
@Mojo(name = "uuid-gen", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class UuidGenMojo extends AbsMojo {

    // uuid属性名
    @Parameter(property = "name", defaultValue = "uuid")
    private String name;

    // uuid串是否包含 "-"
    @Parameter(property = "hyphen", defaultValue = "true")
    private boolean hyphen;

    @Override
    protected void run() throws Exception {
        String uuid = UUID.randomUUID().toString();
        if (!hyphen) {
            uuid = uuid.replace("-", "");
        }
        setProperty(name, uuid);
    }

}
