package com.board.project.blockboard.common.firebase;

import com.board.project.blockboard.dto.UserDTO;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.TopicManagementResponse;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file NotificationService.java
 */
@Slf4j
@Service
public class NotificationService {
  public void subscribe(String token) {
    try {
      TopicManagementResponse response =
          FirebaseMessaging.getInstance()
              .subscribeToTopicAsync(Collections.singletonList(token), "noti").get();
      log.info(response.getSuccessCount() + " tokens were subscribed successfully");
    } catch (InterruptedException | ExecutionException e) {
      log.error("subscribe", e);
    }
  }

  public void createReceiveNotification(UserDTO sender, UserDTO receiver) {

  }
}
