package com.ivnd.knowledgebase.helper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Component("spEl")
public class SpringExprHelper {

    private final SpelExpressionParser parser;
    private final BeanResolver beanResolver;

    public SpringExprHelper(ApplicationContext applicationContext) {
        this.parser = new SpelExpressionParser();
        this.beanResolver = new BeanFactoryResolver(applicationContext);
    }

    public Expression parse(String expr) {
        return parser.parseExpression(expr);
    }

    public Expression parseExpr(String expr) {
        try {
            return parser.parseExpression(expr);
        } catch (Exception e) {
            log.error("ParseExpr: " + e.getMessage(), e);
            return null;
        }

    }

    public Object getValue(String expr, EvaluationContext context) {
        var expression = parse(expr);
        return expression.getValue(context);
    }

    public Object getValue(String expr, Map<String, Object> variableMap) {
        var context = new StandardEvaluationContext(this);
        context.addPropertyAccessor(new MapAccessor());
        context.setVariables(variableMap);

        var expression = parse(expr);
        return expression.getValue(context);
    }


    public Object getValue(String expr, Object object) {
        var context = new StandardEvaluationContext(object);
        var expression = parse(expr);
        return expression.getValue(context);
    }

    public Object getValueExpr(String expr, Object object) {
        var isValueExpr = expr.contains("#");
        if (!isValueExpr) {
            return null;
        }

        var context = new StandardEvaluationContext(object);
        var expression = parseExpr(expr);
        return Objects.nonNull(expression) ? expression.getValue(context) : null;
    }

    public Boolean getValueAsBool(String expr, Object object) {
        var context = new StandardEvaluationContext(object);
        var expression = parse(expr);
        return expression.getValue(context, Boolean.class);
    }

    public Boolean getValueAsBool(String expr, Map<String, Object> variableMap) {
        var context = new StandardEvaluationContext(variableMap);
        context.addPropertyAccessor(new MapAccessor());
        context.setBeanResolver(beanResolver);
        context.setVariables(variableMap);
        var expression = parse(expr);
        return (Boolean) expression.getValue(context);
    }

    /**
     * Mean enum equal.
     *
     * @param obj  enum obj.
     * @param name name.
     */
    public boolean enumEq(Enum<?> obj, String name) {
        return obj.name().equals(name);
    }

    /**
     * validate provided string is empty in expression.
     *
     * @param str string
     * @return boolean
     */
    public boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    /**
     * Parse number to double then compare them.
     *
     * @param num1 number1.
     * @param num2 number2.
     * @return resp as int.
     */
    public int compareNumber(String num1, String num2) {
        var numberDouble1 = Double.parseDouble(num1);
        var numberDouble2 = Double.parseDouble(num2);
        return Double.compare(numberDouble1, numberDouble2);
    }

    /**
     * check collection containAny element in <code>comparor</code>
     */
    public <E, T extends Collection<E>> boolean containAny(T collection, E... comparor) {
        return !(collection == null || comparor == null)
                && Arrays.stream(comparor).anyMatch(collection::contains);
    }

    public <T> boolean isIn(T input, T... elements) {
        return !(input == null || elements == null)
                && Arrays.asList(elements).contains(input);
    }

}
