package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Dto.Request.*;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.LoginResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.Role;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.*;
import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import by.Brahyam.Garcia.Blas.the.message.Service.AuthUserService;
import by.Brahyam.Garcia.Blas.the.message.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthUserServicelmpl implements AuthUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public MessageResponse signUpUser(AuthUserRequest userRequest) {
       if( userRepository.existsByEmail(userRequest.getEmail())){
           throw new EmailAlreadyException("this email already exists");
        }
        User user =new User();
        user.setActive(true);
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        user.setRole(Role.USER);
        user.setCreatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendMessageToVerifyEmail(userRequest.getEmail(),user.getVerificationToken());
        return new MessageResponse("the user has been created");
    }

    @Override
    public MessageResponse validateEmail(ValidateEmailRequest validateEmailRequest) {
        userRepository.findByEmail(validateEmailRequest.
                getEmail()).orElseThrow(()->new EmailNotFoundException("this email wasn't found"));
        return new MessageResponse("this email is valid so you can  verify in your messages go to there");
    }

    @Override
    public MessageResponse verifyEmailUser(String token) {
        User user=userRepository.findByVerificationToken(token)
                .orElseThrow(()->new InvalidTokenException("this token doesn't exists"));
        if(user.getVerificationTokenExpiry().isBefore(Instant.now())){
            throw new TokenExpiredException("this token already has expired");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
        return new MessageResponse("the account has been verify so you can loggin now");
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user=userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new EmailNotFoundException("this email doesn't exist"));
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new PasswordEncoderNotMatch("the passwords don't match ");
        }
        if(!user.isEmailVerified()){
           throw new EmailNotVerifiedException("this email hasn't been verified");
        }

       String token= jwtUtil.generateToken(user.getEmail(),user.getRole().name());
        return new LoginResponse(token,user.getEmail());
    }

    @Override
    public MessageResponse resendVerification(String email) {
        System.out.println("DEBUG: Looking up email for resend -> [" + email + "]");
       User user= userRepository.findByEmail(email)
                .orElseThrow(()->new EmailNotFoundException("this email wasn't found"));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendMessageToVerifyEmail(user.getEmail(),user.getVerificationToken());
        return new MessageResponse("check out your email again to verify you account");
    }

    @Override
    public MessageResponse forgotPasswordUser(ForgotPasswordRequest forgotPasswordRequest) {
        User user=userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(()->new EmailNotFoundException("Email wasn't found"));
        user.setPasswordResetToken(UUID.randomUUID().toString());
        user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));
        userRepository.save(user);
        emailService.sendMessageToResetPassword(user.getEmail(),user.getPasswordResetToken());
        return new MessageResponse("the message was sent so check to the inbox");
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user=userRepository.findByPasswordResetToken(resetPasswordRequest.getToken())
                .orElseThrow(()->new TokenVericationPasswordNotFoundExeption("this token isn't exits"));
        if(user.getPasswordResetTokenExpiry().isBefore(Instant.now())){
            throw new TokenExpiredException("this token has been expired");
        }
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getCommitNewPassword())){
            throw new FieldsNotMatchesException("this fields don't match");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setPasswordResetTokenExpiry(null);
        user.setPasswordResetToken(null);
        userRepository.save(user);
        return new MessageResponse("your password has been changed");
    }

    @Override
    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest, String email) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new EmailNotFoundException("email not found"));
        if(!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(),user.getPassword())){
            throw new PasswordEncoderNotMatch("the current password doesn't match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return new MessageResponse("your password has been changed");
    }

}
