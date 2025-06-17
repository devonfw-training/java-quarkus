package org.example.app.task.logic;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.util.Collections;
import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {

  private static final String CONTENT_TYPE = "Content-Type";
  private static final String CONTENT_TYPE_JSON = "application/json";
  public static final String PATH_GENERATE = "/api/generate";
  private WireMockServer wireMockServer;

  @Override
  public Map<String, String> start() {

    this.wireMockServer = new WireMockServer(options().port(11434));
    this.wireMockServer.start();

    setupStubForSingleRandomItem();
    setupStubForMultipleRandomItems();
    setupStubForRecipeIngredients();

    return Collections.singletonMap("quarkus.rest-client.ollama_api_yaml.url", this.wireMockServer.baseUrl());
  }

  private void setupStubForSingleRandomItem() {

    this.wireMockServer.stubFor(post(urlEqualTo(PATH_GENERATE))
            .withHeader(CONTENT_TYPE, equalTo(CONTENT_TYPE_JSON))
            .withRequestBody(matchingJsonPath("$.prompt", containing("Give me exactly one random item")))
            .willReturn(aResponse()
                    .withHeader(CONTENT_TYPE, CONTENT_TYPE_JSON)
                    .withBody("""
                            {
                              "model": "llama3.2",
                              "created_at": "2025-03-10T13:14:53.8857629Z",
                              "response": "Learn a new programming language",
                              "done": true,
                              "done_reason": "stop",
                              "total_duration": 38037436100,
                              "load_duration": 32303246400
                            }
                            """)));
  }

  private void setupStubForMultipleRandomItems() {

    this.wireMockServer.stubFor(post(urlEqualTo(PATH_GENERATE))
            .withHeader(CONTENT_TYPE, equalTo(CONTENT_TYPE_JSON))
            .withRequestBody(matchingJsonPath("$.prompt", containing("Give me 5-10 random items")))
            .willReturn(aResponse()
                    .withHeader(CONTENT_TYPE, CONTENT_TYPE_JSON)
                    .withBody("""
                            {
                              "model": "llama3.2",
                              "created_at": "2025-03-10T13:14:53.8857629Z",
                              "response": "[{\\"title\\": \\"Read a new book\\"}, {\\"title\\": \\"Go for a walk\\"}]",
                              "done": true,
                              "done_reason": "stop",
                              "total_duration": 38037436100,
                              "load_duration": 32303246400
                            }
                            """)));
  }

  private void setupStubForRecipeIngredients() {

    this.wireMockServer.stubFor(post(urlEqualTo(PATH_GENERATE))
            .withHeader(CONTENT_TYPE, equalTo(CONTENT_TYPE_JSON))
            .withRequestBody(matchingJsonPath("$.prompt", containing("I have this recipe:")))
            .willReturn(aResponse()
                    .withHeader(CONTENT_TYPE, CONTENT_TYPE_JSON)
                    .withBody("""
                            {
                              "model": "llama3.2",
                              "created_at": "2025-03-10T13:14:53.8857629Z",
                              "response": "[{\\"title\\": \\"flour\\"}, {\\"title\\": \\"sugar\\"}, {\\"title\\": \\"chocolate\\"}]",
                              "done": true,
                              "done_reason": "stop",
                              "total_duration": 38037436100,
                              "load_duration": 32303246400
                            }
                            """)));
  }

  @Override
  public void stop() {

    if (null != this.wireMockServer) {
      this.wireMockServer.stop();
    }
  }
}