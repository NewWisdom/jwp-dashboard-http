package nextstep.jwp.controller;

import nextstep.jwp.exception.InvalidLoingInfoException;
import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.http.session.HttpSession;
import nextstep.jwp.model.User;
import nextstep.jwp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LoginController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    public static final String HTTP = "http://";
    private static LoginController loginController = null;

    private final UserService userService;

    private LoginController(UserService userService) {
        this.userService = userService;
    }

    public static void createInstance(UserService userService) {
        if (Objects.isNull(loginController)) {
            loginController = new LoginController(userService);
        }
    }

    public static LoginController getInstance() {
        return Objects.requireNonNull(loginController);
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        LOG.debug("HTTP GET Login Request: {}", request.getPath());
        HttpSession session = request.getSession();
        if (Objects.nonNull(session.getAttribute("user"))) {
            response.responseRedirect(HTTP + request.getHeader("Host") + "/index.html");
            return;
        }
        response.responseOk("/login.html");
    }

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        LOG.debug("HTTP POST Login Request: {}", request.getPath());
        try {
            User loginUser = userService.login(request);

            HttpSession session = request.getSession();
            session.setAttribute("user", loginUser);
            response.responseRedirect(HTTP + request.getHeader("Host") + "/index.html");
        } catch (InvalidLoingInfoException exception) {
            response.responseRedirect(HTTP + request.getHeader("Host") + "/401.html");
        }
    }
}
