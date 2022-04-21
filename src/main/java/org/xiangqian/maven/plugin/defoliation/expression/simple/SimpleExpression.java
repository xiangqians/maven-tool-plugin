package org.xiangqian.maven.plugin.defoliation.expression.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.xiangqian.maven.plugin.defoliation.expression.EvaluationContext;
import org.xiangqian.maven.plugin.defoliation.expression.Expression;

import java.util.Map;
import java.util.Optional;
import java.util.Stack;

/**
 * @author xiangqian
 * @date 21:15:59 2022/04/20
 */
public class SimpleExpression implements Expression {

    private EvaluationContext evaluationContext;

    @Getter
    private String expressionString;

    private String[] names;

    /**
     * @param evaluationContext
     * @param expressionString  ${name}
     */
    public SimpleExpression(EvaluationContext evaluationContext, String expressionString) {
        this.evaluationContext = evaluationContext;
        this.expressionString = expressionString = expressionString.substring(2, expressionString.length() - 1);
        this.names = expressionString.split("\\.");
    }

    @Override
    public <T> T getValue(Class<T> type) throws ClassCastException {
        Stack<Rule> rules = getRules();
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
        Stack<Rule> rules = getRules();
        while (CollectionUtils.isNotEmpty(rules)) {
            Rule rule = rules.pop();
            Value val = rule.match(evaluationContext);
            Stack<Value> valueStack = rule.getValueStack();
            if (val != null && valueStack.size() > 1) {
                String name = valueStack.pop().getName();
                Object object = valueStack.pop().getValue();
                if (object instanceof Map) {
                    ((Map) object).put(name, value);
                }
                break;
            }
        }
    }

    private Stack<Rule> getRules() {
        Stack<Rule> rules = new Stack<>();

        // rule 1
        int length = names.length;
        Rule rule1 = new Rule();
        int index = length;
        while (index-- > 0) {
            rule1.add(names[index]);
        }
        rules.push(rule1);

        // rule 2
        // ...

        return rules;
    }

    public static class Rule {
        private Stack<String> nameStack;

        @Getter
        private Stack<Value> valueStack;

        public Rule() {
            this.nameStack = new Stack<>();
            this.valueStack = new Stack<>();
        }

        public void add(String item) {
            nameStack.push(item);
        }

        public Value match(Object value) {
            if (nameStack.isEmpty() || value == null) {
                return valueStack.peek();
            }

            if (value instanceof EvaluationContext) {
                String name = nameStack.pop();
                Object val = ((EvaluationContext) value).lookupVariable(name);
                valueStack.push(new Value(name, val));
                return match(val);

            } else if (value instanceof Map) {
                String name = nameStack.pop();
                Object val = ((Map) value).get(name);
                valueStack.push(new Value(name, val));
                return match(val);
            }
            return null;
        }

    }

    @Data
    @AllArgsConstructor
    public static class Value {
        public static final Object NULL = new Object();
        private String name;
        private Object value;
    }

}
