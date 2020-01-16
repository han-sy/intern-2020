package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;

import java.util.List;

public interface BoardService {
    List<BoardDTO> allBoard();
    List<BoardDTO> printBoardbyComp(String userID);
    List<PostDTO> printPostbyBoard(String boardID);
    PostDTO printPostContnet(String postID);
    String printCompanyName(String userID);
    boolean checkAdmin(String userID);
    void insertNewBoard(String newBoardName,int companyID);
    int printCompanyId(String userID);
    BoardDTO printboardbyBoardName(String newBoardName);
}

