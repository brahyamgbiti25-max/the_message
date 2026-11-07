package by.Brahyam.Garcia.Blas.the.message.Dto.Response;

import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private String token;
    private String email;


}
