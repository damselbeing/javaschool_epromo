package javaschool.epromo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
@ManagedBean(name = "receiver")
@SessionScoped
public class Receiver {

    private Message message;
    private String hello = "hello";

    public Receiver() {
    }

    public Receiver(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
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
