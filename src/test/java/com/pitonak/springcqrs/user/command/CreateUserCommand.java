package com.pitonak.springcqrs.user.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitonak.springcqrs.TestRelationType;
import com.pitonak.springcqrs.ResourceType;
import com.pitonak.springcqrs.core.annotation.CqrsCommand;
import com.pitonak.springcqrs.core.command.Command;
import com.pitonak.springcqrs.user.dto.UserDto;

@CqrsCommand(resourceType = ResourceType.USERS, relationType = TestRelationType.CREATE_USER)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserCommand extends UserDto implements Command<UserDto> {
}