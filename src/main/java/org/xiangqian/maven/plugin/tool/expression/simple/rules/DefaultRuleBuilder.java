package org.xiangqian.maven.plugin.tool.expression.simple.rules;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiangqian
 * @date 11:28:50 2022/04/23
 */
public class DefaultRuleBuilder implements RuleBuilder {

    private static final String REGEX = "\\[([^]]*)\\]";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Stack<Rule> build(String[] names) {
        Stack<Rule> rules = new Stack<>();

        // rule 1
        int length = names.length;
        DefaultRule rule1 = new DefaultRule();
        int index = length;
        while (index-- > 0) {
            String str = StringUtils.trim(names[index]);
            if (StringUtils.isEmpty(str)) {
                continue;
            }

            // match
            Matcher matcher = PATTERN.matcher(str);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                Name<?> name = NumberUtils.isCreatable(group1) ? new IntName(NumberUtils.toInt(group1)) : new TextName(group1);
                rule1.pushName(name);
                rule1.pushName(new TextName(str.substring(0, matcher.start())));
                continue;
            }

            rule1.pushName(new TextName(str));
        }
        rules.push(rule1);

        // rule 2
        // ...

        return rules;
    }

}
