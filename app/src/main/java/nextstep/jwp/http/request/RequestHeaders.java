package nextstep.jwp.http.request;

import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaders {

    private final Map<String, String> headers = new HashMap<>();

    public void put(String line) {
        String[] tokens = line.split(": ");
        this.headers.put(tokens[0], tokens[1]);
    }

    public int getContentLength() {
        String contentLength = headers.get("Content-Length");
        if (Strings.isNullOrEmpty(contentLength)) {
            return 0;
        }
        return Integer.parseInt(contentLength.trim());
    }

    public String getHeader(String key) {
        return headers.get(key).trim();
    }
}
