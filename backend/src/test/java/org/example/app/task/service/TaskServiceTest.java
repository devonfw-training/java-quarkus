package org.example.app.task.service;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.example.app.task.common.TaskItemEto;
import org.example.app.task.common.TaskListCto;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.example.app.task.dataaccess.TaskListEntity;
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

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;

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

  @Nested
  @DisplayName("/list")
  class TaskListCollection {

    @Nested
    @DisplayName("POST")
    class Post {

      @Test
      void shouldCallSaveUseCaseAndReturn204WhenCreatingTaskList() {
        TaskListEntity taskListEntity = new TaskListEntity();
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
          then(TaskServiceTest.this.deleteTaskList).should().delete(1l);
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
          taskList.setItems(asList(TaskItemMother.complete()));

          given(TaskServiceTest.this.findTaskList.findWithItems(123l)).willReturn(taskList);

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
    @DisplayName("/item")
    class TaskItemCollection {

      @Nested
      @DisplayName("POST")
      class Post {

        @Test
        void shouldCallSaveUseCaseAndReturn201WhenCreatingTaskItem() {

          TaskItemEntity taskItemEntity = new TaskItemEntity();
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
            then(TaskServiceTest.this.deleteTaskItem).should().delete(42l);
          }
        }
      }

    }

  }

}
