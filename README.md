# 简介

maven-tool-plugin， 这是maven插件，生成uuid、时间戳、日期和加载properties/yaml文件

# 使用

在pom.xml添加plugin依赖

```xml

<project>

    <build>

        <plugins>

            <plugin>
                <groupId>org.xiangqian</groupId>
                <artifactId>maven-tool-plugin</artifactId>
                <version>2022.4</version>
                <executions>
                    <!-- 生成uuid -->
                    <execution>
                        <id>uuid</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>uuid-gen</goal>
                        </goals>
                        <configuration>
                            <!-- 定义获取此uuid值的属性名，可通过 "${属性名}" 获取uuid值，默认 uuid -->
                            <name>uuid</name>

                            <!-- 生成uuid是否包含 "-" -->
                            <hyphen>false</hyphen>

                            <!-- 是否应用于所有的项目依赖，默认false（所有的mojo都具有此属性） -->
                            <applyProjectDependencies>false</applyProjectDependencies>

                            <!-- 是否跳过执行，默认false（所有的mojo都具有此属性） -->
                            <skip>false</skip>
                        </configuration>
                    </execution>

                    <!-- 生成timestamp -->
                    <execution>
                        <id>ts</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>ts-gen</goal>
                        </goals>
                        <configuration>
                            <!-- 定义获取时间戳值的属性名，可通过 "${属性名}" 获取时间戳值，默认 ts -->
                            <name>ts</name>

                            <!-- 时间戳单位，s，ms，默认ms -->
                            <timeUnit>ms</timeUnit>
                        </configuration>
                    </execution>

                    <!-- 生成date -->
                    <execution>
                        <id>date</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>date-gen</goal>
                        </goals>
                        <configuration>
                            <!-- 定义获取此date值的属性名，可通过 "${属性名}" 获取date值，默认 date -->
                            <name>date</name>
                            <!-- pattern -->
                            <pattern>yyyy/MM/dd HH:mm:ss.S</pattern>
                        </configuration>
                    </execution>

                    <!-- 加载properties文件 -->
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
                                <!-- 从配置文件中获取 ${server.port} 值，并将其值以属性名 "server.port" 添加到 MavenProject#Properties 中，可通过 "${server.port}" 获取该属性值 -->
                                <server.port>server.port</server.port>

                                <!-- 将配置文件中所有的属性和值添加到 MavenProject#Properties 中（目前只支持properties文件） -->
                                <all>*</all>
                            </properties>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

        </plugins>

    </build>

</project>
```
