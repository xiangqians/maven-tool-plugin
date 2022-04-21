package org.xiangqian.maven.plugin.defoliation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

/**
 * @author xiangqian
 * @date 20:05:28 2022/04/21
 */
public abstract class DefoliationMojo extends AbstractMojo {

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

    protected final void setProperty(String key, Object value) {
        setProperty(new Property(key, value));
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

    private void setProperty(MavenProject project, Property... properties) {
        for (Property property : properties) {
            info("Storing {} property in project {}", property.getKey(), project.getId());
            project.getProperties().setProperty(property.getKey(), String.valueOf(property.getValue()));
        }
    }

    @Data
    @AllArgsConstructor
    protected static class Property {
        private String key;
        private Object value;
    }

    protected final void info(String format, Object... args) {
        log(LogLevel.INFO, format, args);
    }

    protected final void debug(String format, Object... args) {
        log(LogLevel.DEBUG, format, args);
    }

    private enum LogLevel {
        DEBUG, INFO
    }

    private void log(LogLevel level, String format, Object... args) {
        StringBuilder content = new StringBuilder();
        content.append("[").append(this.getClass().getSimpleName()).append("]");
        if (ArrayUtils.isNotEmpty(args)) {
            content.append(String.format(format.replace("{}", "%s"), args));
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
            default:
                break;
        }
    }

}
