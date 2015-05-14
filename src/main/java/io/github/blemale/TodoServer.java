package io.github.blemale;

import net.codestory.http.WebServer;

public class TodoServer {

  public static void main(String[] args) {
    new WebServer().configure(new TodoConfiguration()).start();
  }
}
