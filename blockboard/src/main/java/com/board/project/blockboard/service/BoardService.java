/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BoardService {

  @Autowired
  private BoardMapper boardMapper;

  public List<BoardDTO> getBoardListByCompanyID(int companyID) {
    List<BoardDTO> boardList = boardMapper.selectBoardsByCompanyID(companyID);
    return boardList;
  }

  public void insertNewBoard(String newBoardName, int companyID) {
    BoardDTO newBoard = BoardDTO.builder()
        .companyID(companyID)
        .boardName(newBoardName)
        .build();
    boardMapper.insertBoard(newBoard);
  }

  public void changeBoardName(int boardID, String boardName) {
    BoardDTO boardData = BoardDTO.builder()
        .boardID(boardID)
        .boardName(boardName)
        .build();
    boardMapper.updateBoardName(boardData);
  }


  public void deleteBoard(int boardID) {
    boardMapper.deleteBoard(boardID);
  }

  public void deleteBoardsByDeleteBoardList(List<BoardDTO> deleteBoards) {
    deleteBoards.forEach(boardDTO -> deleteBoard(boardDTO.getBoardID()));
  }

  public void updateChangedName(List<BoardDTO> newTitleList, int companyID) {
    newTitleList.forEach(boardDTO -> changeBoardName(boardDTO.getBoardID(),boardDTO.getBoardName()));
  }
}

