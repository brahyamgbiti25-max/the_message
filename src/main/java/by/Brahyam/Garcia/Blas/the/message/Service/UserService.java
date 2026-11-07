package by.Brahyam.Garcia.Blas.the.message.Service;

import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UnifiedSearchReponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;

public interface UserService {
    UnifiedSearchReponse searchUserAndRooms(String query, String currentUserEmail);

    MessageResponse updateProfilePicture(String email, String imageUrl);
}
