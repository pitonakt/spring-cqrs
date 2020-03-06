package com.pitonak.springcqrs.user.dto;

import org.springframework.hateoas.RepresentationModel;

import com.pitonak.springcqrs.ResourceType;
import com.pitonak.springcqrs.core.annotation.ResourceId;
import com.pitonak.springcqrs.core.annotation.ResourceObject;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ResourceObject(resourceType = ResourceType.USERS)
public class UserDto extends RepresentationModel<UserDto> {
    
    @ResourceId
    private Long id;
    
    private String name;
    
    private int age;
}