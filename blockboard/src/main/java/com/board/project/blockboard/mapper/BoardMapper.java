package com.board.project.blockboard.mapper;

import com.board.project.blockboard.model.Board;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BoardMapper {
    List<Board> allBoard();
}
