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
    public String printCompanyName(String user_id) {
        return boardMapper.selectComNameByUserId(user_id);
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

    @Override
    public boolean checkAdmin(String user_id) {
        String admin = boardMapper.selectUserTypeByUserId(user_id);
        return admin.equals("관리자");
    }

    @Override
    public void insertNewBoard(String newBoardName,int companyID){
        int idx = boardMapper.maxBoardID();
        BoardDTO newBoard = new BoardDTO(idx+1,companyID,newBoardName);
        logger.info("newBoard : "+newBoard);
        boardMapper.insertBoard(newBoard);
    }

    @Override
    public int printCompanyId(String user_id) {
        logger.info("boardMapper userid: "+ user_id);
        String select_result = boardMapper.selectComIdByUserId(user_id);
        logger.info("boardMapper select: "+ select_result);
        return Integer.parseInt(select_result);
    }

    @Override
    public BoardDTO printboardbyBoardName(String newBoardName) {
        return boardMapper.selectBoardByBoardName(newBoardName);
    }
}