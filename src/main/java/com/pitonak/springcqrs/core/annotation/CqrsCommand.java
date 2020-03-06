package com.pitonak.springcqrs.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
public @interface CqrsCommand {
    
    public String resourceType() default "";
    
    public String relationType() default "";
    
    public String consumes() default MediaType.APPLICATION_JSON_VALUE;
    
    public String produces() default MediaType.APPLICATION_JSON_VALUE;
    
    public RequestMethod httpMethod() default RequestMethod.POST;
}