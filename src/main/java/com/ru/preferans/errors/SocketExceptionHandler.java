package com.ru.preferans.errors;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class SocketExceptionHandler extends ExceptionListenerAdapter {

    @Override
    public void onEventException(Exception e, List<Object> args, SocketIOClient client) {
        log.error("Socket error: {}", e.getLocalizedMessage());
    }

    @Override
    public void onDisconnectException(Exception e, SocketIOClient client) {
        log.error("Socket disconnect error: {}", e.getLocalizedMessage());

    }

    @Override
    public void onConnectException(Exception e, SocketIOClient client) {
        log.error("Socket connect error: {}", e.getLocalizedMessage());

    }
}
