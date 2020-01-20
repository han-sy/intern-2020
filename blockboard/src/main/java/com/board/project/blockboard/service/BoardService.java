package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.BoardDTO;
import com.board.project.blockboard.dto.PostDTO;
import com.board.project.blockboard.mapper.BoardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    public String getCompanyNameByUserID(String userID) {
        return boardMapper.selectCompanyNameByUserID(userID);
    }

    public List<BoardDTO> getBoardListByCompanyID(int companyID) {
        //System.out.println("companyID (session): " + session.getAttribute("COMPANY"));
        System.out.println("companyID : "+companyID);
        List<BoardDTO> boardList= boardMapper.selectBoardsByCompanyID(companyID);
        return boardList;
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
        BoardDTO newBoard = new BoardDTO();
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

    public void changeBoardName(int boardID, String boardName) {
        Map<String, Object> boardAttributes = new HashMap<String, Object>();
        boardAttributes.put("boardID",boardID);
        boardAttributes.put("boardName",boardName);
        boardMapper.updateBoardName(boardAttributes);
    }

    public void deleteAllPostInBoard(int boardID) {
        boardMapper.deleteAllPostInBoard(boardID);
    }

    public void deleteBoard(int boardID) {
        boardMapper.deleteBoard(boardID);
    }

    public void deleteAllCommentInBoard(int boardID) {
        boardMapper.deleteAllCommentInBoard(boardID);
    }
}

