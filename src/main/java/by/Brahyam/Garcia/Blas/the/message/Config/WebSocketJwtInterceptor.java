package by.Brahyam.Garcia.Blas.the.message.Config;

import by.Brahyam.Garcia.Blas.the.message.Security.JwtUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class WebSocketJwtInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor= MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
        if(accessor !=null && StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader= accessor.getFirstNativeHeader("Authorization");
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                String token=authHeader.substring(7);
                try{
                    String username= jwtUtil.getUsernameFromToken(token);
                    System.out.println("WebSocket JWT name user: "+username);
                    if(username != null){
                        UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(username,null,null);
                        accessor.setUser(authentication);
                        System.out.println("WebSocket JWT name user: "+username);
                    }
                }catch (Exception e){
                    System.out.println("WebSocket JWT Error: "+e.getMessage());
                }
            }
        }
        return message;
    }
}
