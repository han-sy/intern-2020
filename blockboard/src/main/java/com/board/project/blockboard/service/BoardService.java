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

  public List<BoardDTO> getBoardListByCompanyId(int companyId) {
    List<BoardDTO> boardList = boardMapper.selectBoardsByCompanyId(companyId);
    return boardList;
  }

  public void insertNewBoard(String newBoardName, int companyId) {
    BoardDTO newBoard = BoardDTO.builder()
        .companyId(companyId)
        .boardName(newBoardName)
        .build();
    boardMapper.insertBoard(newBoard);
  }

  public void changeBoardName(int boardId, String boardName) {
    BoardDTO boardData = BoardDTO.builder()
        .boardId(boardId)
        .boardName(boardName)
        .build();
    boardMapper.updateBoardName(boardData);
  }


  public void deleteBoard(int boardId) {
    boardMapper.deleteBoard(boardId);
  }

  public void deleteBoardsByDeleteBoardList(List<BoardDTO> deleteBoards) {
    deleteBoards.forEach(boardDTO -> deleteBoard(boardDTO.getBoardId()));
  }

  public void updateChangedName(List<BoardDTO> newTitleList, int companyId) {
    newTitleList
        .forEach(boardDTO -> changeBoardName(boardDTO.getBoardId(), boardDTO.getBoardName()));
  }
}

