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
import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRoomsRepository chatRoomsRepository;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatRoomServiceImpl roomService;
    @Mock
    JwtUtil jwtUtil;
    @Test
    public void testGetOrCreatePrivateRoomSuccessfully(){
        String currentEmailMock="bjsdbfjvbfvhjb";
        Long mockUserTargetId= 2L;
        User mockUser=new User();
        mockUser.setEmail(currentEmailMock);
        User mockUserTarget=new User();
        mockUserTarget.setId(mockUserTargetId);
        when(userRepository.findByEmail(currentEmailMock)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUserTargetId)).thenReturn(Optional.of(mockUserTarget));
        ChatRooms savedRoom=new ChatRooms();
        savedRoom.setId(1L);
        savedRoom.setTypeMessage(TypeMessage.PRIVATE);
        when(chatRoomsRepository.save(any(ChatRooms.class))).thenReturn(savedRoom);
        Long roomId=roomService.gteOrCreatePrivateRoom(currentEmailMock,mockUserTargetId);
        assertNotNull(roomId);
        assertEquals(1L,roomId);
        assertTrue(mockUser.getChatRooms().contains(savedRoom));
        assertTrue(mockUserTarget.getChatRooms().contains(savedRoom));
        verify(userRepository,times(1)).save(mockUser);
        verify(userRepository,times(1)).save(mockUserTarget);
        verify(chatRoomsRepository,times(1)).save(any(ChatRooms.class));
        //my logic is wrong here I don0t know very well why I will try to understand that
        /*
        newRoom.getUsers().add(mockUser);
        newRoom.getUsers().add(mockUserTarget);
        ChatRooms savedRoom=newRoom;
        when(chatRoomsRepository.save(newRoom)).thenReturn(savedRoom);
        mockUser.getChatRooms().add(savedRoom);
        mockUserTarget.getChatRooms().add(savedRoom);
        roomService.gteOrCreatePrivateRoom(currentEmailMock,mockUserTargetId);
        assertNotNull(newRoom.getTypeMessage());
        assertNotNull(newRoom.getUsers());
        assertNotNull(mockUser.getChatRooms());
        assertNotNull(mockUserTarget.getChatRooms());
        verify(userRepository, times(2)).save(ArgumentMatchers.any(User.class));*/

    }
    @Test
    public void testGetOrCreatePrivateRoomExceptionEmail(){
        String currentEmailMock="bjsdbfjvbfvhjb";
        Long mockUserTargetId= 2L;
        User mockUser=new User();
        mockUser.setEmail(currentEmailMock);
        User mockUserTarget=new User();
        mockUserTarget.setId(mockUserTargetId);
        when(userRepository.findByEmail(currentEmailMock)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->roomService.gteOrCreatePrivateRoom(currentEmailMock,mockUserTargetId));
        assertEquals("Current user not found", exception.getMessage());
        verify(userRepository,never()).save(mockUser);
        verify(chatRoomsRepository,never()).save(any(ChatRooms.class));

    }
    @Test
    public void testGetOrCreatePrivateRoomExceptionTargetUserNotFound(){
        String currentEmailMock="bjsdbfjvbfvhjb";
        Long mockUserTargetId= 2L;
        User mockUser=new User();
        mockUser.setEmail(currentEmailMock);
        User mockUserTarget=new User();
        mockUserTarget.setId(mockUserTargetId);
        when(userRepository.findByEmail(currentEmailMock)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUserTargetId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception=assertThrows(ResourceNotFoundException.class,()->roomService.gteOrCreatePrivateRoom(currentEmailMock,mockUserTargetId));
        assertEquals("Target user not found", exception.getMessage());
        verify(userRepository,never()).save(mockUser);
        verify(userRepository,never()).save(mockUserTarget);
        verify(chatRoomsRepository,never()).save(any(ChatRooms.class));
    }
    @Test
    public void testGetOrCreatePrivateRoomFoundOne(){
        String currentEmailMock="bjsdbfjvbfvhjb";
        Long mockUserTargetId= 2L;
        User mockUser=new User();
        mockUser.setEmail(currentEmailMock);
        User mockUserTarget=new User();
        mockUserTarget.setId(mockUserTargetId);
        ChatRooms roomFound=new ChatRooms();
        roomFound.setId(1L);
        roomFound.setTypeMessage(TypeMessage.PRIVATE);
        roomFound.getUsers().add(mockUserTarget);
        mockUser.getChatRooms().add(roomFound);
        when(userRepository.findByEmail(currentEmailMock)).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUserTargetId)).thenReturn(Optional.of(mockUserTarget));
        Long roomId=roomService.gteOrCreatePrivateRoom(currentEmailMock,mockUserTargetId);
        assertNotNull(roomId);
        assertEquals(1L,roomId);
        verify(userRepository,never()).save(any(User.class));
        verify(chatRoomsRepository,never()).save(any(ChatRooms.class));
    }
    @Test
    public void testGetChatsFromTheUser(){
        int page=0;
        int size=10;
        String email="brahyam@garciablas.com";
        User me=new User();
        me.setEmail(email);
        User friend=new User();
        friend.setEmail("friend@mail.com");
        friend.setFullName("myfriedn");
        ChatRooms room=new ChatRooms();
        room.setId(1L);
        room.getUsers().add(me);
        room.getUsers().add(friend);
        room.setTypeMessage(TypeMessage.PRIVATE);
        Page<ChatRooms> mockPage=new PageImpl<>(List.of(room));
        when(chatRoomsRepository.findByUsers_Email(eq(email),any(Pageable.class)))
                .thenReturn(mockPage);
        PageResponse<RoomsReponse> response=roomService.getChatsFromTheUser(page,size,email);
        assertNotNull(response);
        assertEquals(1,response.getContent().size());
        assertEquals("myfriedn",response.getContent().getFirst().getDisplayTitle());
    }
    @Test
    public void testGetMessagesFromChatRoom(){
        int page=0;
        int size=10;
        String email="brahyam@garciablas.com";
        Long idChatRoomMock=1L;
        ChatRooms chatRoomsMock=new ChatRooms();
        chatRoomsMock.setId(idChatRoomMock);
        User currentUserMock=new User();
        currentUserMock.setEmail(email);
        currentUserMock.getChatRooms().add(chatRoomsMock);
        Message messageMock=new Message();
        messageMock.setUsers(currentUserMock);
        messageMock.setChatRoom(chatRoomsMock);
        Page<Message> mockPage=new PageImpl<>(List.of(messageMock));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUserMock));
        when(messageRepository.findByChatRoom_Id(eq(idChatRoomMock),any(Pageable.class))).thenReturn(mockPage);
        PageResponse<MessageRoomsResponse> response=roomService.getMessagesFromTheChatRoom(page,size,email,idChatRoomMock);
        assertNotNull(response);
        assertEquals(1,response.getContent().size());
        assertEquals(1L,response.getContent().getFirst().getIdChatRoom());
    }
    @Test
    public void testGetMessagesFromChatRoomEmailException(){
        int page=0;
        int size=10;
        String email="brahyam@garciablas.com";
        Long idChatRoomMock=1L;
        User currentUserMock=new User();
        currentUserMock.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->roomService.getMessagesFromTheChatRoom(page,size,email,idChatRoomMock));
        assertEquals("this email doesn't exist",exception.getMessage());
       // Correct Mockito syntax for 'never'
        verify(messageRepository, never()).findByChatRoom_Id(eq(idChatRoomMock), any(Pageable.class));
    }
    @Test
    public void testGetMessagesFromChatRoomRoomNotFound(){
        int page=0;
        int size=10;
        String email="brahyam@garciablas.com";
        Long idChatRoomMock=1L;
        ChatRooms chatRoomsMock=new ChatRooms();
        chatRoomsMock.setId(idChatRoomMock);
        ChatRooms chatRoomsMockFalseIf=new ChatRooms();
        chatRoomsMockFalseIf.setId(2L);
        User currentUserMock=new User();
        currentUserMock.setEmail(email);
        currentUserMock.getChatRooms().add(chatRoomsMockFalseIf);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUserMock));
        RoomNotFoundException exception=assertThrows(RoomNotFoundException.class,()->roomService.getMessagesFromTheChatRoom(page,size,email,idChatRoomMock));
        assertEquals("Access denied: You are not a member of this room",exception.getMessage());
      // Correct Mockito syntax for 'never'
        verify(messageRepository, never()).findByChatRoom_Id(eq(idChatRoomMock), any(Pageable.class));
    }
}
