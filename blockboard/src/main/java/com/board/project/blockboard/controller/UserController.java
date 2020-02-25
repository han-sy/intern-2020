/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.common.util.CookieUtils;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.UserService;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Slf4j
@Controller
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private JwtService jwtService;
  final String HEADER_NAME = "Authorization";

  /**
   * 로그인 검증
   *
   * @param requestUser 로그인을 요청한 User의 정보
   * @param response    쿠키를 담은 객체
   * @return 로그인 메인화면으로 redirect
   */
  @PostMapping("/login")
  public String loginCheck(@ModelAttribute UserDTO requestUser, HttpServletResponse response) {
    boolean isValid = userService.loginCheck(requestUser, response);
    if (isValid) {
      return "redirect:/main";
    } else {
      return "redirect:/login";
    }
  }

  /**
   * 로그인 메인 화면
   *
   * @param request 쿠키 조회하기 위한 객체
   * @return server가 만들어 준 쿠키가 있다면 -> /boards redirect 없다면 -> 로그인 화면 띄운다.
   */
  @GetMapping("/login")
  public String login(HttpServletRequest request) {
    // 이미 JWT 토큰을 가지고 있으면 로그인 생략 후 메인화면으로 이동
    String token = CookieUtils.getCookie(request, HEADER_NAME);

    if (jwtService.isUsable(token)) {
      return "redirect:/main";
    } else {
      return "login";
    }
  }

  /**
   * 로그아웃
   *
   * @param response 유효기간이 0인 쿠키를 담은 객체
   * @return 로그인 메인화면으로 redirect
   */
  @PostMapping("/logout")
  @ResponseBody
  public String logout(HttpServletResponse response) {
    Cookie c = new Cookie(HEADER_NAME, null);
    c.setMaxAge(0);
    response.addCookie(c);
    return "login";
  }


  @PostMapping("/users")
  @ResponseBody
  public UserDTO insertUser(HttpServletRequest request, @ModelAttribute UserDTO user) {
    return userService.insertUser(request, user);
  }

  @GetMapping("/users")
  @ResponseBody
  public List<UserDTO> getAllUserByCompanyId(HttpServletRequest request) {
    int companyId = Integer.parseInt(request.getAttribute("companyId").toString());
    return userService.selectUsersByCompanyId(companyId);
  }

  @GetMapping("/users/{userId}")
  @ResponseBody
  public UserDTO getUserByUserIdAndCompanyId(HttpServletRequest request,
      @PathVariable String userId) {
    int companyId = Integer.parseInt(request.getAttribute("companyId").toString());
    return userService.selectUserByUserIdAndCompanyId(userId, companyId);
  }

  @PutMapping("/users/{userId}/image")
  public void updateUserImage(MultipartHttpServletRequest multipartRequest,
      @PathVariable String userId) {
    userService.updateUserImage(multipartRequest, userId);
  }

  @GetMapping("/users/count")
  @ResponseBody
  public int countUsersByCompanyId(HttpServletRequest request) {
    return userService.countUsersByCompanyId(request);
  }
}
