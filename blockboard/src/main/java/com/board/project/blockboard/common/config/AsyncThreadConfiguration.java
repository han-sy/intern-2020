/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file AsyncThreadConfiguration.java
 */
package com.board.project.blockboard.common.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncThreadConfiguration {

  @Bean
  public Executor asyncThreadTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(10);
    threadPoolTaskExecutor.setMaxPoolSize(20);
    threadPoolTaskExecutor.setThreadNamePrefix("block-board");
    return threadPoolTaskExecutor;
  }
}
