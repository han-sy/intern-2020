package com.board.project.blockboard;

import java.util.HashSet;
import java.util.Set;
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

}
