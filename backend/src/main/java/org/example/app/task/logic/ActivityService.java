package org.example.app.task.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.app.task.common.TaskItemEto;
import org.openapi.quarkus.openai_json.api.ChatCompletionsApi;
import org.openapi.quarkus.openai_json.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.openapi.quarkus.openai_json.model.ChatCompletionUserMessage.RoleEnum.USER;

/**
 * Provides services for retrieving activity suggestions.
 */
@ApplicationScoped
public class ActivityService {

    private static final String RESPONSE_JSON_SCHEMA = "json.schema/task-items.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    @RestClient
    ChatCompletionsApi defaultApi;

    @ConfigProperty(name = "quarkus.rest-client.openai_json.model", defaultValue = "code")
    String model;

    /**
     * Get a random activity suggestion.
     *
     * @return the activity description
     */
    @Fallback(fallbackMethod = "fallbackActivity")
    @Timeout(unit = ChronoUnit.SECONDS, value = 5)
    public String getRandomActivity() {

        String prompt = "Give me exactly one random item (containing maximal 6 words) which I can add to my ToDo list"
                + " " +
                "and return only this item without any additional text.";
        ChatCompletionV1ChatCompletionsPostRequest request = buildRequest(prompt, null);
        return defaultApi.chatCompletionV1ChatCompletionsPost(request).toString();
    }

    /**
     * Retrieves multiple random activity suggestions.
     *
     * @param listTitle the title of the task list
     * @return a list of {@link TaskItemEto} objects, each representing a suggested activity
     */
    @Fallback(fallbackMethod = "fallbackActivities")
    @Timeout(unit = ChronoUnit.SECONDS, value = 30)
    public List<TaskItemEto> getMultipleRandomActivities(String listTitle) {

        String prompt = """
                Give me 5-10 random items (containing maximal 5-6 words) which are related to the topic %s 
                and which I can add to my ToDo list and return them in the provided JSON format to be parsed in Java:
                """.formatted(listTitle);
        var request = buildRequest(prompt, RESPONSE_JSON_SCHEMA);
        return sendRequestAndParseResponse(request, new TypeReference<>() {
        });
    }

    /**
     * Extracts a list of ingredients from the given recipe.
     *
     * @param recipe the recipe text from which to extract ingredients
     * @return a list of {@link TaskItemEto} objects, each representing an ingredient
     */
    @Fallback(fallbackMethod = "fallbackIngredients")
    @Timeout(unit = ChronoUnit.SECONDS, value = 30)
    public List<TaskItemEto> getExtractedIngredients(String recipe) {

        String prompt = String.format("I have this recipe: %s Can you please extract a list of ingredients and return"
                + " them in the provided JSON format?", recipe);
        var request = buildRequest(prompt, RESPONSE_JSON_SCHEMA);
        Log.debug(request);
        return sendRequestAndParseResponse(request, new TypeReference<>() {
        });
    }

    /**
     * Builds a {@link ChatCompletionV1ChatCompletionsPostRequest} with the specified prompt and optional JSON schema.
     *
     * @param prompt     the input text for the model
     * @param schemaPath the path to the JSON schema file; if {@code null}, no schema is used
     * @return a {@link ChatCompletionV1ChatCompletionsPostRequest} configured with the given prompt and schema
     */
    private ChatCompletionV1ChatCompletionsPostRequest buildRequest(String prompt, String schemaPath) {
        ChatCompletionV1ChatCompletionsPostRequest request = new ChatCompletionV1ChatCompletionsPostRequest();
        request.setModel(model);

        if (schemaPath != null) {
            try (InputStream schemaStream = getClass().getClassLoader().getResourceAsStream(schemaPath)) {
                if (schemaStream == null) {
                    throw new FileNotFoundException("Schema file not found: " + schemaPath);
                }
//                Map<String, Object> schemaMap = objectMapper.readValue(schemaStream, new TypeReference<>() {});
                // request.setResponseFormat(schemaMap);
                prompt += " " + new String(schemaStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error loading schema from " + schemaPath, e);
            }
        }
        request.setMessages(buildMessages(prompt));
        Log.info(request.toString());
        return request;
    }

    private List<ChatCompletionUserMessage> buildMessages(String prompt) {
        var message = new ChatCompletionUserMessage();
        message.setRole(USER);
        message.content(prompt);
        return List.of(message);
    }

    /**
     * Sends a {@link ChatCompletionV1ChatCompletionsPostRequest} to the Ollama API and parses the JSON response into
     * the specified type.
     *
     * @param <T>     the type of the desired object
     * @param request the {@link ChatCompletionV1ChatCompletionsPostRequest} to send
     * @param typeRef a {@link TypeReference} indicating the type to parse the response into
     * @return the parsed response of type {@code T}
     */
    private <T> T sendRequestAndParseResponse(ChatCompletionV1ChatCompletionsPostRequest request,
                                              TypeReference<T> typeRef) {
        var response = defaultApi.chatCompletionV1ChatCompletionsPost(request);
        try {
            String content = ((HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>>) response).get("choices").getFirst().get("message").get("content");
            return objectMapper.readValue(content.replace("`", ""), typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during parsing JSON response from Ollama.", e);
        }
    }

    /**
     * Fallback activity in case the suggestion service is down.
     *
     * @return the activity description
     */
    public String fallbackActivity() {

        return "Write more tests";
    }

    /**
     * List of fallback activities in case the suggestion service is down.
     *
     * @param listTitle the title of the task list (not used in fallback)
     * @return a list of default task items
     */
    private List<TaskItemEto> fallbackActivities(String listTitle) {
        Log.warn("Using Fallback for ActivityService");
        return List.of(
                createTaskItem("Walk the dog"),
                createTaskItem("Pick up books"),
                createTaskItem("Water plants tonight")
        );
    }

    /**
     * List of fallback ingredients in case the suggestion service is down.
     *
     * @param recipe the recipe text (not used in fallback)
     * @return a list of default ingredient items
     */
    private List<TaskItemEto> fallbackIngredients(String recipe) {
        Log.warn("Using Fallback for ActivityService");
        return List.of(
                createTaskItem("All-purpose flour"),
                createTaskItem("Granulated sugar"),
                createTaskItem("Pure vanilla extract"),
                createTaskItem("Whole milk")
        );
    }

    /**
     * Creates a TaskItemEto with the given title.
     *
     * @param title the title of the task item
     * @return a new TaskItemEto with the specified title
     */
    private TaskItemEto createTaskItem(String title) {
        TaskItemEto taskItem = new TaskItemEto();
        taskItem.setTitle(title);
        return taskItem;
    }
}
