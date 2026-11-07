package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UnifiedSearchReponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UserReponse;
import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import by.Brahyam.Garcia.Blas.the.message.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; //

import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;


@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockitoBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    Authentication authentication;
    @Test
    @WithMockUser(username = "brahyam@garcia")
    public void testSearch()throws Exception{
        String query="brahyam";
        UserReponse userR=new UserReponse();
        userR.setEmail("brahyam@garcia");
        userR.setFullName("brahyam");
        UnifiedSearchReponse reponse=new UnifiedSearchReponse();
        reponse.getNewContacts().add(userR);
        when(authentication.getName()).thenReturn("brahyam@garcia");
        when(userService.searchUserAndRooms(eq(query),eq("brahyam@garcia"))).thenReturn(reponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bra/users/search")
                .with(csrf())
                .param("query",query))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.existingChats").isEmpty())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.newContacts[0].fullName").value("brahyam"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.newContacts[0].email").value("brahyam@garcia"));
    }
    @Test
    @WithMockUser(username = "brahyam@garcia")
    public void testUpdateProfilePicture()throws Exception{
        String imageUrl="bivfhubvjifrvhb";
        MessageResponse response=new MessageResponse("profile pcture updated succesfully");
        when(userService.updateProfilePicture(eq("brahyam@garcia"),any(String.class))).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/bra/users/profile-picture")
                .with(csrf())
                .param("imageUrl",imageUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.message").value("profile pcture updated succesfully"));
    }
}
