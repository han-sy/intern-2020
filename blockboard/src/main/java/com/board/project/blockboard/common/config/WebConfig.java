/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file WebConfig.java
 */
package com.board.project.blockboard.common.config;

import com.board.project.blockboard.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private static final String[] INCLUDE_PATHS = {
      "/main/**",
      "/main",
      "/boards/**",
      "/functions/**",
      "/functions",
      "/boards",
      "/users",
      "/users/**",
      "/alarms"
  };
  private static final String[] EXCLUDE_PATHS = {
      "/",
      "/login"
  };
  @Autowired
  private JwtInterceptor jwtInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor)
        .addPathPatterns(INCLUDE_PATHS)
        .excludePathPatterns(EXCLUDE_PATHS);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedMethods("GET", "POST", "PUT", "DELETE");
  }
}
