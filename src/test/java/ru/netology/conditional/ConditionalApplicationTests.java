package ru.netology.conditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalApplicationTests {
    private final String HOST = "http://localhost:";
    @Autowired
    private TestRestTemplate restTemplate;
    @Container
    private static final GenericContainer<?> devApp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);
    @Container
    private static final GenericContainer<?> prodApp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);
    @BeforeAll
    public static void setUp() {
        devApp.start();
        prodApp.start();
    }

    @Test
    public void firstContextLoad() {
        var port = devApp.getMappedPort(8080);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(HOST + port + "/profile", String.class);
        System.out.println("Port: " + port);
        System.out.println(forEntity.getBody());
        String expected = "Current profile is dev\n";
        String actual = forEntity.getBody();

        Assertions.assertEquals(expected, actual, "Dev");
    }

    @Test
    public void secondContextLoad() {
        var port = prodApp.getMappedPort(8081);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(HOST + port + "/profile", String.class);
        System.out.println("Port: " + port);
        System.out.println(forEntity.getBody());
        String expected = "Current profile is production\n";
        String actual = forEntity.getBody();

        Assertions.assertEquals(expected, actual, "Production");
    }


}
