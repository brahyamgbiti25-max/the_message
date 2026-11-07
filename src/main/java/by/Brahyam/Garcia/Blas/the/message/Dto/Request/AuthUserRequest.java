package by.Brahyam.Garcia.Blas.the.message.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserRequest {
    private String email;
    private String fullName;
    private String password;
    private String verificationToken;
}
