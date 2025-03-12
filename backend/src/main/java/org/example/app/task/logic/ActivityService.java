package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.openapi.quarkus.ollama_api_yaml.api.DefaultApi;
import org.openapi.quarkus.ollama_api_yaml.model.ApiGeneratePost200Response;
import org.openapi.quarkus.ollama_api_yaml.model.ApiGeneratePostRequest;

import java.time.temporal.ChronoUnit;

/**
 * Provides services for retrieving activity suggestions.
 */
@ApplicationScoped
public class ActivityService {

  @Inject
  @RestClient
  DefaultApi defaultApi;

  /**
   * Get a random activity suggestion.
   *
   * @return the activity description
   */
  @Fallback(fallbackMethod = "fallbackActivity")
  @Timeout(unit = ChronoUnit.SECONDS, value = 5)
  public String getRandomActivity() {

    ApiGeneratePostRequest request = new ApiGeneratePostRequest();
    request.setModel("llama3");
    request.setStream(false);
    request.setKeepAlive(600);
    request.setPrompt("Give me a random item which I can add to my ToDo list like BoredAPI used to do and" +
            "please return only the item, containing maximal 5-6 words, without any other additional text.");

    ApiGeneratePost200Response response = defaultApi.apiGeneratePost(request);
    return response.getResponse();
  }

  /**
   * Fallback activity in case the suggestion service is down.
   *
   * @return the activity description
   */
  public String fallbackActivity() {

    return "Write more tests";
  }
}
