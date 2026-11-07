package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Dao.ChatRoomsRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.MessageRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.PageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.RoomsReponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.Message;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.TypeMessage;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.ResourceNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.RoomNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Service.ChatRoomService;
import by.Brahyam.Garcia.Blas.the.message.Utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomsRepository chatRoomsRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Override
    public Long gteOrCreatePrivateRoom(String currentUserEmail, Long targetUserId) {
        User currenUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EmailNotFoundException("Current user not found"));
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(()->new ResourceNotFoundException("Target user not found"));
        for(ChatRooms room:currenUser.getChatRooms() ){
            if(room.getTypeMessage()== TypeMessage.PRIVATE && room.getUsers().contains(targetUser)){
                return room.getId();
            }
        }
        ChatRooms newRoom=new ChatRooms();
        newRoom.setTypeMessage(TypeMessage.PRIVATE);
        newRoom.getUsers().add(currenUser);
        newRoom.getUsers().add(targetUser);
        ChatRooms savedRoom=chatRoomsRepository.save(newRoom);
        currenUser.getChatRooms().add(savedRoom);
        targetUser.getChatRooms().add(savedRoom);
        userRepository.save(currenUser);
        userRepository.save(targetUser);

        return  savedRoom.getId();
    }

    @Override
    public PageResponse<RoomsReponse> getChatsFromTheUser(int page, int size, String currentUserEmail) {
        Pageable pageable= PaginationUtils.createPageRequest(page,size,"createdAt");
        Page<ChatRooms> roomPage= chatRoomsRepository.findByUsers_Email(currentUserEmail,pageable);
        return PaginationUtils.toPageResponse(roomPage,rooms -> {
            User friend= rooms.getUsers().stream()
                    .filter(u->!u.getEmail().equals(currentUserEmail))
                    .findFirst()
                    .orElse(null);
            return  RoomsReponse.fromEntity(rooms,friend);
        });
    }

    @Override
    public PageResponse<MessageRoomsResponse> getMessagesFromTheChatRoom(int page, int size, String currentUserEmail, Long id) {
        User user=userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EmailNotFoundException("this email doesn't exist"));
       boolean isMember = user.getChatRooms().stream().anyMatch(rooms -> rooms.getId().equals(id));
       if(!isMember){
           throw new RoomNotFoundException("Access denied: You are not a member of this room");
       }
        System.out.println("user click the profile"+currentUserEmail);
        Pageable pageable=PaginationUtils.createPageRequest(page,size,"sendAt");
        Page<Message> messagePage=messageRepository.findByChatRoom_Id(id,pageable);
        return PaginationUtils.toPageResponse(messagePage,MessageRoomsResponse::fromEntity);
    }

}
