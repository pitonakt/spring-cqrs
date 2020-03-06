package com.pitonak.springcqrs.core.command;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FunctionalInterface
public interface CommandHandler<RESPONSE, REQUEST extends Command<RESPONSE>> {
    
    @ResponseBody
    public RESPONSE handle(@RequestBody REQUEST request);
}