package by.Brahyam.Garcia.Blas.the.message.Servicelmpl;

import by.Brahyam.Garcia.Blas.the.message.Dao.ChatRoomsRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.MessageRepository;
import by.Brahyam.Garcia.Blas.the.message.Dao.UserRepository;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.UnifiedSearchReponse;
import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.TypeMessage;
import by.Brahyam.Garcia.Blas.the.message.Exceptions.EmailNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServicelmplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRoomsRepository chatRoomsRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private UserServicelmpl userServicelmpl;

    @Test
    public void testSearchUserAndRoomsSuccessfully(){
        //Arrange
        String query="brahyam";
        String currentUserEmail="brahyam@Iti";
        User mockMemberUser=new User();
        mockMemberUser.setId(2L);
        mockMemberUser.setEmail(query);
        mockMemberUser.setFullName("brahyam@");

        User mockUser=new User();
        mockUser.setId(1L);
        mockUser.setEmail(currentUserEmail);

        ChatRooms roomsMock=new ChatRooms();
        roomsMock.getUsers().add(mockMemberUser);
        roomsMock.setTypeMessage(TypeMessage.PRIVATE);
        mockUser.getChatRooms().add(roomsMock);
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.of(mockUser));
        UnifiedSearchReponse reponse=userServicelmpl.searchUserAndRooms(query,currentUserEmail);
        assertNotNull(reponse);
        assertNotNull(reponse.getExistingChats());
        assertEquals(1,reponse.getExistingChats().size());
    }
    @Test
    public void testSearchUserAndRoomsExceptionEmail(){
        //Arrange
        String query="brahyam";
        String currentUserEmail="brahyam@Iti";
        User mockMemberUser=new User();
        mockMemberUser.setId(2L);
        mockMemberUser.setEmail(query);
        mockMemberUser.setFullName("brahyam@");

        User mockUser=new User();
        mockUser.setId(1L);
        mockUser.setEmail(currentUserEmail);

        ChatRooms roomsMock=new ChatRooms();
        roomsMock.getUsers().add(mockMemberUser);
        mockUser.getChatRooms().add(roomsMock);
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.empty());
        EmailNotFoundException exception=assertThrows(EmailNotFoundException.class,()->userServicelmpl.searchUserAndRooms(query,currentUserEmail));
        assertEquals("this user doesn't exist",exception.getMessage());
    }
    @Test
    public void testSearchUserAndRoomsQueryNewAdd(){
        //Arrange
        String query="brahyam@";
        String currentUserEmail="brahyam@Iti";
        User mockMemberUser=new User();
        mockMemberUser.setId(2L);
        mockMemberUser.setEmail(query);
        mockMemberUser.setFullName("brahyam@");

        User mockUser=new User();
        mockUser.setId(1L);
        mockUser.setEmail(currentUserEmail);

        ChatRooms roomsMock=new ChatRooms();
        mockUser.getChatRooms().add(roomsMock);
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByEmail(query)).thenReturn(Optional.of(mockMemberUser));
        UnifiedSearchReponse reponse=userServicelmpl.searchUserAndRooms(query,currentUserEmail);
        assertNotNull(reponse);
        assertNotNull(reponse.getNewContacts());
        assertEquals(1,reponse.getNewContacts().size());
    }
    @Test
    public void testSearchUserAndRoomsQueryNewAddException(){
        //Arrange
        String query="brahyam@";
        String currentUserEmail="brahyam@Iti";
        User mockMemberUser=new User();
        mockMemberUser.setId(2L);
        mockMemberUser.setEmail("emailunknown");
        mockMemberUser.setFullName("brahyam@");

        User mockUser=new User();
        mockUser.setId(1L);
        mockUser.setEmail(currentUserEmail);

        ChatRooms roomsMock=new ChatRooms();
        mockUser.getChatRooms().add(roomsMock);
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByEmail(query)).thenReturn(Optional.empty());
        UnifiedSearchReponse reponse=userServicelmpl.searchUserAndRooms(query,currentUserEmail);
        assertNotNull(reponse);
        assertNotNull(reponse.getNewContacts());
        assertEquals(0,reponse.getNewContacts().size());
    }
}
