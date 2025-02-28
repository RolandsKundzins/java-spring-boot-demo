package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/users/email/";

        // Clear existing users and add test data
        userRepository.deleteAll();
        userRepository.save(new User(null, "John Doe", "john@example.com"));
    }

    @Test
    void testGetUserByEmail_UserExists() {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "john@example.com", User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        try {
            restTemplate.getForEntity(baseUrl + "notfound@example.com", String.class);
            fail("Expected HttpClientErrorException but request succeeded");
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode()); // ✅ Check that it's a 404
            assertNotNull(e.getResponseBodyAsString()); // ✅ Ensure response body isn't null
            assertTrue(e.getResponseBodyAsString().contains("User not found")); // ✅ Verify response message
        }
    }

}
