package javaschool.epromo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WSHandler extends TextWebSocketHandler {


    private final List<WebSocketSession> sessions = new ArrayList<>();

    public WSHandler() {
    }

    public List<WebSocketSession> getSessions() {
        return sessions;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

//        System.out.println("Receiver has been started.");
//        ObjectMapper objectMapper = new ObjectMapper();
//        ConnectionFactory factory = new ConnectionFactory();
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//        channel.queueDeclare("pop_tariff", false, false, false, null);
//        channel.basicConsume("pop_tariff", true, new DeliverCallback() {
//            @Override
//            public void handle(String myId, Delivery delivery) throws IOException {
//                String json = new String(delivery.getBody(), "UTF-8");
//                System.out.println("received message: " + json);
////                Message message = objectMapper.readValue(json, Message.class);
//                TextMessage message = new TextMessage(json);
//                session.sendMessage(message);
//            }
//        }, new CancelCallback() {
//            @Override
//            public void handle(String myId) throws IOException {
//            }
//        });

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }


}