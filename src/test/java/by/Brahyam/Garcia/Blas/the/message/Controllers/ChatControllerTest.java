package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Request.ChatMessageRequest;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Service.MessageChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTest {
    @Mock
    private  SimpMessagingTemplate messagingTemplate;
    @Mock
    private  MessageChatService messageChatService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private ChatController chatController;
    @Test
    public void testSendMessageSuccessfully(){
        ChatMessageRequest request=new ChatMessageRequest(5L,"hi bro try again");
        String senderEmail="bfahyam@garciablas.com";
        Long roomId=request.getRoomId();
        MessageRoomsResponse savedMessage=new MessageRoomsResponse(1L,5L,"hi bro try again",senderEmail);
        when(authentication.getName()).thenReturn(senderEmail);
        when(messageChatService.saveMessageToDataBase(any(ChatMessageRequest.class),eq(senderEmail))).thenReturn(savedMessage);
        chatController.sendMessage(request,authentication);
        verify(messageChatService).saveMessageToDataBase(any(ChatMessageRequest.class),eq(senderEmail));
        String expectedDestination="/topic/room."+roomId;
        verify(messagingTemplate).convertAndSend(eq(expectedDestination),eq(savedMessage));
        //sorfing

    }


}
