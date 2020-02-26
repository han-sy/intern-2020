/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardService.java
 */
package com.board.project.blockboard.service;

import com.board.project.blockboard.common.exception.BoardValidException;
import com.board.project.blockboard.common.util.LengthCheckUtils;
import com.board.project.blockboard.common.validation.BoardValidation;
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
  @Autowired
  private BoardValidation boardValidation;

  public List<BoardDTO> getBoardListByCompanyId(int companyId) {
    List<BoardDTO> boardList = boardMapper.selectBoardsByCompanyId(companyId);
    return boardList;
  }

  public void insertNewBoard(String newBoardName, int companyId) {
    BoardDTO newBoard = BoardDTO.builder()
        .companyId(companyId)
        .boardName(newBoardName)
        .build();
    LengthCheckUtils.validateBoardName(newBoard);
    boardMapper.insertBoard(newBoard);
  }

  public void changeBoardName(int boardId, String boardName) {
    boardValidation.checkExistedBoard(boardId);
    BoardDTO boardData = BoardDTO.builder()
        .boardId(boardId)
        .boardName(boardName)
        .build();
    LengthCheckUtils.validateBoardName(boardData);
    boardMapper.updateBoardName(boardData);
  }


  public void deleteBoard(int boardId) {
    boardValidation.checkExistedBoard(boardId);
    boardMapper.deleteBoard(boardId);
  }

  public void deleteBoardsByDeleteBoardList(List<BoardDTO> deleteBoards) {
    deleteBoards.forEach(boardDTO -> deleteBoard(boardDTO.getBoardId()));
  }

  public void updateChangedName(List<BoardDTO> newTitleList) {
    newTitleList
        .forEach(boardDTO -> changeBoardName(boardDTO.getBoardId(), boardDTO.getBoardName()));
  }

  public boolean isExistBoard(int boardId) {
    BoardDTO board = boardMapper.selectBoardByBoardIDForCheckExisted(boardId);
    log.info("selectBoardByBoardIDForCheckExisted");
    if(board==null){
      log.info("null");
      return false;
    }
    log.info("not null");
    return true;
  }
}

