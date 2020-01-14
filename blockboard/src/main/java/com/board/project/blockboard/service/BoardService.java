package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import sun.awt.geom.Crossings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface BoardService {
    List<BoardDTO> allBoard();
    List<BoardDTO> printBoardbyComp(String user_id);
    List<PostDTO> printPostbyBoard(String board_id);
    PostDTO printPostContnet(String post_id);
    String printCompanyName(String user_id);
    boolean checkAdmin(String user_id);
    void insertNewBoard(String newBoardName);
    int printCompanyId(String decode);
    BoardDTO printboardbyBoardName(String newBoardName);
}

