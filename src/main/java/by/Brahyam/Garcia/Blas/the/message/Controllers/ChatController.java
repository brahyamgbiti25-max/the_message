package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Request.ChatMessageRequest;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Service.MessageChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {
    //I'm trying to understand message template and I undertood thta this is like
    //the messager becuase he show the same message from that person or everybody
    //in that room or topic in this case we use the room so the data of json body
    //is sent without the another user in that room  ask for it
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageChatService messageChatService;
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest chatRequest, Authentication authentication){
        String senderEmail = authentication.getName();

        // 1. Save and get back the saved MessageRoomsResponse DTO (or Message entity mapped to DTO)
        MessageRoomsResponse savedMessageDto = messageChatService.saveMessageToDataBase(chatRequest, senderEmail);

        String destination = "/topic/room." + chatRequest.getRoomId();

        // 2. Broadcast the COMPLETE DTO (including id, content, and sender email!) over WebSockets
        messagingTemplate.convertAndSend(destination, savedMessageDto);

        /*String senderEmail = authentication.getName();
        messageChatService.saveMessageToDataBase(chatRequest,senderEmail);
        String destination= "/topic/room."+chatRequest.getRoomId();
        messagingTemplate.convertAndSend(destination,chatRequest);*/
    }
    //tomorrow I will come back here to understand this again never lose anymore

}
