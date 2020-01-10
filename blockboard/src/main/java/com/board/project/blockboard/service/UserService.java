package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    List<UserDTO> allUser();
    boolean loginCheck(HttpServletRequest request, HttpSession session);
}
