package javaschool.epromo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class WSHandler extends TextWebSocketHandler {


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Receiver has been started.");
        ObjectMapper objectMapper = new ObjectMapper();
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("pop_tariff", false, false, false, null);
        channel.basicConsume("pop_tariff", true, new DeliverCallback() {
            @Override
            public void handle(String myId, Delivery delivery) throws IOException {
                String json = new String(delivery.getBody(), "UTF-8");
                System.out.println("received message: " + json);
//                Message message = objectMapper.readValue(json, Message.class);
                TextMessage message = new TextMessage(json);
                session.sendMessage(message);
            }
        }, new CancelCallback() {
            @Override
            public void handle(String myId) throws IOException {
            }
        });

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("echo: "+message.getPayload()));
    }
}