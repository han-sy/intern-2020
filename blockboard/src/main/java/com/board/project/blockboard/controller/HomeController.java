/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file HomeController.java
 */
package com.board.project.blockboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
  @GetMapping("")
  public String home() {
    return "redirect:/login";
  }
}
