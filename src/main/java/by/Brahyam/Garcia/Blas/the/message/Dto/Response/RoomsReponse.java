package by.Brahyam.Garcia.Blas.the.message.Dto.Response;

import by.Brahyam.Garcia.Blas.the.message.Entity.ChatRooms;
import by.Brahyam.Garcia.Blas.the.message.Entity.User;
import by.Brahyam.Garcia.Blas.the.message.Enums.TypeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomsReponse {
    private Long roomId;
    private String displayTitle; // For groups, use roomName. For private, use the friend's fullName.
    private String displayImageUrl; // For groups, a default icon. For private, the friend's profilePictureUrl.
    private String typeMessage;// "PRIVATE" or "GROUP"
    public static RoomsReponse fromEntity(ChatRooms rooms,User friend) {
        if (rooms.getTypeMessage()== TypeMessage.PRIVATE){
            return new RoomsReponse(
                    rooms.getId(),
                    friend.getFullName(),
                    friend.getProfilePictureUrl(),
                    rooms.getTypeMessage().name()
            );
        }else {
            return new RoomsReponse(
                    rooms.getId(),rooms.getRoomName(),null,
                    rooms.getTypeMessage().name()
            );
        }

    }
}
