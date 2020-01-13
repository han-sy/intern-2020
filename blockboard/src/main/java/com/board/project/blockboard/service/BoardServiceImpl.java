package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import com.board.project.blockboard.dto.BoardDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private BoardMapper boardMapper;
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    public List<BoardDTO> allBoard() {
        return boardMapper.allBoard();
    }

    @Override
    public List<BoardDTO> printBoardbyComp(String user_id) {
        //System.out.println("com_id (session): " + session.getAttribute("COMPANY"));
        String com_id = boardMapper.selectComIdByUserId(user_id);
        System.out.println("com_id : "+com_id);
        List<BoardDTO> boardlist = boardMapper.selectBoardByComId(com_id);
        return boardlist;
    }
    @Override
    public List<PostDTO> printPostbyBoard(String board_id) {
        List<PostDTO> postlist = boardMapper.selectPostByBoardId(board_id);
        return postlist;
    }

    @Override
    public PostDTO printPostContnet(String post_id) {
        PostDTO post = boardMapper.selectPostByPostID(post_id);
        return post;
    }
}
