package javaschool.epromo;

import com.rabbitmq.client.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
@Log4j2
public class MQReceiver {

    @Autowired
    WSHandler myHandler;

    static int ctxIdx = 0;

//    @EventListener
//    public void handleEvent(ContextRefreshedEvent historyEvent) {
//        log.info(">>>>>>>>            instanceOf: " + historyEvent.getClass().getName());
//    }

    @EventListener(ContextRefreshedEvent.class)
    public void run() throws IOException, TimeoutException {
        ctxIdx += 1;
        int copy = ctxIdx;
        System.out.println("Receiver has been started." + copy);
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("pop_tariff", false, false, false, null);
        channel.basicConsume("pop_tariff", true, new DeliverCallback() {
            @Override
            public void handle(String myId, Delivery delivery) throws IOException {
                String json = new String(delivery.getBody(), "UTF-8");
                TextMessage msg = new TextMessage(json);
                System.out.println("AMOUNT" + myHandler.getSessions().size() + " " + copy);
                myHandler.getSessions().forEach(s-> {
                    try {
                        s.sendMessage(msg);
                        System.out.println("YES!!!!!! " + copy);
                    } catch (IOException e) {
                        System.out.println("MURK!!!!!!");
                        e.printStackTrace();
                    }
                });
                System.out.println("received message: " + json);

            }
        }, new CancelCallback() {
            @Override
            public void handle(String myId) throws IOException {
            }
        });
    }



}
