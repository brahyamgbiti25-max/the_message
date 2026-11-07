package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UnifiedSearchReponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bra/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/search")
    public ResponseEntity<UnifiedSearchReponse> search(@RequestParam String query, Authentication authentication){
        String currentUserEmail=authentication.getName();
        UnifiedSearchReponse reponse=userService.searchUserAndRooms(query,currentUserEmail);
        return ResponseEntity.ok(reponse);
    }
    @PutMapping("/profile-picture")
    public  ResponseEntity<MessageResponse> updateProfilePicture(
            @RequestParam String imageUrl,
            Authentication authentication){
        String email=authentication.getName();
       MessageResponse response =userService.updateProfilePicture(email,imageUrl);
        return ResponseEntity.ok(response);

    }
}
