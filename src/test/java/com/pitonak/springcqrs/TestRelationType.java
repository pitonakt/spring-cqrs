package com.pitonak.springcqrs;

import org.springframework.stereotype.Component;

import com.pitonak.springcqrs.core.command.RelationType;

@Component
public final class TestRelationType implements RelationType {
    
    private TestRelationType() {}

    public static final String SELF = "self";
    
    public static final String CREATE_USER = "create_user";
    
    public static final String UPDATE_USER = "update_user";
}