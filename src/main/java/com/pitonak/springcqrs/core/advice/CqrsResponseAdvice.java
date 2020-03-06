package com.pitonak.springcqrs.core.advice;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.pitonak.springcqrs.core.annotation.ResourceId;
import com.pitonak.springcqrs.core.annotation.ResourceObject;
import com.pitonak.springcqrs.core.command.CommandHandler;
import com.pitonak.springcqrs.core.command.RelationType;
import com.pitonak.springcqrs.core.utils.CqrsUtils;

import lombok.SneakyThrows;

@ControllerAdvice
public class CqrsResponseAdvice implements ResponseBodyAdvice<Object> {
    
    @Autowired
    private List<CommandHandler> handlers;
    
    @Autowired
    private RelationType relationType;
    
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return CommandHandler.class.isAssignableFrom(returnType.getDeclaringClass());
    }

    @Override
    @SneakyThrows
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, 
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
       
        RepresentationModel<?> model = (RepresentationModel<?> ) body;
        final String[] objectResourceType = new String[] {null};
        
        if (body.getClass().isAnnotationPresent(ResourceObject.class)) {
            objectResourceType[0] = body.getClass().getAnnotation(ResourceObject.class).resourceType();
        }
        
        Map<String, String> links = handlers.stream()
                .flatMap(Stream::of)
                .filter(handler -> Objects.equals(CqrsUtils.getCqrsCommandAnnotationForHandler(handler)[0].resourceType(),
                        objectResourceType[0]))
                .collect(Collectors.toMap(handler -> {
                    final String relation = CqrsUtils.getCqrsCommandAnnotationForHandler(handler)[0].relationType();
                    return getRelationTypeName(relation);
                },
                        handler -> {
                            String url = CqrsUtils.determineUrlsForHandler(handler)[0];
                            return url.replaceAll("\\{.*?\\}",
                                    Optional.ofNullable(getResourceIdValue(handler,
                                            body))
                                            .orElse(""));
                        }));
            
            links.entrySet().stream()
                .forEach(entry -> model.add(Link.of(entry.getValue(), entry.getKey())));
        
        return ResponseEntity.ok(model);
    }
    
    @SneakyThrows
    private static String getResourceIdValue(CommandHandler<?, ?> handler, Object response) {
        final Class<?>[] resolveTypeArgument = GenericTypeResolver.resolveTypeArguments(handler.getClass(), CommandHandler.class);
        
        for (Class<?> clazz : resolveTypeArgument) {
            if (clazz.isAnnotationPresent(ResourceObject.class)) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(ResourceId.class)) {
                        try {
                            final boolean isPrivateField = Modifier.isPrivate(field.getModifiers());
                            
                            if (isPrivateField) {
                                field.setAccessible(true);
                            }
                            
                            final String resourceIdValue = String.valueOf(field.get(response));
                            field.setAccessible(!isPrivateField);
                            
                            return resourceIdValue;
                        } catch (IllegalArgumentException | IllegalAccessException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    @SneakyThrows
    private String getRelationTypeName(String relationType) {
        Objects.requireNonNull(relationType, "relationType must no be null");
        
        for (Field field: this.relationType.getClass().getFields()) {
            if (field.get(null).equals(relationType)) {
                return field.getName().toLowerCase();
            }
        }
        
        throw new IllegalStateException(String.format("Missing relationType '%s'", relationType));
    }
}