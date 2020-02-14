/**
 * @author Woohyeok Jun <woohyeok.jun@worksmobile.com>
 * @file UserController.java
 */
package com.board.project.blockboard.controller;

import com.board.project.blockboard.common.util.CookieUtils;
import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.JwtService;
import com.board.project.blockboard.service.UserService;
import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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
  @GetMapping("/logout")
  public String logout(HttpServletResponse response) {
    Cookie c = new Cookie(HEADER_NAME, null);
    c.setMaxAge(0);
    response.addCookie(c);
    return "redirect:/login";
  }


  @GetMapping("/userinfo")
  @ResponseBody
  public UserDTO info(HttpServletRequest request) {
    return new UserDTO(request);
  }

  @PostMapping("/users")
  @ResponseBody
  public UserDTO insertUser(HttpServletRequest request, @ModelAttribute UserDTO user) {
    return userService.insertUser(request, user);
  }


  @PutMapping("/users/{userid}/Image")
  public void updateUserImage(MultipartHttpServletRequest multipartRequest, @PathVariable("userid") String userID, HttpServletResponse response)
      throws IOException {

    userService.updateUserImage(multipartRequest, userID,response);
  }

  @GetMapping("/users")
  @ResponseBody
  public List<UserDTO> getUsers(HttpServletRequest request) {
    int companyID = Integer.parseInt(request.getAttribute("companyID").toString());
    return userService.selectUsersByCompanyID(companyID);
  }

  @GetMapping("/users/{userid}")
  @ResponseBody
  public UserDTO getUser(HttpServletRequest request, @PathVariable("userid") String userID) {
    int companyID = Integer.parseInt(request.getAttribute("companyID").toString());
    return userService.selectUserByUserIdAndCompanyId(userID, companyID);
  }
}
