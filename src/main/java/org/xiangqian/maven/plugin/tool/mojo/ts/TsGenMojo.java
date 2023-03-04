package org.xiangqian.maven.plugin.tool.mojo.ts;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xiangqian.maven.plugin.tool.mojo.AbsMojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * timestamp生成
 *
 * @author xiangqian
 * @date 20:02:45 2022/04/21
 */
@Mojo(name = "ts-gen", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class TsGenMojo extends AbsMojo {

    // timestamp属性名
    @Parameter(property = "name", defaultValue = "ts")
    private String name;

    // 时间戳单位，s，ms
    @Parameter(property = "timeUnit", defaultValue = "ms")
    private String timeUnit;

    @Override
    protected void run() throws Exception {
        timeUnit = StringUtils.trimToNull(timeUnit);

        long ts = 0L;

        // s
        if ("s".equals(timeUnit)) {
            ts = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toEpochSecond();
//            ts = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        }
        // ms
        else {
//            ts = System.currentTimeMillis();
            ts = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        setProperty(name, ts);
    }

}
