/**
 * @author Dongwook Kim <dongwook.kim1211@worksmobile.com>
 * @file BoardMapper.java
 */
package com.board.project.blockboard.mapper;

import com.board.project.blockboard.dto.BoardDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BoardMapper {

  List<BoardDTO> selectBoardsByCompanyId(int companyId);

  void insertBoard(BoardDTO newBoard);

  void updateBoardName(BoardDTO boardData);

  int deleteBoard(int boardId);

  BoardDTO selectBoardByBoardIDForCheckExisted(int boardId);
}

