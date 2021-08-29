package nextstep.jwp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerTest extends ControllerTest {

    @DisplayName("GET /login 요청 시 login.html를 응답한다.")
    @Test
    void doGet() throws IOException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        sendRequest(httpRequest);

        // then
        final URL resource = getClass().getClassLoader().getResource("static/login.html");
        String expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 3797 \r\n" +
                "\r\n" +
                new String(Files.readAllBytes(new File(resource.getFile()).toPath()));
        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("POST /login 요청 성공시 index.html로 리다이렉트한다.")
    @Test
    void doPost() {
        // given
        String requestBody = "account=gugu&password=password";
        final String httpRequest = String.join("\r\n",
                "POST /login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: " + requestBody.getBytes().length + " ",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                requestBody);

        sendRequest(httpRequest);

        // then
        final URL resource = getClass().getClassLoader().getResource("static/login.html");
        String expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/index.html \r\n" ;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("POST /login 요청 실패시 401.html로 리다이렉트한다. - 존재하지 않는 계정")
    @Test
    void doPostFail() {
        // given
        String requestBody = "account=amazzi&password=password";
        final String httpRequest = String.join("\r\n",
                "POST /login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: " + requestBody.getBytes().length + " ",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                requestBody);

        // when
        sendRequest(httpRequest);

        // then
        String expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/401.html \r\n" ;

        assertThat(socket.output()).isEqualTo(expected);
    }

    @DisplayName("POST /login 요청 실패시 401.html로 리다이렉트한다. - 틀린 비밀번호")
    @Test
    void doPostFailWrong() {
        // given
        String requestBody = "account=amazzi&password=wrongpassword";
        final String httpRequest = String.join("\r\n",
                "POST /login HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "Content-Length: " + requestBody.getBytes().length + " ",
                "Content-Type: application/x-www-form-urlencoded ",
                "Accept: */* ",
                "",
                requestBody);

        // when
        sendRequest(httpRequest);

        // then
        String expected = "HTTP/1.1 302 Found \r\n" +
                "Location: http://localhost:8080/401.html \r\n" ;

        assertThat(socket.output()).isEqualTo(expected);
    }
}