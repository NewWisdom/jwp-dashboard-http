package nextstep.jwp.http.session;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    public static final String SESSION_NAME = "JSESSIONID";

    private final String id;
    private final Map<String, Object> values = new HashMap<>();

    HttpSession(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAttribute(String name, Object value) {
    }

    public Object getAttribute(String name) {
        return this.values.get(name);
    }

    public void removeAttribute(String name) {
    }

    public void invalidate() {
    }
}


