package javaschool.epromo;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j2
@Component
public class WSHandler extends TextWebSocketHandler {


    private final List<WebSocketSession> sessions = new ArrayList<>();

    public WSHandler() {
    }

    public List<WebSocketSession> getSessions() {
        return sessions;
    }

    /**
     * Adds the session to the sessions' list and displays the message from Ecare.
     * @param session: the open session.
     * @throws Exception if any session problems occur.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Session has been started.");

        try (Scanner scanner = new Scanner(new File("msgFromEcare.txt"))) {
            while (scanner.hasNext()) {
                String json = scanner.nextLine();
                log.info("---1. msg got from txt is " + json);
                TextMessage message = new TextMessage(json);
                session.sendMessage(message);
                log.info("---2=1. msg sent to session is " + json);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("---!!! error occurred while msg sending to session");

        }
    }

    /**
     * Removes the session from the session's list.
     * @param session: the open session.
     * @param status: the open session's status.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("Session has been closed.");
    }


}