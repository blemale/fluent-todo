package io.github.blemale;

import static io.github.blemale.Todo.Builder.aTodo;
import static net.codestory.http.convert.TypeConvert.fromJson;
import static net.codestory.http.convert.TypeConvert.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import net.codestory.http.WebServer;
import net.codestory.http.misc.Env;
import net.codestory.rest.FluentRestTest;
import net.codestory.rest.Response;
import net.codestory.rest.RestAssert;

public class TodoServerTest implements FluentRestTest {
  private static final TypeReference<List<Todo>> TODOS_TYPE_REF =
      new TypeReference<List<Todo>>() {
      };

  private final WebServer server = new WebServer() {
    @Override
    protected Env createEnv() {
      return Env.prod();
    }
  }.startOnRandomPort();

  @Override
  public int port() {
    return server.port();
  }

  @Before
  public void configureServer() {
    server.configure(new TodoConfiguration());
  }

  @Test
  public void should_succeed() {
    get("/todos").should().succeed();
  }

  @Test
  public void should_return_todos() {
    Response response = get("/todos").response();

    List<Todo> todos = fromJson(response.content(), TODOS_TYPE_REF);

    assertThat(response.contentType()).isEqualTo("application/json;charset=UTF-8");
    assertThat(todos).isNotEmpty();
  }

  @Test
  public void should_create_todo() {
    Todo todo = aTodo().withUuid(UUID.randomUUID()).withTitle("title").build();

    RestAssert restAssert = post("/todos", toJson(todo));

    restAssert.should().respond(201);
  }

  @Test
  public void should_retrieve_created_todo() throws InterruptedException {
    Todo todo = aTodo().withUuid(UUID.randomUUID()).withTitle("title").build();

    post("/todos", toJson(todo)).should().succeed();
    Response response = get("/todos").response();

    assertThat(fromJson(response.content(), TODOS_TYPE_REF)).contains(todo);
  }

  @Test
  public void should_retrive_todo_by_uuid() {
    Todo todo = aTodo().withUuid(UUID.randomUUID()).withTitle("title").build();

    post("/todos", toJson(todo)).should().succeed();
    Response response = get("/todos/" + todo.getUuid().toString()).response();

    assertThat(fromJson(response.content(), Todo.class)).isEqualTo(todo);
  }

  @Test
  public void should_update_todo() {
    Todo todo = aTodo().withUuid(UUID.randomUUID()).withTitle("title").build();

    post("/todos", toJson(todo)).should().succeed();

    todo.setTitle("another title");
    put("/todos/" + todo.getUuid().toString(), toJson(todo)).should().succeed();

    Response response = get("/todos/" + todo.getUuid().toString()).response();

    assertThat(fromJson(response.content(), Todo.class).getTitle()).isEqualTo(todo.getTitle());
  }
}
