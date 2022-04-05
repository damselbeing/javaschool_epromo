package javaschool.epromo;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class App {

    public static void main(String[] args) throws IOException, TimeoutException {

        Receiver receiver = new Receiver();
        receiver.run();
    }
}
