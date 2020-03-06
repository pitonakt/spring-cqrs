package com.pitonak.springcqrs.user.handler;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.pitonak.springcqrs.core.command.CommandHandler;
import com.pitonak.springcqrs.user.command.UpdateUserCommand;
import com.pitonak.springcqrs.user.dto.UserDto;

@Component
public class UpdateUserCommandHandler implements CommandHandler<UserDto, UpdateUserCommand> {
    
    @Override
    public UserDto handle(UpdateUserCommand request) {
                
        UserDto dto = new UserDto();
        dto.setId(Math.abs(new Random().nextLong()));
        dto.setName(request.getName());
        dto.setAge(request.getAge());

        return dto;
    }
}