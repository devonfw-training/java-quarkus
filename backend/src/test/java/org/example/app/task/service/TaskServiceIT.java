
package org.example.app.task.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import io.quarkus.test.junit.TestProfile;
import org.example.app.task.resource.KeycloakTokenProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * E2E black-box test of the To-Do service only via its public REST resource.
 */
@QuarkusIntegrationTest
@TestMethodOrder(OrderAnnotation.class)
@TestProfile(IntegrationTestProfile.class)
@TestInstance(Lifecycle.PER_CLASS)
class TaskServiceIT {

    private Integer taskListId;

    private Integer taskItemId;

    /** Access token of a user that is allowed to do everything (user "manager" of the Keycloak test realm). */
    private String token;

    @BeforeAll
    void fetchAccessToken() {
        this.token = KeycloakTokenProvider.getAccessTokenOfManager();
    }

    @Test
    @Order(1)
    void shouldAllowCreatingANewTaskList() {

        Response response = given().auth().oauth2(this.token).when()
                .body("{ \"title\": \"Shopping List\" }").contentType(ContentType.JSON)
                .post("/api/task/list");
        response.then().statusCode(201).header("Location", not(emptyString()));

        this.taskListId = Integer.parseInt(response.header("Location").replaceAll(".*?/task/list/", ""));
    }

    @Test
    @Order(2)
    void shouldAllowAddingATaskToATaskList() {

        Response response = given().auth().oauth2(this.token).when().body("{ \"title\": \"Buy Milk\", \"taskListId\": " + this.taskListId + " }")
                .contentType(ContentType.JSON).post("/api/task/item");

        response.then().statusCode(201).header("Location", not(emptyString()));

        this.taskItemId = Integer.parseInt(response.header("Location").replaceAll(".*?/task/item/", ""));
    }

    @Test
    @Order(3)
    void shouldAllowRetrievingATaskListWithTaskItems() {

        given().auth().oauth2(this.token).when().get("/api/task/list-with-items/{taskListId}", this.taskListId).then().statusCode(200)
                .body("list.title", Matchers.equalTo("Shopping List")).and().body("list.id",
                        Matchers.equalTo(this.taskListId))
                .and().body("items[0].title", Matchers.equalTo("Buy Milk"));
    }

    @Test
    @Order(4)
    void shouldAllowDeletingATaskListCompletely() {

        given().auth().oauth2(this.token).when().delete("/api/task/list/{taskListId}", this.taskListId).then().statusCode(204);
        given().auth().oauth2(this.token).when().get("/api/task/list/{taskListId}", this.taskListId).then().statusCode(404);
        given().auth().oauth2(this.token).when().get("/api/task/item/{itemId}", this.taskItemId).then().statusCode(404);

    }

    @Test
    @Order(5)
    void shouldAllowReaderToFindTaskLists() {

        given().auth().oauth2(KeycloakTokenProvider.getAccessTokenOfReader()).when().get("/api/task/lists").then()
                .statusCode(200);
    }

    @Test
    @Order(6)
    void shouldForbidReaderToCreateTaskList() {

        given().auth().oauth2(KeycloakTokenProvider.getAccessTokenOfReader()).when()
                .body("{ \"title\": \"Not allowed\" }").contentType(ContentType.JSON).post("/api/task/list").then()
                .statusCode(403);
    }

    @Test
    @Order(7)
    void shouldForbidWriterToDeleteTaskListButAllowManager() {

        String writerToken = KeycloakTokenProvider.getAccessTokenOfWriter();
        Response response = given().auth().oauth2(writerToken).when().body("{ \"title\": \"Writers list\" }")
                .contentType(ContentType.JSON).post("/api/task/list");
        response.then().statusCode(201);
        Integer id = Integer.parseInt(response.header("Location").replaceAll(".*?/task/list/", ""));

        given().auth().oauth2(writerToken).when().delete("/api/task/list/{id}", id).then().statusCode(403);
        given().auth().oauth2(this.token).when().delete("/api/task/list/{id}", id).then().statusCode(204);
    }

    @Test
    @Order(8)
    void shouldRejectRequestWithoutToken() {

        given().when().get("/api/task/lists").then().statusCode(401);
    }

    @Test
    @Order(9)
    void shouldRejectRequestWithInvalidToken() {

        given().auth().oauth2("not-a-valid-token").when().get("/api/task/lists").then().statusCode(401);
    }

    @Test
    @Order(10)
    void shouldProvideLogoutUrlOfKeycloakForAuthenticatedUser() {

        given().auth().oauth2(this.token).when().body("{ \"idToken\": \"dummy-id-token\" }")
                .contentType(ContentType.JSON).post("/api/auth/logout").then().statusCode(200)
                .body("logoutUrl", Matchers.containsString("/protocol/openid-connect/logout"))
                .body("logoutUrl", Matchers.containsString("client_id=frontend"))
                .body("logoutUrl", Matchers.containsString("id_token_hint=dummy-id-token"));
    }

    @Test
    @Order(11)
    void shouldRejectLogoutWithoutToken() {

        given().when().body("{}").contentType(ContentType.JSON).post("/api/auth/logout").then().statusCode(401);
    }
}
