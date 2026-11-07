package by.Brahyam.Garcia.Blas.the.message.Dto.Response;

import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReponse {
    private Long id;
    private String fullName;
    private String email;
    private String profilePictureUrl;
    public static UserReponse fromEntity(User user){
        return new UserReponse (user.getId(),
        user.getFullName(),
        user.getEmail(),
        user.getProfilePictureUrl());
    }
}
