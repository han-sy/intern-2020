package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface BoardService {
    List<BoardDTO> allBoard();
    List<BoardDTO> printBoardbyComp(HttpServletRequest request, HttpSession session);
}
