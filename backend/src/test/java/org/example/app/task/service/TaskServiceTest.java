package org.example.app.task.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.example.app.task.common.TaskItemEto;
import org.example.app.task.common.TaskListCto;
import org.example.app.task.common.TaskListEto;
import org.example.app.task.logic.UcAddRandomActivityTaskItem;
import org.example.app.task.logic.UcDeleteTaskItem;
import org.example.app.task.logic.UcDeleteTaskList;
import org.example.app.task.logic.UcFindTaskItem;
import org.example.app.task.logic.UcFindTaskList;
import org.example.app.task.logic.UcSaveTaskItem;
import org.example.app.task.logic.UcSaveTaskList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Test of {@link TaskService}.
 */
@QuarkusTest
@DisplayName("/task")
class TaskServiceTest extends Assertions {

  @InjectMock
  UcSaveTaskList saveTaskList;

  @InjectMock
  UcFindTaskList findTaskList;

  @InjectMock
  UcDeleteTaskList deleteTaskList;

  @InjectMock
  UcSaveTaskItem saveTaskItem;

  @InjectMock
  UcFindTaskItem findTaskItem;

  @InjectMock
  UcDeleteTaskItem deleteTaskItem;

  @InjectMock
  UcAddRandomActivityTaskItem addRandomActivityTaskItem;

  @Nested
  @DisplayName("/list")
  class TaskListCollection {

    @Nested
    @DisplayName("POST")
    class Post {

      @Test
      void shouldCallSaveUseCaseAndReturn204WhenCreatingTaskList() {
        TaskListEto taskListEntity = new TaskListEto();
        taskListEntity.setId(123L);

        given(TaskServiceTest.this.saveTaskList.save(Mockito.any())).willReturn(taskListEntity);

        given().when().body("{ \"title\": \"Shopping List\" }").contentType(ContentType.JSON).post("/task/list").then()
                .statusCode(201);
      }

      @Test
      void shouldFailWith400AndValidationErrorWhenTitleIsEmpty() {

        given().when().body("{ \"title\": \"\" }").contentType(ContentType.JSON).post("/task/list").then()
            .statusCode(400);
        then(TaskServiceTest.this.saveTaskList).shouldHaveNoInteractions();
      }
    }

    @Nested
    @DisplayName("/{listId}/")
    class TaskList {

      @Nested
      @DisplayName("GET")
      class Get {

        @Test
        void shouldReturnJsonWhenTaskListExists() {

          given(TaskServiceTest.this.findTaskList.findById(anyLong())).willReturn(TaskListMother.complete());

          given().when().get("/task/list/123").then().statusCode(200)
              .body(jsonEquals("{\"id\":123,\"version\":1,\"title\":\"Shopping List\"}"));
        }

        @Test
        void shouldReturn404WhenUnknownTaskList() {

          given(TaskServiceTest.this.findTaskList.findById(anyLong())).willReturn(null);

          given().when().get("/task/list/99").then().statusCode(404);
        }
      }

      @Nested
      @DisplayName("DELETE")
      class Delete {

        @Test
        void shouldCallDeleteUseCaseAndReturn204() {

          given().when().delete("/task/list/1").then().statusCode(204);
          then(TaskServiceTest.this.deleteTaskList).should().delete(1L);
        }
      }

      @Nested
      @DisplayName("/random-activity")
      class RandomActivity {

        @Nested
        @DisplayName("POST")
        class Post {
          @Test
          void shouldCallRandomActivityUseCaseAndReturn201() {

            given().when().post("/task/list/1/random-activity").then().statusCode(201);
            then(TaskServiceTest.this.addRandomActivityTaskItem).should().addRandom(1L);
          }
        }
      }
    }

    @Nested
    @DisplayName("/list-with-items/{taskListId}")
    class TaskListWithItems {

      @Nested
      @DisplayName("GET")
      class Get {

        @Test
        void shouldReturnListWithItemsWhenListExists() {

          TaskListCto taskList = new TaskListCto();
          taskList.setList(TaskListMother.complete());
          taskList.setItems(List.of(TaskItemMother.complete()));

          given(TaskServiceTest.this.findTaskList.findWithItems(123L)).willReturn(taskList);

          given().when().get("/task/list-with-items/123").then().statusCode(200).body(jsonEquals(
              "{\"items\":[{\"id\":42,\"version\":1,\"completed\":false,\"starred\":false,\"taskListId\":123,\"title\":\"Buy Eggs\"}],\"list\":{\"id\":123,\"version\":1,\"title\":\"Shopping List\"}}"));
        }

        @Test
        void shouldReturn404WhenListDoesntExist() {

          given(TaskServiceTest.this.findTaskList.findWithItems(anyLong())).willReturn(null);

          given().when().get("/task/list-with-items/99").then().statusCode(404);
        }
      }
    }

    @Nested
    @DisplayName("/multiple-random-activities")
    class MultipleRandomActivities {

