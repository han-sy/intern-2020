/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.BoardDTO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BoardMapper {

  List<BoardDTO> selectBoardsByCompanyId(int companyId);

  BoardDTO selectBoardByBoardName(String newBoardName);

  void insertBoard(BoardDTO newBoard);

  void updateBoardName(BoardDTO boardData);

  int deleteBoard(int boardId);
}

