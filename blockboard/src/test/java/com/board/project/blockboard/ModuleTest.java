package com.board.project.blockboard;

import com.board.project.blockboard.dto.UserDTO;
import com.board.project.blockboard.service.UserService;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class ModuleTest {

  @Test
  void Test() {
    Set<String> user = new HashSet<>();

    user.add("aaaa");
    user.add("bbbb");
    user.add("cccc");
    user.add("aaaa");

    System.out.println(user.toString());
  }

  @Test
  void insertUserTest() {
    UserDTO user = new UserDTO();
    user.setUserID("1");
    user.setCompanyID(1);
    user.setUserPassword("aabbccdd1!");

    UserService userService = new UserService();
    userService.validateUser(user);
  }
}
