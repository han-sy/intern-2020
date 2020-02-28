package com.board.project.blockboard.common.util;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.dto.PostDTO;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file TagCheckUtils.java
 */
@Slf4j
@Component
public class TagCheckUtils {

  public Set<String> getTaggedUsers(PostDTO post) {
    Set<String> taggedUsers = new HashSet<>();
    Document doc = Jsoup.parse(post.getPostContent());
    Elements body = doc.select("a.mentions_tag");
    for (int i = 0; i < body.size(); i++) {
      Element tagItem = body.get(i);
      String userId = tagItem.dataset().get("id");
      taggedUsers.add(userId);
    }
    return taggedUsers;
  }

  public Set<String> getTaggedUsers(String originalCommentContent) {
    Set<String> taggedUsers = new HashSet<>();
    Document doc = Jsoup.parse(originalCommentContent);
    Elements body = doc.select("a.mentions_tag");

    for (int i = 0; i < body.size(); i++) {
      Element tagItem = body.get(i);
      String userId = tagItem.dataset().get("id");
      taggedUsers.add(userId);
    }
    return taggedUsers;
  }
}
