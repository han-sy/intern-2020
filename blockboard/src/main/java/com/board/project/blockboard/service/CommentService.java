package com.board.project.blockboard.service;

import com.board.project.blockboard.dto.CommentDTO;
import com.board.project.blockboard.mapper.CommentMapper;
import com.board.project.blockboard.mapper.FunctionMapper;

import com.board.project.blockboard.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;


    public List<CommentDTO> getCommentListByPostID(int postID) {
        return commentMapper.selectCommentsByPostID(postID);
    }

    public CommentDTO writeCommentWithUserInfo(String userID, CommentDTO comment, int boardID, int companyID, int postID) {
        comment.setUserName(userMapper.selectUserNameByUserID(userID));
        comment.setPostID(postID);
        comment.setBoardID(boardID);
        comment.setUserID(userID);
        comment.setCompanyID(companyID);
        int result = commentMapper.insertNewCommentByCommentInfo(comment);

        return result ==1? comment : null;
    }


    /*public void writeCommentWithUserInfo(String userID, String commentText, int boardID, int companyID, int postID) {
        Map<String, Object> commentInfo = new HashMap<String, Object>();
        commentInfo.put("userID",userID);
        commentInfo.put("boardID",boardID);
        commentInfo.put("commentText",commentText);
        commentInfo.put("companyID",companyID);
        commentInfo.put("postID",postID);
        log.info("null check"+commentInfo);
        commentMapper.insertNewCommentByCommentInfo(commentInfo);
    }*/
}
