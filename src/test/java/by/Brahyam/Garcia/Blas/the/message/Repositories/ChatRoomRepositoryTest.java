package by.Brahyam.Garcia.Blas.the.message.Repositories;

import by.Brahyam.Garcia.Blas.the.message.Dao.ChatRoomsRepository;
import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.Message;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.TypeMessage;
import by.Brahyam.Garcia.Blas.the.message.Utils.PaginationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

//in this is like the controller of this envirrmoent
//it checks and see verithing of your dta abase mapping everything about
//that so when the test finishes it go back the time using transaccional
//so when the test finishes it's like nothing happens so you can test that again
@DataJpaTest
public class ChatRoomRepositoryTest {
    //ok I undertood that this class Injected over here acts like I cheacker becuase this create
    //a structure of dta abse but just to it knows if you are restpecting the logic or your database
    //so it checks that
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ChatRoomsRepository chatRoomsRepository;

    @Test
    public void testFindByUser_Email() {
        String email = "ncjkccjbdjc";

        // 1. Create and persist User (The Owner)
        User user = new User();
        user.setEmail(email);
        user.setFullName("Brahyam");
        user.setPassword("pass123"); // Make sure to fill non-nullable fields
        entityManager.persistAndFlush(user);

        // 2. Create ChatRoom (The Inverse)
        ChatRooms mockChate = new ChatRooms();
        mockChate.setTypeMessage(TypeMessage.PRIVATE);

        // 3. Link BOTH sides of the bidirectional relationship
        mockChate.getUsers().add(user);
        user.getChatRooms().add(mockChate); // CRITICAL: User is the owner, so it must hold the reference!

        entityManager.persistAndFlush(mockChate);

        // 4. Act
        Pageable pageable = PaginationUtils.createPageRequest(10, 5, "createdAt");
        Page<ChatRooms> result = chatRoomsRepository.findByUsers_Email(email, pageable);

        // 5. Assert
        // Fetch the result
        List<ChatRooms> results = chatRoomsRepository.findByUsers_Email(user.getEmail(), PageRequest.of(0, 10)).getContent();

// Check that it actually found something FIRST
        org.assertj.core.api.Assertions.assertThat(results).isNotEmpty();

// Now it is safe to get the first element
        ChatRooms firstRoom = results.getFirst();
    }
}
