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
    List<BoardDTO> selectBoardByComId(String company_id);
    List<PostDTO> selectPostByBoardId(String board_id);
    PostDTO selectPostByPostID(String post_id);
    String selectComIdByUserId(String user_id);
    String selectComNameByUserId(String user_id);
    String selectUserTypeByUserId(String user_id);
    int maxBoardID();
    void insertBoard(BoardDTO newBoard);
    BoardDTO selectBoardByBoardName(String newBoardName);
}

