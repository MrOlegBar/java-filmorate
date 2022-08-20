package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTests {
    final String URI_USERS = "/users";

    User user = new User("Login"
            , ""
            , "mail@mail.ru"
            , LocalDate.parse("1946-08-20"));

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void userCreateWithEmptyName() {
        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        int actual1 = response.getStatusCodeValue();
        String actual2 = response.getBody();
        String expected2 = "{\"id\":1" +
                ",\"login\":\"Login\"" +
                ",\"name\":\"Login\"" +
                ",\"email\":\"mail@mail.ru\"" +
                ",\"birthday\":\"1946-08-20\"}";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }

    @Test
    void userCreateFailEmail() {
        User user = new User("Login"
                , "Name"
                , "mail.ru"
                , LocalDate.parse("1980-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void userCreateFailLogin() {
        User user = new User("Login Fail"
                , "Name"
                , "mail@mail.ru"
                , LocalDate.parse("1980-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void userCreateFailBirthday() {
        User user = new User(
                "Login"
                , "Name"
                , "mail@mail.ru"
                , LocalDate.parse("2446-08-20"));

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void userUpdate() {
        ResponseEntity<String> response1 = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        User updatedUser = new User(1
                ,"Login"
                , "Nick Name"
                , "mail@mail.ru"
                , LocalDate.parse("1946-08-20"));

        HttpEntity<User> httpEntity = new HttpEntity<>(updatedUser);

        ResponseEntity<String> response2 = this.restTemplate.exchange(URI_USERS
                , HttpMethod.PUT
                , httpEntity
                , String.class);

        int actual1 = response2.getStatusCodeValue();

        String actual2 = response2.getBody();
        String expected2 = "{\"id\":1" +
                ",\"login\":\"Login\"" +
                ",\"name\":\"Nick Name\"" +
                ",\"email\":\"mail@mail.ru\"" +
                ",\"birthday\":\"1946-08-20\"}";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }

    @Test
    void userUpdateUnknown() {
        ResponseEntity<String> response1 = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        User updatedUser = new User(-1
                ,"Login"
                , "Nick Name"
                , "mail@mail.ru"
                , LocalDate.parse("1946-08-20"));

        HttpEntity<User> httpEntity = new HttpEntity<>(updatedUser);

        ResponseEntity<String> response2 = this.restTemplate.exchange(URI_USERS
                , HttpMethod.PUT
                , httpEntity
                , String.class);

        int actual = response2.getStatusCodeValue();

        assertEquals(actual, 500);
    }

    @Test
    void userGetAll() {
        ResponseEntity<String> response1 = this.restTemplate.postForEntity(URI_USERS
                , user
                , String.class);

        ResponseEntity<String> response2 = this.restTemplate.getForEntity(URI_USERS
                , String.class);

        int actual1 = response2.getStatusCodeValue();
        String actual2 = response2.getBody();
        String expected2 = "[{\"id\":1" +
                ",\"login\":\"Login\"" +
                ",\"name\":\"Login\"" +
                ",\"email\":\"mail@mail.ru\"" +
                ",\"birthday\":\"1946-08-20\"}]";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }
}