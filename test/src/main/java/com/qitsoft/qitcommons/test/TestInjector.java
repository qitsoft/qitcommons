package com.qitsoft.qitcommons.test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

public class TestInjector {

    public static void inject(Object service, Object component, Class... genericTypes) throws IllegalAccessException {
        inject(service, service.getClass(), component, genericTypes);
    }

    private static void inject(Object service, Class serviceType, Object component, Class... genericTypes) throws IllegalAccessException {
        if (serviceType == Object.class) {
            return;
        }

        for (Field field : serviceType.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(component.getClass())) {
                if (field.getGenericType() != null && field.getGenericType() instanceof ParameterizedType) {
                    Type[] typeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    if (Arrays.equals(typeArguments, genericTypes)) {
                        field.setAccessible(true);
                        field.set(service, component);
                        return;
                    }
                } else {
                    field.setAccessible(true);
                    field.set(service, component);
                    return;
                }
            }
        }

        inject(service, serviceType.getSuperclass(), component);
    }
}
