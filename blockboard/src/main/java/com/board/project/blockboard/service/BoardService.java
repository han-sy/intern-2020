package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    public List<BoardDTO> allBoard() {
        return boardMapper.selectAllBoard();
    }

    public String getCompanyNameByUserID(String userID) {
        return boardMapper.selectCompanyNameByUserID(userID);
    }

    public List<BoardDTO> getBoardListByUserID(String userID) {
        //System.out.println("companyID (session): " + session.getAttribute("COMPANY"));
        logger.info("companyID"+boardMapper.selectCompanyIDByUserID(userID));
        int companyID = boardMapper.selectCompanyIDByUserID(userID);
        System.out.println("companyID : "+companyID);
        List<BoardDTO> boardlist = boardMapper.selectBoardByCompanyID(companyID);
        return boardlist;
    }

    public List<PostDTO> getPostListByBoardID(int boardID) {
        List<PostDTO> postlist = boardMapper.selectPostByBoardID(boardID);
        return postlist;
    }

    public PostDTO getPostByPostID(int postID) {
        PostDTO post = boardMapper.selectPostByPostID(postID);
        return post;
    }

    public boolean checkAdmin(String userID) {
        String admin = boardMapper.selectUserTypeByUserID(userID);
        return admin.equals("관리자");
    }

    public void insertNewBoard(String newBoardName,int companyID){
        int idx = boardMapper.maxBoardID();
        BoardDTO newBoard = new BoardDTO();
        newBoard.setBoardID(idx+1);
        newBoard.setCompanyID(companyID);
        newBoard.setBoardName(newBoardName);
        logger.info("newBoard : "+newBoard);
        boardMapper.insertBoard(newBoard);
    }

    public int getCompanyIDByUserID(String userID) {
        logger.info("boardMapper userid: "+ userID);
        return boardMapper.selectCompanyIDByUserID(userID);
    }
    
    public BoardDTO getBoardByBoardName(String boardName) {
        return boardMapper.selectBoardByBoardName(boardName);
    }
}

