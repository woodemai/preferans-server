package com.ru.preferans.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.ru.preferans.errors.SocketExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SocketIOConfig {

    private final SocketExceptionHandler exceptionHandler;

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setExceptionListener(exceptionHandler);

        return new SocketIOServer(config);
    }
}