      @Nested
      @DisplayName("POST")
      class Post {
        @Test
        void shouldCallRandomActivitiesUseCaseAndReturn201() {

          given().when().body("Shopping list").contentType(ContentType.TEXT).post("/task/list/multiple-random-activities").then().statusCode(201);
          then(TaskServiceTest.this.addRandomActivityTaskItem).should().addMultipleRandom(anyLong(), anyString());
        }

        @Test
        void shouldFailWith400AndValidationErrorWhenTitleIsEmpty() {

          given().when().contentType(ContentType.TEXT).post("/task/list/multiple-random-activities").then().statusCode(400);
          then(TaskServiceTest.this.addRandomActivityTaskItem).shouldHaveNoInteractions();
        }
      }
    }

    @Nested
    @DisplayName("/ingredient-list")
    class IngredientList {

      @Nested
      @DisplayName("POST")
      class Post {
        @Test
        void shouldCallRandomActivitiesUseCaseAndReturn201() {

          given().when().body("{\"listTitle\": \"Shopping list\", \"recipe\": \"Take flour, sugar and chocolate and mix everything.\"}")
                  .contentType(ContentType.JSON).post("/task/list/ingredient-list").then().statusCode(201);
          then(TaskServiceTest.this.addRandomActivityTaskItem).should().addExtractedIngredients(anyLong(), anyString());
        }

        @Test
        void shouldFailWith400AndValidationErrorWhenTitleIsEmpty() {

          given().when().body("{\"recipe\": \"Take flour, sugar and chocolate and mix everything.\"}").contentType(ContentType.JSON).post("/task/list/ingredient-list").then().statusCode(400);
          then(TaskServiceTest.this.addRandomActivityTaskItem).shouldHaveNoInteractions();
        }

        @Test
        void shouldFailWith400AndValidationErrorWhenRecipeIsEmpty() {

          given().when().body("{\"listTitle\": \"Shopping list\"}").contentType(ContentType.JSON).post("/task/list/ingredient-list").then().statusCode(400);
          then(TaskServiceTest.this.addRandomActivityTaskItem).shouldHaveNoInteractions();
        }
      }
    }
  }
  @Nested
  @DisplayName("/item")
  class TaskItemCollection {

    @Nested
    @DisplayName("POST")
    class Post {

      @Test
      void shouldCallSaveUseCaseAndReturn201WhenCreatingTaskItem() {
        TaskItemEto taskItemEntity = new TaskItemEto();
        taskItemEntity.setId(42L);

        given(TaskServiceTest.this.saveTaskItem.save(Mockito.any())).willReturn(taskItemEntity);

        given().when().body("{ \"title\": \"Buy Milk\", \"taskListId\": 123 }").contentType(ContentType.JSON)
                .post("/task/item").then().statusCode(201).body(is("42"));

        ArgumentCaptor<TaskItemEto> taskItemCaptor = ArgumentCaptor.forClass(TaskItemEto.class);
        then(TaskServiceTest.this.saveTaskItem).should().save(taskItemCaptor.capture());
        BDDAssertions.then(taskItemCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(TaskItemMother.notYetSaved());
      }

      @Test
      void shouldFailWith400AndValidationErrorWhenTitleIsEmpty() {

        given().when().body("{ \"title\": \"\", \"taskListId\": 123 }").contentType(ContentType.JSON)
            .post("/task/item").then().statusCode(400);
        then(TaskServiceTest.this.saveTaskItem).shouldHaveNoInteractions();
      }

      @Test
      void shouldFailWith400AndValidationErrorWhenTaskListIdNotGiven() {

        given().when().body("{ \"title\": \"Buy Milk\" }").contentType(ContentType.JSON).post("/task/item").then()
            .statusCode(400);
        then(TaskServiceTest.this.saveTaskItem).shouldHaveNoInteractions();
      }
    }

    @Nested
    @DisplayName("/{itemId}/")
    class TaskItem {

      @Nested
      @DisplayName("GET")
      class Get {

        @Test
        void shouldReturnJsonWhenItemExists() {

          given(TaskServiceTest.this.findTaskItem.findById(anyLong())).willReturn(TaskItemMother.complete());

          given().when().get("/task/item/42").then().statusCode(200).body(jsonEquals(
              "{\"id\":42,\"version\":1,\"completed\":false,\"starred\":false,\"taskListId\":123,\"title\":\"Buy Eggs\"}"));
        }

        @Test
        void shouldReturn404WhenUnknownTaskItem() {

          given(TaskServiceTest.this.findTaskItem.findById(anyLong())).willReturn(null);

          given().when().get("/task/item/99").then().statusCode(404);
        }

      }

      @Nested
      @DisplayName("DELETE")
      class Delete {

        @Test
        void shouldCallDeleteUseCaseAndReturn204() {

          given().when().delete("/task/item/42").then().statusCode(204);
          then(TaskServiceTest.this.deleteTaskItem).should().delete(42L);
        }
      }
    }

  }

}
