package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BoardMapper {
    List<BoardDTO> allBoard();
    List<BoardDTO> selectBoardByComId(String companyID);
    List<PostDTO> selectPostByBoardId(String boardID);
    PostDTO selectPostByPostID(String postID);
    String selectComIdByUserId(String userID);
    String selectComNameByUserId(String userID);
    String selectUserTypeByUserId(String userID);
    int maxBoardID();
    void insertBoard(BoardDTO newBoard);
    BoardDTO selectBoardByBoardName(String newBoardName);
}

