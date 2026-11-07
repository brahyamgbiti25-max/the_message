package by.Brahyam.Garcia.Blas.the.message.Service;

import by.Brahyam.Garcia.Blas.the.message.Dto.Request.ChatMessageRequest;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;

public interface MessageChatService {
    MessageRoomsResponse saveMessageToDataBase(ChatMessageRequest chatRequest, String senderEmail);
}
