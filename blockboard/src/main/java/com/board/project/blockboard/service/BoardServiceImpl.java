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
        return boardMapper.selectAllBoard();
    }

    @Override
    public String getCompanyNameByUserID(String userID) {
        return boardMapper.selectCompanyNameByUserID(userID);
    }

    @Override
    public List<BoardDTO> getBoardListByUserID(String userID) {
        //System.out.println("companyID (session): " + session.getAttribute("COMPANY"));
        String companyID = boardMapper.selectCompanyIDByUserID(userID);
        System.out.println("companyID : "+companyID);
        List<BoardDTO> boardlist = boardMapper.selectBoardByCompanyID(companyID);
        return boardlist;
    }
    @Override
    public List<PostDTO> getPostListByBoardID(String boardID) {
        List<PostDTO> postlist = boardMapper.selectPostByBoardID(boardID);
        return postlist;
    }

    @Override
    public PostDTO getPostByPostID(String postID) {
        PostDTO post = boardMapper.selectPostByPostID(postID);
        return post;
    }

    @Override
    public boolean checkAdmin(String userID) {
        String admin = boardMapper.selectUserTypeByUserID(userID);
        return admin.equals("관리자");
    }

    @Override
    public void insertNewBoard(String newBoardName,int companyID){
        int idx = boardMapper.maxBoardID();
        BoardDTO newBoard = new BoardDTO();
        newBoard.setBoardID(idx+1);
        newBoard.setCompanyID(companyID);
        newBoard.setBoardName(newBoardName);
        logger.info("newBoard : "+newBoard);
        boardMapper.insertBoard(newBoard);
    }

    @Override
    public int getCompanyIDByUserID(String userID) {
        logger.info("boardMapper userid: "+ userID);
        return Integer.parseInt(boardMapper.selectCompanyIDByUserID(userID));
    }

    @Override
    public BoardDTO getBoardByBoardName(String boardName) {
        return boardMapper.selectBoardByBoardName(boardName);
    }
}