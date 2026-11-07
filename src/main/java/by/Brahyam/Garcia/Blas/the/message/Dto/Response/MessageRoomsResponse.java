package by.Brahyam.Garcia.Blas.the.message.Dto.Response;

import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRoomsResponse {

    private Long id;
    private Long idChatRoom;
    private String messages;
    private String sender;
    public static MessageRoomsResponse fromEntity(Message message) {
        System.out.println("user"+message.getUsers().getEmail());
        return new MessageRoomsResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getContent(),
                message.getUsers().getEmail()
        );
    }


}
