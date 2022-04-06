package javaschool.epromo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@ManagedBean(name = "receiver")
@SessionScoped
public class MQReceiver {

    private Message message;

    public MQReceiver() {
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    public void run() throws IOException, TimeoutException {
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
                Message message = objectMapper.readValue(json, Message.class);
                setMessage(message);
                System.out.println("set message " + getMessage());
            }
        }, new CancelCallback() {
            @Override
            public void handle(String myId) throws IOException {
            }
        });
    }
}
