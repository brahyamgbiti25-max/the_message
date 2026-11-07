package by.Brahyam.Garcia.Blas.the.message.Controllers;


import by.Brahyam.Garcia.Blas.the.message.Dto.Request.ChangePasswordRequest;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.MessageRoomsResponse;
import by.Brahyam.Garcia.Blas.the.message.Dto.Response.PageResponse;
import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import by.Brahyam.Garcia.Blas.the.message.Service.ChatRoomService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatRoomController.class)
public class ChatRoomControllerTest {
    @MockitoBean
    private ChatRoomService chatRoomService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "user@mail.com")
    public  void  testGetOrCreatePrivatRoom() throws Exception{
        Long roomId=1L;
        Long targetUser=1L;

        when(chatRoomService.gteOrCreatePrivateRoom(eq("user@mail.com"),eq(targetUser)))
                .thenReturn(roomId);
        mockMvc.perform(post("/api/bra/rooms/private/{targetUser}",targetUser)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value(roomId));
    }
    @Test
    @WithMockUser(username = "user@mail.com")
    public void  testGetChatsFromTheUser() throws Exception{
        Long idChatRoom=1L;
        int page=0;
        int size=15;
        MessageRoomsResponse response=new MessageRoomsResponse();
        response.setMessages("bnjcdnkcvjdvj");
        response.setIdChatRoom(idChatRoom);
        response.setId(1L);
        response.setSender("user@mail.com");
        List<MessageRoomsResponse> responses=new ArrayList<>();
        responses.add(response);
        PageResponse<MessageRoomsResponse> response1=new PageResponse<>();
        response1.setContent(responses);
        response1.setSize(size);
        response1.setNumber(0);
        response1.setTotalPages(0);
        response1.setTotalElements(1);
        when(chatRoomService.getMessagesFromTheChatRoom(page,size,"user@mail.com",idChatRoom))
                .thenReturn(response1);
        mockMvc.perform(get("/api/bra/rooms/{isChatRoom}/message",idChatRoom)
                .with(csrf())
                        .param("page","0")
                        .param("size","15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].messages").value("bnjcdnkcvjdvj"))
                .andExpect(jsonPath("$.content[0].sender").value("user@mail.com"))

                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(15));
    }




}
