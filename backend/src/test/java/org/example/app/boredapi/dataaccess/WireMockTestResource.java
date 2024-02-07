package org.example.app.boredapi.dataaccess;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockTestResource implements QuarkusTestResourceLifecycleManager {

  private WireMockServer wireMockServer;

  @Override
  public Map<String, String> start() {

    this.wireMockServer = new WireMockServer(options().dynamicPort());
    this.wireMockServer.start();

    this.wireMockServer
        .stubFor(get(urlEqualTo("/activity")).willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody("{\r\n" + "  \"activity\": \"Learn a new programming language\",\r\n"
                + "  \"type\": \"education\",\r\n" + "  \"participants\": 1,\r\n" + "  \"price\": 0.1,\r\n"
                + "  \"link\": \"\",\r\n" + "  \"key\": \"5881028\",\r\n" + "  \"accessibility\": 0.25\r\n" + "}")));

    return Collections.singletonMap("quarkus.rest-client.bored-api.url", this.wireMockServer.baseUrl());
  }

  @Override
  public void stop() {

    if (null != this.wireMockServer) {
      this.wireMockServer.stop();
    }
  }
}
