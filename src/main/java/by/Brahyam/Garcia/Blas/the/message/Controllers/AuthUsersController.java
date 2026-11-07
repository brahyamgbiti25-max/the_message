package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Request.*;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.LoginResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// well understanding RestController Already i know that when you use this
//automatically this sends and brings data on format Json and if you wanna send
//also html response you have to use @Controllernormal to do this
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bra/auth")
public class AuthUsersController {
    @Autowired
    AuthUserService authUserService;
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUp(@RequestBody AuthUserRequest userRequest){
        MessageResponse response= authUserService.signUpUser(userRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/validate-email")
    public ResponseEntity<MessageResponse> validateEmail(@RequestBody ValidateEmailRequest validateEmailRequest){
        MessageResponse response=authUserService.validateEmail(validateEmailRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token){
        MessageResponse response=authUserService.verifyEmailUser(token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authUserService.loginUser(loginRequest));
    }
    @PostMapping("/resend-verification")
    public ResponseEntity<MessageResponse> resendVerification (@RequestBody AuthUserRequest authUserRequest){
        System.out.println(authUserRequest.getEmail());
        MessageResponse response=authUserService.resendVerification(authUserRequest.getEmail());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        MessageResponse response=authUserService.forgotPasswordUser(forgotPasswordRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword (@RequestBody ResetPasswordRequest resetPasswordRequest){
        MessageResponse response=authUserService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok(response);
    }
    //tomorrow I need to finish this end poitn and beginf to undestando about webSockets (never give up)
    @PostMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(Authentication authentication, @RequestBody ChangePasswordRequest changePasswordRequest){
        String email=authentication.getName();
        MessageResponse response=authUserService.changePassword(changePasswordRequest,email);
        return ResponseEntity.ok(response);
    }
}
