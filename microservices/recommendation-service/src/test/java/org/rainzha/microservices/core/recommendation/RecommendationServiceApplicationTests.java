package org.rainzha.microservices.core.recommendation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
class RecommendationServiceApplicationTests {
    @Test
    void getRecommendationsByProductId(@Autowired WebTestClient webClient) {
        int productId = 1;

        webClient.get()
                .uri("/recommendation?productId=" + productId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].productId").isEqualTo(productId);
    }

    @Test
    void getRecommendationsMissingParameter(@Autowired WebTestClient webClient) {
        webClient.get()
                .uri("/recommendation")
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message").isEqualTo("Required int parameter 'productId' is not present");
    }

    @Test
    void getRecommendationsInvalidParameter(@Autowired WebTestClient webClient) {
        webClient.get()
                .uri("/recommendation?productId=no-integer")
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    void getRecommendationsNotFound(@Autowired WebTestClient webClient) {
        int productIdNotFound = 113;

        webClient.get()
                .uri("/recommendation?productId=" + productIdNotFound)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(0);
    }

    @Test
    void getRecommendationsInvalidParameterNegativeValue(@Autowired WebTestClient webClient) {
        int productIdInvalid = -1;

        webClient.get()
                .uri("/recommendation?productId=" + productIdInvalid)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/recommendation")
                .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }
}