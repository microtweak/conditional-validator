package com.github.microtweak.validator.conditional.core;

import com.github.microtweak.validator.conditional.core.exception.InvalidConditionalExpressionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class ExpressionEvaluator {

    private JexlEngine engine;
    private Map<String, Object> staticContext;

    private boolean staticContextInitialized;

    public ExpressionEvaluator(Class<?>... contextClasses) {
        engine = new JexlBuilder().create();
        staticContext = new HashMap<>();
        Stream.of( contextClasses ).forEach(this::addEnumToContext);
    }

    private void fillStaticContextWithInnerClasses(Class<?> beanClass) {
        if (!staticContextInitialized) {
            Stream.of( beanClass.getClasses() ).forEach(this::addEnumToContext);
            staticContextInitialized = true;
        }
    }

    private <E extends Enum> void addEnumToContext(Class<?> clazz) {
        if (!clazz.isEnum()) {
            return;
        }

        for (E constant : ((Class<E>) clazz).getEnumConstants()) {
            final String key = clazz.getSimpleName() + "." + constant.name();
            staticContext.put(key, constant);
        }
    }

    public boolean isTrueExpression(Object bean, String expression) {
        fillStaticContextWithInnerClasses(bean.getClass());

        JexlContext ctx = new MapContext();
        staticContext.forEach(ctx::set);

        fillContextWithBean(ctx, bean);

        Object result = engine
                .createExpression(expression)
                .evaluate(ctx);

        if (!Boolean.class.isInstance(result)) {
            throw new InvalidConditionalExpressionException("The expression \"" + expression + "\" should return boolean!");
        }

        return Boolean.class.cast(result);
    }

    private void fillContextWithBean(JexlContext ctx, Object bean) {
        ctx.set("self", bean);

        for (Field field : bean.getClass().getDeclaredFields()) {
            try {
                Object value = FieldUtils.readField(field, bean, true);
                ctx.set(field.getName(), value);
            } catch (IllegalAccessException e) {
                throw new InvalidConditionalExpressionException("Unable to read/access field \"" + field.getName() + "\" used in expression");
            }
        }
    }

}
