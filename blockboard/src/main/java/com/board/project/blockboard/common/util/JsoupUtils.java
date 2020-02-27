package com.board.project.blockboard.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file JsoupUtils.java
 */
@Slf4j
public class JsoupUtils {

  public static String filterStringForXSS(String content) {
    return Jsoup.clean(content, "http://https://block-board-inline.s3.amazonaws.com/",
        Whitelist.relaxed()
            .preserveRelativeLinks(true).addAttributes("a", "data-id", "href", "class")
            .addEnforcedAttribute("a", "href", "javascript:void(0)"),
        new Document.OutputSettings().prettyPrint(false));
  }

  /**
   * @param content filterStringForXSS 를 거친 후 나온 String 이라 허용된 HTMl 태그만 가지고 있다.
   *
   *  키보드로 입력된 HTML 태그들은 검색이 가능해야 한다.
   *  키보드로 입력된 태그들은 escapeHtml 상태로 저장되어 있고, 아닌것들은 HTML 그대로 가지고 있다.
   *  따라서 해당 String 을 JSoup 을 사용해 HTML 태그는 다 없애고 escapeHtml 은 unescape 해주면
   *  HTML 태그문들도 검색이 가능하게 된다.
   *
   */
  public static String unescapeHtmlFromStringOfFilteringXSS(String content) {
    return Jsoup.parse(content).text();
  }
}
