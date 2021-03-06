package org.fluentlenium.core.events;

import com.google.common.base.Function;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.utils.ReflectionUtils;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Element annotation listener
 */
class AnnotationElementListener extends AbstractAnnotationListener implements ElementListener {
    private final Method method;
    private final String annotationName;

    /**
     * Creates a new element annotation listener
     *
     * @param method         method to call when the event occurs
     * @param container      container to call when the event occurs
     * @param annotationName name of the annotation
     * @param priority       priority of this listener
     */
    AnnotationElementListener(final Method method, final Object container, final String annotationName, final int priority) {
        super(container, priority);
        this.method = method;
        this.annotationName = annotationName;
    }

    /**
     * Get a function that retrieves argument value based on argument type.
     *
     * @param element element
     * @param driver  driver
     * @return function
     */
    protected Function<Class<?>, Object> getArgsFunction(final FluentWebElement element, final WebDriver driver) {
        return new Function<Class<?>, Object>() {
            @Override
            public Object apply(final Class<?> input) {
                if (input.isAssignableFrom(FluentWebElement.class)) {
                    return element;
                }
                if (input.isAssignableFrom(WebDriver.class)) {
                    return driver;
                }
                return null;
            }
        };
    }

    @Override
    public void on(final FluentWebElement element, final WebDriver driver) {
        final Class<?>[] parameterTypes = method.getParameterTypes();

        final Object[] args = ReflectionUtils.toArgs(getArgsFunction(element, driver), parameterTypes);

        try {
            ReflectionUtils.invoke(method, getContainer(), args);
        } catch (final IllegalAccessException e) {
            throw new EventAnnotationsException("An error has occured in " + annotationName + " " + method, e);
        } catch (final InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            } else if (e.getTargetException() instanceof Error) {
                throw (Error) e.getTargetException();
            }
            throw new EventAnnotationsException("An error has occured in " + annotationName + " " + method, e);
        }
    }
}
