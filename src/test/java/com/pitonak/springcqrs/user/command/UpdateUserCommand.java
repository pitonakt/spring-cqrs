package com.pitonak.springcqrs.user.command;

import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitonak.springcqrs.TestRelationType;
import com.pitonak.springcqrs.ResourceType;
import com.pitonak.springcqrs.core.annotation.CqrsCommand;
import com.pitonak.springcqrs.core.annotation.ResourceId;
import com.pitonak.springcqrs.core.command.Command;
import com.pitonak.springcqrs.user.dto.UserDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@CqrsCommand(resourceType = ResourceType.USERS, relationType = TestRelationType.UPDATE_USER, httpMethod = RequestMethod.PUT)
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserCommand extends UserDto implements Command<UserDto> {
 
    @ResourceId
    private Long id;
}