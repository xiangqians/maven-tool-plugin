# 简介

defoliation-maven-plugin

# 快速开始

在pom.xml添加plugin依赖

```xml

<plugin>
    <groupId>org.xiangqian</groupId>
    <artifactId>defoliation-maven-plugin</artifactId>
    <version>2022.4</version>
    <executions>
        <!-- 生成timestamp -->
        <execution>
            <id>timestamp</id>
            <phase>validate</phase>
            <goals>
                <goal>generate-timestamp</goal>
            </goals>
            <configuration>
                <!-- 定义获取此timestamp值的属性名，可通过 "${属性名}" 获取timestamp值，默认 defoliation-timestamp -->
                <name>defoliation-timestamp</name>
                <!-- 是否应用于所有的项目依赖，默认false（defoliation-maven-plugin所有的mojo都具有此属性） -->
                <applyProjectDependencies>false</applyProjectDependencies>
                <!-- 是否跳过执行，默认false（defoliation-maven-plugin所有的mojo都具有此属性） -->
                <skip>false</skip>
            </configuration>
        </execution>

        <!-- 生成uuid -->
        <execution>
            <id>uuid</id>
            <phase>validate</phase>
            <goals>
                <goal>generate-uuid</goal>
            </goals>
            <configuration>
                <!-- 定义获取此uuid值的属性名，可通过 "${属性名}" 获取uuid值，默认 defoliation-uuid -->
                <name>defoliation-uuid</name>
                <!-- 生成uuid是否包含 "-" -->
                <hyphen>false</hyphen>
            </configuration>
        </execution>

        <!-- 生成时间 -->
        <execution>
            <id>date</id>
            <phase>validate</phase>
            <goals>
                <goal>generate-date</goal>
            </goals>
            <configuration>
                <!-- 定义获取此date值的属性名，可通过 "${属性名}" 获取date值，默认 defoliation-date -->
                <name>defoliation-date</name>
                <!-- pattern -->
                <pattern>yyyy/MM/dd HH:mm:ss.S</pattern>
            </configuration>
        </execution>

        <!-- 加载配置文件 -->
        <execution>
            <id>properties</id>
            <phase>validate</phase>
            <goals>
                <goal>properties</goal>
            </goals>
            <configuration>
                <!-- 包含要读取的配置文件，目前只支持yaml格式的配置文件 -->
                <includes>
                    <include>${project.basedir}/src/main/resources/bootstrap.yml</include>
                </includes>

                <!-- 配置将要获取资源文件的属性名以及该属性值的引用名 -->
                <properties>
                    <!-- 从配置文件中获取 ${server.port} 值，并将其值以属性名 "defoliation.server.port" 添加到 MavenProject#Properties 中，可通过 "${defoliation.server.port}" 获取该属性值 -->
                    <server.port>defoliation.server.port</server.port>
                </properties>
            </configuration>
        </execution>
    </executions>
</plugin>
```
