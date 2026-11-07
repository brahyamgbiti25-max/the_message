package by.Brahyam.Garcia.Blas.the.message.Service;

import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.PageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.RoomsReponse;

public interface ChatRoomService {
    Long gteOrCreatePrivateRoom(String currentUserEmail, Long targetUserId);



    PageResponse<RoomsReponse> getChatsFromTheUser(int page, int size, String currentUserEmail);

    PageResponse<MessageRoomsResponse> getMessagesFromTheChatRoom(int page, int size, String currentUserEmail, Long id);
}
