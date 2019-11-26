package org.rainzha.microservices.core.product;

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
class ProductServiceApplicationTests {
    @Test
    void getProductById(@Autowired WebTestClient webClient) {
        int productId = 1;

        webClient.get()
                .uri("/product/" + productId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(productId);
    }

    @Test
    void getProductInvalidParameterString(@Autowired WebTestClient webClient) {
        webClient.get()
                .uri("/product/no-integer")
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/product/no-integer")
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    void getProductNotFound(@Autowired WebTestClient webClient) {
        int productIdNotFound = 13;

        webClient.get()
                .uri("/product/" + productIdNotFound)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/product/" + productIdNotFound)
                .jsonPath("$.message").isEqualTo("No product found for productId: " + productIdNotFound);
    }

    @Test
    void getProductInvalidParameterNegativeValue(@Autowired WebTestClient webClient) {
        int productIdInvalid = -1;

        webClient.get()
                .uri("/product/" + productIdInvalid)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/product/" + productIdInvalid)
                .jsonPath("$.message").isEqualTo("Invalid productId: " + productIdInvalid);
    }
}