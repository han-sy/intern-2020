package com.board.project.blockboard;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;


class ModuleTest {


  @Test
  public void completableFuture()  {
    CompletableFuture completableFuture1 = CompletableFuture.supplyAsync(() -> {
      System.out.println("future-1");
      return "1";
    });

    CompletableFuture completableFuture2 = CompletableFuture.supplyAsync(() -> {
      System.out.println("future-2");
      return "2";
    });

    CompletableFuture completableFuture3 = CompletableFuture.supplyAsync(() -> {
      System.out.println("future-3");
      return "3";
    });

    List<CompletableFuture> futures = Arrays.asList(completableFuture1,
        completableFuture2,
        completableFuture3);
    CompletableFuture.allOf(completableFuture1, completableFuture2, completableFuture3)
        .thenAccept(s -> {
          List<Object> result = futures.stream()
              .map(pageContentFuture -> pageContentFuture.join())
              .collect(Collectors.toList());
          System.out.println(result.toString());
        });

  }

}
