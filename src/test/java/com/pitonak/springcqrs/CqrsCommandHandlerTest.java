package com.pitonak.springcqrs;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitonak.springcqrs.user.command.CreateUserCommand;
import com.pitonak.springcqrs.user.command.UpdateUserCommand;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CqrsCommandHandlerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper jacksonObjectMapper;
    
    @Test
    public void should_return_created_user___when_user_is_created() throws Exception {
 
        final CreateUserCommand request = createUserCommand();
        
        mockMvc.perform(post("/users/create-user")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(jacksonObjectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode", is("OK")))
            .andExpect(jsonPath("$.statusCodeValue", is(200)))
            .andExpect(jsonPath("$.body.name", is(request.getName())))
            .andExpect(jsonPath("$.body.age", is(request.getAge())));
    }
    
    @Test
    public void should_return_updated_user___when_user_is_updated() throws Exception {
 
        final UpdateUserCommand request = updateUserCommand();
        
        mockMvc.perform(put("/users/{id}/update-user", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(jacksonObjectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusCode", is("OK")))
            .andExpect(jsonPath("$.statusCodeValue", is(200)))
            .andExpect(jsonPath("$.body.name", is(request.getName())))
            .andExpect(jsonPath("$.body.age", is(request.getAge())));
    }
    
    private CreateUserCommand createUserCommand() {
        CreateUserCommand request = new CreateUserCommand();
        request.setName("Test");
        request.setAge(18);
        
        return request;
    }
    
    private UpdateUserCommand updateUserCommand() {
        UpdateUserCommand request = new UpdateUserCommand();
        request.setId(1L);
        request.setName("Test");
        request.setAge(18);
        
        return request;
    }
}