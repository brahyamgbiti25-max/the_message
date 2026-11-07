package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Dao.ChatRoomsRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.MessageRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Dto.Request.ChatMessageRequest;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.Message;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.ResourceNotFoundException;
import by.Brahyam.Garcia.Blas.the.message.Service.MessageChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageChatServicelmpl implements MessageChatService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomsRepository chatRoomsRepository;

    @Override
    public MessageRoomsResponse saveMessageToDataBase(ChatMessageRequest chatRequest, String senderEmail) {
        User sender=userRepository.findByEmail(senderEmail).orElseThrow(()->new EmailNotFoundException("Sender not found"));

        ChatRooms room= chatRoomsRepository.findById(chatRequest.getRoomId())
                .orElseThrow(()->new ResourceNotFoundException("Chat room not found"));
        Message message=new Message();
        message.setContent(chatRequest.getContent());
        message.setUsers(sender);
        message.setChatRoom(room);
        message.setSendAt(Instant.now());
        messageRepository.save(message);
        return new MessageRoomsResponse(message.getId(),message.getChatRoom().getId(),message.getContent(),message.getUsers().getEmail());
    }
}
