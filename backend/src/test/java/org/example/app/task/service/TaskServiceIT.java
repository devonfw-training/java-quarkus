package org.example.app.task.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import org.example.app.task.resource.KeycloakTokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * E2E black-box test of the To-Do service only via its public REST resource.
 */
@QuarkusIntegrationTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class TaskServiceIT {

    private String token;

    @BeforeAll
    void getJwt() {
        token = KeycloakTokenProvider.getAccessToken();
    }
}
