package org.xiangqian.maven.tool.plugin.file.yaml.expression.simple;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.EvaluationContext;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.Expression;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.rules.DefaultRuleBuilder;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.rules.Rule;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.rules.RuleBuilder;
import org.xiangqian.maven.tool.plugin.file.yaml.expression.simple.rules.Value;

import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiangqian
 * @date 21:15:59 2022/04/20
 */
public class SimpleExpression implements Expression {

    private static final String REGEX = "\\$\\{([^}]*)\\}";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private EvaluationContext evaluationContext;

    @Getter
    private String expressionString;

    private String[] names;

    private RuleBuilder ruleBuilder;

    /**
     * @param evaluationContext
     * @param expressionString  ${name}
     */
    public SimpleExpression(EvaluationContext evaluationContext, String expressionString) {
        this.evaluationContext = evaluationContext;
        Matcher matcher = PATTERN.matcher(expressionString);
        this.expressionString = matcher.find() ? matcher.group(1) : expressionString;
        this.names = this.expressionString.split("\\.");
        this.ruleBuilder = new DefaultRuleBuilder();
    }

    @Override
    public <T> T getValue(Class<T> type) throws ClassCastException {
        Stack<Rule> rules = ruleBuilder.build(names);
        Object value = null;
        while (value == null && CollectionUtils.isNotEmpty(rules)) {
            value = Optional.ofNullable(rules.pop().match(evaluationContext)).map(Value::getValue).orElse(null);
        }

        if (value == null) {
            return null;
        }

        if (type.isInstance(value)) {
            return (T) value;
        }

        throw new ClassCastException(value + ": " + value.getClass());
    }

    @Override
    public void setValue(Object value) {
//        Stack<Rule> rules = getRules();
//        while (CollectionUtils.isNotEmpty(rules)) {
//            Rule rule = rules.pop();
//            Value val = rule.match(evaluationContext);
//            Value v = rule.popValueStack();
//            while (v != null) {
//                String name = v.getName();
//                Object object = v.getValue();
//                if (object instanceof Map) {
//                    ((Map) object).put(name, value);
//                }
//                break;
//            }
//        }
    }

}
