package javaschool.epromo;

import com.rabbitmq.client.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Log4j2
public class Receiver {

    public static void main(String[] args) throws IOException, TimeoutException {
        log.info("Receiver has been started.");
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("pop_tariff", false, false, false, null);

        channel.basicConsume("pop_tariff", true, new DeliverCallback() {
            @Override
            public void handle(String myId, Delivery delivery) throws IOException {
                String json = new String(delivery.getBody(), "UTF-8");
                log.info("received message: " + json);
            }
        }, new CancelCallback() {
            @Override
            public void handle(String myId) throws IOException {
            }
        });
    }
}
