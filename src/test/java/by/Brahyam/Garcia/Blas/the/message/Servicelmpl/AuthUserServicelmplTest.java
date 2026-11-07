package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Dto.Request.*;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.LoginResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.Role;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.*;
import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import by.Brahyam.Garcia.Blas.the.message.Service.EmailService;
import by.Brahyam.Garcia.Blas.the.message.Servicelmpl.AuthUserServicelmpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthUserServicelmplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthUserServicelmpl authUserService;
    @Mock
    JwtUtil jwtUtil;

    @Test
    public void testSignUpUserSuccess() {
        // 1. ARRANGE: Set up test input data
        AuthUserRequest request = new AuthUserRequest();
        request.setEmail("test@mail.com");
        request.setFullName("Brahyam Test");
        request.setPassword("password123");

        // Tell the mock repository that the email does NOT exist yet
        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        // 2. ACT: Call the signup method
        MessageResponse response = authUserService.signUpUser(request);

        // 3. ASSERT: Verify the result and that methods were called
        assertNotNull(response);
        assertEquals("the user has been created", response.getMessage());

        // Verify that the user was actually saved to the mock repository
        verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));

        // Verify that the verification email was sent out
        verify(emailService, times(1)).sendMessageToVerifyEmail(eq("test@mail.com"), anyString());
    }

    @Test
    public void testSignUpUserThrowsExceptionWhenEmailExists() {
        // 1. ARRANGE: Set up a request with an email that already exists
        AuthUserRequest request = new AuthUserRequest();
        request.setEmail("existing@mail.com");

        // Tell the mock repository that this email IS already taken
        when(userRepository.existsByEmail("existing@mail.com")).thenReturn(true);

        // 2. ACT & ASSERT: Verify that it throws your custom EmailAlreadyException
        EmailAlreadyException exception = assertThrows(EmailAlreadyException.class, () -> {
            authUserService.signUpUser(request);
        });

        assertEquals("this email already exists", exception.getMessage());

        // Verify that save was NEVER called because it stopped early
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    public void testVerificationEmail(){
        ValidateEmailRequest request=new ValidateEmailRequest();
        request.setEmail("brahyam.gb.xd");
        User mockUser=new User();
        mockUser.setEmail(("brahyam.gb.xd"));
        when(userRepository.findByEmail("brahyam.gb.xd")).thenReturn(Optional.of(mockUser));

        MessageResponse response =  authUserService.validateEmail(request);
        assertNotNull(response);
        assertEquals("this email is valid so you can  verify in your messages go to there",response.getMessage());
    }
    @Test
    public void testVerificationEmailThrowsEmailNotFoundException(){
        ValidateEmailRequest request=new ValidateEmailRequest();
        request.setEmail("brahyam.gb.xd");
        when(userRepository.findByEmail("brahyam.gb.xd")).thenReturn(Optional.empty());
        EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, () -> {
            authUserService.validateEmail(request);
        });
        assertEquals("this email wasn't found",exception.getMessage());
    }
    @Test
    public void testVerifyEmailUserSuccess() {
        // 1. ARRANGE
        String token = "nvrjfvnrjvrbj";

        User mockUser = new User();
        mockUser.setVerificationToken(token);
        // Set expiry to 1 hour in the FUTURE so it is NOT expired
        mockUser.setVerificationTokenExpiry(Instant.now().plusSeconds(3600));

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(mockUser));

        // 2. ACT: Actually call the method you are testing!
        authUserService.verifyEmailUser(token);

        // 3. ASSERT: Verify that the user was updated and saved to the database
        assertTrue(mockUser.isEmailVerified());
        assertNull(mockUser.getVerificationToken());
        assertNull(mockUser.getVerificationTokenExpiry());

        // Verify that userRepository.save() was called exactly 1 time
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    public void testVerifyEmailUserThrowsInvalidTokenException() {
        // 1. ARRANGE
        String token = "invalid_token";

        // Tell the repository to return empty (simulating a token that doesn't exist)
        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.empty());

        // 2. ACT & ASSERT: Verify that it throws your InvalidTokenException
        InvalidTokenException exception = assertThrows(InvalidTokenException.class, () -> {
            authUserService.verifyEmailUser(token);
        });

        // Match the exact message from your implementation: "this token doesn't exists"
        assertEquals("this token doesn't exists", exception.getMessage());

        // Ensure save was never called
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testVerifyEmailUserThrowsTokenExpiredException() {
        // 1. ARRANGE
        String token = "expired_token";

        User mockUser = new User();
        mockUser.setVerificationToken(token);
        // Set expiry to 1 hour in the PAST so it IS expired
        mockUser.setVerificationTokenExpiry(Instant.now().minusSeconds(3600));

        when(userRepository.findByVerificationToken(token)).thenReturn(Optional.of(mockUser));

        // 2. ACT & ASSERT: Verify that it throws your TokenExpiredException
        TokenExpiredException exception = assertThrows(TokenExpiredException.class, () -> {
            authUserService.verifyEmailUser(token);
        });

        // Match the exact message from your implementation: "this token already has expired"
        assertEquals("this token already has expired", exception.getMessage());

        // Ensure save was never called because it failed early
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    public void testLoginUserSuccess() {
        // 1. ARRANGE
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("brahyam.iti25");
        loginRequest.setPassword("brahyam12345");

        User mockUser = new User();
        mockUser.setEmail(loginRequest.getEmail());
        mockUser.setPassword("encoded_password");
        mockUser.setEmailVerified(true);
        mockUser.setRole(Role.USER);

        // Tell repository to return the mock user
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(mockUser));

        // Tell passwordEncoder to return true (password matches)
        when(passwordEncoder.matches(loginRequest.getPassword(), mockUser.getPassword())).thenReturn(true);

        // Tell jwtUtil to return a fake generated token string
        when(jwtUtil.generateToken(mockUser.getEmail(), mockUser.getRole().name())).thenReturn("mocked_jwt_token_123");

        // 2. ACT
        LoginResponse response = authUserService.loginUser(loginRequest);

        // 3. ASSERT
        assertNotNull(response);
        assertEquals("mocked_jwt_token_123", response.getToken());
        assertEquals("brahyam.iti25", response.getEmail());
    }
    @Test
    public void testLoginUserFailedEmail(){
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setEmail("brahyam.iti25");
        loginRequest.setPassword("brahyam12345");
        User mockUser=new User();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->{
            authUserService.loginUser(loginRequest);
        });
        assertEquals("this email doesn't exist",exception.getMessage());
    }

    @Test
    public void testLoginUserFailedPassword(){
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setEmail("brahyam.iti25");
        loginRequest.setPassword("brahyam12345");
        User mockUser=new User();
        mockUser.setPassword(loginRequest.getPassword());
        mockUser.setEmail(loginRequest.getEmail());
        mockUser.setEmailVerified(true);

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), mockUser.getPassword())).thenReturn(false);
       PasswordEncoderNotMatch exception=assertThrows(PasswordEncoderNotMatch.class,()->{
           authUserService.loginUser(loginRequest);
       });
        assertEquals("the passwords don't match ",exception.getMessage());
    }
    @Test
    public void testLoginUserFailedEmailVerified(){
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setEmail("brahyam.iti25");
        loginRequest.setPassword("brahyam12345");
        User mockUser=new User();
        mockUser.setPassword(loginRequest.getPassword());
        mockUser.setEmail(loginRequest.getEmail());
        mockUser.setEmailVerified(false);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), mockUser.getPassword())).thenReturn(true);
        EmailNotVerifiedException exception=assertThrows(EmailNotVerifiedException.class,()->{
            authUserService.loginUser(loginRequest);
        });
        assertEquals("this email hasn't been verified",exception.getMessage());
    }
    @Test
    public void testResendVerificationSuccessful(){
        String email="jncjbvbrdvnrdjbvrbdj";
        User mockUser=new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        mockUser.setVerificationToken("jkhcfjcvfejfrnvnb");
        mockUser.setVerificationTokenExpiry(Instant.now().plusSeconds(36000));
        authUserService.resendVerification(email);
    }
    @Test
    public void testResendVerifyBadException(){
        String email="jncjbvbrdvnrdjbvrbdj";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->{
            authUserService.resendVerification(email);
        });
        assertEquals("this email wasn't found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    public void testForgotPasswordSuccess() {
        // 1. ARRANGE
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        String email = "test@mail.com";
        request.setEmail(email);

        User mockUser = new User();
        mockUser.setEmail(email);

        // Tell the mock repository to return the user successfully
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // 2. ACT
        authUserService.forgotPasswordUser(request);

        // 3. ASSERT: Verify that a reset token was generated, saved, and emailed
        assertNotNull(mockUser.getPasswordResetToken());
        assertNotNull(mockUser.getPasswordResetTokenExpiry());

        // Verify that the user was saved to the repository
        verify(userRepository, times(1)).save(mockUser);

        // Verify that the reset password email was triggered
        verify(emailService, times(1)).sendMessageToResetPassword(eq(email), anyString());
    }
    @Test
    public void testForgotPassword(){
        ForgotPasswordRequest request=new ForgotPasswordRequest();
        String email="jncjbvbrdvnrdjbvrbdj";
        request.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->{
            authUserService.forgotPasswordUser(request);
        });
        assertEquals("Email wasn't found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(emailService,never()).sendMessageToResetPassword(anyString(),anyString());
    }
    @Test
    public void testResetPasswordSuccessful() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("vbfvhfjvbfulhvgrufcygebfhcerf");
        request.setNewPassword("njcdncjcndjc");
        request.setCommitNewPassword("njcdncjcndjc");
        User mockUser = new User();
        mockUser.setPasswordResetToken(request.getToken());
        mockUser.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600)); // Valid expiry
        when(userRepository.findByPasswordResetToken(request.getToken())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encoded_password");
        authUserService.resetPassword(request);
        assertNull(mockUser.getPasswordResetToken());
        assertNull(mockUser.getPasswordResetTokenExpiry());
        assertEquals("encoded_password", mockUser.getPassword());
        verify(userRepository,times(1)).save(mockUser);
    }
    @Test
    public void testResetPasswordExceptionTokenNotFound() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("vbfvhfjvbfulhvgrufcygebfhcerf");
        request.setNewPassword("njcdncjcndjc");
        request.setCommitNewPassword("njcdncjcndjc");
        when(userRepository.findByPasswordResetToken(request.getToken())).thenReturn(Optional.empty());
        TokenVericationPasswordNotFoundExeption exception=
                assertThrows(TokenVericationPasswordNotFoundExeption.class,()->{authUserService.resetPassword(request);});
        assertEquals("this token isn't exits",exception.getMessage());
        verify(userRepository,never()).save(any(User.class));
    }
    @Test
    public void testResetPasswordExceptionTokenExpired() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("vbfvhfjvbfulhvgrufcygebfhcerf");
        request.setNewPassword("njcdncjcndjc");
        request.setCommitNewPassword("njcdncjcndjc");
        User mockUser = new User();
        mockUser.setPasswordResetToken(request.getToken());
        mockUser.setPasswordResetTokenExpiry(Instant.now().minusSeconds(3600));
        when(userRepository.findByPasswordResetToken(request.getToken())).thenReturn(Optional.of(mockUser));
        TokenExpiredException exception=
                assertThrows(TokenExpiredException.class,()->{authUserService.resetPassword(request);});
        assertEquals("this token has been expired",exception.getMessage());
        verify(userRepository,never()).save(any(User.class));
    }
    @Test
    public void testResetPasswordExceptionFieldsNotMatches() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("vbfvhfjvbfulhvgrufcygebfhcerf");
        request.setNewPassword("njcdncjcndjc");
        request.setCommitNewPassword("njcdncjcndjcty");
        User mockUser = new User();
        mockUser.setPasswordResetToken(request.getToken());
        mockUser.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));
        when(userRepository.findByPasswordResetToken(request.getToken())).thenReturn(Optional.of(mockUser));
        FieldsNotMatchesException exception=assertThrows(FieldsNotMatchesException.class,()->authUserService.resetPassword(request));
        assertEquals("this fields don't match",exception.getMessage());
        verify(userRepository,never()).save(any(User.class));
    }
    @Test
    public void testChangePasswordSuccessful() {
        String email="gcemkgtgjerfberjh";
        ChangePasswordRequest request=new ChangePasswordRequest();
        request.setNewPassword("bjknvfbverjkvbfjk");
        request.setCurrentPassword("bvrjkvbrdjvbjdb");
        User mockUser=new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getCurrentPassword(),mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encoded_password");
        authUserService.changePassword(request,email);
        assertNotNull(mockUser.getPassword());
        verify(userRepository,times(1)).save(mockUser);
    }
    @Test
    public void testChangePasswordExceptionEmail() {
        String email="gcemkgtgjerfberjh";
        ChangePasswordRequest request=new ChangePasswordRequest();
        request.setNewPassword("bjknvfbverjkvbfjk");
        request.setCurrentPassword("bvrjkvbrdjvbjdb");
        User mockUser=new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->authUserService.changePassword(request,email));
        assertEquals("email not found",exception.getMessage());
        verify(userRepository,never()).save(mockUser);
    }
    @Test
    public void testChangePasswordExceptionPasswordEncode() {
        String email="gcemkgtgjerfberjh";
        ChangePasswordRequest request=new ChangePasswordRequest();
        request.setNewPassword("bjknvfbverjkvbfjk");
        request.setCurrentPassword("bvrjkvbrdjvbjdb");
        User mockUser=new User();
        mockUser.setEmail(email);
        mockUser.setPassword("bvrjkvbrdjvbjdbty");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getCurrentPassword(),mockUser.getPassword())).thenReturn(false);
        PasswordEncoderNotMatch exception=assertThrows(PasswordEncoderNotMatch.class,()->authUserService.changePassword(request,email));
        assertEquals("the current password doesn't match",exception.getMessage());
        verify(userRepository,never()).save(mockUser);
    }
}