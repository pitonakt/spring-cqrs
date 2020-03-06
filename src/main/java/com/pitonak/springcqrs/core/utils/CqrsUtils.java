package com.pitonak.springcqrs.core.utils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.GenericTypeResolver;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pitonak.springcqrs.core.annotation.CqrsCommand;
import com.pitonak.springcqrs.core.annotation.ResourceId;
import com.pitonak.springcqrs.core.command.CommandHandler;

public final class CqrsUtils {
    
    private CqrsUtils() {}

    public static String[] determineUrlsForHandler(CommandHandler<?, ?> handler) {
        return Stream.of(getCqrsCommandAnnotationForHandler(handler))
                .map(annotation -> LinksUtils.getUrlFromRelation(annotation.resourceType(), annotation.relationType(), getResourceIdName(handler)))
                .toArray(String[]::new);
    }
    
    public static RequestMethod[] determineRequestMethodsForHandler(CommandHandler<?, ?> handler) {
        return Stream.of(getCqrsCommandAnnotationForHandler(handler))
                .map(CqrsCommand::httpMethod)
                .toArray(RequestMethod[]::new);
    }
        
    public static String getResourceIdName(CommandHandler<?, ?> handler) {
        return Optional.ofNullable(getResourceId(handler)).map(Field::getName).orElse(null);
    }
    
    public static Field getResourceId(CommandHandler<?, ?> handler) {
        final Class<?>[] resolveTypeArgument = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);
        
        
        for (Class<?> clazz : resolveTypeArgument) {
            if (clazz.isAnnotationPresent(CqrsCommand.class)) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ResourceId.class)) {
                        return field;
                    }
                }
            }
        }
        
        return null;
    }
    
    public static CqrsCommand[] getCqrsCommandAnnotationForHandler(CommandHandler<?, ?> handler) {
        final Class<?>[] resolveTypeArgument = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);
        
        return Stream.of(resolveTypeArgument)
            .filter(clazz -> clazz.isAnnotationPresent(CqrsCommand.class))
            .map(clazz -> clazz.getAnnotation(CqrsCommand.class))
            .toArray(CqrsCommand[]::new);
    }
}