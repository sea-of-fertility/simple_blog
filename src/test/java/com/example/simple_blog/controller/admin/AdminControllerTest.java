package com.example.simple_blog.controller.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {


    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("admin만 접근 가능")
    public void adminAccess() throws Exception {
        //given
        mockMvc.perform(get("/chat-blog/admin"))
                .andDo(print())
                .andExpect(status().isOk());

    }
}