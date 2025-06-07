package com.tow.mandu.config.websocket;

import com.tow.mandu.enums.RoleType;
import com.tow.mandu.utils.JwtUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.stomp.port}")
    private int rabbitStompPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configure RabbitMQ as the STOMP broker relay for broadcasting messages
        config.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(rabbitHost)
                .setRelayPort(rabbitStompPort)
                .setClientLogin(rabbitUsername)
                .setClientPasscode(rabbitPassword);


        config.setApplicationDestinationPrefixes("/send");

        config.setUserDestinationPrefix("/private");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*");

        registry.addEndpoint("/websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(25_000);
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String userToken = accessor.getFirstNativeHeader("Authorization");
                    String email = accessor.getFirstNativeHeader("email");
                    RoleType roleType = RoleType.valueOf(accessor.getFirstNativeHeader("roleType"));

                    if (userToken == null || !userToken.startsWith("Bearer ")) {
                        throw new RuntimeException("Access token is missing or invalid");
                    }

                    if (!validateUser(userToken, email, roleType)) {
                        throw new RuntimeException("Invalid access token");
                    }
                }
                return message;
            }
        });
    }

    private boolean validateUser(String token, String email, RoleType roleType) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " (7 characters)
        }
        return jwtUtil.validateToken(token, email, roleType);
    }
}
