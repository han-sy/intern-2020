package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface BoardMapper {
    List<BoardDTO> selectAllBoard();
    List<BoardDTO> selectBoardByCompanyID(int companyID);
    List<PostDTO> selectPostByBoardID(int boardID);
    PostDTO selectPostByPostID(int postID);
    int selectCompanyIDByUserID(String userID);
    String selectCompanyNameByUserID(String userID);
    String selectUserTypeByUserID(String userID);
    int maxBoardID();
    void insertBoard(BoardDTO newBoard);
    BoardDTO selectBoardByBoardName(String newBoardName);
    BoardDTO getBoardIDByComIDAndBoardName(Map<String, Object> map);

    void updateBoardName(Map<String, Object> boardAttributes);
}

