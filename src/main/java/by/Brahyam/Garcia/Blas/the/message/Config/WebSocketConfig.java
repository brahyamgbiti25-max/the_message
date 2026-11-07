package by.Brahyam.Garcia.Blas.the.message.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
// this class is especific to configure websockets
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private  WebSocketJwtInterceptor webSocketJwtInterceptor;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. Enable a simple in-memory broker for destinations prefixed with "/topic" (for broadcasting to rooms)
        registry.enableSimpleBroker("/topic");
        // 2. Set the prefix for messages bound for @MessageMapping methods in controllers
        registry.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:*", "http://10.0.2.2:*") // Safe pattern matching without "*"
                .withSockJS();// Fallback option for browsers that don't support native WebSockets
    }
    //this like a security gate

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketJwtInterceptor);
    }
}
