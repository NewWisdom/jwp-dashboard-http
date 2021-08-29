package nextstep.jwp;

import nextstep.jwp.controller.*;
import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FrontController implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FrontController.class);

    private final Socket connection;
    private final Map<String, Controller> controllerMap = new HashMap<>();

    public FrontController(Socket connection) {
        this.connection = Objects.requireNonNull(connection);
        controllerMap.put("/", new IndexController());
        controllerMap.put("/login", new LoginController(new UserService()));
        controllerMap.put("/register", new RegisterController(new UserService()));
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (final InputStream inputStream = connection.getInputStream();
             final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             final OutputStream outputStream = connection.getOutputStream()) {

            HttpRequest request = new HttpRequest(reader);
            HttpResponse response = new HttpResponse();

            String uri = request.getPath();
            Controller controller = controllerMap.getOrDefault(uri, new DefaultController());
            controller.process(request, response);

            response.write(outputStream);
        } catch (IOException exception) {
            log.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            log.error("Exception closing socket", exception);
        }
    }
}
