package com.example.GithubInfoApi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.GithubInfoApi.controllers.GithubInfoRestController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubInfoApiApplicationTests {

    WebTestClient client = WebTestClient.bindToController(new GithubInfoRestController()).build();

    @Test
    public void integrationTest() {
        
    }

}
