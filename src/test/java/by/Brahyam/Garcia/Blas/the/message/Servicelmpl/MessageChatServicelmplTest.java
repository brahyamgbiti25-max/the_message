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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MessageChatServicelmplTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRoomsRepository chatRoomsRepository;
    @InjectMocks
    private MessageChatServicelmpl servicelmpl;

    @Test
    public void  testSaveMessageToDataBase(){
        ChatMessageRequest request =new ChatMessageRequest();
        request.setContent("Hi bro I'm learning programtion and english at the same time");
        request.setRoomId(1L);
        String senderEmail="brahyam@hcdj";
        User mockUserSender=new User();
        mockUserSender.setEmail(senderEmail);
        ChatRooms roomsMock=new ChatRooms();
        roomsMock.setId(request.getRoomId());
        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(mockUserSender));
        when(chatRoomsRepository.findById(request.getRoomId())).thenReturn(Optional.of(roomsMock));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {Message savedMessage=invocation.getArgument(0);
        savedMessage.setId(10L);
        return  savedMessage;});
        MessageRoomsResponse roomsResponse=servicelmpl.saveMessageToDataBase(request,senderEmail);
        assertNotNull(roomsResponse);
        assertNotNull(request.getContent());
        assertNotNull(mockUserSender);
    }
    @Test
    public void  testSaveMessageToDataBaseExceptionEmail(){
        ChatMessageRequest request =new ChatMessageRequest();
        request.setContent("Hi bro I'm learning programtion and english at the same time");
        request.setRoomId(1L);
        String senderEmail="brahyam@hcdj";
        User mockUserSender=new User();
        mockUserSender.setEmail(senderEmail);
        ChatRooms roomsMock=new ChatRooms();
        roomsMock.setId(request.getRoomId());
        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->servicelmpl.saveMessageToDataBase(request,senderEmail));
        verify(messageRepository,never()).save(any(Message.class));
        assertEquals("Sender not found",exception.getMessage());
    }
    @Test
    public void  testSaveMessageToDataBaseExceptionResource(){
        ChatMessageRequest request =new ChatMessageRequest();
        request.setContent("Hi bro I'm learning programtion and english at the same time");
        request.setRoomId(1L);
        String senderEmail="brahyam@hcdj";
        User mockUserSender=new User();
        mockUserSender.setEmail(senderEmail);
        ChatRooms roomsMock=new ChatRooms();
        roomsMock.setId(request.getRoomId());
        when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(mockUserSender));
        when(chatRoomsRepository.findById(request.getRoomId())).thenReturn(Optional.empty());
        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->servicelmpl.saveMessageToDataBase(request,senderEmail));
        verify(messageRepository,never()).save(any(Message.class));
        assertEquals("Chat room not found",exception.getMessage());
    }


}
