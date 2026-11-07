package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Dao.ChatRoomsRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.MessageRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.RoomsReponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UnifiedSearchReponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UserReponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicelmpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomsRepository chatRoomsRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Override
    public UnifiedSearchReponse searchUserAndRooms(String query, String currentUserEmail) {
        User currentuser=userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EmailNotFoundException("this user doesn't exist"));
        List<RoomsReponse> existingChats = new ArrayList<>();
        List<User> alreadyChattingWith = new ArrayList<>();
        for(ChatRooms rooms: currentuser.getChatRooms()){
            for(User member: rooms.getUsers()){
                if(!member.getId().equals(currentuser.getId())){
                    boolean matchesName= member.getFullName() != null&& member.getFullName().toLowerCase().contains(query.toLowerCase());
                    boolean matchesEmail= member.getEmail() != null&& member.getEmail().toLowerCase().contains(query.toLowerCase());
                    if(matchesEmail || matchesName){
                        existingChats.add(RoomsReponse.fromEntity(rooms,member));
                        alreadyChattingWith.add(member);
                    }
                }
            }
        }
        List<UserReponse> newContacts = new ArrayList<>();
        if(query.contains("@")){
            Optional<User> databaseMatch=userRepository.findByEmail(query);
            if(databaseMatch.isPresent()){
                User match=databaseMatch.get();
                if (!match.getId().equals(currentuser.getId()) && !alreadyChattingWith.contains(match)) {
                    newContacts.add(UserReponse.fromEntity(match));
                }
            }
        }
        return  new UnifiedSearchReponse(existingChats,newContacts);
        //this code of here doesn't work becuase this search in all database even if you don't have them like friends or add
        /*
        List<User> databaseMatches=userRepository.findByFullNameContainingIgnoreCase(query);
        List<UserReponse> newContacts=new ArrayList<>();

        for(User match:databaseMatches){
            if(!match.getId().equals(currentuser.getId()) && !alreadyChattingWith.contains(match)){
                newContacts.add(UserReponse.fromEntity(match));
            }
        }
        return new UnifiedSearchReponse(existingChats,newContacts);*/
    }

    @Override
    public MessageResponse updateProfilePicture(String email, String imageUrl) {
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new EmailNotFoundException("Email not found"));
        user.setProfilePictureUrl(imageUrl);
        userRepository.save(user);
        return new MessageResponse("profile pcture updated succesfully");
    }
}
