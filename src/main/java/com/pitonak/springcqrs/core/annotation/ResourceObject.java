package com.pitonak.springcqrs.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceObject {
    
    public String resourceType() default "";
}