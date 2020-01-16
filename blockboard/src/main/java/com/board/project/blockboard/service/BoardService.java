package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
<<<<<<< HEAD
=======

>>>>>>> 37f19286f213b4380c7fa85fc442ced0093fcc11
import java.util.List;

public interface BoardService {
    List<BoardDTO> allBoard();
    List<BoardDTO> printBoardbyComp(String user_id);
    List<PostDTO> printPostbyBoard(String board_id);
    PostDTO printPostContnet(String post_id);
    String printCompanyName(String user_id);
    boolean checkAdmin(String user_id);
    void insertNewBoard(String newBoardName,int companyID);
    int printCompanyId(String decode);
    BoardDTO printboardbyBoardName(String newBoardName);
}

