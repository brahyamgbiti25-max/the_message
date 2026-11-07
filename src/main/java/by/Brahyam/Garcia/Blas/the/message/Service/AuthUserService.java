package by.Brahyam.Garcia.Blas.the.message.Service;

import by.Brahyam.Garcia.Blas.the.message.Dto.Request.*;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.LoginResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;

public interface AuthUserService {
    MessageResponse signUpUser(AuthUserRequest userRequest);
    
    MessageResponse validateEmail(ValidateEmailRequest validateEmailRequest);

    MessageResponse verifyEmailUser(String token);

    LoginResponse loginUser(LoginRequest loginRequest);

    MessageResponse resendVerification(String email);

    MessageResponse forgotPasswordUser(ForgotPasswordRequest forgotPasswordRequest);

    MessageResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

    MessageResponse  changePassword(ChangePasswordRequest changePasswordRequest, String email);
}
