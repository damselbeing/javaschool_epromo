package javaschool.epromo;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Log4j2
@Component
public class SocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        String clientMessage = message.getPayload();

        if (clientMessage.startsWith("hello") || clientMessage.startsWith("greet")) {
            session.sendMessage(new TextMessage("Hello there!"));
        } else if (clientMessage.startsWith("time")) {
            session.sendMessage(new TextMessage("Bye"));
        } else {

            session.sendMessage(new TextMessage("Unknown command"));
        }
    }
}