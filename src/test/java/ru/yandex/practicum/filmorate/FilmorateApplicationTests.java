package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class FilmorateApplicationTests {
    final String URI_USERS = "/users";
    final String URI_FILMS = "/films";

    User user = new User("Login"
            , ""
            , "mail@mail.ru"
            , LocalDate.parse("1946-08-20"));

    Film film = new Film("name"
            , "description"
            , LocalDate.parse("1967-03-25")
            , 100);

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

    @Test
    public void filmCreate() {
        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual1 = response.getStatusCodeValue();
        System.out.println(response.getBody());
        String actual2 = response.getBody();
        String expected2 = "{\"id\":1" +
                ",\"name\":\"name\"" +
                ",\"description\":\"description\"" +
                ",\"releaseDate\":\"1967-03-25\"" +
                ",\"duration\":100}";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }

    @Test
    void filmCreateFailName() {
        Film film = new Film(""
                , "Description"
                , LocalDate.parse("1900-03-25")
                , 200);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void filmCreateFailDescription() {
        Film film = new Film("Name"
                , "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят" +
                " разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов," +
                " который за время «своего отсутствия», стал кандидатом Коломбани."
                , LocalDate.parse("1900-03-25")
                , 200);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void filmCreateFailReleaseDate() {
        Film film = new Film("Name"
                , "Description"
                , LocalDate.parse("1890-03-25")
                , 200);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 500);
    }

    @Test
    void filmCreateFailDuration() {
        Film film = new Film("Name"
                , "Description"
                , LocalDate.parse("1900-03-25")
                , -200);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void filmUpdate() {
        ResponseEntity<String> response1 = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        Film updatedFilm = new Film(1
                ,"Film Updated"
                , "New film update description"
                , LocalDate.parse("1989-04-17")
                , 190);

        HttpEntity<Film> httpEntity = new HttpEntity<>(updatedFilm);

        ResponseEntity<String> response2 = this.restTemplate.exchange(URI_FILMS
                , HttpMethod.PUT
                , httpEntity
                , String.class);

        int actual1 = response2.getStatusCodeValue();
        String actual2 = response2.getBody();
        String expected2 = "{\"id\":1" +
                ",\"name\":\"Film Updated\"" +
                ",\"description\":\"New film update description\"" +
                ",\"releaseDate\":\"1989-04-17\"" +
                ",\"duration\":190}";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }

    @Test
    void filmUpdateUnknown() {
        ResponseEntity<String> response1 = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        Film updatedFilm = new Film(-1
                ,"Film Updated"
                , "New film update description"
                , LocalDate.parse("1989-04-17")
                , 190);

        HttpEntity<Film> httpEntity = new HttpEntity<>(updatedFilm);

        ResponseEntity<String> response2 = this.restTemplate.exchange(URI_FILMS
                , HttpMethod.PUT
                , httpEntity
                , String.class);

        int actual1 = response2.getStatusCodeValue();

        assertEquals(actual1, 500);
    }

    @Test
    void filmGetAll() {
        ResponseEntity<String> response1 = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        ResponseEntity<String> response2 = this.restTemplate.getForEntity(URI_FILMS
                , String.class);

        int actual1 = response2.getStatusCodeValue();
        String actual2 = response2.getBody();
        String expected2 = "[{\"id\":1" +
                ",\"name\":\"name\"" +
                ",\"description\":\"description\"" +
                ",\"releaseDate\":\"1967-03-25\"" +
                ",\"duration\":100}]";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }
}