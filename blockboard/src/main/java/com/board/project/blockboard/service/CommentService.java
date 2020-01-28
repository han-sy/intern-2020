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

    public CommentDTO writeCommentWithUserInfo(String userID, String commentContent, int boardID, int companyID, int postID) {
        CommentDTO comment = new CommentDTO();
        comment.setCommentContent(commentContent);
        comment.setUserName(userMapper.selectUserNameByUserID(userID));
        comment.setPostID(postID);
        comment.setBoardID(boardID);
        comment.setUserID(userID);
        comment.setCompanyID(companyID);
        int result = commentMapper.insertNewCommentByCommentInfo(comment);

        return result ==1? comment : null;
    }

    public void deleteComment(int commentID) {
        commentMapper.deleteCommentByCommentID(commentID);
    }

    public void updateComment(int commentID, String newComment) {
        Map<String, Object> commentAttribute = new HashMap<String, Object>();
        commentAttribute.put("commentID",commentID);
        commentAttribute.put("newComment",newComment);
        commentMapper.updateComment(commentAttribute);
    }
}
