package com.board.project.blockboard.common.firebase;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file FCMInitializer.java
 */
@Slf4j
@Service
public class FCMInitializer {

  private static final String FIREBASER_CONFIG_PATH = "blockboard-firebase-key.json";

  @PostConstruct
  public void initialize() {
    try {
      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials
              .fromStream(new ClassPathResource(FIREBASER_CONFIG_PATH).getInputStream()))
          .setDatabaseUrl("https://blockboard-a2fb8.firebaseio.com")
          .build();
      if(FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("Firebase application has been initialized.");
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
