/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardMapper.java
 */
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
    List<BoardDTO> selectBoardsByCompanyID(int companyID);
    List<PostDTO> selectPostByBoardID(int boardID);
    PostDTO selectPostByPostID(int postID);
    int selectCompanyIDByUserID(String userID);
    String selectCompanyNameByUserID(String userID);
    String selectUserTypeByUserID(String userID);
    void insertBoard(BoardDTO newBoard);
    BoardDTO selectBoardByBoardName(String newBoardName);
    void updateBoardName(Map<String, Object> boardAttributes);
    void deleteAllPostInBoard(int boardID);
    int deleteBoard(int boardID);
    void deleteAllCommentInBoard(int boardID);
}

