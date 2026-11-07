package by.Brahyam.Garcia.Blas.the.message.Repositories;


import by.Brahyam.Garcia.Blas.the.message.Dao.MessageRepository;
import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.Message;
import by.Brahyam.Garcia.Blas.the.message.Enums.TypeMessage;
import by.Brahyam.Garcia.Blas.the.message.Utils.PaginationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
public class MessageRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private MessageRepository messageRepository;
    @Test
    public void testFindByChatRooms(){
        //arrange

        ChatRooms mockChat=new ChatRooms();
        mockChat.setTypeMessage(TypeMessage.PRIVATE);
        entityManager.persistAndFlush(mockChat);
        Message mockMessage=new Message();
        mockMessage.setContent("xd");
        mockMessage.setChatRoom(mockChat);
        mockChat.getMessages().add(mockMessage);
        entityManager.persistAndFlush(mockMessage);
        //act
        Pageable pageable = PaginationUtils.createPageRequest(10, 5, "sendAt");
        Page<Message> page=messageRepository.findByChatRoom_Id(mockChat.getId(),pageable);
        List<Message> results = messageRepository.findByChatRoom_Id(mockChat.getId(),PageRequest.of(0, 10)).getContent();
        org.assertj.core.api.Assertions.assertThat(results).isNotEmpty();
        Message firstMe=results.getFirst();

    }
}
