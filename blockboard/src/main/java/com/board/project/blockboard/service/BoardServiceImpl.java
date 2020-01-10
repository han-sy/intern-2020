package com.board.project.blockboard.service;

import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.mapper.UserMapper;
import com.board.project.blockboard.model.Board;
import com.board.project.blockboard.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{
    private BoardMapper boardMapper;

    @Autowired
    BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<Board> allBoard() {
        return boardMapper.allBoard();
    }
}
