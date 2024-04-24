package com.example.simple_blog.config.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    public void adminPage() throws Exception {
        this.mockMvc.perform(get("/chat-blog/admin"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }


    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    public void memberPage() throws Exception {
        this.mockMvc.perform(get("/chat-blog/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}