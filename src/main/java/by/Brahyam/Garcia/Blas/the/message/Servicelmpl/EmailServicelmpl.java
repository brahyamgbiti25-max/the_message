package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailSendingException;
import by.Brahyam.Garcia.Blas.the.message.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServicelmpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${app.frontend.url}")
    private String frontUrl;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Override
    public void sendMessageToVerifyEmail(String toEmail, String token) {
        try{
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Talent_isn't_require");
            String verificationLink=frontUrl+"/verify-email?token="+token;
            String emailBody=
                    "Welcome to nefflix clone!\n\n"
                            +"thanks you for registering. Please verify your email address by clicking the link below\n\n"
                            + verificationLink
                            +"\n\n"
                            +"This link will expire in 24 hours. \n\n"
                            +"If you didn't create this account , ignore this eamil. \n\n"
                            +"Best regards. \n"
                            +"Amazon clone team";
            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("email has been sent to the user to verify its account");
        }catch (Exception ex){
            throw new EmailSendingException("Failed to send verification email");
        }
    }

    @Override
    public void sendMessageToResetPassword(String toEmail, String passwordResetToken) {
        try{
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Talent_isn't_require");
            String emailBody=
                    "Welcome to The message!\n\n"
                            +"to use this token to reset your password enter this link\n\n"
                            + passwordResetToken
                            +"\n\n"
                            +"This link will expire in 1 hours. \n\n"
                            +"If you didn't requested this , ignore this eamil. \n\n"
                            +"Best regards. \n"
                            +"The message team";
            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("email has been sent to the user to reset the password");
        }catch (Exception ex){
            throw new EmailSendingException("Failed to send reset password email");
        }
    }
}
