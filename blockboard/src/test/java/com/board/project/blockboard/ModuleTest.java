package com.board.project.blockboard;

import com.board.project.blockboard.common.util.JsoupUtils;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
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

  @Test
  public void testCommentContent() {
    String content = "<a class='mentions_tag name' data-id='irene' href='javascript:void(0)'><strong>@아이린</strong></a>&nbsp;<div></div> 이렇게 입력해봐";
    System.out.println(JsoupUtils.filterStringForXSS(content));
    System.out.println(StringEscapeUtils.escapeHtml4(content));
    System.out.println(Jsoup.parse(content).text());

  }
}
