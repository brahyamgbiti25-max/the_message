package by.Brahyam.Garcia.Blas.the.message.Controllers;

import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.PageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.RoomsReponse;
import by.Brahyam.Garcia.Blas.the.message.Service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bra/rooms")
@CrossOrigin(origins = "*")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;
    @PostMapping("/private/{targetUserId}")
    public ResponseEntity<?> getOrCreatePrivateRoom(
            @PathVariable Long targetUserId, Authentication authentication){
        String currentUserEmail= authentication.getName();
        Long roomId= chatRoomService.gteOrCreatePrivateRoom(currentUserEmail, targetUserId);
        return ResponseEntity.ok(Map.of("roomId",roomId));
    }
    @GetMapping("/my-chats")
    public ResponseEntity<PageResponse<RoomsReponse>> getChatsFromTheUser(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "15")int size, Authentication authentication){
        String currentUserEmail= authentication.getName();
        PageResponse<RoomsReponse> response=chatRoomService.getChatsFromTheUser(page,size,currentUserEmail);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{roomId}/message")
    public ResponseEntity<PageResponse<MessageRoomsResponse>> getMessagesFromTheChatRoom(@PathVariable("roomId") Long id,@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "15")int size, Authentication authentication){
        String currentUserEmail= authentication.getName();
        PageResponse<MessageRoomsResponse> response=chatRoomService.getMessagesFromTheChatRoom(page,size,currentUserEmail,id);
        return ResponseEntity.ok(response);
    }

}
