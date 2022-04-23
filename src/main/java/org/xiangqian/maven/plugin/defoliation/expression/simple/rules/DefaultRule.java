package org.xiangqian.maven.plugin.defoliation.expression.simple.rules;

import org.apache.commons.collections4.CollectionUtils;
import org.xiangqian.maven.plugin.defoliation.expression.EvaluationContext;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author xiangqian
 * @date 11:08:01 2022/04/23
 */
public class DefaultRule implements Rule {

    private final Stack<Name<?>> nameStack;
    private final Stack<Value> valueStack;

    public DefaultRule() {
        this.nameStack = new Stack<>();
        this.valueStack = new Stack<>();
    }

    public void pushName(Name<?> name) {
        nameStack.push(name);
    }

    @Override
    public Value match(Object value) {
        if (nameStack.isEmpty() || value == null) {
            return valueStack.peek();
        }

        Name<?> name = null;
        Object result = null;

        // EvaluationContext
        if (value instanceof EvaluationContext) {
            name = nameStack.pop();
            result = parseValueOfEvaluationContext((EvaluationContext) value, name);

        }
        // Map
        else if (value instanceof Map) {
            name = nameStack.pop();
            result = parseValueOfMap((Map) value, name);

        }
        // List
        else if (value instanceof List) {
            name = nameStack.pop();
            result = parseValueOfList((List<Object>) value, name);

        }
        // Unknown
        else {
            name = nameStack.pop();
            result = parseValueOfUnknown(value, name);
        }

        valueStack.push(new Value(name, result));
        return match(result);
    }

    @Override
    public Value popValue() {
        if (valueStack.isEmpty()) {
            return null;
        }
        return valueStack.pop();
    }

    protected Object parseValueOfEvaluationContext(EvaluationContext evaluationContext, Name<?> name) {
        return name.isText() ? evaluationContext.lookupVariable((String) name.get()) : null;
    }

    protected Object parseValueOfMap(Map<String, Object> map, Name<?> name) {
        return name.isText() ? map.get((String) name.get()) : null;
    }

    protected Object parseValueOfList(List<Object> list, Name<?> name) {
        int index = -1;
        if (CollectionUtils.isEmpty(list) || !name.isInt() || (index = (int) name.get()) < 0) {
            return null;
        }
        return index < list.size() ? list.get(index) : null;
    }

    protected Object parseValueOfUnknown(Object object, Name<?> name) {
        return null;
    }

}
