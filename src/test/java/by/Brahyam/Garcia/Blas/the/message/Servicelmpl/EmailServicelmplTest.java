package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailSendingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EmailServicelmplTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private EmailServicelmpl emailService;
  // Point to the implementation class
    @Test
    public void testSendMessageToVerifyEmailSuccessfully(){
        ReflectionTestUtils.setField(emailService,"frontUrl","http://localhost:3000");
        ReflectionTestUtils.setField(emailService,"fromEmail","test@app.com");

        String toEmail="user@mail.com";
        String token="abc-123-token";

        emailService.sendMessageToVerifyEmail(toEmail,token);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender,times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage=messageCaptor.getValue();
        assertEquals("user@mail.com",sentMessage.getTo()[0]);
        assertEquals("test@app.com",sentMessage.getFrom());
        assertEquals("Talent_isn't_require",sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("http://localhost:3000/verify-email?token=abc-123-token"));
    }
    @Test
    public void testSendMessageToVerifyEmailException(){
        ReflectionTestUtils.setField(emailService,"frontUrl","http://localhost:3000");
        ReflectionTestUtils.setField(emailService,"fromEmail","test@App.com");

        String toEmail="user@mail.com";
        String token="adv-123-token";

        doThrow(new RuntimeException("SMTP server down")).when(mailSender).send(any(SimpleMailMessage.class));
        EmailSendingException exception=assertThrows(EmailSendingException.class,()->emailService.sendMessageToVerifyEmail(toEmail,token));
        assertEquals("Failed to send verification email",exception.getMessage());
        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }
    @Test
    public void testSendMessageToResetPassword(){
        ReflectionTestUtils.setField(emailService,"frontUrl","http://localhost:3000");
        ReflectionTestUtils.setField(emailService,"fromEmail","test@app.com");

        String toEmail="user@mail.com";
        String passwordToken="abc-123-token";
        emailService.sendMessageToResetPassword(toEmail,passwordToken);
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender,times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage=messageCaptor.getValue();
        assertEquals("user@mail.com",sentMessage.getTo()[0]);
        assertEquals("test@app.com",sentMessage.getFrom());
        assertEquals("Talent_isn't_require",sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("abc-123-token"));

    }
    @Test
    public void testSendMessageToResetPasswordException(){
        ReflectionTestUtils.setField(emailService,"frontUrl","http://localhost:3000");
        ReflectionTestUtils.setField(emailService,"fromEmail","test@App.com");

        String toEmail="user@mail.com";
        String token="adv-123-token";

        doThrow(new RuntimeException("SMTP server down")).when(mailSender).send(any(SimpleMailMessage.class));
        EmailSendingException exception=assertThrows(EmailSendingException.class,()->emailService.sendMessageToResetPassword(toEmail,token));
        assertEquals("Failed to send reset password email",exception.getMessage());
        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }

}
