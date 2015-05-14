package io.github.blemale;

import static io.github.blemale.Todo.Builder.aTodo;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.codestory.http.Configuration;
import net.codestory.http.logs.Logs;
import net.codestory.http.payload.Payload;
import net.codestory.http.routes.Routes;

public class TodoConfiguration implements Configuration {
  private static final Logger LOGGER = LoggerFactory.getLogger(TodoConfiguration.class);
  
  private final Map<UUID, Todo> todos = new ConcurrentHashMap<>();

  public TodoConfiguration() {
    Todo todo = aTodo().withUuid(UUID.randomUUID()).withTitle("my first todo").build();
    createTodo(todo);
  }

  @Override
  public void configure(Routes routes) {
    routes.url("/todos")
        .get(() -> getTodos())
        .post(context -> {
          Todo todo = context.extract(Todo.class);
          createTodo(todo);
          return Payload.created();
        });
    
    routes.url("/todos/:uuid")
        .get((context, uuid) -> getTodo(uuid))
        .put((context, uuid) -> {
          Todo todo = context.extract(Todo.class);
          updateTodo(uuid, todo);
          return Payload.ok();
        });
    
    routes.filter((uri, context, next) -> {
      Logs.uri(uri);
      LOGGER.info("Call: {} {}", context.method(), uri);
      return next.get();
    });
  }

  private void updateTodo(String uuid, Todo todo) {
    todos.put(UUID.fromString(uuid), todo);
  }

  private Todo getTodo(String uuid) {
    return todos.get(UUID.fromString(uuid));
  }

  private void createTodo(Todo todo) {
    todos.put(todo.getUuid(), todo);
  }

  private Collection<Todo> getTodos() {
    return todos.values();
  }


}
