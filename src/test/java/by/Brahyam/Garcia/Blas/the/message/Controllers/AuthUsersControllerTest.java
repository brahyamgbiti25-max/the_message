package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Request.*;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.LoginResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import by.Brahyam.Garcia.Blas.the.message.Service.AuthUserService;
import by.Brahyam.Garcia.Blas.the.message.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static javax.management.Query.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// well understanding RestController Already i know that when you use this
//automatically this sends and brings data on format Json and if you wanna send
//also html response you have to use @Controllernormal to do this
@WebMvcTest(AuthUsersController.class)
public class AuthUsersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private  AuthUserService authUserService;
    @MockitoBean
    private EmailService emailService;
    @MockitoBean
    private JwtUtil jwtUtil;
    @Test
    @WithMockUser
    public  void testSingUpSuccessfully() throws Exception{
        AuthUserRequest request=new AuthUserRequest("user@mail.com","password123","nxendejj","jopewmkn");
        MessageResponse messageResponse=new MessageResponse("the user has been created");
        when(authUserService.signUpUser(any(AuthUserRequest.class))).thenReturn(messageResponse);
        mockMvc.perform(post("/api/bra/auth/signup")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("the user has been created"));
    }
    @Test
    @WithMockUser
    public  void testValidateEmailSuccessfully() throws Exception{
        ValidateEmailRequest request=new ValidateEmailRequest("user@mail.com");
        MessageResponse messageResponse=new MessageResponse("this email is valid so you can  verify in your messages go to there");
        when(authUserService.validateEmail(any(ValidateEmailRequest.class))).thenReturn(messageResponse);
        mockMvc.perform(post("/api/bra/auth/validate-email")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("this email is valid so you can  verify in your messages go to there"));
    }
    @Test
    @WithMockUser
    public  void testVerifyEmail() throws Exception{
        String token="kvnjfnvfmvfjkvbnw";
        MessageResponse messageResponse=new MessageResponse("the account has been verify so you can loggin now");
        when(authUserService.verifyEmailUser(token)).thenReturn(messageResponse);
        mockMvc.perform(get("/api/bra/auth/verify-email")
                        .with(csrf())
                        .param("token",token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("the account has been verify so you can loggin now"));
    }
    @Test
    @WithMockUser
    public  void testLoginUser() throws Exception{
        LoginRequest loginRequest=new LoginRequest("vnjkfbjvjf","njkvfnjvj");
        LoginResponse loginResponse=new LoginResponse("gvfjkhbirufhejfruefurh","njkvfnjvj");
        when(authUserService.loginUser(any(LoginRequest.class))).thenReturn(loginResponse);
        mockMvc.perform(post("/api/bra/auth/login")
                        .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("gvfjkhbirufhejfruefurh"))
                .andExpect(jsonPath("$.email").value("njkvfnjvj"));
    }
    @Test
    @WithMockUser
    public  void testResendVerification() throws Exception{
        AuthUserRequest request=new AuthUserRequest("user@mail.com","password123","nxendejj","jopewmkn");
        MessageResponse response=new MessageResponse("check out your email again to verify you account");
        when(authUserService.resendVerification(ArgumentMatchers.eq("user@mail.com"))).thenReturn(response);
        mockMvc.perform(post("/api/bra/auth/resend-verification")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("check out your email again to verify you account"));
    }
    @Test
    @WithMockUser
    public  void testForgotPassword() throws Exception{
        ForgotPasswordRequest request=new ForgotPasswordRequest("brhayam@");
        MessageResponse response=new MessageResponse("the message was sent so check to the inbox");
        when(authUserService.forgotPasswordUser(request)).thenReturn(response);
        mockMvc.perform(post("/api/bra/auth/forgot-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("the message was sent so check to the inbox"));
    }
    @Test
    @WithMockUser
    public  void testResetPassword() throws Exception{
        ResetPasswordRequest request=new ResetPasswordRequest("njdcjdbcjjd","njcejfbj","hfejfbnrd");
        MessageResponse response=new MessageResponse("your password has been changed");
        when(authUserService.resetPassword(request)).thenReturn(response);
        mockMvc.perform(post("/api/bra/auth/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("your password has been changed"));
    }
    @Test
    @WithMockUser(username = "user@mail.com")
    public  void testChangePassword() throws Exception{
        ChangePasswordRequest request=new ChangePasswordRequest("nfkjbrjbcjkv","bjxbddekl");
        MessageResponse response=new MessageResponse("your password has been changed");
        when(authUserService.changePassword(any(ChangePasswordRequest.class),any(String.class)))
                .thenReturn(response);
        mockMvc.perform(post("/api/bra/auth/change-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("your password has been changed"));
    }

}
