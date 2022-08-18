package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTest {

    @LocalServerPort
    private int port = 8080;

    @Autowired
    private UserController userController;

    @Autowired
    private FilmController filmController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void userCreate() {
        User user = new User("Login"
                , "Nick Name"
                , "mail@mail.ru"
                , LocalDate.parse("1946-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/users"
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 200);
    }

    /*@Test
    void userCreateFailEmail() {
        User user = new User("Login"
                , ""
                , "mail.ru"
                , LocalDate.parse("1980-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/users"
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void userCreateFailLogin() {
        User user = new User("Login Fail"
                , ""
                , "mail@mail.ru"
                , LocalDate.parse("1980-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/users"
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void userCreateFailBirthday() {
        User user = new User("Login"
                , ""
                , "mail@mail.ru"
                , LocalDate.parse("2446-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:" + port + "/users"
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void userUpdate() {
        userCreate();
        User user = new User(1
                ,"Login"
                , "Nick Name"
                , "mail@mail.ru"
                , LocalDate.parse("1946-08-20"));

        ResponseEntity<String> response = this.restTemplate.exchange("http://localhost:" + port + "/users"
                , HttpMethod.PUT
                , new HttpEntity<>(user)
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 200);
    }*/
}