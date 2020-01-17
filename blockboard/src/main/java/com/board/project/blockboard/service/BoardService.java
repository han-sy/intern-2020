package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;

import java.util.List;
import java.util.Map;

public interface BoardService {
    List<BoardDTO> allBoard();
    List<BoardDTO> getBoardListByUserID(String userID);
    List<PostDTO> getPostListByBoardID(String boardID);
    PostDTO getPostByPostID(String postID);
    String getCompanyNameByUserID(String userID);
    boolean checkAdmin(String userID);
    void insertNewBoard(String newBoardName,int companyID);
    int getCompanyIDByUserID(String userID);
    BoardDTO getBoardByBoardName(String boardName);
    int selectBoardIDByComIDAndBoardName(Map<String, Object> map);
}

