package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTests {
    final String URI_FILMS = "/films";

    Film film = new Film("name"
            , "description"
            , LocalDate.parse("1967-03-25")
            , 100L
            , null);
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void filmCreate() {
        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual1 = response.getStatusCodeValue();
        String actual2 = response.getBody();
        String expected2 = "{\"id\":1" +
                ",\"name\":\"name\"" +
                ",\"description\":\"description\"" +
                ",\"releaseDate\":\"1967-03-25\"" +
                ",\"duration\":100" +
                ",\"likes\":null}";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }

    @Test
    void filmCreateFailName() {
        Film film = new Film(""
                , "Description"
                , LocalDate.parse("1900-03-25")
                , 200L
                , null);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 500);
    }

    @Test
    void filmCreateFailDescription() {
        Film film = new Film("Name"
                , "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят" +
                " разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов," +
                " который за время «своего отсутствия», стал кандидатом Коломбани."
                , LocalDate.parse("1900-03-25")
                , 200L
                , null);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 500);
    }

    @Test
    void filmCreateFailReleaseDate() {
        Film film = new Film("Name"
                , "Description"
                , LocalDate.parse("1890-03-25")
                , 200L
                , null);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 400);
    }

    @Test
    void filmCreateFailDuration() {
        Film film = new Film("Name"
                , "Description"
                , LocalDate.parse("1900-03-25")
                , -200L
                , null);

        ResponseEntity<String> response = this.restTemplate.postForEntity(URI_FILMS
                , film
                , String.class);

        int actual = response.getStatusCodeValue();

        assertEquals(actual, 500);
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
                , 190L
                , null);

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
                ",\"duration\":190" +
                ",\"likes\":null}";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }

    @Test
    void filmUpdateUnknown() {
        Film updatedFilm = new Film(-1
                ,"Film Updated"
                , "New film update description"
                , LocalDate.parse("1989-04-17")
                , 190L
                , null);

        HttpEntity<Film> httpEntity = new HttpEntity<>(updatedFilm);

        ResponseEntity<String> response2 = this.restTemplate.exchange(URI_FILMS
                , HttpMethod.PUT
                , httpEntity
                , String.class);

        int actual1 = response2.getStatusCodeValue();

        assertEquals(actual1, 404);
    }

    @Test
    void filmGetAll() {
        ResponseEntity<String> response2 = this.restTemplate.getForEntity(URI_FILMS
                , String.class);

        int actual1 = response2.getStatusCodeValue();
        String actual2 = response2.getBody();
        String expected2 = "[{\"id\":1" +
                ",\"name\":\"name\"" +
                ",\"description\":\"description\"" +
                ",\"releaseDate\":\"1967-03-25\"" +
                ",\"duration\":100" +
                ",\"likes\":null}]";

        assertEquals(actual1, 200);
        assertEquals(actual2, expected2);
    }
}