package javaschool.epromo;

import com.rabbitmq.client.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;

@Log4j2
@Component
public class MQReceiver {

    @Autowired
    WSHandler myHandler;

    static int contextId = 0;

    @EventListener(ContextRefreshedEvent.class)
    public void run() throws IOException, TimeoutException {
        contextId += 1;
        int copy = contextId;

        log.info("Receiver has been started, contextId is " + copy);

        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("pop_tariff", false, false, false, null);
        channel.basicConsume("pop_tariff", true, new DeliverCallback() {

            @Override
            public void handle(String myId, Delivery delivery) throws IOException {
                log.info(">>>1. sessions amount is " + myHandler.getSessions().size() + ", contextId is " + copy);

                String json = new String(delivery.getBody(), "UTF-8");
                try (PrintWriter out = new PrintWriter("msgFromEcare.txt")) {
                    out.println(json);
                    log.info(">>>2. msg saved to txt is " + json);
                }
                TextMessage msg = new TextMessage(json);
                myHandler.getSessions().forEach(s-> {
                    try {
                        s.sendMessage(msg);
                        log.info(">>>3. msg has been sent to session, contextId is " + copy);
                    } catch (IOException e) {
                        log.error(">>>!!! error occurred while msg sending to session");
                        e.printStackTrace();
                    }
                });
                log.info(">>>4=3=2. msg received from Ecare is " + json);

            }
        }, new CancelCallback() {
            @Override
            public void handle(String myId) throws IOException {
            }
        });
    }



}
