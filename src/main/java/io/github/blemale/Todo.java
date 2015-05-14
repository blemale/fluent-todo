package io.github.blemale;

import static java.util.Objects.hash;
import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Todo {
  private final UUID uuid;
  private String title;
  private String content = "";
  private boolean done = false;

  @JsonCreator
  public Todo(
      @JsonProperty("uuid") UUID uuid,
      @JsonProperty("title") String title) {
    this.uuid = uuid;
    this.title = title;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Boolean getDone() {
    return done;
  }

  public void setDone(boolean done) {
    this.done = done;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Todo todo = (Todo) o;
    return Objects.equals(this.uuid, todo.uuid);
  }

  @Override
  public int hashCode() {
    return hash(uuid);
  }


  public static class Builder {
    private UUID uuid;
    private String title;
    private String content = "";
    private boolean done = false;

    private Builder() {
    }

    public static Builder aTodo() {
      return new Builder();
    }

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withContent(String content) {
      this.content = content;
      return this;
    }

    public Builder withDone(boolean done) {
      this.done = done;
      return this;
    }

    public Builder withUuid(UUID uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder but() {
      return aTodo().withTitle(title).withContent(content).withDone(done).withUuid(uuid);
    }

    public Todo build() {
      Todo todo = new Todo(uuid, title);
      todo.setContent(content);
      todo.setDone(done);
      return todo;
    }
  }
}
