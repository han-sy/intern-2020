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
    public String printCompanyName(String userID) {
        return boardMapper.selectComNameByUserId(userID);
    }

    @Override
    public List<BoardDTO> printBoardbyComp(String userID) {
        //System.out.println("companyID (session): " + session.getAttribute("COMPANY"));
        String companyID = boardMapper.selectComIdByUserId(userID);
        System.out.println("companyID : "+companyID);
        List<BoardDTO> boardlist = boardMapper.selectBoardByComId(companyID);
        return boardlist;
    }
    @Override
    public List<PostDTO> printPostbyBoard(String boardID) {
        List<PostDTO> postlist = boardMapper.selectPostByBoardId(boardID);
        return postlist;
    }

    @Override
    public PostDTO printPostContnet(String postID) {
        PostDTO post = boardMapper.selectPostByPostID(postID);
        return post;
    }

    @Override
    public boolean checkAdmin(String userID) {
        String admin = boardMapper.selectUserTypeByUserId(userID);
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
    public int printCompanyId(String userID) {
        logger.info("boardMapper userid: "+ userID);
        return Integer.parseInt(boardMapper.selectComIdByUserId(userID));
    }

    @Override
    public BoardDTO printboardbyBoardName(String newBoardName) {
        return boardMapper.selectBoardByBoardName(newBoardName);
    }
}