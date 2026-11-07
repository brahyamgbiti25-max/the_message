package by.Brahyam.Garcia.Blas.the.message.Service;

public interface EmailService {

    void sendMessageToVerifyEmail(String email, String verificationToken);

    void sendMessageToResetPassword(String email, String passwordResetToken);
}
