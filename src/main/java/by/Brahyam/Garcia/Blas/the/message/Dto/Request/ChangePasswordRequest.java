package by.Brahyam.Garcia.Blas.the.message.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String newPassword;
    private String currentPassword;
}
