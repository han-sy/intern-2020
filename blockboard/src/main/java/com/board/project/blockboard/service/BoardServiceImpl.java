package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.dto.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private BoardMapper boardMapper;

    @Autowired
    BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<BoardDTO> allBoard() {
        return boardMapper.allBoard();
    }

    @Override
    public List<BoardDTO> printBoardbyComp(HttpServletRequest request, HttpSession session) {
        System.out.println("com_id (session): " + session.getAttribute("COMPANY"));
        List<BoardDTO> boardlist = boardMapper.selectBoardByComId(session.getAttribute("COMPANY") + "");
        return boardlist;
    }
    @Override
    public List<PostDTO> printPostbyBoard(String board_id) {
        List<PostDTO> postlist = boardMapper.selectPostByBoardId(board_id);
        return postlist;
    }
}
