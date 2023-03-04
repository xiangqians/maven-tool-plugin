package org.xiangqian.maven.plugin.tool;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 抽象的 {@link Mojo}
 *
 * @author xiangqian
 * @date 20:05 2022/04/21
 */
public abstract class AbsMojo extends AbstractMojo {

    // maven project
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    // maven session
    @Parameter(defaultValue = "${session}", required = true, readonly = true)
    private MavenSession session;

    // 是否应用于所有的项目依赖
    @Parameter(property = "applyProjectDependencies", defaultValue = "false")
    private boolean applyProjectDependencies;

    // Whether to skip this execution.
    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            return;
        }

        try {
            run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void run() throws Exception;

    protected final void setProperty(String name, Object value) {
        name = StringUtils.trimToNull(name);
        if (Objects.isNull(name)) {
            return;
        }

        setProperty(new Property(name, value));
    }

    protected final void setProperty(Property... properties) {
        if (ArrayUtils.isEmpty(properties)) {
            return;
        }

        if (!applyProjectDependencies) {
            setProperty(project, properties);
            return;
        }

        List<MavenProject> sortedProjects = session.getProjectDependencyGraph().getSortedProjects();
        for (MavenProject project : sortedProjects) {
            setProperty(project, properties);
        }
    }

    /**
     * @param project
     * @param properties
     */
    private void setProperty(MavenProject project, Property... properties) {
        for (Property property : properties) {
            if (Objects.isNull(property) || !property.isValid()) {
                continue;
            }

            info("Storing the '%s' property in the '%s' project with value of '%s'.", property.getKey(), project.getId(), property.getValue());
            project.getProperties().setProperty(property.getKey(), Optional.ofNullable(property.getValue()).map(Object::toString).map(StringUtils::trimToEmpty).orElse(""));
        }
    }

    @Data
    protected static class Property {
        private String key;
        private Object value;

        public Property(String key, Object value) {
            this.key = StringUtils.trimToNull(key);
            this.value = value;
        }

        public void setKey(String key) {
            this.key = StringUtils.trimToNull(key);
        }

        /**
         * 判断 {@link Property} 是否有效
         *
         * @return
         */
        public boolean isValid() {
            return Objects.nonNull(key);
        }
    }

    protected final void error(String format, Object... args) {
        log(LogLevel.ERROR, format, args);
    }

    protected final void warn(String format, Object... args) {
        log(LogLevel.WARN, format, args);
    }

    protected final void info(String format, Object... args) {
        log(LogLevel.INFO, format, args);
    }

    protected final void debug(String format, Object... args) {
        log(LogLevel.DEBUG, format, args);
    }

    protected enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    /**
     * log
     *
     * @param level  {@link LogLevel}
     * @param format
     * @param args
     */
    private void log(LogLevel level, String format, Object... args) {
        StringBuilder content = new StringBuilder();
        content.append(this.getClass().getSimpleName()).append(": ");
        if (ArrayUtils.isNotEmpty(args)) {
            content.append(String.format(format, args));
        } else {
            content.append(format);
        }

        switch (level) {
            case DEBUG:
                getLog().debug(content);
                break;

            case INFO:
                getLog().info(content);
                break;

            case WARN:
                getLog().warn(content);
                break;

            case ERROR:
                getLog().error(content);
                break;

            default:
                break;
        }
    }

}
